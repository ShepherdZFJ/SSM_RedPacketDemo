package com.shepherd.ssm_redpacket.service.impl;

import com.shepherd.ssm_redpacket.dao.IRedPacketDao;
import com.shepherd.ssm_redpacket.domain.RedPacket;
import com.shepherd.ssm_redpacket.service.IRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedPacketServiceImpl implements IRedPacketService {
    @Autowired
    private IRedPacketDao redPacketDao = null;
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RedPacket getRedPacket(Long id) {
        return redPacketDao.getRedPacket(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int decreaseRedPacet(Long id) {
        return redPacketDao.decreaseRedPacket(id);
    }
}
