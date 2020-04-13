package org.jeecg.modules.app.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param filePath   文件路径
     * @param excelTitle 文件表头
     * @param map        要写入的数据集合
     * @param sheetName  sheet名称
     */
    public void writeMapToExcel(String filePath, String[] excelTitle, Map<Integer, Map<String, Object>> map, String sheetName) {
//        System.out.println("开始写入文件>>>>>>>>>>>>");
        Workbook workbook = null;
        //2003
        if (filePath.toLowerCase().endsWith("xls")) {
            workbook = new HSSFWorkbook();
            //2007
        } else if (filePath.toLowerCase().endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            logger.debug("invalid file name,should be xls or xlsx");
        }
        // create sheet
        Sheet sheet = workbook.createSheet(sheetName);

        // 标识位，用于标识sheet的行号
        int rowIndex = 0;
        // 遍历数据集，将其写入excel中
        try {
            //写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                //创建表头单元格,填值
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
            System.out.println("表头写入完成>>>>>>>>");

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

            System.out.println("主表数据写入完成>>>>>>>>");

            ByteArrayOutputStream byteArrayOutputStream =null;


            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            System.out.println(filePath + "\t--写入文件成功>>>>>>>>>>>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
