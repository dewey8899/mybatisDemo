package com.system.vo.tb_report_oem_report_difference;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by deweydu
 * Date on 2019/11/12 14:31
 */
@Data
@Slf4j
public class ReportOemProductDifferenceSearchOutDto {
    private String dateType;
    private String startDate;
    private Date endTime;
    private String endDate;
    private String materialNumber;//商品编号
    private String productName;//商品名称
    private String taskOrderReplenishmentAccountRuleType;      //配送任务订单补货账户规则类型（1：20190831之前的规则，2:20190901之后的规则
    private Integer deliveryQuantity;                           //配送任务单   数量
    private BigDecimal deliveryServiceBusinessTaxExcludedPrice;//配送任务单   服务商单价
    private BigDecimal deliveryServiceBusinessTaxExcludedAmount;//配送任务单   服务商未税总金额
    private BigDecimal deliveryGeneralAgencyTaxExcludedPrice;//配送任务单   总代未税单价
    private BigDecimal deliveryGeneralAgencyTaxExcludedAmount;//配送任务单   总代未税总金额
    private Integer replenishQuantity;                          //站点送货订单或补货订单   数量
    private BigDecimal replenishServiceBusinessTaxExcludedPrice;//站点送货订单或补货订单   服务商单价
    private BigDecimal replenishServiceBusinessTaxExcludedAmount;//站点送货订单或补货订单   服务商未税总金额
    private BigDecimal replenishGeneralAgencyTaxExcludedPrice;//站点送货订单或补货订单   总代未税单价
    private BigDecimal replenishGeneralAgencyTaxExcludedAmount;//站点送货订单或补货订单   总代未税总金额
    private String deliveryPriceRatio;//配送任务单   总代未税单价/服务商未税单价
    private String replenishPriceRatio;//站点送货订单或补货订单   总代未税单价/服务商未税单价
    private String serviceBusinessEntityInternalCode;//站点送货订单或补货订单   总代未税单价/服务商未税单价
    private String serviceBusinessEntityCompanyShortName;//站点送货订单或补货订单   总代未税单价/服务商未税单价
    private String generalAgencyEntityInternalCode;
}
