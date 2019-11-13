package com.system.vo.tb_report_oem_report_difference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by deweydu
 * Date on 2019/11/12 14:39
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReportOemProductDifferenceSearchInDto {
    private String queryFlag; // 统计维度
    private String startYear;//开始年份
    private String endYear;//结束年份

    private String startYearMonth;//开始月
    private String endYearMonth;//结束月

    private String startWeek;//开始周
    private String endWeek;//结束周

    private Date startDay;//开始时间(天)
    private Date endDay;//结束时间(天)
    private String startDateStr;
    private String endDateStr;
    private Date startDate;//开始时间(周天)
    private Date endDate;//结束时间(周天)
    // 起始季度
    private String startQuarter;
    // 结束季度
    private String endQuarter;
    // 起始半年度
    private String startHalfYear;
    // 结束半年度
    private String endHalfYear;

    private String generalAgencyEntityInternalCode;//总代编号
    private String serviceBusinessEntityInternalCode;//服务商编号
    private String materialNumber;//商品编号
    private String generalAgencyEntityCompanyShortName;//总代名称
    private String serviceBusinessEntityCompanyShortName;//服务商名称
    private String productName;//商品名称
    private String manufacturerBrandNameEn;//厂商品牌
    private String ruleType;//厂商品牌
}
