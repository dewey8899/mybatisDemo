package com.system.scheduled;

import com.system.entity.TBReport;
import com.system.entity.TBSservice;
import com.system.mapper.ServiceMapper;
import com.system.service.ReportService;
import com.system.utils.BigDecimalUtils;
import com.system.utils.excel.ExcelUtils;
import com.system.vo.ReportExportOutVo;
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

/**
 * Created by deweydu
 * Date on 2019年8月22日15:56:12
 */
@Slf4j
@Component
public class ReportScheduledService {

    @Value("${outPath}")
    private String outPath;
    @Value("${deliveryCoefficient}")
    private BigDecimal deliveryCoefficient;
    @Value("${replenishCoefficient}")
    private BigDecimal replenishCoefficient;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ServiceMapper serviceMapper;
    private static final SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

//    @Scheduled(cron = "0 0/1 * * * ?")//每分钟执行一次
//    @Scheduled(cron = "0 25 14 * * ?")//14:18:30 秒执行
    public void scheduled() throws FileNotFoundException, IllegalAccessException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("=====>>>>>开始导出...  {}", format.format(currentTimeMillis));
        TBSservice service = new TBSservice();
        List<TBSservice> tbServiceList = serviceMapper.getList(service);
        TBReport tbReport = new TBReport();
        List<TBReport> list1 = reportService.getList(tbReport);
        if (CollectionUtils.isEmpty(tbServiceList)) {
            log.error("没有数据导出");
            return;
        }
        Set<String> set = new HashSet<>();
        List<ReportExportOutVo> allVos = new ArrayList<>(30000);
        String fileName = String.format("报表数据.xlsx");
        File file = new File(outPath + fileName);
        FileOutputStream os = null;
        os = new FileOutputStream(file);
        tbServiceList.forEach(s->{
            TBReport report = new TBReport();
            report.setBrandName(s.getRoleMark());
            report.setServiceCode(s.getServiceCode());
            List<TBReport> list = reportService.getList(report);
            if (CollectionUtils.isEmpty(list)){
                return;
            }else {
                List<ReportExportOutVo> vos = new ArrayList<>(30000);
                list.forEach(t->{
                    updateData(vos, t);
                });
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(new File(outPath + String.format(s.getRoleMark()+"_"+s.getServiceCode()+".xlsx")));
                } catch (FileNotFoundException e ) {
                    e.printStackTrace();
                }
                try {
                    ExcelUtils.write2007(vos, ReportExportOutVo.class, fileName, outputStream);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        });
//        Integer size = new Integer(0);
//        Map<String, String> map = new HashMap<>(tbServiceList.size());
//        Map<String, String> reportMap = new HashMap<>(list1.size());
//        tbServiceList.forEach(s->{
//            String key = String.format(s.getRoleMark()+":"+s.getServiceCode());
//            map.put(key,key);
//        });
//        list1.forEach(s->{
//            String key = String.format(s.getBrandName()+":"+s.getServiceCode());
//            String s1 = map.get(key);
//            if (s1 == null){
//                reportMap.put(key,key);
//            }
//        });

//        for(Map.Entry<String,String> m:map.entrySet()){
//            String key = m.getKey();
//            String[] split = key.split(":");
//            String roleMark = split[0];
//            String serviceCode = split[1];
//            TBReport report = new TBReport();
//            report.setServiceCode(serviceCode);
//            report.setBrandName(roleMark);
//            List<TBReport> list = reportService.getList(report);
//            if (CollectionUtils.isEmpty(list)) {
//                set.add(String.format(roleMark + "_" + serviceCode));
//            } else {
//                size += list.size();
//                list.forEach(s -> {
//                    ReportExportOutVo vo = new ReportExportOutVo();
//                    BeanUtils.copyProperties(s, vo);
//                    //配送总代含税总金额
//                    BigDecimal deliveryAgencyAmount = vo.getDeliveryAgencyAmount();
//                    //配货数量
//                    Integer deliveryQuantity = vo.getDeliveryQuantity();
//                    vo.setDeliveryCoefficient(deliveryCoefficient);
//                    //配货经销商含税总金额
//                    BigDecimal deliveryDealerAmount = BigDecimalUtils.multiply(deliveryAgencyAmount, deliveryCoefficient);
//                    vo.setDeliveryDealerAmount(deliveryDealerAmount);
//                    if (deliveryQuantity != null && deliveryQuantity.intValue() != 0) {
//                        BigDecimal delQua = new BigDecimal(deliveryQuantity);
//                        vo.setDeliveryAgencyPrice(BigDecimalUtils.divide(deliveryAgencyAmount, delQua, 2, BigDecimal.ROUND_HALF_UP));
//                        vo.setDeliveryDealerPrice(BigDecimalUtils.divide(deliveryDealerAmount, delQua, 2, BigDecimal.ROUND_HALF_UP));//配货经销商单价
//                    }
//                    //补货总代含税总金额
//                    BigDecimal replenishAgencyAmount = vo.getReplenishAgencyAmount();
//                    //补货数量
//                    Integer replenishQuantity = vo.getReplenishQuantity();
//                    vo.setReplenishCoefficient(replenishCoefficient);
//                    BigDecimal replenishDealerAmount = BigDecimalUtils.multiply(replenishAgencyAmount, deliveryCoefficient);
//                    //补货经销商含税总金额
//                    vo.setReplenishDealerAmount(replenishDealerAmount);
//                    if (replenishQuantity != null && replenishQuantity.intValue() != 0) {
//                        BigDecimal repQua = new BigDecimal(replenishQuantity);
//                        vo.setReplenishDealerPrice(BigDecimalUtils.divide(replenishAgencyAmount, repQua, 2, BigDecimal.ROUND_HALF_UP));
//                        vo.setReplenishAgencyPrice(BigDecimalUtils.divide(replenishDealerAmount, repQua, 2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    vos.add(vo);
//                });
//            }
////                String fileName = String.format(roleMark+"_"+serviceCode+".xlsx");
////                File file = new File(outPath+fileName);
////                FileOutputStream os = null;
////                try {
////                    os = new FileOutputStream(file);
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                }
////                try {
////                    ExcelUtils.write2007(vos, ReportExportOutVo.class, fileName, os);
////                } catch (IllegalAccessException e) {
////                    e.printStackTrace();
////                }
////                log.info("{}导出结束。耗时【{}】", i.getAndIncrement(),System.currentTimeMillis() - currentTim);
//        }
        list1.forEach(s -> {
            updateData(allVos, s);
        });
        ExcelUtils.write2007(allVos, ReportExportOutVo.class, fileName, os);
        log.info("总耗时【{}】", (System.currentTimeMillis() - currentTimeMillis)/1000);
//        log.info("共有数据：{},没有配送单和补货单的服务商有：{}个",size,set.size());
//        for (String s : set) {
//            log.info("{}；", s);
//        }
//        for(Map.Entry<String,String> m:reportMap.entrySet()){
//            log.info("{}；", m.getKey());
//        }
    }

    private void updateData(List<ReportExportOutVo> vos, Object s) {

        ReportExportOutVo vo = new ReportExportOutVo();
        BeanUtils.copyProperties(s, vo);
        //配送总代含税总金额
        BigDecimal deliveryAgencyAmount = vo.getDeliveryAgencyAmount();
        //配货数量
        Integer deliveryQuantity = vo.getDeliveryQuantity();
        vo.setDeliveryCoefficient(deliveryCoefficient);
        //配货经销商含税总金额
        BigDecimal deliveryDealerAmount = BigDecimalUtils.multiply(deliveryAgencyAmount, deliveryCoefficient);
        vo.setDeliveryDealerAmount(deliveryDealerAmount);
        if (deliveryQuantity != null && deliveryQuantity.intValue() != 0) {
            BigDecimal delQua = new BigDecimal(deliveryQuantity);
            vo.setDeliveryAgencyPrice(BigDecimalUtils.divide(deliveryAgencyAmount, delQua, 2, BigDecimal.ROUND_HALF_UP));
            vo.setDeliveryDealerPrice(BigDecimalUtils.divide(deliveryDealerAmount, delQua, 2, BigDecimal.ROUND_HALF_UP));//配货经销商单价
        }
        //补货总代含税总金额
        BigDecimal replenishAgencyAmount = vo.getReplenishAgencyAmount();
        //补货数量
        Integer replenishQuantity = vo.getReplenishQuantity();
        vo.setReplenishCoefficient(replenishCoefficient);
        BigDecimal replenishDealerAmount = BigDecimalUtils.multiply(replenishAgencyAmount, deliveryCoefficient);
        //补货经销商含税总金额
        vo.setReplenishDealerAmount(replenishDealerAmount);
        if (replenishQuantity != null && replenishQuantity.intValue() != 0) {
            BigDecimal repQua = new BigDecimal(replenishQuantity);
            vo.setReplenishDealerPrice(BigDecimalUtils.divide(replenishAgencyAmount, repQua, 2, BigDecimal.ROUND_HALF_UP));
            vo.setReplenishAgencyPrice(BigDecimalUtils.divide(replenishDealerAmount, repQua, 2, BigDecimal.ROUND_HALF_UP));
        }
        vos.add(vo);
    }
}
