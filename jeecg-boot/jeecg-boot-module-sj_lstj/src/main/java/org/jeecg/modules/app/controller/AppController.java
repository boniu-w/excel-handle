package org.jeecg.modules.app.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.app.entity.*;
import org.jeecg.modules.app.enumeration.Title;
import org.jeecg.modules.app.service.BankFlowService;
import org.jeecg.modules.app.service.FileUploadService;
import org.jeecg.modules.app.service.TypeToFieldNameService;
import org.jeecg.modules.app.service.WordbookInterface;
import org.jeecg.modules.app.util.DatawashReadExcel;
import org.jeecg.modules.app.util.ExcelUtil;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author wg
 * @Package org.jeecg.modules.app.controller
 * @date 2020/3/24 9:56
 * @Copyright
 */
@Controller
@RequestMapping(value = "/app/appController")
public class AppController extends JeecgController<BankFlow, BankFlowService> {

    @Autowired(required = true)
    BankFlowService bankFlowService;

    @Autowired
    WordbookInterface wordbookInterface;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    TypeToFieldNameService typeToFieldNameService;

    static HashMap<Integer, ArrayList<String>> hashMap;  // hashMap 是不会变的 他的结果是 字典映射表

    int index = 1;

    static String[] excelTitles;  // 353

    static String[] titles; // 汉字形式的标题

    static HashMap map = new HashMap();

    static ResponseData responseData;


    HashMap<String, String> titleMap;


    /**
     * Quantities: 数目
     *
     * @param request
     */
    @PostMapping(value = "/getInfo")
    @ResponseBody
    public ResponseData getFileInfo(HttpServletRequest request, HttpServletResponse response) {
        responseData = new ResponseData();
        String message = "";
        List<MultipartFile> fileList = getFileInfo(request);

        Iterator<MultipartFile> multipartFileIterator = fileList.iterator();
        while (multipartFileIterator.hasNext()) {

            MultipartFile file = multipartFileIterator.next();
            // 判断文件 是否在数据库中已经存在,如果已经存在,则不再添加到数据库
            String name = file.getOriginalFilename();
            //System.out.println("file name: " + name);
            String fileName = fileUploadService.examineFileName(name);

            if (!StringUtils.isEmpty(fileName)) {
                message = " '" + name + "' 这个文件已经导入过数据库了,不需要重复导入";
                responseData.setFileMessage(message);
                responseData.setFileName(fileName);

                System.out.println("responseData: " + responseData);
                return responseData;
            } else {

                String uuid = UUID.randomUUID().toString().replace("-", "");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                // insertIntoFileUpload(uuid,name,date);
                FileUpload fileUpload = new FileUpload();
                fileUpload.setId(uuid);
                fileUpload.setFileName(name);
                fileUpload.setUpdateTime(timestamp);

                DatawashReadExcel readExcel = new DatawashReadExcel(file);

                // 此处sheetQuantities 为读取的excel表的sheet数量,如果不需要可以修改为1;
                int sheetQuantities = readExcel.getSheetQuantities();

                responseData = insertExcelIntoDatabase(file, 0);
                System.out.println("responseData: " + responseData);

                if (!StringUtils.isEmpty(responseData.getMessage())) {
                    return responseData;
                }

                responseData.setFileName(fileName);

                for (int j = 0; j < titles.length; j++) {
                    String excelTitle = excelTitles[j];
                    String[] split = excelTitle.split("_");
                    StringBuilder stringBuilder = new StringBuilder(split[0]);

                    for (int i = 0; i < split.length - 1; i++) {

                        stringBuilder.append(split[i + 1].substring(0, 1).toUpperCase() + split[i + 1].substring(1));
                    }

                    map.put(stringBuilder.toString(), titles[j]);

                }

                String fileTitle = JSON.toJSONString(map);

                System.out.println("fileTitle: " + fileTitle);
                fileUpload.setFileTitle(fileTitle);

                //System.out.println("getInfo map:   " + map);
                if (responseData.getCount() > 0) {

                    fileUploadService.insertIntoFileUpload(fileUpload);
                }

                return responseData;

            }
        }


        return responseData;
    }

