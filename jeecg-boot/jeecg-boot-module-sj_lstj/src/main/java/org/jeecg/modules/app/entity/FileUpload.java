package org.jeecg.modules.app.entity;

import lombok.Data;

@Data
public class FileUpload {

  private String id;
  private String fileName;
  private double fileSize;
  private String fileLocation;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
  private String fileTitle;



}
