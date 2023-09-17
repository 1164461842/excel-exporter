package com.yjntc.excelexporter.excel;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import com.yjntc.excelexporter.excel.def.DynamicFieldDataGetter;
import com.yjntc.excelexporter.excel.def.handler.ToStringHandler;
import com.yjntc.excelexporter.excel.def.manager.CachedRegisterHelper;
import com.yjntc.excelexporter.excel.ifce.*;
import com.yjntc.excelexporter.excel.ifce.processor.CellProcessor;
import com.yjntc.excelexporter.excel.ifce.processor.RowProcessor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WangKangSheng
 * @date 2022-04-28 18:05
 */
public class ExcelWriter<T> {

    private static final DynamicFieldDataGetter DYNAMIC_FIELD_DATA_GETTER = new DynamicFieldDataGetter();

    private static final ExcelTypeHandler<Object> DEF_HANDLER = new ToStringHandler();

    /**
     * 字段取值器
     */
    private FieldDataGetter<T> dataGetter;

    /**
     * 表头行数
     */
    private int headerRow = 0;
    /**
     * 记录导出时间
     */
    private boolean recordTime = true;
    /**
     * 是否可以更新导出时间记录
     */
    private boolean canUpdateRt = true;

    /**
     * 类型处理管理器
     */
    private TypeHandlerManager handlerManager;

    /**
     *  头数据填充之前的处理器
     */
    private final List<RowProcessor> beforeHeaderRowProcessor = new ArrayList<>();

    /**
     *  头数据填充之后的处理器
     */
    private final List<RowProcessor> afterHeaderRowProcessor = new ArrayList<>();

    /**
     * 设置内容前的 头的单元格处理器
     */
    private final List<CellProcessor> beforeHeaderCellProcessor = new ArrayList<>();

    /**
     * 设置内容后的 头的单元格处理器
     */
    private final List<CellProcessor> afterHeaderCellProcessor = new ArrayList<>();

    /**
     *  数据填充之前的处理器
     *   不处理头信息
     */
    private final List<RowProcessor> beforeRowProcessor = new ArrayList<>();

    /**
     *  数据填充之后的处理器
     *   不处理头信息
     */
    private final List<RowProcessor> afterRowProcessor = new ArrayList<>();

    /**
     * 设置内容前的 单元格处理器
     *  不处理头信息
     */
    private final List<CellProcessor> beforeCellProcessor = new ArrayList<>();

    /**
     * 设置内容后的 单元格处理器
     *  不处理头信息
     */
    private final List<CellProcessor> afterCellProcessor = new ArrayList<>();


    public ExcelWriter() {
        this.handlerManager = CachedRegisterHelper.getDefManager();
    }


    public void write(ExcelMeta meta, DataProvider<T> provider, OutputStream ops) throws IOException {
        write(meta,provider,null,ops);
    }

    public void write(ExcelMeta meta, DataProvider<T> provider,OutputStream ops,FieldDataGetter<T> dataGetter) throws IOException {
        this.dataGetter = dataGetter;
        write(meta,provider,null,ops);
    }

    public void write(ExcelMeta meta, DataProvider<T> provider, ExcelStore es) throws IOException {
        write(meta,provider,es, (OutputStream) null);
    }

    public void write(ExcelMeta meta, DataProvider<T> provider,ExcelStore es,FieldDataGetter<T> dataGetter) throws IOException {
        this.dataGetter = dataGetter;
        write(meta, provider,es,new ByteArrayOutputStream());
    }

    private void write(ExcelMeta meta, DataProvider<T> provider,ExcelStore es,OutputStream ops) throws IOException {
        handlerMeta(meta);

        SXSSFWorkbook workbook = new SXSSFWorkbook();

        String sheetName = WorkbookUtil.createSafeSheetName(Optional.ofNullable(meta.getSheetName()).orElse("sheet"));
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        if (this.recordTime){
            this.canUpdateRt = false;
            System.out.println(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN)+"["+meta.getExcelName() + "] 开始导出.");
        }

        createTitle(meta,sheet);

        List<FieldMapping> fms = meta.getFieldMappings();
        List<T> list = null;
        int times = 1;
        int rowCount = this.getHeaderRow() + 1;

