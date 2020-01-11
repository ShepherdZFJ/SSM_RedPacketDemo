package com.shepherd.ssm_redpacket.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RedPacket implements Serializable {
    private Long id;
    private Long userId;
    private Double amount;
    private Timestamp sendDate;
    private Integer total;
    private Double unitAmount;
    private Integer stock;
    private Integer version;
    private String note;

}
