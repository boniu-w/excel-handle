package org.jeecg.modules.app.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wg
 * @Package org.jeecg.modules.demo.datawash.util
 * @date 2020/1/8 17:37
 * @Copyright
 */
public class ReadExcelUtil {




    private Logger logger = LoggerFactory.getLogger(ReadExcelUtil.class);
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public ReadExcelUtil() {

    }

    public ReadExcelUtil(String filepath) {
        if (filepath == null) {
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);

            switch (ext) {
                case ".xls":
                    wb = new HSSFWorkbook(is);
                    break;
                case ".xlsx":
                    wb = new XSSFWorkbook(is);
                    break;
                case ".et":
                    wb = new XSSFWorkbook(is);
                    break;
                default:
                    wb = null;
                    break;
            }

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     * @author zengwendong
     */
    public String[] readExcelTitle() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
//        System.out.println("colNum:" + colNum);

        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
//            title[i] = getStringCellValue(row.getCell((short) i));
//            title[i] = row.getCell(i).getCellFormula();
            title[i] = row.getCell(i).getStringCellValue();
        }
        return title;
    }

    /**
     * 读取的 excel 内容 应该以 表头对应字段 为键 形成map
     *
     * @param titleArray 表头数组
     */
    public Map<Integer, Map<String, Object>> readExcelContent(String[] titleArray) throws Exception {

        // 获取sheet 总数
        int numberOfSheets = wb.getNumberOfSheets();


        if (ObjectUtils.isEmpty(wb)) {
            throw new Exception("Workbook对象为空！");
        }

        Map<Integer, Map<String, Object>> contentMap = new HashMap<Integer, Map<String, Object>>();

        // 得到总行数
        sheet = wb.getSheetAt(1);
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
                    } else {// 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRING
                case Cell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_BOOLEAN: // Boolean
                    cellvalue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA: // 公式
                    cellvalue = String.valueOf(cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_ERROR: // 故障
                    cellvalue = "非法字符";
                    break;
                case Cell.CELL_TYPE_BLANK: // 空值
                    cellvalue = "";
                    break;
                default:// 默认的Cell值
                    cellvalue = "未知类型";
                    break;
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
}
