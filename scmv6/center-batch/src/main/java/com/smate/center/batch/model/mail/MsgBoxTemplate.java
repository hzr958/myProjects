package com.smate.center.batch.model.mail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 消息中心查看详情模板.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "MSG_BOX_TEMPLATE")
public class MsgBoxTemplate implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -6153114090050631858L;
  private int tmpId;
  private String tmpName;
  private String remark;

  @Id
  @Column(name = "TMP_ID")
  public int getTmpId() {
    return tmpId;
  }

  public void setTmpId(int tmpId) {
    this.tmpId = tmpId;
  }

  @Column(name = "TMP_NAME")
  public String getTmpName() {
    return tmpName;
  }

  public void setTmpName(String tmpName) {
    this.tmpName = tmpName;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
