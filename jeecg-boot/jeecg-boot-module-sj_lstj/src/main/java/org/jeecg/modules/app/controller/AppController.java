package org.jeecg.modules.app.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.app.entity.*;
import org.jeecg.modules.app.service.BankFlowService;
import org.jeecg.modules.app.service.FileUploadService;
import org.jeecg.modules.app.service.TypeToFieldNameService;
import org.jeecg.modules.app.service.WordbookInterface;
import org.jeecg.modules.app.util.DatawashReadExcel;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author wg
 * @Package org.jeecg.modules.app.controller
 * @date 2020/3/24 9:56
 * @Copyright
 */
@Controller
@RequestMapping(value = "/app/appController")
public class AppController extends JeecgController<BankFlow, BankFlowService> {

    @Autowired
    BankFlowService bankFlowService;

    @Autowired
    WordbookInterface wordbookInterface;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    TypeToFieldNameService typeToFieldNameService;

    static HashMap<Integer, ArrayList<String>> hashMap;  // hashMap 是不会变的 他的结果是 字典映射表

    int index = 1;

    static String[] excelTitles;

    static String[] titles; // 汉字形式的标题

    static HashMap map = new HashMap();

    ResponseData responseData = new ResponseData();


    /**
     * Quantities: 数目
     *
     * @param request
     */
    @PostMapping(value = "/getInfo")
    @ResponseBody
    public ResponseData getFileInfo(HttpServletRequest request, HttpServletResponse response) {

        ResponseData responseData = new ResponseData();
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
                message = name + "--这个文件已经导入过数据库了,不需要重复导入";
                responseData.setFileMessage(message);

                responseData.setFileName(fileName);

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

                ResponseData resData = insertExcelIntoDatabase(file, 0);
                //System.out.println(",,,,,,,getInfo,,,,,,,," + resData);
                resData.setFileName(fileName);

                HashMap<Integer, ArrayList<String>> hash = oneToMany();


                for (int j = 0; j < titles.length; j++) {
                    String excelTitle = excelTitles[j];
                    String[] split = excelTitle.split("_");

                    String a = split[1];
                    String b = a.substring(0, 1).toUpperCase();
                    String c = a.substring(1);
                    String d = b + c;

                    String m = split[0] + d;

                    map.put(m, titles[j]);
                }


                String fileTitle = JSON.toJSONString(map);

                fileUpload.setFileTitle(fileTitle);

                //System.out.println("getInfo map:   " + map);
                int i = fileUploadService.insertIntoFileUpload(fileUpload);

                return resData;

            }
        }

        return null;
    }

    /**
     * 获取 前端 传过来的表格信息
     *
     * @param request
     * @return
     */
    //@RequestMapping(value = "/getTableInfo")
    public List<TableData> getTableInfo(HttpServletRequest request) {
        String tableData = request.getParameter("data");
        //System.out.println("tableData : " + tableData);

        List<TableData> tableDataList = JSON.parseArray(tableData, TableData.class);

        //System.out.println("tableDataList : " + tableDataList);
        return tableDataList;
    }


    /**
     * 获取 前端 上传的文件信息
     */
    //@PostMapping(value = "/getFileInfo")
    public List<MultipartFile> getFileInfo(HttpServletRequest request) {
        // 将当前上下文初始化给commonsMultipartResolver,判断request是否为MultipartHttpServletRequest类型
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (request instanceof MultipartHttpServletRequest) {
            // 解析文件流，查看是否存在文件信息
            // 将request强转为MultipartHttpServletRequest类型
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

            // 创建一个迭代器，为接下来的迭代做准备
            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

            // 检查form中是否有enctype="multipart/form-data"，并且判断Iterator是否还存在值 && iterator.hasNext()
            if (commonsMultipartResolver.isMultipart(request)) {

                // 开始遍历文件
                // 创建文件存储的一个集合,并初始化
                // (这里使用Vector，而不使用ArrayLsit，是怕引起线程安全问题，因为后面会引用到相同的内存地址)
                List<MultipartFile> fileVector = new Vector<>();

                //对iterator进行遍历
                while (iterator.hasNext()) {
                    // 将当前文件名一致的文件流放入同一个集合中
                    List<MultipartFile> fileRows = multipartHttpServletRequest.getFiles(iterator.next());

                    //对文件做去重设置
                    //判断集合是否存在，并且是否大于0
                    if (fileRows != null && fileRows.size() != 0) {
                        //对文件集合进行遍历
                        for (MultipartFile file : fileRows) {
                            //判断文件是否存在
                            String name = file.getName();

                            if (file != null && !file.isEmpty()) {
                                //添加文件
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
        HashMap<Integer, ArrayList<String>> arrayListHashMap = oneToMany();

        int count = 0;

        ResponseData responseData = resolveExcelTitle(file, sheetIndex);
        //System.out.println("**********" + responseData);
        try {
            // 解析表头成功 , 执行插入操作
            if (StringUtils.isEmpty(responseData.getMessage())) {
                // to_date(#{transaction_date},'yyyy-mm-dd hh24:mi:ss'),
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

                //  获取源表的数据, 以更改后的 表头名作为键
                DatawashReadExcel readExcelUtil = new DatawashReadExcel(file);
                Map<Integer, Map<String, Object>> contentMapMap = readExcelUtil.readExcelContent(excelTitles, sheetIndex);

                Set<Map.Entry<Integer, Map<String, Object>>> contentEntries = contentMapMap.entrySet();
                Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = contentEntries.iterator();
                for (contentEntries.iterator(); iterator.hasNext(); ) {
                    Map.Entry<Integer, Map<String, Object>> contentMap = iterator.next();
                    // string : 列名   object : 要的数据

                    Map<String, Object> value = contentMap.getValue();

                    Object transaction_date = value.get("transaction_date");

                    Date date = simpleDateFormat.parse(transaction_date.toString());
                    long dateTime = date.getTime();
                    value.put("transaction_date", new Timestamp(dateTime));

                    //if ("".equals(transaction_date)) {
                    //    value.put("transaction_date", transaction_date);
                    //} else if (transaction_date instanceof String || transaction_date instanceof Integer) {
                    //    Date parse = simpleDateFormat.parse((String) transaction_date);
                    //    value.put("transaction_date", parse);
                    //} else if (transaction_date.getClass().isInstance(Date.class) || transaction_date instanceof Date) {
                    //    long time = ((Date) transaction_date).getTime();
                    //    Timestamp timestamp = new Timestamp(time);
                    //    value.put("transaction_date", timestamp);
                    //}

                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    value.put("id", uuid);


                    Set<Map.Entry<String, Object>> entries = value.entrySet();
                    Iterator<Map.Entry<String, Object>> entryIterator = entries.iterator();
                    while (entryIterator.hasNext()) {
                        Map.Entry<String, Object> next = entryIterator.next();
                        //System.out.println(next.getKey() + "--" + next.getValue());
                    }

                    //System.out.println("value.size() : " + value.size());
                    //System.out.println("contentMap : " + contentMap);


                    // 调用insert方法 加入数据库


                    wordbookInterface.insertBankFlow(value);
                    count++;
                }
                //System.out.println("--------" + contentMapMap);
                responseData.setCount(count);
                return responseData;
            } else {
                return null;
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
        // 以type 为键 fieldName 的集合 为值
        // 存储数据库字段<type fieldName>

        // 查询 标准表
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
    @ApiOperation(value = "解析表头,如果表头中字段与数据库不匹配返回信息")
    public ResponseData resolveExcelTitle(MultipartFile file, int sheetIndex) {
        ResponseData responseData = new ResponseData();
        DatawashReadExcel readExcelUtil = new DatawashReadExcel(file);
        // 这一步是为了 改变 表头的名字 ,把表头改成 数据库里的 fieldcode 如果数据库中没有匹配的字段 则
        titles = readExcelUtil.readExcelTitle(sheetIndex);
        //responseData.setTitles(titles);

        excelTitles = Arrays.copyOf(titles, titles.length);

        //System.out.println("******resolveExcelTitle****    " + responseData);

        List<String> fieldNameList = wordbookInterface.examineFiledNameFromMatchingToWordbook();

        for (int i = 0; i < excelTitles.length; i++) {
            boolean contains = fieldNameList.indexOf(excelTitles[i]) >= 0;
            if (!contains) {
                //System.out.println("在excel表中,字段 ---" + excelTitles[i] + " --- 在数据库表中不存在,请添加或修改");
                responseData.setMessage("在excel表中,字段 ' " + excelTitles[i] + " ' 在数据库表中不存在,请添加或修改");
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
                            // 再通过 j的值 (type 值) 查数据库 得到fieldname
                            // 再把 fidlename 赋给 excelTitles[i]
                            String fieldCode = wordbookInterface.examineWordbookFieldCodeByType(j);
                            excelTitles[i] = fieldCode;
                        }
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

        List<TableData> tableDataList = getTableInfo(request);

        //System.out.println("resolve tableDataList.size() : " + tableDataList.size());

        String parameter = request.getParameter("fileName");
        //System.out.println("parameter  " + parameter);


        // 获取表格数据之后,作为查询条件,查询流水表
        Iterator<TableData> iterator = tableDataList.iterator();
        while (iterator.hasNext()) {
            TableData data = iterator.next();
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
        String percentage = request.getParameter("percentage");
        String dateScope = request.getParameter("dateScope");
        String counterParty = request.getParameter("counterParty");

        if ("undefined".equals(counterParty)) {
            counterParty = "";
        }

        List<BankFlow> list = new ArrayList<>();
        List<BankFlow> collect = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");


        List<MultipartFile> fileList = getFileInfo(request);
        Iterator<MultipartFile> iterator = fileList.iterator();
        while (iterator.hasNext()) {
            MultipartFile file = iterator.next();
            Workbook workbook = initWorkbook(file);
            String[] titleArray = transformExcelTitle(file);

            //System.out.println("Arrays.asList(titleArray).toString() : " + Arrays.asList(titleArray).toString());

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

                while (j < colNum) {
                    Object obj = DatawashReadExcel.getCellFormatValue(rowContent.getCell(j));
                    rowMap.put(titleArray[j], obj);

                    String[] s = titleArray[j].split("_");
                    String s1 = s[0] + s[1].substring(0, 1).toUpperCase() + s[1].substring(1);
                    bankFlowMap.put(s1, obj);
                    j++;
                }

                //System.out.println("bankFlowMap : " + bankFlowMap);
                //System.out.println("rowMap : " + rowMap);


                try {
                    // bankFlowMap 转换 成BankFlow
                    BankFlow bankFlow = JSON.parseObject(JSON.toJSONString(bankFlowMap), BankFlow.class);
                    //System.out.println("bankFlow : " + bankFlow);

                    list.add(bankFlow);


                    /* 开始 查询分析 */
                    TableData tableData = new TableData();
                    Object transaction_amount = rowMap.get("transaction_amount");
                    float transactionMoney = Float.parseFloat(transaction_amount.toString());
                    tableData.setStartMoney(transactionMoney * (1 - Float.parseFloat(percentage) / 100));
                    tableData.setEndMoney(transactionMoney * (1 + Float.parseFloat(percentage) / 100));

                    Object recovery_mark = rowMap.get("recovery_mark");
                    if ("进".equals(recovery_mark.toString())) {
                        tableData.setRecoveryMark("出");
                    }
                    if ("出".equals(recovery_mark.toString())) {
                        tableData.setRecoveryMark("进");
                    }


                    Object transactionDate = rowMap.get("transaction_date");

                    Date date = simpleDateFormat.parse(transactionDate.toString());
                    long time = date.getTime();
                    int i1 = (24 * 60 * 60 * 1000) * Integer.parseInt(dateScope);
                    long start = time - i1;
                    long end = time + i1;

                    tableData.setStartTime(simpleDateFormat.format(new Date(start)));
                    tableData.setEndTime(simpleDateFormat.format(new Date(end)));
                    tableData.setCounterParty(counterParty);
                    tableData.setCardEntity(rowMap.get("card_entity").toString());

                    //System.out.println("tableData : " + tableData);

                    List<BankFlow> bankFlowList = bankFlowService.examimeBankFlowByCondition(tableData);
                    //System.out.println("bankFlowList.size() : " + bankFlowList.size());

                    list.addAll(bankFlowList);

                    collect = Stream.of(bankFlowList, list)
                      .flatMap(Collection::stream)
                      .distinct()
                      .collect(Collectors.toList());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        //System.out.println("collect.size() : " + collect.size());
        responseData.setBankFlowList(collect);
        return collect;
    }


    /**
     * 获取表头数组,转换表头数组,由文字 转换成 fieldCode;
     * 2020/3/31
     */
    public String[] transformExcelTitle(MultipartFile file) {
        oneToMany();
        DatawashReadExcel readExcelUtil = new DatawashReadExcel(file);

        String[] titles = readExcelUtil.readExcelTitle(0);

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
                        }
                    }
                }
            }
        }

        return titles;

    }

    /**
     * Map转成实体对象
     * @param map map实体对象包含属性
     * @param clazz 实体对象类型
     * @param  flag map的key是下划线(和数据库字段名称一致)命名则为true，key是驼峰命名则为false
     * @return
     */
    //public static Object map2Object(Map<String, Object> map, Class<?> clazz, boolean flag) {
    //    if (map == null) {
    //        return null;
    //    }
    //    Object obj = null;
    //    try {
    //        obj = clazz.newInstance();
    //
    //        Field[] fields = obj.getClass().getDeclaredFields();
    //        for (Field field : fields) {
    //            int mod = field.getModifiers();
    //            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
    //                continue;
    //            }
    //            field.setAccessible(true);
    //            if (flag)
    //                field.set(obj, map.get(HumpToUnderline(field.getName())));
    //            else
    //                field.set(obj, map.get((field.getName())));
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return obj;
    //}


}
