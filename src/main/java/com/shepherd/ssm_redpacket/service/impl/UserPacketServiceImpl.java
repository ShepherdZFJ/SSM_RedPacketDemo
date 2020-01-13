package com.shepherd.ssm_redpacket.service.impl;

import com.shepherd.ssm_redpacket.dao.IRedPacketDao;
import com.shepherd.ssm_redpacket.dao.IUserRedPacketDao;
import com.shepherd.ssm_redpacket.domain.RedPacket;
import com.shepherd.ssm_redpacket.domain.UserRedPacket;
import com.shepherd.ssm_redpacket.service.IRedisRedPacketService;
import com.shepherd.ssm_redpacket.service.IUserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

@Service
public class UserPacketServiceImpl implements IUserRedPacketService {

    @Autowired
    private RedisTemplate redisTemplate = null;

    @Autowired
    private IRedisRedPacketService redisRedPacketService = null;

    @Autowired
    private IUserRedPacketDao userRedPacketDao = null;
    @Autowired
    private IRedPacketDao redPacketDao = null;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacket(Long redPaketId, Long userId) {

        //获取红包信息
        // RedPacket redPacket = redPacketDao.getRedPacket(redPaketId);
        RedPacket redPacket = redPacketDao.getRedPacketForUpdate(redPaketId);
        if(redPacket.getStock() > 0){
            redPacketDao.decreaseRedPacket(redPaketId);
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPaketId);
            userRedPacket.setUserId(userId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setNote("抢红包"+redPaketId);
            int result = userRedPacketDao.grapRedPacket(userRedPacket);
            return result;
        }
        return 0;
    }

    /**
     * 为了解决30000次请求完成之后，还剩余大量的红包，加时间戳重入抢红包
     * @param redPacketId
     * @param userId
     * @return
     */
    /*
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacketForVersion(Long redPacketId, Long userId) {
        Long start = System.currentTimeMillis();
        while (true) {
            Long end = System.currentTimeMillis();
            if(end - start > 100){
                return 0;
            }
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            if (redPacket.getStock() > 0) {
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
                if (update == 0) {
                    continue;
                }
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("抢红包" + redPacketId);
                int result = userRedPacketDao.grapRedPacket(userRedPacket);
                return result;
            }else{
                return 0;
            }
        }
    }
     */

    /**
     * 为了解决30000次请求完成之后，还剩余大量的红包，按次数戳重入抢红包
     * @param redPacketId
     * @param userId
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacketForVersion(Long redPacketId, Long userId) {
        for (int i = 0; i < 10; i++) {
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            if (redPacket.getStock() > 0) {
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
                if (update == 0) {
                    continue;
                }
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("抢红包" + redPacketId);
                int result = userRedPacketDao.grapRedPacket(userRedPacket);
                return result;
            } else {
                return 0;
            }
        }
        return 0;
    }

    // Lua脚本
    String script = "local listKey = 'red_packet_list_'..KEYS[1] \n"
            + "local redPacket = 'red_packet_'..KEYS[1] \n"
            + "local stock = tonumber(redis.call('hget', redPacket, 'stock')) \n"
            + "if stock == nil then return 0 end \n"
            + "if stock <= 0 then return 0 end \n"
            + "stock = stock -1 \n"
            + "redis.call('hset', redPacket, 'stock', tostring(stock)) \n"
            + "redis.call('rpush', listKey, ARGV[1]) \n"
            + "if stock == 0 then return 2 end \n"
            + "return 1 \n";

    // 在缓存LUA脚本后，使用该变量保存Redis返回的32位的SHA1编码，使用它去执行缓存的LUA脚本[加入这句话]
    String sha1 = null;

    @Override
    public Long grapRedPacketByRedis(Long redPacketId, Long userId) {
        // 当前抢红包用户和日期信息
        String args = userId + "-" + System.currentTimeMillis();
        Long result = null;
        // 获取底层Redis操作对象
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try {
            // 如果脚本没有加载过，那么进行加载，这样就会返回一个sha1编码
            if (sha1 == null) {
                sha1 = jedis.scriptLoad(script);
            }
            // 执行脚本，返回结果
            Object res = jedis.evalsha(sha1, 1, redPacketId + "", args);
            result = (Long) res;
            // 返回2时为最后一个红包，此时将抢红包信息通过异步保存到数据库中
            if (result == 2) {
                // 获取单个小红包金额
                String unitAmountStr = jedis.hget("red_packet_" + redPacketId, "unit_amount");
                // 触发保存数据库操作
                Double unitAmount = Double.parseDouble(unitAmountStr);
                System.err.println("thread_name = " + Thread.currentThread().getName());
                System.out.println("执行保存了");
                redisRedPacketService.saveUserRedPacketByRedis(redPacketId, unitAmount);
                System.out.println("执行保存结束了");
            }
        } finally {
            // 确保jedis顺利关闭
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
        return result;
    }


}
