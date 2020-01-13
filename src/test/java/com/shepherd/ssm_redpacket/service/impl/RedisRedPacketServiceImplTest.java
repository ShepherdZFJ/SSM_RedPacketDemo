package com.shepherd.ssm_redpacket.service.impl;

import com.shepherd.ssm_redpacket.service.IRedisRedPacketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisRedPacketServiceImplTest {

    @Autowired
    private IRedisRedPacketService redisRedPacketService = null;
    @Test
    public void saveUserRedPacketByRedis() {
        redisRedPacketService.saveUserRedPacketByRedis(1l,10d);

    }
}