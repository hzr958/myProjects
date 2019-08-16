package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人抓取成果XML临时存储表合并异常.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "DBCACHE_PFETCH_ERROR")
public class DbCachePfetchError implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -9044311579563864858L;
  private Long xmlId;
  // 异常日志
  private String errorMsg;

  public DbCachePfetchError() {
    super();
  }

  public DbCachePfetchError(Long xmlId, String errorMsg) {
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
