package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 动态内容.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "DYN_MSG_CONTENT")
public class DynMsgContent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6195697778046463628L;

  private Long dcId;
  public String dynJson;

  public DynMsgContent() {

  }

  public DynMsgContent(Long dcId, String dynJson) {
    this.dcId = dcId;
    this.dynJson = dynJson;
  }

  @Id
  @Column(name = "DC_ID")
  public Long getDcId() {
    return dcId;
  }

  public void setDcId(Long dcId) {
    this.dcId = dcId;
  }

  @Column(name = "DYN_JSON")
  public String getDynJson() {
    return dynJson;
  }

  public void setDynJson(String dynJson) {
    this.dynJson = dynJson;
  }

}
