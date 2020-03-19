package org.jeecg.modules.demo.bqrq.entity;

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
 * @Description: 补全流水表
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
@Data
@TableName("bqrq")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bqrq对象", description="补全流水表")
public class Dqrq {
    
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
	/**流水序号*/
	@Excel(name = "流水序号", width = 15)
    @ApiModelProperty(value = "流水序号")
	private java.lang.Integer maxreserve2;
	/**最后账号余额*/
	@Excel(name = "最后账号余额", width = 15)
    @ApiModelProperty(value = "最后账号余额")
	private java.lang.Double minbalance;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
	private java.lang.Double maxamount;
	/**卡号*/
	@Excel(name = "卡号", width = 15)
    @ApiModelProperty(value = "卡号")
	private java.lang.String queryCardNumber;
	/**案件id*/
	@Excel(name = "案件id", width = 15)
    @ApiModelProperty(value = "案件id")
	private java.lang.String caseId;
	/**流水条数*/
	@Excel(name = "流水条数", width = 15)
    @ApiModelProperty(value = "流水条数")
	private java.lang.Integer num;
	/**最后余额*/
	@Excel(name = "最后余额", width = 15)
    @ApiModelProperty(value = "最后余额")
	private java.lang.Double MaxBalance;
}
