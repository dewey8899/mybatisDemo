package com.system.vo.tb_report_oem_report_difference;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by deweydu
 * Date on 2019/9/3 15:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportOemProductDifferenceGetDataTotalOutDto {
    private String deliveryRatio;//总代总金额/服务商总金额（开票或审核）
    private String replenishRatio;//总代总金额/服务商总金额（发货）
    private BigDecimal serviceLast;////服务商补货剩余金额
    private BigDecimal agencyLast;//总代补货剩余金额
}
