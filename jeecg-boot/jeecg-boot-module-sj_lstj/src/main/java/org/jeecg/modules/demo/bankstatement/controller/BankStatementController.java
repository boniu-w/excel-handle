<<<<<<< HEAD
package org.jeecg.modules.demo.bankstatement.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import org.jeecg.modules.demo.bankstatement.service.IBankStatementService;
import org.jeecg.modules.demo.bqrq.controller.DqrqController;
import org.jeecg.modules.demo.bqrq.entity.Dqrq;
import org.jeecg.modules.demo.bqrq.service.IDqrqService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date: 2020-02-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "流水表")
@RestController
@RequestMapping("/bankstatement/bankStatement")
public class BankStatementController extends JeecgController<BankStatement, IBankStatementService> {
    @Autowired
    private IBankStatementService bankStatementService;
    @Autowired
    private DqrqController  dqrq;
    @Autowired
	private IDqrqService dqrqService;

    /**
     * 分页列表查询
     *
     * @param bankStatement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "流水表-分页列表查询")
    @ApiOperation(value = "流水表-分页列表查询", notes = "流水表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BankStatement bankStatement,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<BankStatement> pageList = null;
        if (bankStatement.getReserve1() != null) {
            String[] split = bankStatement.getReserve1().split(",");
            bankStatement.setReserve1(null);
            QueryWrapper<BankStatement> queryWrapper = QueryGenerator.initQueryWrapper(bankStatement, req.getParameterMap());
            queryWrapper.orderByAsc("Reserve2");
            queryWrapper.between("transaction_date", split[0], split[1]);
            Page<BankStatement> page = new Page<BankStatement>(pageNo, pageSize);
            pageList = bankStatementService.page(page, queryWrapper);
        } else {
            QueryWrapper<BankStatement> queryWrapper = QueryGenerator.initQueryWrapper(bankStatement, req.getParameterMap());
            queryWrapper.orderByAsc("Reserve2");
            Page<BankStatement> page = new Page<BankStatement>(pageNo, pageSize);
            pageList = bankStatementService.page(page, queryWrapper);
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param bankStatement
     * @return
     */
    @AutoLog(value = "流水表-添加")
    @ApiOperation(value = "流水表-添加", notes = "流水表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BankStatement bankStatement) {
        bankStatementService.save(bankStatement);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bankStatement
     * @return
     */
    @AutoLog(value = "流水表-编辑")
    @ApiOperation(value = "流水表-编辑", notes = "流水表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BankStatement bankStatement) {
        bankStatementService.updateById(bankStatement);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流水表-通过id删除")
    @ApiOperation(value = "流水表-通过id删除", notes = "流水表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bankStatementService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "流水表-批量删除")
    @ApiOperation(value = "流水表-批量删除", notes = "流水表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bankStatementService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流水表-通过id查询")
    @ApiOperation(value = "流水表-通过id查询", notes = "流水表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BankStatement bankStatement = bankStatementService.getById(id);
        return Result.ok(bankStatement);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bankStatement
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BankStatement bankStatement) {
        return super.exportXls(request, bankStatement, BankStatement.class, "流水表");
    }

    /**
     * 导出excel模板
     *
     * @param request
     * @param bankStatement
     */
    @RequestMapping(value = "/exportXls1")
    public ModelAndView exportXls1(HttpServletRequest request, BankStatement bankStatement) {
        return super.exportXls1(request, bankStatement, BankStatement.class, "流水表");
    }


    public Result<?> importExcel1(HttpServletRequest request, HttpServletResponse response, Class<BankStatement> clazz) {

        String id = request.getParameter("upload");
        System.out.println(id+"----------------------");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象


            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<BankStatement> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCaseId(id);
                }
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                bankStatementService.saveBatch(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                //update-end-author:taoyan date:20190528 for:批量插入数据
                
                HashMap<Object, Object> hashMap = new HashMap<>();
                Date a = new Date();
                hashMap.put("caseId", id);
                hashMap.put("createTime",a);
                //补全流余额
                List<BankStatement> Card = bankStatementService.selectCard(hashMap);
                List<BankStatement> MaxDate = bankStatementService.selectMaxDate();
                hashMap.put("MaxDate", MaxDate.get(0).getReserve1());
                hashMap.put("MinDate", MaxDate.get(0).getTransactionDate());
                Dqrq pp = new Dqrq();
                for (int i = 0; i < Card.size(); i++) {
                	hashMap.put("queryCardNumber",Card.get(i).getQueryCardNumber());
                	List<Dqrq> bqrqlist =  dqrqService.selectbqrq(hashMap);
                	double d = 0;
                	for (int j = 0; j < bqrqlist.size(); j++) {
                		
                		if(bqrqlist.get(j).getNum() == 0) {
                			bqrqlist.get(j).setMaxamount(d);
                		}else {
                			d = bqrqlist.get(j).getMinbalance();
                		}
					}
                	dqrqService.saveBatch(bqrqlist);
                	System.out.println("------卡号："+bqrqlist.get(0).getQueryCardNumber());
				}
                //汇总最大金额
                int i = bankStatementService.insertMaximumBalance(hashMap);
                System.out.println(i + ">>>>>>>");
                
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        Result<?> result = importExcel1(request, response, BankStatement.class);

        return result;
    }

}
=======
package org.jeecg.modules.demo.bankstatement.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import org.jeecg.modules.demo.bankstatement.service.IBankStatementService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date: 2020-02-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "流水表")
@RestController
@RequestMapping("/bankstatement/bankStatement")
public class BankStatementController extends JeecgController<BankStatement, IBankStatementService> {
    @Autowired
    private IBankStatementService bankStatementService;

    /**
     * 分页列表查询
     *
     * @param bankStatement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "流水表-分页列表查询")
    @ApiOperation(value = "流水表-分页列表查询", notes = "流水表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BankStatement bankStatement,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<BankStatement> pageList = null;
        if (bankStatement.getReserve1() != null) {
            String[] split = bankStatement.getReserve1().split(",");
            bankStatement.setReserve1(null);
            QueryWrapper<BankStatement> queryWrapper = QueryGenerator.initQueryWrapper(bankStatement, req.getParameterMap());
            queryWrapper.between("transaction_date", split[0], split[1]);
            Page<BankStatement> page = new Page<BankStatement>(pageNo, pageSize);
            pageList = bankStatementService.page(page, queryWrapper);
        } else {
            QueryWrapper<BankStatement> queryWrapper = QueryGenerator.initQueryWrapper(bankStatement, req.getParameterMap());
            Page<BankStatement> page = new Page<BankStatement>(pageNo, pageSize);
            pageList = bankStatementService.page(page, queryWrapper);
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param bankStatement
     * @return
     */
    @AutoLog(value = "流水表-添加")
    @ApiOperation(value = "流水表-添加", notes = "流水表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BankStatement bankStatement) {
        bankStatementService.save(bankStatement);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bankStatement
     * @return
     */
    @AutoLog(value = "流水表-编辑")
    @ApiOperation(value = "流水表-编辑", notes = "流水表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BankStatement bankStatement) {
        bankStatementService.updateById(bankStatement);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流水表-通过id删除")
    @ApiOperation(value = "流水表-通过id删除", notes = "流水表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bankStatementService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "流水表-批量删除")
    @ApiOperation(value = "流水表-批量删除", notes = "流水表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bankStatementService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流水表-通过id查询")
    @ApiOperation(value = "流水表-通过id查询", notes = "流水表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BankStatement bankStatement = bankStatementService.getById(id);
        return Result.ok(bankStatement);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bankStatement
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BankStatement bankStatement) {
        return super.exportXls(request, bankStatement, BankStatement.class, "流水表");
    }

    /**
     * 导出excel模板
     *
     * @param request
     * @param bankStatement
     */
    @RequestMapping(value = "/exportXls1")
    public ModelAndView exportXls1(HttpServletRequest request, BankStatement bankStatement) {
        return super.exportXls1(request, bankStatement, BankStatement.class, "流水表");
    }


    public Result<?> importExcel1(HttpServletRequest request, HttpServletResponse response, Class<BankStatement> clazz) {

        String id = request.getParameter("upload");
        System.out.println(id+"----------------------");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象


            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<BankStatement> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCaseId(id);
                }
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                bankStatementService.saveBatch(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据

                HashMap<Object, Object> hashMap = new HashMap<>();
                Date a = new Date();
                hashMap.put("caseId", id);
                hashMap.put("createTime",a);

                int i = bankStatementService.insertMaximumBalance(hashMap);

                System.out.println(i + ">>>>>>>");


                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        Result<?> result = importExcel1(request, response, BankStatement.class);

        return result;
    }

}
>>>>>>> master