        // 死循环一直获取数据
        for (;;){
            list = provider.obtainData(times++);
            if (null == list || list.isEmpty()){
                break;
            }
            for (T t : list) {
                SXSSFRow row = sheet.createRow(rowCount++);
                // before processor
                rowProcess(sheet,row,getHeaderRow(),fms,getBeforeRowProcessor());

                for (int cellIdx = 0; cellIdx < fms.size(); cellIdx++) {

                    Object rawObj;
                    FieldMapping fm = fms.get(cellIdx);
                    if (null == this.dataGetter){
                        rawObj = DYNAMIC_FIELD_DATA_GETTER.getFromBean(t,fm.getFieldName());
                    }else {
                        rawObj = this.dataGetter.getFromBean(t,fm.getFieldName());
                    }
                    SXSSFCell cell = row.createCell(cellIdx);

                    // 前置处理
                    cellProcess(sheet,row,cell,cellIdx,fms.get(cellIdx),getBeforeCellProcessor());

                    if (null != rawObj){
                        String cellVal ;
                        ExcelTypeHandler eth = choseExcelTypeHandler(rawObj,fm);
                        if (null != eth){
                            cellVal = eth.serialize(rawObj,fm,meta);
                        }else {
                            cellVal = DEF_HANDLER.serialize(rawObj,fm,meta);
                        }
                        // 设置值
                        cell.setCellValue(cellVal);
                    }

                    // 后置处理
                    cellProcess(sheet,row,cell,cellIdx,fms.get(cellIdx),getAfterCellProcessor());

                }

                // after processor
                rowProcess(sheet,row,getHeaderRow(),fms,getAfterRowProcessor());

            }
        }

        // 如果使用ExcelStore将输出流copy为输入流
        // 让ExcelStore自动实现存储逻辑
        if (null != es){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            workbook.dispose();
            byteArrayOutputStream.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            es.store(inputStream);
        }else {
            workbook.write(ops);
            workbook.dispose();
        }

