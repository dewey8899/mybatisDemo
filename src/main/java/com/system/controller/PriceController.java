package com.system.controller;

import com.system.annotion.RedisCache;
import com.system.vo.ReportExportOutVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dewey.du
 * @date 2019-11-17 13:42
 */
@RestController
@Slf4j
public class PriceController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/redis_demo")
    public ReportExportOutVo getRedisData(){
        ReportExportOutVo vo = new ReportExportOutVo();
        vo.setAgencyName("dewey");
        redisTemplate.opsForValue().set("dewey",vo);
        ReportExportOutVo dewey = (ReportExportOutVo)redisTemplate.opsForValue().get("dewey");
        log.info("dewey:-->{}",dewey);
        vo.setAgencyName(dewey.getAgencyName());
        return vo;
    }

    @GetMapping(value = "/cache_demo")
    @RedisCache(key = "#userId")
    public ReportExportOutVo getByCache(@RequestParam(value = "dewey",required = false)String userId){
        ReportExportOutVo vo = new ReportExportOutVo();
        vo.setAgencyName("Attack is the best form of defense.");
        return vo;
    }

}
