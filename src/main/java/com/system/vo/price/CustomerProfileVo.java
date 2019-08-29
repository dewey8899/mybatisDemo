package com.system.vo.price;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by deweydu
 * Date on 2019/8/27 15:03
 */
@Data
@Slf4j
public class CustomerProfileVo {
    private Long id;
    private String entityCode;
    private String entitySubCode;
    private String entityInternalCode;
}
