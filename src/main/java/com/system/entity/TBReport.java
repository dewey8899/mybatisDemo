package com.system.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by deweydu
 * Date on 2019/8/22 17:32
 */
@Data
public class TBReport {
    private Long id;
    private String agencyCode;
    private String agencyName;
    private String serviceCode;
    private String serviceName;
    private String brandName;
    private String materialNumber;
    private String productName;
    private Integer deliveryQuantity;
    private BigDecimal deliveryAgencyAmount;
    private Integer replenishQuantity;
    private BigDecimal replenishAgencyAmount;

}
