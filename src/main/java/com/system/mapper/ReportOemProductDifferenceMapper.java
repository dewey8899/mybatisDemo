package com.system.mapper;

import com.system.vo.tb_report_oem_report_difference.ReportOemProductDifferenceSearchInDto;
import com.system.vo.tb_report_oem_report_difference.ReportOemProductDifferenceSearchOutDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by deweydu
 * Date on 2019/11/12 14:36
 */
public interface ReportOemProductDifferenceMapper {

    List<ReportOemProductDifferenceSearchOutDto> search(@Param("param") ReportOemProductDifferenceSearchInDto reportOemProductDifferenceSearchInDto);

    List<ReportOemProductDifferenceSearchOutDto> listDeliveries(@Param("param") ReportOemProductDifferenceSearchInDto reportOemProductDifferenceSearchInDto);

    List<ReportOemProductDifferenceSearchOutDto> listReplenishes(@Param("param") ReportOemProductDifferenceSearchInDto reportOemProductDifferenceSearchInDto);
}
