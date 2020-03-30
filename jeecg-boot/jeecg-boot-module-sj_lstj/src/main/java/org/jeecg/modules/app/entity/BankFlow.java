package org.jeecg.modules.app.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;


@Data
public class BankFlow {

    private String id;

    @Excel(name = "案件id")
    private String caseId;

    @Excel(name = "导入人id", width = 15)
    private String introductionId;

    @Excel(name = "交易主体", width = 15)
    private String transactionSubject;

    @Excel(name = "交易主体账号")
    private String accountSubject;

    @Excel(name = "交易主体卡号")
    private String cardEntity;

    @Excel(name = "收付标志")
    private String recoveryMark;

    @Excel(name = "交易时间")
    private java.sql.Timestamp transactionDate;

    @Excel(name = "交易对手")
    private String counterParty;

    @Excel(name = "交易对手账号")
    private String accountCounterparty;

    @Excel(name = "交易主体卡号")
    private String cardCounterparty;

    @Excel(name = "交易金额", width = 15)
    private double transactionAmount;

    @Excel(name = "摘要")
    private String abstract_content;

    @Excel(name = "交易后余额")
    private double balanceTransaction;

    @Excel(name = "交易主体归属行")
    private String transactionBank;

    @Excel(name = "交易对手归属行")
    private String counterpartyBank;

    @Excel(name = "交易地点")
    private String placeTransaction;

    @Excel(name = "交易方式")
    private String tradingPlace;

    @Excel(name = "交易流水号")
    private String transactionNumber;

    @Excel(name = "mac地址")
    private String mac;

    @Excel(name = "ip地址")
    private String ip;

    @Excel(name = "币种")
    private String currency;

    @Excel(name = "备注")
    private String temarks;

    @Excel(name = "交易机构号")
    private String tradingNo;

    @Excel(name = "柜员号")
    private String tellerNumber;

    @Excel(name = "对方机构号")
    private String institutionParty;

    private double deleteIdentifier;

    @Excel(name = "客户代码")
    private String customerCode;

    @Excel(name = "日志号")
    private String logNumber;

    @Excel(name = "apsh地点")
    private String apshPlace;

    @Excel(name = "交易对手客户代码")
    private String matcherCode;

    @Excel(name = "对手交易后余额")
    private String matcherBalance;


}
