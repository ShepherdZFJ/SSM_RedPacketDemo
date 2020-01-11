package com.shepherd.ssm_redpacket.service;

public interface IUserRedPacketService {

    /**
     * 插入抢红包的信息
     * @param userRedPacket  红包信息
     * @return    影响的记录数
     */
    public int grapRedPacket(Long redPaketId,Long userId);
}
