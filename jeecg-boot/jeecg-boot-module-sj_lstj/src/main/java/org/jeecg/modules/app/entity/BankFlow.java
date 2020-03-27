package org.jeecg.modules.app.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
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
  private String abstract_content;
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


}
