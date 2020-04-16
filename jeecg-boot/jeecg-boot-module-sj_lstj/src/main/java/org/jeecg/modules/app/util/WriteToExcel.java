package org.jeecg.modules.app.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wg
 * @Package org.jeecg.modules.demo.datawash.util
 * @date 2020/1/8 17:38
 * @Copyright
 */
public class WriteToExcel {


    private Logger logger = LoggerFactory.getLogger(WriteToExcel.class);

    /**
     * 将List集合数据写入excel（单个sheet）
     *
     * @param excelTitle 文件表头
     * @param map        要写入的数据集合
     * @param sheetName  sheet名称
     */
    public void writeMapToExcel(String fileName, String[] excelTitle, Map<Integer, Map<String, Object>> map,
                                String sheetName, HttpServletResponse response) {
        try {
            Workbook workbook = null;

            if (fileName.endsWith("xls")) {   //2003
                workbook = new HSSFWorkbook();
            } else if (fileName.toLowerCase().endsWith("xlsx")) {  //2007
                workbook = new XSSFWorkbook();
            } else {
                logger.debug("invalid file name,should be xls or xlsx");
            }
            Sheet sheet = workbook.createSheet(sheetName);

            int rowIndex = 0;

            //写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
            //System.out.println("表头写入完成>>>>>>>>");

            rowIndex++;

            //循环写入主表数据


            Set<Map.Entry<Integer, Map<String, Object>>> entries = map.entrySet();
            Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = entries.iterator();


            for (entries.iterator(); iterator.hasNext(); ) {

                Map.Entry<Integer, Map<String, Object>> next = iterator.next();
                Map<String, Object> contentMap = next.getValue();

                Row row = sheet.createRow(rowIndex);
                for (int i = 0; i < excelTitle.length; i++) {
                    row.createCell(i).setCellValue((String) contentMap.get(excelTitle[i]));
                }

                rowIndex++;

            }

            //System.out.println("主表数据写入完成>>>>>>>>");

            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
