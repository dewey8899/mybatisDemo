package com.system.vo.price;

import com.system.utils.excel.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by deweydu
 * Date on 2019/8/28 9:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PriceExportOutVo {
    @Column(index = 0, type = Cell.CELL_TYPE_STRING, title = "参考tb_customer_entity_profile", width = 30 * 100)
    private String entityCode;
    @Column(index = 1, type = Cell.CELL_TYPE_STRING, title = "参考tb_customer_entity_role", width = 30 * 100)
    private String entitySubCode;
    @Column(index = 2, type = Cell.CELL_TYPE_STRING, title = "参考tb_customer_entity_profile（品牌商用）", width = 30 * 100)
    private String relationEntityCode;
    @Column(index = 3, type = Cell.CELL_TYPE_STRING, title = "参考tb_customer_entity_role（品牌商用）", width = 30 * 100)
    private String relationEntitySubCode;
    @Column(index = 4, type = Cell.CELL_TYPE_STRING, title = "价格维护编号", width = 30 * 100)
    private String priceBatchCode;
    @Column(index = 5, type = Cell.CELL_TYPE_STRING, title = "参考tb_product_base", width = 30 * 100)
    private String productCode;
    @Column(index = 6, type = Cell.CELL_TYPE_STRING, title = "厂商自用料号", width = 30 * 100)
    private String materialNumber;
    @Column(index = 7, type = Cell.CELL_TYPE_NUMERIC, title = "商品价格（未税）", width = 30 * 160,format="#,##0.00")
    private BigDecimal taxExcludedPrice;
    @Column(index = 8, type = Cell.CELL_TYPE_NUMERIC, title = "商品价格（含税）", width = 30 * 160,format="#,##0.00")
    private BigDecimal taxIncludedPrice;
    @Column(index = 9, type = Cell.CELL_TYPE_NUMERIC, title = "税额", width = 30 * 160,format="#,##0.00")
    private BigDecimal taxPrice;
    @Column(index = 10, type = Cell.CELL_TYPE_NUMERIC, title="适用开始日期",format="yyyy-MM-dd", width= 10 * 256)
    private Date startTime;
    @Column(index = 11, type = Cell.CELL_TYPE_NUMERIC, title="适用结束日期",format="yyyy-MM-dd", width= 10 * 256)
    private Date endTime;
    @Column(index = 12, type = Cell.CELL_TYPE_STRING, title = "最新价格标识【0：历史价格，1：最新价格】", width = 30 * 100)
    private String latestFlg;
    @Column(index = 13, type = Cell.CELL_TYPE_NUMERIC, title="创建时间",format="yyyy-MM-dd HH:mm:ss", width= 25 * 256)
    private Date sysCreatedTime;
    @Column(index = 14, type = Cell.CELL_TYPE_STRING, title = "创建操作者的姓名", width = 30 * 100)
    private String sysCreatedOperatorName;
    @Column(index = 15, type = Cell.CELL_TYPE_NUMERIC, title="更新时间",format="yyyy-MM-dd HH:mm:ss", width= 25 * 256)
    private Date sysUpdatedTime;
    @Column(index = 16, type = Cell.CELL_TYPE_STRING, title = "更新操作者的姓名", width = 30 * 100)
    private String sysUpdatedOperatorName;
    @Column(index = 17, type = Cell.CELL_TYPE_STRING, title = "记录删除状态", width = 30 * 100)
    private String sysDelFlg;
}
