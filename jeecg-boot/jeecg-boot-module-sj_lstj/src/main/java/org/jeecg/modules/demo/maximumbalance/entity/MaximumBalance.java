package org.jeecg.modules.demo.maximumbalance.entity;

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
 * @Description: 最大余额表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("maximum_balance")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="maximum_balance对象", description="最大余额表")
public class MaximumBalance {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**日期*/
	@Excel(name = "日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
	private java.util.Date date;
	/**最大金额*/
	@Excel(name = "最大金额", width = 15)
    @ApiModelProperty(value = "最大金额")
	private java.lang.Double maxMoney;
	/**最大余额*/
	@Excel(name = "最大余额", width = 15)
    @ApiModelProperty(value = "最大余额")
	private java.lang.Double maxBalance;
	/**案件id*/
	@Excel(name = "案件id", width = 15)
    @ApiModelProperty(value = "案件id")
	private java.lang.String caseId;
	/**Reserve1*/
	@Excel(name = "Reserve1", width = 15)
    @ApiModelProperty(value = "Reserve1")
	private java.lang.String reserve1;
	/**Reserve2*/
	@Excel(name = "Reserve2", width = 15)
    @ApiModelProperty(value = "Reserve2")
	private java.lang.String reserve2;
	/**日期*/
	@Excel(name = "日期", width = 15, format = "yyyy-MM-dd hh:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "日期")
	private java.util.Date create_time;
}
