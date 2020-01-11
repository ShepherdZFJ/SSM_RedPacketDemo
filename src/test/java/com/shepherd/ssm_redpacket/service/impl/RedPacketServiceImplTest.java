package com.shepherd.ssm_redpacket.service.impl;

import com.shepherd.ssm_redpacket.domain.RedPacket;
import com.shepherd.ssm_redpacket.service.IRedPacketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedPacketServiceImplTest {


    @Autowired
    private IRedPacketService redPacketService;
    @Test
    public void getRedPacket() {
        RedPacket redPacket = redPacketService.getRedPacket((long) 1);
        System.out.println(redPacket);

    }
}