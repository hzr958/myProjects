package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 批量抓取成果XML临时存储表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "DBCACHE_BFETCH")
public class DbCacheBfetch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1911052680437455107L;

  /**
   * XML_ID NUMBER(18) N 主键 NUMBER(18) N 单位ID CLOB N 文件数据 NUMBER(1) N 0 0未处理，1已处理，9异常 VARCHAR2(1000
   * CHAR) Y 异常消息
   */

  private Long xmlId;
  // 单位ID
  private Long insId;
  // 文件数据
  private String xmlData;
  // XML导入文件名
  private String fileName;
  // 成果年份
  private Integer pubYear;
  // 0未处理，1已处理，9异常
  private Integer status = 0;
  // 异常消息
  private String errorMsg;

  public DbCacheBfetch() {
    super();
  }

  public DbCacheBfetch(Long insId, String xmlData, String fileName, Integer pubYear) {
    super();
    this.insId = insId;
    this.xmlData = xmlData;
    this.fileName = fileName;
    this.pubYear = pubYear;
    this.status = 0;
  }

  public DbCacheBfetch(Long insId, String xmlData, String fileName, Integer pubYear, Integer status) {
    super();
    this.insId = insId;
    this.xmlData = xmlData;
    this.fileName = fileName;
    this.pubYear = pubYear;
    this.status = status;
  }

  @Id
  @Column(name = "XML_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DBCACHE_BFETCH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getXmlId() {
    return xmlId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "XML_DATA")
  public String getXmlData() {
    return xmlData;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  @Column(name = "FILE_NAME")
  public String getFileName() {
    return fileName;
  }

  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

}
