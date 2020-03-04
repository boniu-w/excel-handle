package org.jeecg.modules.demo.bankstatement.entity;

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
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Data
@TableName("bank_statement")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bank_statement对象", description="流水表")
public class BankStatement {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**案件id*/
	@Excel(name = "案件id", width = 15)
    @ApiModelProperty(value = "案件id")
	private java.lang.String caseId;
	/**交易日期*/
	@Excel(name = "交易日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "交易日期")
	private java.util.Date transactionDate;
	/**交易时间*/
	@Excel(name = "交易时间", width = 15, format = "HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @ApiModelProperty(value = "交易时间")
	private java.util.Date transactionTime;
	/**查询卡号*/
	@Excel(name = "查询卡号", width = 15)
    @ApiModelProperty(value = "查询卡号")
	private java.lang.String queryCardNumber;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
	private java.lang.String fullName;
	/**账号余额*/
	@Excel(name = "账号余额", width = 15)
    @ApiModelProperty(value = "账号余额")
	private java.lang.Double accountBalance;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
	private java.lang.Double transactionAmount;
	/**借贷标识*/
	@Excel(name = "借贷标识", width = 15)
    @ApiModelProperty(value = "借贷标识")
	private java.lang.String loanIdentification;
	/**对手账号*/
	@Excel(name = "对手账号", width = 15)
    @ApiModelProperty(value = "对手账号")
	private java.lang.String opponentAccountNumber;
	/**户名*/
	@Excel(name = "户名", width = 15)
    @ApiModelProperty(value = "户名")
	private java.lang.String accountName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createId;
	/**Reserve1*/
	@Excel(name = "Reserve1", width = 15)
    @ApiModelProperty(value = "Reserve1")
	private java.lang.String reserve1;
	/**Reserve2*/
	@Excel(name = "Reserve2", width = 15)
    @ApiModelProperty(value = "Reserve2")
	private java.lang.String reserve2;
}
