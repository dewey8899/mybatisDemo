package com.system.utils.excel.v3x;

import com.system.utils.excel.Column;
import com.system.utils.excel.v3x.annotations.MSExcelCell;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MSExcelHelper {

//    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0");// 格式化 number为整
//
//    private static final DecimalFormat DECIMAL_FORMAT_PERCENT = new DecimalFormat("##.00%");//格式化分比格式，后面不足2位的用0补齐

//  private static final DecimalFormat df_per_ = new DecimalFormat("0.00%");//格式化分比格式，后面不足2位的用0补齐,比如0.00,%0.01%

//  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // 格式化日期字符串

//    private static final DateFormat FAST_DATE_FORMAT = DateFormat.getDateTimeInstance();
//
//    private static final DecimalFormat DECIMAL_FORMAT_NUMBER  = new DecimalFormat("0.00E000"); //格式化科学计数器
//
//    private static final Pattern DECIMAL_PATTERN = Pattern.compile("0.0+_*[^/s]+"); //小数匹配

    protected InputStream inputStream;

    protected Workbook workbook;

    protected MSExcelFileType msExcelFileType = MSExcelFileType.invalid;

    protected FormulaEvaluator evaluator;

    protected int rowAccessWindowSize;

    protected int sheetSize;

    protected List<MSExcelError> msExcelErrorList;

    public List<MSExcelError> getMsExcelErrorList() {
      return msExcelErrorList;
    }

    public static MSExcelHelper build(File file, int rowAccessWindowSize) throws IOException, InvalidFormatException {
        MSExcelHelper msExcelHelper = new MSExcelHelper(null, rowAccessWindowSize);
        msExcelHelper.workbook = WorkbookFactory.create(file);
        msExcelHelper.evaluator = msExcelHelper.workbook.getCreationHelper().createFormulaEvaluator();
        msExcelHelper.sheetSize = msExcelHelper.workbook.getNumberOfSheets();
        msExcelHelper.msExcelErrorList = new LinkedList<>();
        return msExcelHelper;
    }

    public static MSExcelHelper build(File file) throws IOException, InvalidFormatException {
        return MSExcelHelper.build(file, 0);
    }

    public static MSExcelHelper build(InputStream inputStream, int rowAccessWindowSize){
        MSExcelHelper msExcelHelper = new MSExcelHelper(inputStream, rowAccessWindowSize);
        msExcelHelper.init();
        return msExcelHelper;
    }

    public static MSExcelHelper build(InputStream inputStream){
        return MSExcelHelper.build(inputStream, 0);
    }

    private MSExcelHelper(InputStream inputStream, int rowAccessWindowSize){
        this.inputStream = inputStream;
        this.rowAccessWindowSize = rowAccessWindowSize;
    }

    protected void init(){
        if(this.workbook == null){
            try{
                this.workbook = new XSSFWorkbook(this.inputStream);
                if(rowAccessWindowSize > 0){
                    this.workbook = new SXSSFWorkbook((XSSFWorkbook)this.workbook, rowAccessWindowSize);
                }
                msExcelFileType = MSExcelFileType.excel2007;
            } catch (OLE2NotOfficeXmlFileException e){
                log.warn(e.getMessage(), e);
                // pass through
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                IOUtils.closeQuietly(this.inputStream);
            }
        }

        if(this.workbook == null){
            try{
                this.workbook = new HSSFWorkbook(this.inputStream);
                msExcelFileType = MSExcelFileType.excel2003;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                IOUtils.closeQuietly(this.inputStream);
            }
        }

        if(this.workbook == null){
            IOUtils.closeQuietly(inputStream);
            throw new MSExcelInvalidFileException("input stream is not excel stream.");
        }
        this.evaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
        this.sheetSize = this.workbook.getNumberOfSheets();
        this.msExcelErrorList = new LinkedList<>();
    }

    public Workbook getWorkbook(){
        return this.workbook;
    }

    public MSExcelHelper setActiveSheet(int sheetIndex){
        this.workbook.setActiveSheet(sheetIndex);
        return this;
    }

    public List<List<List<Object>>> read(){
        List<List<List<Object>>> list = new LinkedList<>();
        try{
            Iterator<Sheet> sheetIterator = this.workbook.sheetIterator();
            while(sheetIterator.hasNext()){
                list.add(this.read(sheetIterator.next()));
            }
        }finally {
        }
        return list;
    }

    public List<List<Object>> read(Sheet sheet){
        List<List<Object>> dataList = new LinkedList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            List<Object> linkList = new LinkedList<>();
            if(row == null){
                dataList.add(linkList);
                continue;
            }else{
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    if(cell == null){
                        linkList.add(null);
                        continue;
                    }else{
                        Object value = this.getCellValue(cell);
                        linkList.add(value);
                    }
                }
            }
            dataList.add(linkList);
        }
        return dataList;
    }

    public <T> List<T> read(Class<T> clazz, boolean ignoreError) throws IllegalAccessException, InstantiationException {
        return this.read(this.workbook.getActiveSheetIndex(), 0, clazz, ignoreError);
    }

    public <T> List<T> read(int sheetIndex, Class<T> clazz, boolean ignoreError) throws IllegalAccessException, InstantiationException {
        return this.read(sheetIndex, 0, clazz, ignoreError);
    }

    public <T> List<T> read(int sheetIndex, int fromRowIndex, Class<T> clazz, boolean ignoreError) throws IllegalAccessException, InstantiationException {
        List<T> dataList = new LinkedList<>();
        Field [] fields = clazz.getDeclaredFields();
        List<Field> fieldList = Arrays.asList(fields == null ? new Field[0] : fields)
                .stream()
                .filter(p->(p.getAnnotation(Column.class) != null))
                .collect(Collectors.toList());
        int fieldSize = fieldList.size();
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        int rowSize = sheet.getPhysicalNumberOfRows();
        for(int rowIndex = fromRowIndex; rowIndex < rowSize; rowIndex++){
            T newInstance = clazz.newInstance();
            Row row = sheet.getRow(rowIndex);
            if(row == null){
                for(int colIndex = 0; colIndex < fieldSize; colIndex ++){
                    Field field = fields[colIndex];
                    field.setAccessible(true);
                    field.set(newInstance, null);
                }
            }else{
                int colSize = row.getLastCellNum();
                for(int colIndex = 0; colIndex < fieldSize; colIndex ++){
                    Field field = fields[colIndex];
                    field.setAccessible(true);
                    Column column = field.getAnnotation(Column.class);
                    if(column != null
                            &&  column.index() < colSize){
                        try{
                            Object value = this.getCellValue(sheet, rowIndex, column.index());
                            String fieldTypeName = field.getType().getName();
                            if(value == null){
                                field.set(newInstance, value);
                            }else{
                                if(Integer.class.getName().equals(fieldTypeName)){
                                    field.set(newInstance,  (new BigDecimal(String.valueOf(value))).intValue());
                                }else if(String.class.getName().equals(fieldTypeName)){
                                    field.set(newInstance,  String.valueOf(value));
                                }else if(BigDecimal.class.getName().equals(fieldTypeName)){
                                    field.set(newInstance, new BigDecimal(String.valueOf(value)));
                                }else if(Date.class.getName().equals(fieldTypeName)){
                                    field.set(newInstance, value);
                                }else{
                                    field.set(newInstance, value);
                                }
                            }
                        }catch (Exception e){
                            String error;
                            if(e instanceof NumberFormatException){
                                error = "数字格式异常";
                            }else if(e instanceof ClassCastException){
                                error = "类转换异常";
                            }else if( e instanceof IllegalArgumentException){
                                error = "非法参数异常";
                            }else{
                                error = "未知异常";
                            }
                            log.warn(error);
                            MSExcelError msExcelError = new MSExcelError(rowIndex, colIndex, error, field, e);
                            msExcelErrorList.add(msExcelError);
                            if(!ignoreError){
                                throw e;
                            }
                        }
                    }else{
                        field.set(newInstance, null);
                    }
                }
                dataList.add(newInstance);
            }
        }
        return dataList;
    }

    public void close(){
        IOUtils.closeQuietly(this.workbook);
        IOUtils.closeQuietly(this.inputStream);
    }

    public void addRow(int sheetIndex, int fromRowIndex, int rows){
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        sheet.shiftRows(fromRowIndex + 1, sheet.getLastRowNum(), rows);
    }

    public void setCell(int sheetIndex, int rowIndex, int colIndex, CellType cellType, CellStyle cellStyle, Object value){
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        Row row = sheet.getRow(rowIndex);
        if(row == null){
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.createCell(colIndex, cellType);
        if(cellStyle != null){
            cell.setCellStyle(cellStyle);
        }
        this.setCellValue(cell, value);
    }

    public void setCell(int sheetIndex, int rowIndex, int colIndex, CellType cellType, Object value){
        this.setCell(sheetIndex, rowIndex, colIndex, cellType, null, value);
    }

    protected void setCellValue(Cell cell, Object value){
        if(cell != null){
            switch (cell.getCellTypeEnum()) {
                case _NONE:
                    break;
                case BLANK:
                case NUMERIC:
                    if(value instanceof Date){
                        cell.setCellValue((Date)value);
                    }else if(value instanceof  Number){
                        cell.setCellValue(((Number) value).doubleValue());
                    }else{
                        cell.setCellValue(String.valueOf(value));
                    }
                    break;
                case STRING:
                    cell.setCellValue((String)value);
                    break;
                case BOOLEAN:
                    cell.setCellValue((Boolean) value);
                    break;
                default:
                    // pass through
            }
        }
    }

    public int getRowSize(int sheetIndex){
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        return sheet.getPhysicalNumberOfRows();
    }

    public Object getCellValue(int sheetIndex, int rowIndex, int colIndex){
        return this.getCellValue(this.workbook.getSheetAt(sheetIndex), rowIndex,  colIndex);
    }

    public Object getCellValue(Sheet sheet, int rowIndex, int colIndex){
        Row row = sheet.getRow(rowIndex);
        if(row == null || colIndex > row.getLastCellNum()){
            return null;
        }
        Cell cell = row.getCell(colIndex);
        if(cell == null){
            return null;
        }
        Object value;
        switch (cell.getCellTypeEnum()) {
            case FORMULA:
                value = this.getCellValue(evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
            case STRING:
            case BLANK:
            case BOOLEAN:
            case ERROR:
            default:
                value = this.getCellValue(cell);
        }
        return value;
    }

    protected Object getCellValue(Cell cell){
        if(cell == null){
            return null;
        }
        Object value = null;
        switch (cell.getCellTypeEnum()) {
            case FORMULA:
                value = this.getCellValue(evaluator.evaluateInCell(cell));
                break;
            case _NONE:
                break;
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    value = cell.getDateCellValue();
                }else{
                    value = cell.getNumericCellValue();
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BLANK:
                break;
            case BOOLEAN:
                value = Boolean.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                value = FormulaError.forInt(cell.getErrorCellValue()).getString();
                break;
            default:
                value = "UNKNOWN value of type " + cell.getCellTypeEnum();
        }
        return value;
    }

    public void write(OutputStream outputStream) throws IOException {
        this.workbook.write(outputStream);
        this.close();
        IOUtils.closeQuietly(outputStream);
    }

    public void write(OutputStream outputStream, int rowAccessWindowSize) throws IOException {
        if(this.isExcel2007()){
            this.workbook = new SXSSFWorkbook((XSSFWorkbook)this.workbook, rowAccessWindowSize);
        }
        this.workbook.write(outputStream);
        outputStream.flush();
        this.close();
        IOUtils.closeQuietly(outputStream);
    }

    public static void write(OutputStream outputStream, String sheetName, List<?> dataList, MSExcelFileType msExcelFileType, int rowAccessWindowSize) throws IllegalAccessException {
        write(outputStream, new HashSet<>(Arrays.asList(new String[]{sheetName})), Arrays.asList(new List []{dataList}), msExcelFileType, rowAccessWindowSize);
    }



    public static void write(OutputStream outputStream, Set<String> sheetNameList, List<List<?>> sheetDataList, MSExcelFileType msExcelFileType, int rowAccessWindowSize) throws IllegalAccessException {
        Workbook workbook = null;
        try{

            int sheetNameSize = sheetNameList.size();
            int sheetDataSize = sheetDataList.size();
            if(sheetNameSize != sheetDataSize){
                throw new IllegalAccessException(String.format("工作表的数量【%s】，工作表的数据结构的数量【%s】不相同。", sheetNameSize, sheetDataSize));
            }

            if(MSExcelFileType.excel2003.equals(msExcelFileType)){
                workbook = new HSSFWorkbook();
            }else{
                if(rowAccessWindowSize > 0){
                    workbook = new SXSSFWorkbook(rowAccessWindowSize);
                }else{
                    workbook = new XSSFWorkbook();
                }
            }

            // 新建sheet
            Iterator<String> sheetNameIterator = sheetNameList.iterator();
            while(sheetNameIterator.hasNext()){
                workbook.createSheet(sheetNameIterator.next());
            }

            int sheetSize = sheetNameList.size();
            for(int sheetIndex = 0; sheetIndex < sheetSize; sheetIndex ++){
                List<?> dataList = sheetDataList.get(sheetIndex);
                int dataListSize = dataList.size();
                Field [] fields = new Field[0];
                if(dataListSize > 0){
                    Object data = dataList.get(0);
                    fields = data.getClass().getDeclaredFields();
                }
                Map<Integer, Field> fieldMap = new HashMap<>();
                Map<Integer, Column> msExcelColumnMap = new HashMap<>();
                Map<Map<Integer, Integer>, MSExcelCell> msExcelCellMap = new HashMap<>();
                for(int i = 0, size = fields.length; i < size; i ++){
                    Field field = fields[i];
                    Column column = field.getAnnotation(Column.class);
                    if(column != null){
                        Integer index = Integer.valueOf(column.index());
                        msExcelColumnMap.put(index, column);
                        fieldMap.put(index, field);
                    }
                    MSExcelCell[] msExcelCells = field.getAnnotationsByType(MSExcelCell.class);
                    if(column != null && msExcelCells != null && msExcelCells.length > 0){
                        for(int j = 0; j < msExcelCells.length; j ++){
                            MSExcelCell msExcelCell = msExcelCells[j];
                            msExcelCellMap.put(new HashMap<>(Integer.valueOf(msExcelCell.rowIndex()), Integer.valueOf(column.index())), msExcelCell);
                        }
                    }
                }

                List<Column> columnList = new ArrayList<>(msExcelColumnMap.values())
                        .stream().sorted(Comparator.comparingInt(Column::index)).collect(Collectors.toList());
                int colSize = columnList.size();
                colSize = colSize == 0 ? colSize : columnList.get(colSize - 1).index() + 1; // 数据列数
                int rowSize = dataListSize; // 数据行数

                write(workbook.getSheetAt(sheetIndex), dataList, rowSize, colSize, fieldMap, msExcelColumnMap, msExcelCellMap, false);
            }

            workbook.setActiveSheet(0);
            workbook.write(outputStream);
        } catch (IOException e) {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(outputStream);
        }finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    private static void write(Sheet sheet, List<?> dataList, int rowSize, int colSize, Map<Integer, Field> fieldMap, Map<Integer, Column> msExcelColumnMap, Map<Map<Integer, Integer>, MSExcelCell> msExcelCellMap, boolean ignoreTitle) throws IllegalAccessException {
        Workbook workbook = sheet.getWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();
        ClientAnchor clientAnchor = creationHelper.createClientAnchor();
        if(!ignoreTitle){
            Row row = sheet.createRow(0);
            for(int colIndex = 0; colIndex < colSize; colIndex ++){
                Cell cell = row.createCell(colIndex);
                Column column = msExcelColumnMap.get(Integer.valueOf(colIndex));
                String title = column == null ? null : column.title();
                cell.setCellValue(title);
            }
        }

        // 设定列宽
        for(int colIndex = 0; colIndex < colSize; colIndex ++){
            Integer colIndexInteger = Integer.valueOf(colIndex);
            Column column = msExcelColumnMap.get(colIndexInteger);
            Integer width;
            if(column == null){
                width = null;
            }else{
                width = column.width() < 0 ? null : Integer.valueOf(column.width());
            }
            if(width != null){
                sheet.setColumnWidth(colIndex, width);
            }
        }

        // styles
        Map<Integer, CellStyle> columnStyleMap = new HashMap<>();
        for(int colIndex = 0; colIndex < colSize; colIndex ++){
            Integer colIndexInteger = Integer.valueOf(colIndex);
            Column column = msExcelColumnMap.get(colIndexInteger);
            String format;
            if(column == null){
                format = null;
            }else{
                format = column.format();
            }
            // format
            if(Utils.isNotEmpty(format)){
                CellStyle cellStyle = workbook.createCellStyle();
                DataFormat dataFormat = workbook.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat(format));
                columnStyleMap.put(colIndexInteger, cellStyle);
            }
        }


        for(int rowIndex = 0; rowIndex < rowSize; rowIndex ++){
            Integer rowIndexInteger = Integer.valueOf(rowIndex);
            Row row = sheet.createRow(ignoreTitle ? rowIndex : rowIndex + 1);
            Object data = dataList.get(rowIndex);
            for(int colIndex = 0; colIndex < colSize; colIndex ++){
                Integer colIndexInteger = Integer.valueOf(colIndex);
                Cell cell = row.createCell(colIndex);
                Field field = fieldMap.get(colIndexInteger);
                if(field != null){
                    field.setAccessible(true);
                    CellStyle cellStyle = columnStyleMap.get(colIndex);
                    if(cellStyle != null){
                        cell.setCellStyle(cellStyle);
                    }
                    Column column = msExcelColumnMap.get(colIndexInteger);
                    if(column != null){
                        Map<Integer, Integer> cellCoordination = new HashMap<>();
                        cellCoordination.put(rowIndexInteger, colIndex);
                        MSExcelCell msExcelCell = msExcelCellMap.get(cellCoordination);
                        if(msExcelCell != null){
                            String author = msExcelCell.author();
                            String comment = msExcelCell.comment();
                            String format =  msExcelCell.format();
                            // format
                            if(Utils.isNotEmpty(format)){
                                cellStyle = workbook.createCellStyle();
                                DataFormat dataFormat = workbook.createDataFormat();
                                cellStyle.setDataFormat(dataFormat.getFormat(format));
                                cell.setCellStyle(cellStyle);
                            }
                            // comment
                            if(Utils.isNotEmpty(comment)){
                                Drawing drawing = sheet.createDrawingPatriarch();
                                Comment cellComment = drawing.createCellComment(clientAnchor);
                                cellComment.setAuthor(author);
                                cellComment.setString(creationHelper.createRichTextString(comment));
                                cell.setCellComment(cellComment);
                            }
                        }
                    }
                    Object value = field.get(data);
                    if(value instanceof Number){
                        cell.setCellValue(Double.parseDouble(String.valueOf(value)));
                    }else if(value instanceof String){
                        cell.setCellValue(String.valueOf(value));
                        // formula
                    }else if(value instanceof Date){
                        cell.setCellValue((Date)value);
                    }else if(value instanceof Boolean){
                        cell.setCellValue((Boolean)value);
                    }
                }
            }
        }
    }

    public boolean isExcel2007() {
        return MSExcelFileType.excel2007.equals(this.msExcelFileType);
    }

    public boolean isExcel2003() {
        return MSExcelFileType.excel2003.equals(this.msExcelFileType);
    }

    public enum MSExcelFileType {
        excel2003, excel2007, invalid;
    }

    public static class MSExcelError{
        private int rowIndex;
        private int colIndex;
        private String error;
        private Field field;
        private Throwable throwable;

        public MSExcelError(int rowIndex, int colIndex, String error, Field field, Throwable throwable){
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
            this.error = error;
            this.field = field;
            this.throwable = throwable;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColIndex() {
            return colIndex;
        }

        public String getError() {
            return error;
        }

        public Field getField() {
            return field;
        }

        public Throwable getThrowable() {
            return throwable;
        }
        
        @Override
        public String toString(){
          return String.format("第%s行%s列错误：%s", this.rowIndex + 1, this.colIndex + 1, this.error);
        }
    }

    private static class Utils{

        public static boolean isNotEmpty(String value){
            return value != null && !"".equals(value);
        }
    }
}
