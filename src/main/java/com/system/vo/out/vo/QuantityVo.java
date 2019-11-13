package com.system.vo.out.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by deweydu
 * Date on 2019/11/13 14:03
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class QuantityVo {
    private Integer deliveryQuantity = 0;
    private Integer replenishQuantity = 0;
}
