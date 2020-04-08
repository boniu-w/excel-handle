package org.jeecg.modules.app.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Objects;


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
    private float transactionAmount;

    @Excel(name = "摘要")
    private String abstract_content;

    @Excel(name = "交易后余额")
    private float balanceTransaction;

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

    private Integer deleteIdentifier;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankFlow bankFlow = (BankFlow) o;
        return Float.compare(bankFlow.transactionAmount, transactionAmount) == 0 &&
          Float.compare(bankFlow.balanceTransaction, balanceTransaction) == 0 &&
          Objects.equals(id, bankFlow.id) &&
          Objects.equals(caseId, bankFlow.caseId) &&
          Objects.equals(introductionId, bankFlow.introductionId) &&
          Objects.equals(transactionSubject, bankFlow.transactionSubject) &&
          Objects.equals(accountSubject, bankFlow.accountSubject) &&
          Objects.equals(cardEntity, bankFlow.cardEntity) &&
          Objects.equals(recoveryMark, bankFlow.recoveryMark) &&
          Objects.equals(transactionDate, bankFlow.transactionDate) &&
          Objects.equals(counterParty, bankFlow.counterParty) &&
          Objects.equals(accountCounterparty, bankFlow.accountCounterparty) &&
          Objects.equals(cardCounterparty, bankFlow.cardCounterparty) &&
          Objects.equals(abstract_content, bankFlow.abstract_content) &&
          Objects.equals(transactionBank, bankFlow.transactionBank) &&
          Objects.equals(counterpartyBank, bankFlow.counterpartyBank) &&
          Objects.equals(placeTransaction, bankFlow.placeTransaction) &&
          Objects.equals(tradingPlace, bankFlow.tradingPlace) &&
          Objects.equals(transactionNumber, bankFlow.transactionNumber) &&
          Objects.equals(mac, bankFlow.mac) &&
          Objects.equals(ip, bankFlow.ip) &&
          Objects.equals(currency, bankFlow.currency) &&
          Objects.equals(temarks, bankFlow.temarks) &&
          Objects.equals(tradingNo, bankFlow.tradingNo) &&
          Objects.equals(tellerNumber, bankFlow.tellerNumber) &&
          Objects.equals(institutionParty, bankFlow.institutionParty) &&
          Objects.equals(deleteIdentifier, bankFlow.deleteIdentifier) &&
          Objects.equals(customerCode, bankFlow.customerCode) &&
          Objects.equals(logNumber, bankFlow.logNumber) &&
          Objects.equals(apshPlace, bankFlow.apshPlace) &&
          Objects.equals(matcherCode, bankFlow.matcherCode) &&
          Objects.equals(matcherBalance, bankFlow.matcherBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, caseId, introductionId, transactionSubject, accountSubject, cardEntity, recoveryMark, transactionDate, counterParty, accountCounterparty, cardCounterparty, transactionAmount, abstract_content, balanceTransaction, transactionBank, counterpartyBank, placeTransaction, tradingPlace, transactionNumber, mac, ip, currency, temarks, tradingNo, tellerNumber, institutionParty, deleteIdentifier, customerCode, logNumber, apshPlace, matcherCode, matcherBalance);
    }
}