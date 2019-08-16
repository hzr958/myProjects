package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果类型
 * 
 * @author jszhou
 *
 */
@Entity
@Table(name = "CONST_PUB_TYPE")
public class SieConstPubType implements Serializable {

  private static final long serialVersionUID = 2604384174807880623L;

  private Integer typeId;

  private String zhName;

  private String enName;

  private boolean enabled;

  private int seqNo;

  @Id
  @Column(name = "TYPE_ID")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "ENABLED")
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Column(name = "SEQ_NO")
  public int getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(int seqNo) {
    this.seqNo = seqNo;
  }

}
