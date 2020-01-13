package com.shepherd.ssm_redpacket.service.impl;

import com.shepherd.ssm_redpacket.dao.IRedPacketDao;
import com.shepherd.ssm_redpacket.dao.IUserRedPacketDao;
import com.shepherd.ssm_redpacket.domain.RedPacket;
import com.shepherd.ssm_redpacket.domain.UserRedPacket;
import com.shepherd.ssm_redpacket.service.IUserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPacketServiceImpl implements IUserRedPacketService {

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

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacketForVersion(Long redPacketId, Long userId) {
        RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
        if(redPacket.getStock() > 0){
            int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
            if(update == 0){
                return 0;
            }
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPacketId);
            userRedPacket.setUserId(userId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setNote("抢红包"+redPacketId);
            int result = userRedPacketDao.grapRedPacket(userRedPacket);
            return result;
        }

        return 0;
    }
}
