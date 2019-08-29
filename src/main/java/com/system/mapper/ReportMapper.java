package com.system.mapper;

import com.system.entity.TBReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by deweydu
 * Date on 2019/8/22 17:35
 */
public interface ReportMapper {
    List<TBReport> getList(@Param("condition")TBReport report);
}
