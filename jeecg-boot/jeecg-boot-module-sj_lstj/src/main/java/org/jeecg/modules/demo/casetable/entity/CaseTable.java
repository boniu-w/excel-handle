package org.jeecg.modules.demo.casetable.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 案件表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("case_table")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="case_table对象", description="案件表")
public class CaseTable {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**案件名称*/
	@Excel(name = "案件名称", width = 15)
    @ApiModelProperty(value = "案件名称")
	private java.lang.String caseName;
	/**案件类型*/
	@Excel(name = "案件类型", width = 15)
    @ApiModelProperty(value = "案件类型")
	private java.lang.String caseTypeId;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
    @ApiModelProperty(value = "创建人id")
	private java.lang.String introductionId;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
	private java.lang.Object path;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.Object remarks;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
	private java.lang.String timeStamp;
	/**更改次数*/
	@Excel(name = "更改次数", width = 15)
    @ApiModelProperty(value = "更改次数")
	private java.lang.Integer updateCount;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
	private java.lang.Integer deleteIdentifier;
	/**Reserve1*/
	@Excel(name = "Reserve1", width = 15)
    @ApiModelProperty(value = "Reserve1")
	private java.lang.String reserve1;
	/**Reserve2*/
	@Excel(name = "Reserve2", width = 15)
    @ApiModelProperty(value = "Reserve2")
	private java.lang.String reserve2;
	/**Reserve3*/
	@Excel(name = "Reserve3", width = 15)
    @ApiModelProperty(value = "Reserve3")
	private java.lang.String reserve3;
}
