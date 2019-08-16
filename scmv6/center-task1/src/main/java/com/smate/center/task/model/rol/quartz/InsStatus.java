package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;
/**
 * 单位状态，是否是BPO审核通过，还是基金委科研在线那边登录过
 * 
 * @author hd
 *
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INS_STATUS")
public class InsStatus implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -203872460327486750L;
  // 单位ID
  private Long insId;
  // BPO是否审核通过,默认0
  private Long bpostatus = 0L;
  // 基金委科研在线是否登录 ,默认0
  private Long scmLogin = 0L;

  public InsStatus() {}

  public InsStatus(Long insId) {
    this.insId = insId;
  }


  public InsStatus(Long insId, Long bpostatus, Long scmLogin) {
    this.insId = insId;
    this.bpostatus = bpostatus;
    this.scmLogin = scmLogin;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "BPOREG_STATUS")
  public Long getBpostatus() {
    return bpostatus;
  }

  public void setBpostatus(Long bpostatus) {
    this.bpostatus = bpostatus;
  }

  @Column(name = "SCMLOGIN_STATUS")
  public Long getScmLogin() {
    return scmLogin;
  }

  public void setScmLogin(Long scmLogin) {
    this.scmLogin = scmLogin;
  }


}
