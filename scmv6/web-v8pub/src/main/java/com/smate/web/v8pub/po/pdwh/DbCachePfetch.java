package com.smate.web.v8pub.po.pdwh;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 个人抓取成果XML临时存储表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "DBCACHE_PFETCH")
public class DbCachePfetch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2733638878087490683L;

  private Long xmlId;
  // 外键，人员ID
  private Long psnId;
  // 单位ID，个人抓取为空
  private Long insId;
  // 文件数据
  private String xmlData;
  // 0未处理，9异常
  private Integer status = 0;
  // 1：sns,2:rol
  private Integer webType;
  // Xml检索时间
  private Date fetchTime;

  public DbCachePfetch() {
    super();
  }

  public DbCachePfetch(Long psnId, Long insId, String xmlData, Integer webType, Date fetchTime) {
    super();
    status = 0;
    this.psnId = psnId;
    this.insId = insId;
    this.xmlData = xmlData;
    this.webType = webType;
    this.fetchTime = fetchTime;
  }

  @Id
  @Column(name = "XML_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DBCACHE_PFETCH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getXmlId() {
    return xmlId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "XML_DATA")
  public String getXmlData() {
    return xmlData;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "WEB_TYPE")
  public Integer getWebType() {
    return webType;
  }

  @Column(name = "FETCH_TIME")
  public Date getFetchTime() {
    return fetchTime;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setXmlData(String xmlData) {
    this.xmlData = xmlData;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setWebType(Integer webType) {
    this.webType = webType;
  }

  public void setFetchTime(Date fetchTime) {
    this.fetchTime = fetchTime;
  }

}
