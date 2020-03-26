package org.jeecg.modules.app.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wg
 * @Package org.jeecg.modules.demo.datawash.util
 * @date 2020/1/8 17:34
 * @Copyright
 */
public class DatawashReadExcel {


    private Logger logger = LoggerFactory.getLogger(DatawashReadExcel.class);
    private Workbook workbook;
    private Sheet sheet;
    private Row row;
    private int sheetQuantities;


        public DatawashReadExcel(MultipartFile file) {
        if (file == null) {
            return;
        }
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));

        try {
            InputStream is = file.getInputStream();
            switch (ext) {
                case ".xls":
                    workbook = new HSSFWorkbook(is);
                    sheetQuantities = workbook.getNumberOfSheets();
                    break;
                case ".xlsx":
                    workbook = new XSSFWorkbook(is);
                    sheetQuantities = workbook.getNumberOfSheets();
                    break;
                default:
                    workbook = null;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @param n sheet 的 index
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(int n) {
        if (workbook == null) {
            try {
                throw new Exception("Workbook对象为空！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sheet = workbook.getSheetAt(n);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();

        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = row.getCell(i).getStringCellValue();
        }
        return title;


    }

    /**
     * 读取的 excel 内容 应该以 表头对应字段 为键 形成map
     *
     * @param titleArray 表头数组
     * @param n          sheet 的 index
     */
    public Map<Integer, Map<String, Object>> readExcelContent(String[] titleArray, int n) throws Exception {

        if (ObjectUtils.isEmpty(workbook)) {
            throw new Exception("Workbook对象为空！");
        }

        Map<Integer, Map<String, Object>> contentMap = new HashMap<Integer, Map<String, Object>>();
        // 得到总行数
        sheet = workbook.getSheetAt(n);
        int rowNum = sheet.getLastRowNum();

        // 总列数
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();

        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            HashMap<String, Object> cellValue = new HashMap<String, Object>();
            while (j < colNum) {
                Cell cell = row.getCell(j);
                CellStyle cellStyle = cell.getCellStyle();

                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(titleArray[j], obj);
                j++;
            }
            contentMap.put(i, cellValue);

        }
        return contentMap;
    }


    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";

        DecimalFormat decimalFormat = new DecimalFormat();


        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_NUMERIC: {
                    // 判断当前的cell是否为Date
                    short s = cell.getCellStyle().getDataFormat();
                    if (ExcelDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else { // 如果是纯数字
                        // 取得当前Cell的数值
                        //cellvalue = String.valueOf(cell.getNumericCellValue());
                        cellvalue = decimalFormat.format(cell.getNumericCellValue()).replace(",", "");
                    }
                    break;
                }
                // 如果当前Cell的Type为STRING
                case Cell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串

                    cellvalue = cell.getRichStringCellValue().getString().replace(",", "");

                    break;

                case Cell.CELL_TYPE_BOOLEAN:
                    cellvalue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cellvalue = String.valueOf(cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellvalue = "非法字符";
                    break;
                case Cell.CELL_TYPE_BLANK:
                    cellvalue = "";
                    break;
                default:
                    cellvalue = "未知类型";
                    break;
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public int getSheetQuantities() {
        return sheetQuantities;
    }
}
