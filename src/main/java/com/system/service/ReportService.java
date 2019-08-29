package com.system.service;

import com.system.entity.TBReport;
import com.system.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by deweydu
 * Date on 2019/8/22 17:33
 */
@Service
public class ReportService {

    @Autowired
    private ReportMapper reportMapper;

    public List<TBReport> getList(TBReport report) {
        return reportMapper.getList(report);
    }
}
