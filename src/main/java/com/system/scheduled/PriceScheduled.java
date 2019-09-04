package com.system.scheduled;

import com.system.entity.TBReport;
import com.system.entity.TBSservice;
import com.system.mapper.ServiceMapper;
import com.system.mapper.price.PriceMapper;
import com.system.service.ReportService;
import com.system.utils.BigDecimalUtils;
import com.system.utils.excel.ExcelUtils;
import com.system.vo.ReportExportOutVo;
import com.system.vo.price.CustomerProfileVo;
import com.system.vo.price.PriceExportOutVo;
import com.system.vo.price.PriceVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by deweydu
 * Date on 2019年8月27日14:56:17
 */
@Slf4j
@Component
public class PriceScheduled {

    private static final String STR12 = "-12";
    private static final String STR13 = "-13";

    private Date currentTime = new Date();
    @Value("${outPath}")
    private String outPath;
    @Autowired
    private PriceMapper priceMapper;
    private static final SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduled() throws FileNotFoundException, IllegalAccessException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("=====>>>>>开始导出...  {}", format.format(currentTimeMillis));
        CustomerProfileVo vo = new CustomerProfileVo();
//        vo.setId(100L);
        List<CustomerProfileVo> customerList = priceMapper.getCustomerList( vo);
        Map<String, CustomerProfileVo> customerMap = customerList.stream().
                collect(Collectors.toMap(CustomerProfileVo::getEntityInternalCode, Function.identity(), (s1, s2) -> s1));
        List<PriceExportOutVo> allVos = new ArrayList<>(35000);
        for (CustomerProfileVo c : customerList) {
            String priceBatchCode = getPriceBatchCode();
            String entitySubCode = c.getEntitySubCode();
            String entityInternalCode = c.getEntityInternalCode();
            String internal_12 = entityInternalCode + STR12;
            String internal_13 = entityInternalCode + STR13;
            CustomerProfileVo vo1 = customerMap.get(internal_12);
            CustomerProfileVo vo2 = customerMap.get(internal_13);
            //如果vo1不是空，则执行lambda表达式v ->updatePrice(allVos, priceBatchCode, entitySubCode, v)
            //表达式中的 v 代表判断的对象  vo1 或者 vo2
            Optional.ofNullable(vo1).ifPresent(v ->updatePrice(allVos, priceBatchCode, entitySubCode, v));
            Optional.ofNullable(vo2).ifPresent(v -> updatePrice(allVos, priceBatchCode, entitySubCode, v));
        }
        String fileName = String.format("价格数据.xlsx");
        File file = new File(outPath + fileName);
        FileOutputStream os;
        os = new FileOutputStream(file);
        ExcelUtils.write2007(allVos, PriceExportOutVo.class, fileName, os);
        log.info("总耗时【{}】", (System.currentTimeMillis() - currentTimeMillis)/1000);
    }

    private void updatePrice(List<PriceExportOutVo> allVos, String priceBatchCode, String entitySubCode, CustomerProfileVo vo1) {
        String entityCode_;
        String entitySubCode_;
        List<PriceVo> priceList;
        entityCode_ = vo1.getEntityCode();
        entitySubCode_ = vo1.getEntitySubCode();
        priceList = getPriceList(entitySubCode);
        if (CollectionUtils.isEmpty(priceList)) {
            return;
        }
        updatePriceList(allVos, priceBatchCode, priceList, entityCode_, entitySubCode_);
    }

    private void updatePriceList(List<PriceExportOutVo> allVos, String priceBatchCode, List<PriceVo> priceList, String entityCode_, String entitySubCode_) {
        priceList.forEach(s->{
            PriceExportOutVo outVo = new PriceExportOutVo();
            s.setId(null);
            s.setEntityCode(entityCode_);
            s.setEntitySubCode(entitySubCode_);
            s.setPriceBatchCode(priceBatchCode);
            s.setStartTime(currentTime);
            s.setLatestFlg("0");
            s.setSysCreatedTime(new Date());
            s.setSysCreatedOperatorName("金晓鸣");
            BeanUtils.copyProperties(s,outVo);
            allVos.add(outVo);
        });
        priceMapper.insertSelectiveBatch(priceList);
    }

    /**
     * 根据entity_sub_code获取到对应的商品价格记录
     */
    private List<PriceVo> getPriceList(String entitySubCode) {
        CustomerProfileVo vo = new CustomerProfileVo();
        vo.setEntitySubCode(entitySubCode);
        List<PriceVo>  priceList = priceMapper.getPriceList(vo);
        return priceList;
    }

    public String getPriceBatchCode() {
        return this.getTimestampCode("PBC");
    }

    private String getTimestampCode(String prefix) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateString = formatter.format(currentTime);
        String temeCode = prefix + dateString;
        return temeCode;
    }
}
