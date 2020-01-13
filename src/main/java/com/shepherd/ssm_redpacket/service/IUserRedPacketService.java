package com.shepherd.ssm_redpacket.service;

public interface IUserRedPacketService {

    /**
     * 插入抢红包的信息
     * @param   redPaketId  红包信息
     * @param   userId   抢红包的用户编号
     * @return    影响的记录数
     */
    public int grapRedPacket(Long redPaketId,Long userId);

    public int grapRedPacketForVersion(Long redPacketId,Long userId);

    public Long grapRedPacketByRedis(Long redPacketId,Long userId);
}
