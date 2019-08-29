package com.system.vo.price;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by deweydu
 * Date on 2019/8/27 15:05
 */
@Slf4j
@Data
public class PriceVo {
    private Long id;
    private String entityCode;
    private String entitySubCode;
    private String relationEntityCode;
    private String relationEntitySubCode;
    private String priceBatchCode;
    private String productCode;
    private String materialNumber;
    private BigDecimal taxExcludedPrice;
    private BigDecimal taxIncludedPrice;
    private BigDecimal taxPrice;
    private Date startTime;
    private Date endTime;
    private String latestFlg;
    private Date sysCreatedTime;
    private String sysCreatedOperatorName;
    private Date sysUpdatedTime;
    private String sysUpdatedOperatorName;
    private String sysDelFlg;
}
