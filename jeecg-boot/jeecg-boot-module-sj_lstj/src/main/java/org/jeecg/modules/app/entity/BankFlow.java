package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("bank_flow")
public class BankFlow {

  private String id;
  private String caseId;
  private String introductionId;
  private String transactionSubject;
  private String accountSubject;
  private String cardEntity;
  private String recoveryMark;
  private java.sql.Timestamp transactionDate;
  private String counterParty;
  private String accountCounterparty;
  private String cardCounterparty;
  private double transactionAmount;
  private String abstractContent;
  private double balanceTransaction;
  private String transactionBank;
  private String counterpartyBank;
  private String placeTransaction;
  private String tradingPlace;
  private String transactionNumber;
  private String mac;
  private String ip;
  private String currency;
  private String temarks;
  private String tradingNo;
  private String tellerNumber;
  private String institutionParty;
  private double deleteIdentifier;
  private String customerCode;
  private String logNumber;
  private String apshPlace;
  private String matcherCode;
  private String matcherBalance;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getIntroductionId() {
    return introductionId;
  }

  public void setIntroductionId(String introductionId) {
    this.introductionId = introductionId;
  }


  public String getTransactionSubject() {
    return transactionSubject;
  }

  public void setTransactionSubject(String transactionSubject) {
    this.transactionSubject = transactionSubject;
  }


  public String getAccountSubject() {
    return accountSubject;
  }

  public void setAccountSubject(String accountSubject) {
    this.accountSubject = accountSubject;
  }


  public String getCardEntity() {
    return cardEntity;
  }

  public void setCardEntity(String cardEntity) {
    this.cardEntity = cardEntity;
  }


  public String getRecoveryMark() {
    return recoveryMark;
  }

  public void setRecoveryMark(String recoveryMark) {
    this.recoveryMark = recoveryMark;
  }


  public java.sql.Timestamp getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(java.sql.Timestamp transactionDate) {
    this.transactionDate = transactionDate;
  }


  public String getCounterParty() {
    return counterParty;
  }

  public void setCounterParty(String counterParty) {
    this.counterParty = counterParty;
  }


  public String getAccountCounterparty() {
    return accountCounterparty;
  }

  public void setAccountCounterparty(String accountCounterparty) {
    this.accountCounterparty = accountCounterparty;
  }


  public String getCardCounterparty() {
    return cardCounterparty;
  }

  public void setCardCounterparty(String cardCounterparty) {
    this.cardCounterparty = cardCounterparty;
  }


  public double getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(double transactionAmount) {
    this.transactionAmount = transactionAmount;
  }


  public String getAbstractContent() {
    return abstractContent;
  }

  public void setAbstractContent(String abstractContent) {
    this.abstractContent = abstractContent;
  }


  public double getBalanceTransaction() {
    return balanceTransaction;
  }

  public void setBalanceTransaction(double balanceTransaction) {
    this.balanceTransaction = balanceTransaction;
  }


  public String getTransactionBank() {
    return transactionBank;
  }

  public void setTransactionBank(String transactionBank) {
    this.transactionBank = transactionBank;
  }


  public String getCounterpartyBank() {
    return counterpartyBank;
  }

  public void setCounterpartyBank(String counterpartyBank) {
    this.counterpartyBank = counterpartyBank;
  }


  public String getPlaceTransaction() {
    return placeTransaction;
  }

  public void setPlaceTransaction(String placeTransaction) {
    this.placeTransaction = placeTransaction;
  }


  public String getTradingPlace() {
    return tradingPlace;
  }

  public void setTradingPlace(String tradingPlace) {
    this.tradingPlace = tradingPlace;
  }


  public String getTransactionNumber() {
    return transactionNumber;
  }

  public void setTransactionNumber(String transactionNumber) {
    this.transactionNumber = transactionNumber;
  }


  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }


  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  public String getTemarks() {
    return temarks;
  }

  public void setTemarks(String temarks) {
    this.temarks = temarks;
  }


  public String getTradingNo() {
    return tradingNo;
  }

  public void setTradingNo(String tradingNo) {
    this.tradingNo = tradingNo;
  }


  public String getTellerNumber() {
    return tellerNumber;
  }

  public void setTellerNumber(String tellerNumber) {
    this.tellerNumber = tellerNumber;
  }


  public String getInstitutionParty() {
    return institutionParty;
  }

  public void setInstitutionParty(String institutionParty) {
    this.institutionParty = institutionParty;
  }


  public double getDeleteIdentifier() {
    return deleteIdentifier;
  }

  public void setDeleteIdentifier(double deleteIdentifier) {
    this.deleteIdentifier = deleteIdentifier;
  }


  public String getCustomerCode() {
    return customerCode;
  }

  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }


  public String getLogNumber() {
    return logNumber;
  }

  public void setLogNumber(String logNumber) {
    this.logNumber = logNumber;
  }


  public String getApshPlace() {
    return apshPlace;
  }

  public void setApshPlace(String apshPlace) {
    this.apshPlace = apshPlace;
  }


  public String getMatcherCode() {
    return matcherCode;
  }

  public void setMatcherCode(String matcherCode) {
    this.matcherCode = matcherCode;
  }


  public String getMatcherBalance() {
    return matcherBalance;
  }

  public void setMatcherBalance(String matcherBalance) {
    this.matcherBalance = matcherBalance;
  }

}