    /**
     * 获取 前端 传过来的表格信息
     *
     * @param request
     * @return
     */
    //@RequestMapping(value = "/getTableInfo")
    public List<ConditionData> getTableInfo(HttpServletRequest request) {
        String tableData = request.getParameter("data");
        //System.out.println("tableData : " + tableData);

        List<ConditionData> conditionDataList = JSON.parseArray(tableData, ConditionData.class);

        //System.out.println("conditionDataList : " + conditionDataList);
        return conditionDataList;
    }


    /**
     * 获取 前端 上传的文件信息
     */
    public List<MultipartFile> getFileInfo(HttpServletRequest request) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

            if (commonsMultipartResolver.isMultipart(request)) {

                // (这里使用Vector，而不使用ArrayLsit，是怕引起线程安全问题，因为后面会引用到相同的内存地址)
                List<MultipartFile> fileVector = new Vector<>();

                while (iterator.hasNext()) {
                    // 将当前文件名一致的文件流放入同一个集合中
                    List<MultipartFile> fileRows = multipartHttpServletRequest.getFiles(iterator.next());

                    //对文件做去重设置
                    //判断集合是否存在，并且是否大于0
                    if (fileRows != null && fileRows.size() != 0) {
                        for (MultipartFile file : fileRows) {
                            String name = file.getName();

                            if (file != null && !file.isEmpty()) {
                                fileVector.add(file);
                            }
                        }
                    }
                }
                return fileVector;
            }
        }
        return null;
    }


    /**
     * 解析excel,并插入数据库
     * 插入数据库之前, 验证 excel表的表头
     */
    public ResponseData insertExcelIntoDatabase(MultipartFile file, int sheetIndex) {
        responseData.setMessage("");
        oneToMany();

        int count = 0;

        String fileName = file.getOriginalFilename();

        responseData = resolveExcelTitle(file, sheetIndex);
        //System.out.println("**********" + responseData);
        try {

            if (StringUtils.isEmpty(responseData.getMessage())) {
                // to_date(#{transaction_date},'yyyy-mm-dd hh24:mi:ss'),
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

                DatawashReadExcel readExcelUtil = new DatawashReadExcel(file);
                Map<Integer, Map<String, Object>> contentMapMap = readExcelUtil.readExcelContent(excelTitles, sheetIndex);

                Set<Map.Entry<Integer, Map<String, Object>>> contentEntries = contentMapMap.entrySet();
                Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = contentEntries.iterator();
                for (contentEntries.iterator(); iterator.hasNext(); ) {
                    Map.Entry<Integer, Map<String, Object>> contentMap = iterator.next();
                    // string : 列名   object : 要的数据

                    Map<String, Object> value = contentMap.getValue();

                    Object transaction_date = value.get("transaction_date");
                    long dateTime = 0L;
                    if (transaction_date instanceof Date) {
                        dateTime = ((Date) transaction_date).getTime();
                    }

                    if (transaction_date instanceof String) {
                        dateTime = simpleDateFormat.parse(transaction_date.toString()).getTime();

                    }

                    value.put("transaction_date", new Timestamp(dateTime));

                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    value.put("id", uuid);
                    value.put("excel_name", fileName);

                    //System.out.println("value.size() : " + value.size());
                    //System.out.println("contentMap : " + contentMap);


                    // 调用insert方法 加入数据库
                    wordbookInterface.insertBankFlow(value);
                    count++;
                }
                System.out.println("count: " + count);
                responseData.setCount(count);
                return responseData;
            } else {
                return responseData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseData;
    }


    /**
     * 字典表 与 匹配表 匹配 形成 1对多 关系 map
     *
     * @return
     */
    public HashMap<Integer, ArrayList<String>> oneToMany() {
        hashMap = new HashMap<>();
        // 以type 为键 fieldName 的集合 为值 , 存储数据库字段<type fieldName>
        List<Wordbook> wordbooks = wordbookInterface.examineWordbookAll();

        // 查询匹配表
        List<MatchingToWordbook> matchingToWordbooks = wordbookInterface.examineMatchingToWordbook();

        for (int i = 1; i < wordbooks.size() + 1; i++) {
            ArrayList<String> arrayList = new ArrayList<>();

            for (MatchingToWordbook matchingToWordbook : matchingToWordbooks) {
                Integer type = matchingToWordbook.getType();
                String fieldName = matchingToWordbook.getFieldName();

                if (type == i) {
                    arrayList.add(fieldName);
                }
            }
            hashMap.put(i, arrayList);

        }

        //System.out.println("hashMap:  " + hashMap);
        return hashMap;

    }

    /**
     * 1. 解析源表表头,形成表头数组
     * 2. 转换表头, 转换成字典表里的标准表头
     * <p>
     * 转换后的表头 是 String[] excelTitles
     */
    @ApiOperation(value = "解析流水表表头,如果表头中字段与数据库不匹配返回信息")
    public ResponseData resolveExcelTitle(MultipartFile file, int sheetIndex) {
        DatawashReadExcel readExcelUtil = new DatawashReadExcel(file);
        // 这一步是为了 改变 表头的名字 ,把表头改成 数据库里的 fieldcode 如果数据库中没有匹配的字段 则
        titles = readExcelUtil.readExcelTitle(sheetIndex);

        excelTitles = Arrays.copyOf(titles, titles.length);

        //System.out.println("responseData-resolveExcelTitle:  " + responseData);

        List<String> fieldNameList = wordbookInterface.examineFiledNameFromMatchingToWordbook();

        for (int i = 0; i < excelTitles.length; i++) {
            boolean contains = fieldNameList.indexOf(excelTitles[i]) >= 0;
            if (!contains) {

                //System.out.println("在excel表中,字段 ---" + excelTitles[i] + " --- 在数据库字典表中不存在,请修改或联系管理员");
                responseData.setMessage("在excel表中,字段 ' " + excelTitles[i] + " ' 在数据库字典表中不存在,请修改或联系管理员");
                return responseData;
            } else {
                for (int j = 1; j < hashMap.size() + 1; j++) {
                    // 得到 type = j 时 对应的字段 列表
                    ArrayList<String> strings = hashMap.get(j);

                    Iterator<String> iterator = strings.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();

                        // 如果数据库字段 与 excel标题字段 匹配
                        if (next.contains(excelTitles[i])) {
                            String fieldCode = wordbookInterface.examineWordbookFieldCodeByType(j);
                            excelTitles[i] = fieldCode;
                        }

                        System.out.println("Arrays.toString(excelTitles): " + Arrays.toString(excelTitles));
                    }
                }

            }
        }

        //System.out.println("responseData : " + responseData);
        return responseData;
    }


    /**
     * 根据条件 查询 出 符合的内容
     * 查询 流水表
     * 分析
     */
    @RequestMapping(value = "/resolve")
    @ResponseBody
    public ResponseData resolveExcelContent(HttpServletRequest request, HttpServletResponse response) {
        List<BankFlow> list = new ArrayList();

        List<ConditionData> conditionDataList = getTableInfo(request);

        //System.out.println("resolve conditionDataList.size() : " + conditionDataList.size());

        String parameter = request.getParameter("fileName");
        //System.out.println("parameter  " + parameter);


        // 获取表格数据之后,作为查询条件,查询流水表
        Iterator<ConditionData> iterator = conditionDataList.iterator();
        while (iterator.hasNext()) {
            ConditionData data = iterator.next();
            //System.out.println("<<<<<<<  " + data);

            List<BankFlow> list1 = bankFlowService.examimeBankFlowByCondition(data);
            //System.out.println(">>>>>>>>>  " + list1.size());

            FileUpload fileUpload = fileUploadService.examineFileUpload(parameter);

            //System.out.println("fileUpload  " + fileUpload);

            String uuid = fileUpload.getId();
            String fileTitle = fileUpload.getFileTitle();
            Map map = (Map) JSON.parse(fileTitle);

            responseData.setTitleMap(map);

            for (int i = 0; i < list1.size(); i++) {
                list.add(list1.get(i));
            }

        }
        responseData.setBankFlowList(list);
        //System.out.println("^^^^^^^^^^^^^^" + responseData.getBankFlowList().size());
        return responseData;

    }

    /**
     * 页面初始化时,查询所有的已经上传过的表
     */
    @RequestMapping(value = "/getFileInfoUploaded")
    @ResponseBody
    public List<FileUpload> getFileInfoUploaded(HttpServletRequest request, HttpServletResponse response) {
        List<FileUpload> fileUploadList = fileUploadService.examineAllFileInfo();

        Iterator<FileUpload> iterator = fileUploadList.iterator();
        while (iterator.hasNext()) {
            FileUpload next = iterator.next();
            String fileTitle = next.getFileTitle();
            Map<String, String> parse = (Map) JSON.parse(fileTitle);
            //System.out.println("parse  " + parse);

        }

        return fileUploadList;
    }


    /**
     * app小程序的导出excel
     *
     * @param request
     * @author wg
     */
    @RequestMapping(value = "/exportXls")
    protected ModelAndView appExportXls(HttpServletRequest request, String title) {
        title = "银行流水";
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<BankFlow> bankFlowList = responseData.getBankFlowList();

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, BankFlow.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title));
        mv.addObject(NormalExcelConstants.DATA_LIST, bankFlowList);
        return mv;
    }


    public Workbook initWorkbook(MultipartFile file) {
        if (file == null) {
            return null;
        }
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));

        try {
            InputStream is = file.getInputStream();
            switch (ext) {
                case ".xls":
                    Workbook hssfWorkbook = new HSSFWorkbook(is);
                    return hssfWorkbook;
                case ".xlsx":
                    Workbook xssfWorkbook = new XSSFWorkbook(is);
                    return xssfWorkbook;
                default:
                    Workbook workbook = null;
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 逐条解析excel表格的每一行的内容,去查询
     *
     * @author: wg
     * @time: 2020/3/31 14:19
     */
    @RequestMapping(value = "/conditionExcelExamine")
    @ResponseBody
    public List<BankFlow> conditionExcelExamine(HttpServletRequest request, HttpServletResponse response) {
        responseData = new ResponseData();
        String percentage = request.getParameter("percentage");
        String dateScope = request.getParameter("dateScope");
        String counterParty = request.getParameter("counterParty");

        if ("undefined".equals(counterParty)) {
            counterParty = "";
        }


        List<BankFlow> conditionExcelList = new ArrayList<>();
        List<BankFlow> collect = new ArrayList<>();
        List<BankFlow> bankFlowList = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        List<MultipartFile> fileList = getFileInfo(request);
        Iterator<MultipartFile> iterator = fileList.iterator();
        while (iterator.hasNext()) {
            MultipartFile file = iterator.next();
            String filename = file.getOriginalFilename();
            Workbook workbook = initWorkbook(file);
            String[] titleArray = transformExcelTitle(file);

            //System.out.println("Arrays.asList(titleArray).toString() : " + Arrays.asList(titleArray).toString());
            //System.out.println("titleMap: " + titleMap.toString());

            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();

            Row rowTitle = sheet.getRow(0);
            int colNum = rowTitle.getPhysicalNumberOfCells();

            // 正文内容应该从第二行开始
            for (int i = 1; i <= rowNum; i++) {
                int j = 0;

                Row rowContent = sheet.getRow(i);
                HashMap<String, Object> rowMap = new HashMap<String, Object>();
                HashMap<String, Object> bankFlowMap = new HashMap<String, Object>();
                try {
                    while (j < titleArray.length) {
                        Object obj = DatawashReadExcel.getCellFormatValue(rowContent.getCell(j));
                        rowMap.put(titleArray[j], obj);

                        String[] s = titleArray[j].split("_");
                        StringBuilder stringBuilder = new StringBuilder(s[0]);

                        for (int k = 0; k < s.length - 1; k++) {
                            stringBuilder.append(s[k + 1].substring(0, 1).toUpperCase() + s[k + 1].substring(1));
                        }

                        //System.out.println("stringBuilder: " + stringBuilder);
                        bankFlowMap.put(stringBuilder.toString(), obj);

                        j++;
                    }

                    bankFlowMap.put("tick", "conditionExcel");


                    //System.out.println("conditionExcelMap : " + conditionExcelMap);
                    //System.out.println("rowMap : " + rowMap);


                    // bankFlowMap 转换 成BankFlow
                    BankFlow bankFlow = JSON.parseObject(JSON.toJSONString(bankFlowMap), BankFlow.class);
                    //System.out.println("bankFlow : " + bankFlow);

                    conditionExcelList.add(bankFlow);



                    /* 开始 查询分析 */
                    ConditionData conditionData = new ConditionData();
                    Object transaction_amount = rowMap.get("transaction_amount");
                    float transactionMoney = Float.parseFloat(transaction_amount.toString());
                    conditionData.setStartMoney(transactionMoney * (1 - Float.parseFloat(percentage) / 100));
                    conditionData.setEndMoney(transactionMoney * (1 + Float.parseFloat(percentage) / 100));

                    Object recovery_mark = rowMap.get("recovery_mark");
                    if ("进".equals(recovery_mark.toString())) {
                        conditionData.setRecoveryMark("出");
                    }
                    if ("出".equals(recovery_mark.toString())) {
                        conditionData.setRecoveryMark("进");
                    }


                    Object transactionDate = rowMap.get("transaction_date");
                    long time = 0L;
                    if (transactionDate instanceof String) {

                        Date date = simpleDateFormat.parse(transactionDate.toString());
                        time = date.getTime();

                    }
                    if (transactionDate instanceof Date) {
                        time = ((Date) transactionDate).getTime();

                    }
                    int i1 = (24 * 60 * 60 * 1000) * Integer.parseInt(dateScope);
                    long start = time - i1;
                    long end = time + i1;
                    conditionData.setStartTime(simpleDateFormat.format(new Date(time)));
                    conditionData.setEndTime(simpleDateFormat.format(new Date(end)));


                    conditionData.setCounterParty(counterParty);
                    //conditionData.setCardEntity(rowMap.get("card_entity").toString());

                    //System.out.println("conditionData : " + conditionData);

                    bankFlowList = bankFlowService.examimeBankFlowByCondition(conditionData);

                    Iterator<BankFlow> bankFlowIterator = bankFlowList.iterator();
                    while (bankFlowIterator.hasNext()) {
                        BankFlow next = bankFlowIterator.next();
                        next.setTick("resultExcel");
                    }

                    //System.out.println("bankFlowList.toString(): "+bankFlowList.toString());


                    //System.out.println("bankFlowList.size() : " + bankFlowList.size());

                    conditionExcelList.addAll(bankFlowList);


                    //collect = Stream.of(bankFlowList, conditionExcelList)
                    //  .flatMap(Collection::stream)
                    //  .distinct()
                    //  .collect(Collectors.toList());

                    collect = conditionExcelList.stream().distinct().collect(Collectors.toList());


                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NullPointerException nullPointer) {
                    nullPointer.printStackTrace();
                }

            }
            System.out.println("collect.toString(): " + collect.toString());
            System.out.println("collect.size() : " + collect.size());
            //System.out.println("Arrays.toString(titles): " + Arrays.toString(titles));
        }
        responseData.setBankFlowList(collect);

        return collect;
    }

    /**
     * 获取表头数组,转换表头数组,由文字 转换成 fieldCode;
     * 2020/3/31
     */
    public String[] transformExcelTitle(MultipartFile file) {
        oneToMany();

        titleMap = new HashMap();

        //System.out.println("hashMap: "+hashMap);

        DatawashReadExcel readExcelUtil = new DatawashReadExcel(file);

        String[] titleNames = readExcelUtil.readExcelTitle(0);

        System.out.println("Arrays.toString(titleNames): " + Arrays.toString(titleNames));

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < titleNames.length; i++) {
            if (!StringUtils.isEmpty(titleNames[i])) {
                arrayList.add(titleNames[i]);
            }
        }

        String[] titles = arrayList.toArray(new String[arrayList.size()]);

        List<String> fieldNameList = wordbookInterface.examineFiledNameFromMatchingToWordbook();

        for (int i = 0; i < titles.length; i++) {
            boolean contains = fieldNameList.indexOf(titles[i]) >= 0;
            if (!contains) {
                //System.out.println("在excel表中,字段 ---" + titles[i] + " --- 在数据库表中不存在,请添加或修改");
                //responseData.setConditionExcelMessage("在excel表中,字段 ' " + titles[i] + " ' 在数据库表中不存在,请添加或修改");
            } else {
                for (int j = 1; j < hashMap.size() + 1; j++) {
                    ArrayList<String> strings = hashMap.get(j);
                    Iterator<String> iterator = strings.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        if (next.contains(titles[i])) {
                            String fieldCode = wordbookInterface.examineWordbookFieldCodeByType(j);
                            titles[i] = fieldCode;
                            titleMap.put(titles[i], titleNames[i]);
                        }
                    }
                }
            }
        }

        return titles;

    }

    /**
     * 转驼峰
     *
     * @author: wg
     * @time: 2020/4/8 10:54
     */
    public String getHumpString(String string) {

        String[] s = string.split("_");
        StringBuilder stringBuilder = new StringBuilder(s[0]);

        for (int k = 0; k < s.length - 1; k++) {
            stringBuilder.append(s[k + 1].substring(0, 1).toUpperCase() + s[k + 1].substring(1));
        }

        return stringBuilder.toString();
    }

    /**
     * @author: wg
     * @time: 2020/4/9 16:06
     */
    @RequestMapping(value = "/clearDatabase")
    @ResponseBody
    public String clearDatabase(HttpServletRequest request, HttpServletResponse response) {

        bankFlowService.clearBankFlow();
        fileUploadService.clearFileUpload();
        String message = "删除成功";
        return message;
    }


    /**
     * @author: wg
     * @time: 2020/4/13 9:30
     */
    @PostMapping(value = "/exportExcel")
    public void export1(String data, HttpServletResponse response, HttpServletRequest request) {

        System.out.println(data);

        List<BankFlow> bankFlowList = JSON.parseArray(data, BankFlow.class);
        System.out.println(bankFlowList.toString());

        //String[] titles = {
        //  Title.TRANSACTION_SUBJECT.getFileName(),  // 交易主体
        //  Title.ACCOUNT_SUBJECT.getFileName(),  // 交易主体账号
        //  Title.CARD_ENTITY.getFileName(),  // 交易主体
        //  Title.RECOVERY_MARK.getFileName(),  // 收付标志
        //  Title.TRANSACTION_AMOUNT.getFileName(),  // 交易金额
        //  Title.TRANSACTION_DATE.getFileName(),  // 交易日期
        //  Title.COUNTER_PARTY.getFileName(),  // 交易对手
        //  Title.BALANCE_TRANSACTION.getFileName(),  // 交易后余额
        //  Title.TRANSACTION_BANK.getFileName(),  // 交易主体归属行
        //  Title.COUNTERPARTY_BANK.getFileName(),  // 交易对手归属行
        //  Title.PLACE_TRANSACTION.getFileName(),
        //  Title.TRADING_PLACE.getFileName(),
        //  Title.TRANSACTION_NUMBER.getFileName(),
        //  Title.CURRENCY.getFileName()
        //};


        Title[] titleArray = Title.values();
        String[] titles = new String[titleArray.length];
        for (int i = 0; i < titleArray.length; i++) {
            Title title = titleArray[i];
            String fileName = title.getFileName();
            titles[i] = fileName;
        }


        String fileName = "导出流水表.xls";
        String sheetName = "导出流水sheet1";

        Workbook workbook = new SXSSFWorkbook(1000);
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);

        Row row = sheet.createRow(0); // 第一行,
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(cellStyle);
        }

        String[][] content = new String[bankFlowList.size()][titles.length];

        for (int i = 0; i < bankFlowList.size(); i++) {
            content[i] = new String[bankFlowList.size()];
            BankFlow bankFlow = bankFlowList.get(i);
            content[i][0] = bankFlow.getAbstractContent();
            content[i][1] = bankFlow.getTick();
            content[i][2] = String.valueOf(bankFlow.getTransactionDate());


        }


        HSSFWorkbook hssfWorkbook = this.getHSSFWorkbook(sheetName, titles, content, null);

        try {

            this.setResponseHeader(response, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * @author: wg
     * @time: 2020/4/15 10:26
     */
    public HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook hssfWorkbook) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (hssfWorkbook == null) {
            hssfWorkbook = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern((short) 1);

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                if (values[i][j] != null && values[i][j] instanceof String && values[i][j].equals("conditionExcel")) {
                    HSSFCell rowCell = row.createCell(j);
                    rowCell.setCellValue(values[i][j]);
                    rowCell.setCellStyle(style);
                } else {
                    HSSFCell rowCell = row.createCell(j);
                    rowCell.setCellValue(values[i][j]);
                }
            }
        }
        return hssfWorkbook;
    }


    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @RequestMapping(value = "excelUtilTest")
    public void excelUtilTest(String data, HttpServletResponse response) {

        List<BankFlow> bankFlowList = JSON.parseArray(data, BankFlow.class);
        ExcelUtil util = new ExcelUtil(BankFlow.class);
        util.exportExcel(bankFlowList, "导出工具测试", response);
    }

    public void export2(String data, HttpServletResponse response) {
        List<BankFlow> bankFlowList = JSON.parseArray(data, BankFlow.class);

        String fileName = "导出流水表.xls";
        String sheetName = "导出流水sheet1";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFRow row = null;
        HSSFCell cell = null;

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);

        // 把所有 带有@Excel 注解的字段拿出来 形成arrayList 以形成表头
        BankFlow bankFlow = new BankFlow();
        Field[] fields = bankFlow.getClass().getDeclaredFields();
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Excel.class)) {
                fieldArrayList.add(field);
            }
        }
        row = sheet.createRow(0);
        for (int i = 0; i < fieldArrayList.size(); i++) {
            cell = row.createCell(i);
            Field field = fieldArrayList.get(i);
            Excel annotation = field.getAnnotation(Excel.class);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(annotation.name());
        }

        // 正文 field.get(bankFlow)
        try {
            for (int i = 0; i < bankFlowList.size(); i++) {
                row = sheet.createRow(i + 1);
                BankFlow flow = bankFlowList.get(i);
                for (int j = 0; j < fieldArrayList.size(); j++) {
                    cell = row.createCell(j);
                    Field field = fieldArrayList.get(j);
                    field.setAccessible(true);
                    if (field.get(flow) instanceof String) {
                        cell.setCellValue((String) field.get(flow));
                    }


                }

            }
        } catch (IllegalAccessException e) {
			// 测试 git拉取
            e.printStackTrace();
        }
    }


}
