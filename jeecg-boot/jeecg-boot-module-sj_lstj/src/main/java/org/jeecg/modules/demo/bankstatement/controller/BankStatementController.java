package org.jeecg.modules.demo.bankstatement.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="流水表")
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
	@ApiOperation(value="流水表-分页列表查询", notes="流水表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BankStatement bankStatement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		IPage<BankStatement> pageList = null;
		if(bankStatement.getReserve1() != null) {
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
	@ApiOperation(value="流水表-添加", notes="流水表-添加")
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
	@ApiOperation(value="流水表-编辑", notes="流水表-编辑")
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
	@ApiOperation(value="流水表-通过id删除", notes="流水表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
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
	@ApiOperation(value="流水表-批量删除", notes="流水表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
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
	@ApiOperation(value="流水表-通过id查询", notes="流水表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
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

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
	  Result<?> result = super.importExcel(request, response, BankStatement.class);

	  System.out.println("><><><><><"+result.toString());

	  //String caseId = bankStatement.getCaseId();
	  String caseId = "11111111";

	  System.out.println(caseId+"---------");

	  HashMap<Object, Object> hashMap = new HashMap<>();
	  hashMap.put("caseId", caseId);

	  int i = bankStatementService.insertMaximumBalance(hashMap);

	  System.out.println(i+">>>>>>>");

	  return result;
  }

}
