package com.shepherd.ssm_redpacket.service;

import com.shepherd.ssm_redpacket.domain.RedPacket;

public interface IRedPacketService {

    /**
     *
     * 获取红包信息
     * @param id  红包id
     * @return   红包具体信息
     */
    public RedPacket getRedPacket(Long id);

    /**
     * 扣减红包数量
     * @param id   红包id
     * @return    更新记录条数
     */
    public int decreaseRedPacet(Long id);
}
