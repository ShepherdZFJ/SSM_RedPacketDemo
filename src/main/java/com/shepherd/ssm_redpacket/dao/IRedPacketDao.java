package com.shepherd.ssm_redpacket.dao;

import com.shepherd.ssm_redpacket.domain.RedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IRedPacketDao {

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
    public int decreaseRedPacket(Long id);
//
//
//    public RedPacket getRedPacketForUpdate(Long id);
//
//
//    public int decreaseRedPacketForVersion(@Param("id") Long id, @Param("version") Integer version);

}
