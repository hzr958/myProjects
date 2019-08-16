package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 缓存数据表，客户端exe抓取程序使用的临时缓存库.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "DBCACHE")
public class DbCache implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6962602036641336913L;

  private Long xmlId;
  // 单位ID
  private Long insId;
  // 出版年份
  private Integer pubYear;
  // 网站ID
  private Integer dbId;
  // 文件数据
  private String xmlData;

  @Id
  @Column(name = "XML_ID")
  public Long getXmlId() {
    return xmlId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PUBYEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  @Column(name = "DBID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "XML_DATA")
  public String getXmlData() {
    return xmlData;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

}
