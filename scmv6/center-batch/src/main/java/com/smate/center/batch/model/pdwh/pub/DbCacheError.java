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
@Table(name = "DBCACHE_ERROR")
public class DbCacheError implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8076279095842460268L;

  private Long xmlId;
  // 异常日志
  private String errorMsg;

  public DbCacheError() {
    super();
  }

  public DbCacheError(Long xmlId, String errorMsg) {
    super();
    this.xmlId = xmlId;
    this.errorMsg = errorMsg;
  }

  @Id
  @Column(name = "XML_ID")
  public Long getXmlId() {
    return xmlId;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

}
