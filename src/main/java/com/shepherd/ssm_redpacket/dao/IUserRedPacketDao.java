package com.shepherd.ssm_redpacket.dao;

import com.shepherd.ssm_redpacket.domain.UserRedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IUserRedPacketDao {
    /**
     * 插入抢红包的信息
     * @param userRedPacket  红包信息
     * @return    影响的记录数
     */
    public int grapRedPacket(UserRedPacket userRedPacket);
}