        if (this.recordTime){
            this.canUpdateRt = false;
            System.out.println(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN)+"["+meta.getExcelName() + "] 导出完成.");
        }
    }



    /**
     * 选择类型转换器
     * @param obj 字段数据
     * @return ExcelTypeHandler<?>
     */
    private ExcelTypeHandler<?> choseExcelTypeHandler(Object obj,FieldMapping fm){
        return getHandlerManager().getHandler(obj, fm);
    }


    public ExcelWriter<T> recordTime(){
        if (canUpdateRt){
            this.recordTime = true;
        }
        return this;
    }

    public ExcelWriter<T> disableRecordTime(){
        if (canUpdateRt){
            this.recordTime = false;
        }
        return this;
    }

    public FieldDataGetter<T> getDataGetter() {
        return dataGetter;
    }

    public ExcelWriter<T> dataGetter(FieldDataGetter<T> dataGetter) {
        this.dataGetter = dataGetter;
        return this;
    }

    public int getHeaderRow() {
        return headerRow;
    }

    public ExcelWriter<T> setHeaderRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public TypeHandlerManager getHandlerManager() {
        return handlerManager;
    }

    public ExcelWriter<T> setHandlerManager(TypeHandlerManager handlerManager) {
        if (null == handlerManager){
            throw new NullPointerException("类型处理器不可设置Null值.");
        }
        this.handlerManager = handlerManager;
        return this;
    }


    /**
     * 创建标题
     * @param meta ExcelMeta
     * @param sheet SXSSFSheet
     */
    protected void createTitle(ExcelMeta meta,SXSSFSheet sheet){
        List<FieldMapping> fms = meta.getFieldMappings();
        SXSSFRow row = sheet.createRow(getHeaderRow());

        // before processor
        rowProcess(sheet,row,getHeaderRow(),fms,getBeforeHeaderRowProcessor());

        // fill header
        for (int cellIdx = 0; cellIdx < fms.size(); cellIdx++) {
            SXSSFCell cell = row.createCell(cellIdx);

            // 前置处理
            cellProcess(sheet,row,cell,cellIdx,fms.get(cellIdx),getBeforeHeaderCellProcessor());

            // 设置值
            cell.setCellValue(fms.get(cellIdx).getHeaderName());

            // 后置处理
            cellProcess(sheet,row,cell,cellIdx,fms.get(cellIdx),getAfterHeaderCellProcessor());
        }

        // after processor
        rowProcess(sheet,row,getHeaderRow(),fms,getAfterHeaderRowProcessor());

    }


    private void rowProcess(SXSSFSheet sheet, SXSSFRow row, int rowNum, List<FieldMapping> fms,List<RowProcessor> processors){
        if (!processors.isEmpty()){
            for (RowProcessor processor : processors) {
                processor.process(sheet, row, rowNum, fms);
            }
        }
    }


    private void cellProcess(SXSSFSheet sheet, SXSSFRow row, Cell cell, int cellNum, FieldMapping fm,List<CellProcessor> processors){
        if (!processors.isEmpty()){
            for (CellProcessor processor : processors) {
                processor.process(sheet, row, cell, cellNum, fm);
            }
        }
    }


    /**
     * 处理元信息
     * @param meta ExcelMeta
     */
    protected void handlerMeta(ExcelMeta meta){
        // 过滤无效字段
        meta.setFieldMappings(
                meta.getFieldMappings()
                        .stream()
                        .filter(f-> Validator.isNotEmpty(f.getHeaderName()))
                        .filter(f-> Validator.isNotEmpty(f.getFieldName()))
                        .collect(Collectors.toList())
        );

        // 排序
        if (!Boolean.TRUE.equals(meta.getSortable())){
            List<FieldMapping> fieldMappings =
                    meta.getFieldMappings();
            meta.setFieldMappings(
                    fieldMappings
                            .parallelStream()
                            .sorted(Comparator.comparingInt(fm-> Optional.ofNullable(fm.getSeq()).orElse(99)))
                            .collect(Collectors.toList())
            );
        }

    }

    public List<RowProcessor> getBeforeHeaderRowProcessor() {
        return this.beforeHeaderRowProcessor;
    }

    public List<RowProcessor> getAfterHeaderRowProcessor() {
        return this.afterHeaderRowProcessor;
    }

    public List<CellProcessor> getBeforeHeaderCellProcessor() {
        return this.beforeHeaderCellProcessor;
    }

    public List<CellProcessor> getAfterHeaderCellProcessor() {
        return this.afterHeaderCellProcessor;
    }

    public List<RowProcessor> getBeforeRowProcessor() {
        return this.beforeRowProcessor;
    }

    public List<RowProcessor> getAfterRowProcessor() {
        return this.afterRowProcessor;
    }

    public List<CellProcessor> getBeforeCellProcessor() {
        return this.beforeCellProcessor;
    }

    public List<CellProcessor> getAfterCellProcessor() {
        return this.afterCellProcessor;
    }

    public ExcelWriter<T> addBeforeHeaderRowProcessor(RowProcessor beforeHeaderRowProcessor) {
        this.beforeHeaderRowProcessor.add(beforeHeaderRowProcessor);
        return this;
    }

    public ExcelWriter<T> addAfterHeaderRowProcessor(RowProcessor afterHeaderRowProcessor) {
        this.afterHeaderRowProcessor.add(afterHeaderRowProcessor);
        return this;
    }

    public ExcelWriter<T> addBeforeHeaderCellProcessor(CellProcessor beforeHeaderCellProcessor) {
        this.beforeHeaderCellProcessor.add(beforeHeaderCellProcessor);
        return this;
    }

    public ExcelWriter<T> addAfterHeaderCellProcessor(CellProcessor afterHeaderCellProcessor) {
        this.afterHeaderCellProcessor.add(afterHeaderCellProcessor);
        return this;
    }

    public ExcelWriter<T> addBeforeRowProcessor(RowProcessor beforeRowProcessor) {
        this.beforeRowProcessor.add(beforeRowProcessor);
        return this;
    }

    public ExcelWriter<T> addAfterRowProcessor(RowProcessor afterRowProcessor) {
        this.afterRowProcessor.add(afterRowProcessor);
        return this;
    }

    public ExcelWriter<T> addBeforeCellProcessor(CellProcessor beforeCellProcessor) {
        this.beforeCellProcessor.add(beforeCellProcessor);
        return this;
    }

    public ExcelWriter<T> addAfterCellProcessor(CellProcessor afterCellProcessor) {
        this.afterCellProcessor.add(afterCellProcessor);
        return this;
    }

}
