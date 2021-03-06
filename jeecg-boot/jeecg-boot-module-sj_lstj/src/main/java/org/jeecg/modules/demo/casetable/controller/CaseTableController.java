package org.jeecg.modules.demo.casetable.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import org.jeecg.modules.demo.bankstatement.service.IBankStatementService;
import org.jeecg.modules.demo.bqrq.entity.Dqrq;
import org.jeecg.modules.demo.bqrq.service.IDqrqService;
import org.jeecg.modules.demo.casetable.entity.CaseTable;
import org.jeecg.modules.demo.casetable.service.ICaseTableService;
import org.jeecg.modules.demo.maximumbalance.entity.MaximumBalance;
import org.jeecg.modules.demo.maximumbalance.service.IMaximumBalanceService;

import java.util.Date;
import java.util.HashMap;

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
 * @Description: 案件表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="案件表")
@RestController
@RequestMapping("/casetable/caseTable")
public class CaseTableController extends JeecgController<CaseTable, ICaseTableService> {
	@Autowired
    private IBankStatementService bankStatementService;
	@Autowired
	private IMaximumBalanceService maximumBalanceService;
	@Autowired
	private IDqrqService dqrqService;
	@Autowired
	private ICaseTableService caseTableService;
	
	/**
	 * 分页列表查询
	 *
	 * @param caseTable
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "案件表-分页列表查询")
	@ApiOperation(value="案件表-分页列表查询", notes="案件表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CaseTable caseTable,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		IPage<CaseTable> pageList = null;
		if(caseTable.getReserve1() != null) {
			String[] split = caseTable.getReserve1().split(",");
			caseTable.setReserve1(null);
			QueryWrapper<CaseTable> queryWrapper = QueryGenerator.initQueryWrapper(caseTable, req.getParameterMap());
			queryWrapper.between("create_time", split[0], split[1]);
			Page<CaseTable> page = new Page<CaseTable>(pageNo, pageSize);
			pageList = caseTableService.page(page, queryWrapper);
		}else {
			QueryWrapper<CaseTable> queryWrapper = QueryGenerator.initQueryWrapper(caseTable, req.getParameterMap());
			Page<CaseTable> page = new Page<CaseTable>(pageNo, pageSize);
			pageList = caseTableService.page(page, queryWrapper);
		}
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param caseTable
	 * @return
	 */
	@AutoLog(value = "案件表-添加")
	@ApiOperation(value="案件表-添加", notes="案件表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CaseTable caseTable) {
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		System.out.println("当前用户id："+sysUser.getRealname());
		caseTable.setIntroductionId(sysUser.getRealname());
		caseTable.setCreateTime(date);
		caseTableService.save(caseTable);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param caseTable
	 * @return
	 */
	@AutoLog(value = "案件表-编辑")
	@ApiOperation(value="案件表-编辑", notes="案件表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CaseTable caseTable) {
		caseTableService.updateById(caseTable);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "案件表-通过id删除")
	@ApiOperation(value="案件表-通过id删除", notes="案件表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(HttpServletRequest request,@RequestParam(name="id",required=true) String id) {
		deleteCase(request,id);
		caseTableService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "案件表-批量删除")
	@ApiOperation(value="案件表-批量删除", notes="案件表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.caseTableService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "案件表-通过id查询")
	@ApiOperation(value="案件表-通过id查询", notes="案件表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CaseTable caseTable = caseTableService.getById(id);
		return Result.ok(caseTable);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param caseTable
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, CaseTable caseTable) {
      return super.exportXls(request, caseTable, CaseTable.class, "案件表");
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
      return super.importExcel(request, response, CaseTable.class);
  }
  
  /**
   * 通过案件id删除 
   *
   * @param request
   * @param response
   * @return
   */
  @DeleteMapping(value = "/deleteCase")
  public void deleteCase(HttpServletRequest request,@RequestParam(name="id",required=true)String id) {
	  BankStatement bankStatement = new BankStatement();
	  bankStatement.setCaseId(id);
	  QueryWrapper<BankStatement> queryWrapper = QueryGenerator.initQueryWrapper(bankStatement, request.getParameterMap());
	  bankStatementService.remove(queryWrapper);
	  
	  MaximumBalance maximumBalance = new MaximumBalance();
	  maximumBalance.setCaseId(id);
	  QueryWrapper<MaximumBalance> queryWrapper1 = QueryGenerator.initQueryWrapper(maximumBalance, request.getParameterMap());
	  maximumBalanceService.remove(queryWrapper1);
	  
	  Dqrq dqrq = new Dqrq();
	  dqrq.setCaseId(id);
	  QueryWrapper<Dqrq> queryWrapper2 = QueryGenerator.initQueryWrapper(dqrq, request.getParameterMap());
	  dqrqService.remove(queryWrapper2);
	  HashMap<Object,Object> hashMap = new HashMap<>();
	  hashMap.put("caseId", id);
	  hashMap.put("type", 0);
	  caseTableService.upCase(hashMap);
  }

}
