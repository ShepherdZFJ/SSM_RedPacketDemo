package com.shepherd.ssm_redpacket.service;

public interface IRedisRedPacketService {
    /**
     * 通过redis保存抢红包的信息
     * @param redPackwtId   红包id
     * @param unitAmount    每个红包金额
     */
    public void saveUserRedPacketByRedis(Long redPackwtId,Double unitAmount);
}
