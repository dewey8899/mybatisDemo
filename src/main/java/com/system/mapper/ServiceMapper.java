package com.system.mapper;

import com.system.entity.TBSservice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by deweydu
 * Date on 2019/8/23 16:36
 */
public interface ServiceMapper {
    List<TBSservice> getList(@Param("condition")TBSservice sservice);
}
