package com.system.vo;

import com.system.utils.excel.Column;
import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Cell;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by deweydu
 * Date on 2019/8/23 13:22
 */
@Data
@ToString
public class ReportExportOutVo implements Serializable {
    @Column(index = 0, type = Cell.CELL_TYPE_STRING, title = "总代编号", width = 30 * 100)
    private String agencyCode;

    @Column(index = 1, type = Cell.CELL_TYPE_STRING, title = "总代名称", width = 30 * 280)
    private String agencyName;

    @Column(index = 2, type = Cell.CELL_TYPE_STRING, title = "服务商编号", width = 30 * 100)
    private String serviceCode;

    @Column(index = 3, type = Cell.CELL_TYPE_STRING, title = "服务商名称", width = 30 * 280)
    private String serviceName;

    @Column(index = 4, type = Cell.CELL_TYPE_STRING, title = "厂商品牌", width = 30 * 100)
    private String brandName;

    @Column(index = 5, type = Cell.CELL_TYPE_STRING, title = "商品编号", width = 30 * 100)
    private String materialNumber;

    @Column(index = 6, type = Cell.CELL_TYPE_STRING, title = "商品名称", width = 30 * 256)
    private String productName;

    @Column(index = 7, type = Cell.CELL_TYPE_NUMERIC, title = "配货系数", width = 30 * 100,format="#,##0.00")
    private BigDecimal deliveryCoefficient;

    @Column(index = 8, type = Cell.CELL_TYPE_NUMERIC, title = "配货数量", width = 30 * 100)
    private Integer deliveryQuantity;

    @Column(index = 9, type = Cell.CELL_TYPE_NUMERIC, title = "配货经销商单价", width = 30 * 160,format="#,##0.00")
    private BigDecimal deliveryDealerPrice;

    @Column(index = 10, type = Cell.CELL_TYPE_NUMERIC, title = "配货经销商含税总金额", width = 30 * 160,format="#,##0.00")
    private BigDecimal deliveryDealerAmount;

    @Column(index = 11, type = Cell.CELL_TYPE_NUMERIC, title = "配货总代单价", width = 30 * 160,format="#,##0.00")
    private BigDecimal deliveryAgencyPrice;

    @Column(index = 12, type = Cell.CELL_TYPE_NUMERIC, title = "配货总代含税总金额", width = 30 * 160,format="#,##0.00")
    private BigDecimal deliveryAgencyAmount;

    @Column(index = 13, type = Cell.CELL_TYPE_NUMERIC, title = "补货系数", width = 30 * 100,format="#,##0.00")
    private BigDecimal replenishCoefficient;

    @Column(index = 14, type = Cell.CELL_TYPE_NUMERIC, title = "补货数量", width = 30 * 100)
    private Integer replenishQuantity;

    @Column(index = 15, type = Cell.CELL_TYPE_NUMERIC, title = "补货经销商单价", width = 30 * 160,format="#,##0.00")
    private BigDecimal replenishDealerPrice;

    @Column(index = 16, type = Cell.CELL_TYPE_NUMERIC, title = "补货经销商含税总金额", width = 30 * 160,format="#,##0.00")
    private BigDecimal replenishDealerAmount;

    @Column(index = 17, type = Cell.CELL_TYPE_NUMERIC, title = "补货总代单价", width = 30 * 160,format="#,##0.00")
    private BigDecimal replenishAgencyPrice;

    @Column(index = 18, type = Cell.CELL_TYPE_NUMERIC, title = "补货总代含税总金额", width = 30 * 160,format="#,##0.00")
    private BigDecimal replenishAgencyAmount;
}
