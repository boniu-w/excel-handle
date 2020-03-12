package org.jeecg.modules.demo.bqrq.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.bqrq.entity.Dqrq;
import org.jeecg.modules.demo.bqrq.service.IDqrqService;
import java.util.Date;
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
 * @Description: 补全流水表
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="补全流水表")
@RestController
@RequestMapping("/bqrq/dqrq")
public class DqrqController extends JeecgController<Dqrq, IDqrqService> {
	@Autowired
	private IDqrqService dqrqService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dqrq
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "补全流水表-分页列表查询")
	@ApiOperation(value="补全流水表-分页列表查询", notes="补全流水表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Dqrq dqrq,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Dqrq> queryWrapper = QueryGenerator.initQueryWrapper(dqrq, req.getParameterMap());
		Page<Dqrq> page = new Page<Dqrq>(pageNo, pageSize);
		IPage<Dqrq> pageList = dqrqService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	public IPage<Dqrq> queryPageList1(Dqrq dqrq,
								   HttpServletRequest req) {
		QueryWrapper<Dqrq> queryWrapper = QueryGenerator.initQueryWrapper(dqrq, req.getParameterMap());
		Page<Dqrq> page = new Page<Dqrq>(1, 1000000);
		IPage<Dqrq> pageList = dqrqService.page(page, queryWrapper);
		return pageList;
	}
	
	/**
	 * 添加
	 *
	 * @param dqrq
	 * @return
	 */
	@AutoLog(value = "补全流水表-添加")
	@ApiOperation(value="补全流水表-添加", notes="补全流水表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Dqrq dqrq) {
		dqrqService.save(dqrq);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param dqrq
	 * @return
	 */
	@AutoLog(value = "补全流水表-编辑")
	@ApiOperation(value="补全流水表-编辑", notes="补全流水表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Dqrq dqrq) {
		dqrqService.updateById(dqrq);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "补全流水表-通过id删除")
	@ApiOperation(value="补全流水表-通过id删除", notes="补全流水表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dqrqService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "补全流水表-批量删除")
	@ApiOperation(value="补全流水表-批量删除", notes="补全流水表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dqrqService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "补全流水表-通过id查询")
	@ApiOperation(value="补全流水表-通过id查询", notes="补全流水表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Dqrq dqrq = dqrqService.getById(id);
		return Result.ok(dqrq);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param dqrq
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Dqrq dqrq) {
      return super.exportXls(request, dqrq, Dqrq.class, "补全流水表");
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
      return super.importExcel(request, response, Dqrq.class);
  }

}
