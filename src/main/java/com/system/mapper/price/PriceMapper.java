package com.system.mapper.price;

import com.system.vo.price.CustomerProfileVo;
import com.system.vo.price.PriceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by deweydu
 * Date on 2019/8/27 15:07
 */
public interface PriceMapper {
    List<PriceVo> getPriceList(@Param("condition") CustomerProfileVo vo);
    List<CustomerProfileVo> getCustomerList(@Param("condition") CustomerProfileVo vo);
    int insertSelectiveBatch(List<PriceVo> records);
    sout
}
