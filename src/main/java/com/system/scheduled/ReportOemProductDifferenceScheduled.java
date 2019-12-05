package com.system.scheduled;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.system.mapper.ReportOemProductDifferenceMapper;
import com.system.mapper.price.PriceMapper;
import com.system.utils.BigDecimalUtils;
import com.system.utils.IntegerUtils;
import com.system.utils.excel.ExcelUtils;
import com.system.utils.excel.v3x.MSExcelHelper;
import com.system.vo.out.vo.QuantityVo;
import com.system.vo.price.CustomerProfileVo;
import com.system.vo.price.PriceExportOutVo;
import com.system.vo.tb_report_oem_report_difference.ReportOemProductDifferenceGetDataTotalOutDto;
import com.system.vo.tb_report_oem_report_difference.ReportOemProductDifferenceSearchInDto;
import com.system.vo.tb_report_oem_report_difference.ReportOemProductDifferenceSearchOutDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by deweydu
 * Date on 2019/11/12 14:34
 */
@Slf4j
@Component
public class ReportOemProductDifferenceScheduled {

    @Value("${reportOemProductDifferencePath}")
    private String reportOemProductDifferencePath;
    @Autowired
    private ReportOemProductDifferenceMapper reportOemProductDifferenceMapper;
    private static final SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduled() throws FileNotFoundException {
        long currentTimeMillis = System.currentTimeMillis();
        List<ReportOemProductDifferenceSearchOutDto> outDtos = new ArrayList<>();
        log.info("Dewey attention=====>>>>>开始导出...  {}", format.format(currentTimeMillis));
        ReportOemProductDifferenceSearchInDto inDto = new ReportOemProductDifferenceSearchInDto();
        List<ReportOemProductDifferenceSearchOutDto> search = reportOemProductDifferenceMapper.search(inDto);
        List<ReportOemProductDifferenceSearchOutDto> listDeliveries = reportOemProductDifferenceMapper.listDeliveries(inDto);
        List<ReportOemProductDifferenceSearchOutDto> listReplenishes = reportOemProductDifferenceMapper.listReplenishes(inDto);

        if (CollectionUtils.isEmpty(search) && CollectionUtils.isEmpty(listDeliveries) && CollectionUtils.isEmpty(listReplenishes)) {
            return;
        }
        //所有数据源 用 MaterialNumber + TaskOrderReplenishmentAccountRuleType + DateType 作为 key  -> listAll
        List<String> listAll = new ArrayList<>();
        ReportOemProductDifferenceSearchOutDto firstOutDto = new ReportOemProductDifferenceSearchOutDto();
        outDtos.add(firstOutDto);
        List<ReportOemProductDifferenceSearchOutDto> dtos = new ArrayList<>();
        Set<String> searchSet = search.stream().map(p -> p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode() + p.getMaterialNumber() + p.getTaskOrderReplenishmentAccountRuleType()).collect(Collectors.toSet());
        Set<String> listDeliveriesSet = listDeliveries.stream().map(p -> p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode() + p.getMaterialNumber() + p.getTaskOrderReplenishmentAccountRuleType()).collect(Collectors.toSet());
        Set<String> listReplenishesSet = listReplenishes.stream().map(p -> p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode() + p.getMaterialNumber() + p.getTaskOrderReplenishmentAccountRuleType()).collect(Collectors.toSet());
        searchSet.addAll(listDeliveriesSet);
        searchSet.addAll(listReplenishesSet);
        listAll.addAll(searchSet);
//        所有的开始时间和结束时间查询
        List<String> listKey = new ArrayList<>();
        Set<String> searchKeySet = search.stream().map(p -> p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode()).collect(Collectors.toSet());
        Set<String> listDeliveriesKeySet = listDeliveries.stream().map(p -> p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode()).collect(Collectors.toSet());
        Set<String> listReplenishesKeySet = listReplenishes.stream().map(p -> p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode()).collect(Collectors.toSet());
        searchKeySet.addAll(listDeliveriesKeySet);
        searchKeySet.addAll(listReplenishesKeySet);
        listKey.addAll(searchKeySet);
        Map<String, ReportOemProductDifferenceSearchOutDto> searchMap = search.stream().collect(Collectors.toMap(p ->
                p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode() + p.getMaterialNumber() + p.getTaskOrderReplenishmentAccountRuleType(), Function.identity(), (v1, v2) -> v1));
        Map<String, ReportOemProductDifferenceSearchOutDto> listDeliveriesMap = listDeliveries.stream().collect(Collectors.toMap(p ->
                p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode() + p.getMaterialNumber() + p.getTaskOrderReplenishmentAccountRuleType(), Function.identity(), (v1, v2) -> v1));
        Map<String, ReportOemProductDifferenceSearchOutDto> listReplenishesMap = listReplenishes.stream().collect(Collectors.toMap(p ->
                p.getGeneralAgencyEntityInternalCode() + "_" + p.getServiceBusinessEntityInternalCode() + p.getMaterialNumber() + p.getTaskOrderReplenishmentAccountRuleType(), Function.identity(), (v1, v2) -> v1));
        listAll.forEach(s -> {
            ReportOemProductDifferenceSearchOutDto searchDto = new ReportOemProductDifferenceSearchOutDto();
            if (searchMap.containsKey(s)) {
                searchDto = searchMap.get(s);
                if (listDeliveriesMap.containsKey(s)) {
                    ReportOemProductDifferenceSearchOutDto deliveryDto = listDeliveriesMap.get(s);
                    if (null != deliveryDto.getDeliveryQuantity()) {
                        searchDto.setDeliveryQuantity(deliveryDto.getDeliveryQuantity());
                        BigDecimal deliveryServiceBusinessTaxExcludedAmount = searchDto.getDeliveryServiceBusinessTaxExcludedAmount();
                        BigDecimal quantity = new BigDecimal(searchDto.getDeliveryQuantity());
                        BigDecimal divide = BigDecimalUtils.divide(deliveryServiceBusinessTaxExcludedAmount, quantity, 2, BigDecimal.ROUND_HALF_UP);
                        searchDto.setDeliveryServiceBusinessTaxExcludedPrice(divide);
                        searchDto.setDeliveryGeneralAgencyTaxExcludedPrice(BigDecimalUtils.divide(searchDto.getDeliveryGeneralAgencyTaxExcludedAmount(),
                                new BigDecimal(searchDto.getDeliveryQuantity()), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
            if (listReplenishesMap.containsKey(s)) {
                ReportOemProductDifferenceSearchOutDto dto = listReplenishesMap.get(s);
                searchDto.setDateType(dto.getDateType());
                searchDto.setProductName(dto.getProductName());
                searchDto.setServiceBusinessEntityCompanyShortName(dto.getServiceBusinessEntityCompanyShortName());
                searchDto.setServiceBusinessEntityInternalCode(dto.getServiceBusinessEntityInternalCode());
                searchDto.setGeneralAgencyEntityInternalCode(dto.getGeneralAgencyEntityInternalCode());
                searchDto.setTaskOrderReplenishmentAccountRuleType(dto.getTaskOrderReplenishmentAccountRuleType());
                searchDto.setMaterialNumber(dto.getMaterialNumber());
                searchDto.setReplenishQuantity(dto.getReplenishQuantity());
                searchDto.setReplenishServiceBusinessTaxExcludedAmount(dto.getReplenishServiceBusinessTaxExcludedAmount());
                searchDto.setReplenishGeneralAgencyTaxExcludedAmount(dto.getReplenishGeneralAgencyTaxExcludedAmount());
                searchDto.setReplenishGeneralAgencyTaxExcludedPrice(dto.getReplenishGeneralAgencyTaxExcludedPrice());
                searchDto.setReplenishServiceBusinessTaxExcludedPrice(dto.getReplenishServiceBusinessTaxExcludedPrice());
            }
            dtos.add(searchDto);
        });
        Map<String, List<ReportOemProductDifferenceSearchOutDto>> collect = new HashMap<>(dtos.size());
        Map<String, String> listKeyMap = new HashMap<>(listKey.size());
        listKey.forEach(k -> {
            listKeyMap.put(k, k);
        });
        listKeyMap.forEach((k, v) -> {
            for (int i = 0; i < dtos.size(); i++) {
                ReportOemProductDifferenceSearchOutDto dto = dtos.get(i);
                String mapKey = dto.getGeneralAgencyEntityInternalCode() + "_" + dto.getServiceBusinessEntityInternalCode();
                if (k.equals(mapKey)) {
                    List<ReportOemProductDifferenceSearchOutDto> listDtos = collect.get(k);
                    if (CollectionUtils.isNotEmpty(listDtos)) {
                        listDtos.add(dto);
                    } else {
                        listDtos = new ArrayList<>();
                        listDtos.add(dto);
                        collect.put(k, listDtos);
                    }
                }
            }
        });
//        发货总量
        QuantityVo vo = new QuantityVo();
        for (Map.Entry<String, List<ReportOemProductDifferenceSearchOutDto>> map : collect.entrySet()) {
            Integer deliveryQuantity = vo.getDeliveryQuantity();
            Integer replenishQuantity = vo.getReplenishQuantity();
            exportThisMapService(map, vo);
            Integer sub = IntegerUtils.sub(vo.getDeliveryQuantity(), deliveryQuantity);
            Integer sub1 = IntegerUtils.sub(vo.getReplenishQuantity(), replenishQuantity);
            log.info("key,quantity,deliveryQuantity：{}, {}, {}, quantity,replenishQuantity: {}, {}", map.getKey(), sub, vo.getDeliveryQuantity(), sub1, vo.getReplenishQuantity());
        }
        log.info("总耗时【{}】 s", (System.currentTimeMillis() - currentTimeMillis) / 1000);
    }

    private void exportThisMapService(Map.Entry<String, List<ReportOemProductDifferenceSearchOutDto>> map,
                                      QuantityVo vo) throws FileNotFoundException {
        File templateFile = new File("e:/report_oem_product_difference.xlsx");
        MSExcelHelper outputMSExcelHelper = MSExcelHelper.build(new FileInputStream(templateFile));
        List<ReportOemProductDifferenceSearchOutDto> outDtos = new ArrayList<>();
        ReportOemProductDifferenceSearchOutDto firstOutDto = new ReportOemProductDifferenceSearchOutDto();
        outDtos.add(firstOutDto);
        String key = map.getKey();
        List<ReportOemProductDifferenceSearchOutDto> dtos = map.getValue();
        AtomicReference<Integer> deliveryQuantity = new AtomicReference<>(0);
        AtomicReference<Integer> replenishQuantity = new AtomicReference<>(0);
        AtomicReference<BigDecimal> deliveryServiceAmount = new AtomicReference<>(new BigDecimal(BigInteger.ZERO));
        AtomicReference<BigDecimal> deliveryAgencyAmount = new AtomicReference<>(new BigDecimal(BigInteger.ZERO));
        AtomicReference<BigDecimal> replenishServiceAmount = new AtomicReference<>(new BigDecimal(BigInteger.ZERO));
        AtomicReference<BigDecimal> replenishAgencyAmount = new AtomicReference<>(new BigDecimal(BigInteger.ZERO));
        dtos.forEach(s -> {
            deliveryQuantity.set(IntegerUtils.add(s.getDeliveryQuantity(), deliveryQuantity.get()));
            replenishQuantity.set(IntegerUtils.add(s.getReplenishQuantity(), replenishQuantity.get()));
            deliveryServiceAmount.set(BigDecimalUtils.add(s.getDeliveryServiceBusinessTaxExcludedAmount(), deliveryServiceAmount.get()));
            deliveryAgencyAmount.set(BigDecimalUtils.add(s.getDeliveryGeneralAgencyTaxExcludedAmount(), deliveryAgencyAmount.get()));
            replenishServiceAmount.set(BigDecimalUtils.add(s.getReplenishServiceBusinessTaxExcludedAmount(), replenishServiceAmount.get()));
            replenishAgencyAmount.set(BigDecimalUtils.add(s.getReplenishGeneralAgencyTaxExcludedAmount(), replenishAgencyAmount.get()));
            Optional.ofNullable(s.getDeliveryServiceBusinessTaxExcludedPrice()).ifPresent(bigDecimal -> {
                if (bigDecimal.compareTo(new BigDecimal("0.00")) == 0) {
                    s.setDeliveryPriceRatio("0.00%");
                } else {
                    BigDecimal deliveryPriceRatioMultiply = BigDecimalUtils.multiply(new BigDecimal("100"), s.getDeliveryGeneralAgencyTaxExcludedPrice());
                    BigDecimal deliveryPriceRatio = BigDecimalUtils.divide(deliveryPriceRatioMultiply, bigDecimal, 2, BigDecimal.ROUND_HALF_UP);
                    s.setDeliveryPriceRatio(deliveryPriceRatio.toString() + "%");
                }
            });
            Optional.ofNullable(s.getReplenishServiceBusinessTaxExcludedPrice()).ifPresent(bigDecimal -> {
                if (bigDecimal.compareTo(new BigDecimal("0.00")) == 0) {
                    s.setDeliveryPriceRatio("0.00%");
                } else {
                    BigDecimal replenishPriceRatioMultiply = BigDecimalUtils.multiply(new BigDecimal("100"), s.getReplenishGeneralAgencyTaxExcludedPrice());
                    BigDecimal replenishPriceRatio = BigDecimalUtils.divide(replenishPriceRatioMultiply, bigDecimal, 2, BigDecimal.ROUND_HALF_UP);
                    s.setReplenishPriceRatio(replenishPriceRatio.toString() + "%");
                }
            });
        });
        vo.setDeliveryQuantity(IntegerUtils.add(vo.getDeliveryQuantity(), deliveryQuantity.get()));
        vo.setReplenishQuantity(IntegerUtils.add(vo.getReplenishQuantity(), replenishQuantity.get()));
        firstOutDto.setStartDate("合计：");
        firstOutDto.setDeliveryQuantity(deliveryQuantity.get());
        firstOutDto.setReplenishQuantity(replenishQuantity.get());
        firstOutDto.setDeliveryServiceBusinessTaxExcludedAmount(deliveryServiceAmount.get());
        firstOutDto.setDeliveryGeneralAgencyTaxExcludedAmount(deliveryAgencyAmount.get());
        firstOutDto.setReplenishQuantity(replenishQuantity.get());
        firstOutDto.setReplenishServiceBusinessTaxExcludedAmount(replenishServiceAmount.get());
        firstOutDto.setReplenishGeneralAgencyTaxExcludedAmount(replenishAgencyAmount.get());
        outDtos.addAll(dtos);
        String fileName = String.format("差异报表" + key + ".xlsx");
        File file = new File(reportOemProductDifferencePath + fileName);
        FileOutputStream os = new FileOutputStream(file);
        Workbook workbook = outputMSExcelHelper.getWorkbook();
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        CellStyle cellStyleWithOutBorder = workbook.createCellStyle();
        cellStyleWithOutBorder.setFont(font);
        cellStyleWithOutBorder.setBorderTop(BorderStyle.THIN);
        cellStyleWithOutBorder.setBorderBottom(BorderStyle.THIN);
        cellStyleWithOutBorder.setBorderLeft(BorderStyle.THIN);
        cellStyleWithOutBorder.setBorderRight(BorderStyle.THIN);
        Short format0 = workbook.createDataFormat().getFormat("General");
        Short format1 = workbook.createDataFormat().getFormat("#,##0");
        Short format2 = workbook.createDataFormat().getFormat("#,##0.00");
        ReportOemProductDifferenceSearchOutDto searchOutDto = outDtos.get(0);
        ReportOemProductDifferenceGetDataTotalOutDto outDto = new ReportOemProductDifferenceGetDataTotalOutDto();
        updateOutDto(searchOutDto, outDto);
        cellStyleWithOutBorder.setDataFormat(format0);
        outputMSExcelHelper.setCell(0, 1, 4, CellType.STRING, cellStyleWithOutBorder, outDto.getDeliveryRatio());
        outputMSExcelHelper.setCell(0, 1, 9, CellType.STRING, cellStyleWithOutBorder, outDto.getReplenishRatio());
        cellStyleWithOutBorder.setDataFormat(format1);
        outputMSExcelHelper.setCell(0, 3, 4, CellType.NUMERIC, cellStyleWithOutBorder, searchOutDto.getDeliveryQuantity());
        outputMSExcelHelper.setCell(0, 3, 9, CellType.NUMERIC, cellStyleWithOutBorder, searchOutDto.getReplenishQuantity());
        cellStyleWithOutBorder.setDataFormat(format2);
        outputMSExcelHelper.setCell(0, 3, 16, CellType.NUMERIC, cellStyleWithOutBorder, outDto.getServiceLast());
        outputMSExcelHelper.setCell(0, 3, 18, CellType.NUMERIC, cellStyleWithOutBorder, outDto.getAgencyLast());
        outputMSExcelHelper.setCell(0, 3, 6, CellType.NUMERIC, cellStyleWithOutBorder, searchOutDto.getDeliveryServiceBusinessTaxExcludedAmount());
        outputMSExcelHelper.setCell(0, 3, 8, CellType.NUMERIC, cellStyleWithOutBorder, searchOutDto.getDeliveryGeneralAgencyTaxExcludedAmount());
        outputMSExcelHelper.setCell(0, 3, 11, CellType.NUMERIC, cellStyleWithOutBorder, searchOutDto.getReplenishServiceBusinessTaxExcludedAmount());
        outputMSExcelHelper.setCell(0, 3, 13, CellType.NUMERIC, cellStyleWithOutBorder, searchOutDto.getReplenishGeneralAgencyTaxExcludedAmount());
        int baseRowIndex = 4;
        int row = 0;
        int sheetIndex = 0;
        outputMSExcelHelper.addRow(0, baseRowIndex, outDtos.size() - 1);
        for (int i = 1; i < outDtos.size(); i++) {
            int column = 0;
            ReportOemProductDifferenceSearchOutDto dto = outDtos.get(i);
            cellStyleWithOutBorder.setDataFormat(format0);
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getServiceBusinessEntityInternalCode());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getServiceBusinessEntityCompanyShortName());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getMaterialNumber());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getProductName());
            cellStyleWithOutBorder.setDataFormat(format1);
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getDeliveryQuantity() == null ? "" : dto.getDeliveryQuantity());
            cellStyleWithOutBorder.setDataFormat(format2);
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getDeliveryServiceBusinessTaxExcludedPrice() == null ? "" : dto.getDeliveryServiceBusinessTaxExcludedPrice());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getDeliveryServiceBusinessTaxExcludedAmount() == null ? "" : dto.getDeliveryServiceBusinessTaxExcludedAmount());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getDeliveryGeneralAgencyTaxExcludedPrice() == null ? "" : dto.getDeliveryGeneralAgencyTaxExcludedPrice());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getDeliveryGeneralAgencyTaxExcludedAmount() == null ? "" : dto.getDeliveryGeneralAgencyTaxExcludedAmount());
            cellStyleWithOutBorder.setDataFormat(format1);
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getReplenishQuantity() == null ? "" : dto.getReplenishQuantity());
            cellStyleWithOutBorder.setDataFormat(format2);
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getReplenishServiceBusinessTaxExcludedPrice() == null ? "" : dto.getReplenishServiceBusinessTaxExcludedPrice());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getReplenishServiceBusinessTaxExcludedAmount() == null ? "" : dto.getReplenishServiceBusinessTaxExcludedAmount());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getReplenishGeneralAgencyTaxExcludedPrice() == null ? "" : dto.getReplenishGeneralAgencyTaxExcludedPrice());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.NUMERIC, cellStyleWithOutBorder, dto.getReplenishGeneralAgencyTaxExcludedAmount() == null ? "" : dto.getReplenishGeneralAgencyTaxExcludedAmount());
            cellStyleWithOutBorder.setDataFormat(format0);
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, "");
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, "");
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, "");
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, "");
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, "");
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getDeliveryPriceRatio());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getReplenishPriceRatio());
            outputMSExcelHelper.setCell(sheetIndex, baseRowIndex + row, column++, CellType.STRING, cellStyleWithOutBorder, dto.getTaskOrderReplenishmentAccountRuleType());
            row++;
        }
        try {
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            log.error("导出Excel失败。");
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private void updateOutDto(ReportOemProductDifferenceSearchOutDto searchOutDto, ReportOemProductDifferenceGetDataTotalOutDto outDto) {
        BigDecimal deliveryServiceBusinessTaxExcludedAmount = searchOutDto.getDeliveryServiceBusinessTaxExcludedAmount();
        BigDecimal replenishServiceBusinessTaxExcludedAmount = searchOutDto.getReplenishServiceBusinessTaxExcludedAmount();
        if (deliveryServiceBusinessTaxExcludedAmount.compareTo(BigDecimal.ZERO) == 0) {
            outDto.setDeliveryRatio("0.00%");
        } else {
            outDto.setDeliveryRatio(BigDecimalUtils.divide(BigDecimalUtils.multiply(new BigDecimal("100"),
                    searchOutDto.getDeliveryGeneralAgencyTaxExcludedAmount()), deliveryServiceBusinessTaxExcludedAmount, 2, BigDecimal.ROUND_HALF_UP) + "%");
        }
        if (replenishServiceBusinessTaxExcludedAmount.compareTo(BigDecimal.ZERO) == 0) {
            outDto.setReplenishRatio("0.00%");
        } else {
            outDto.setReplenishRatio(BigDecimalUtils.divide(BigDecimalUtils.multiply(new BigDecimal("100"),
                    searchOutDto.getReplenishGeneralAgencyTaxExcludedAmount()), searchOutDto.getReplenishServiceBusinessTaxExcludedAmount(), 2, BigDecimal.ROUND_HALF_UP) + "%");
        }
        outDto.setServiceLast(BigDecimalUtils.subtract(searchOutDto.getDeliveryServiceBusinessTaxExcludedAmount(), searchOutDto.getReplenishServiceBusinessTaxExcludedAmount()));
        outDto.setAgencyLast(BigDecimalUtils.subtract(searchOutDto.getDeliveryGeneralAgencyTaxExcludedAmount(), searchOutDto.getReplenishGeneralAgencyTaxExcludedAmount()));
    }


}
