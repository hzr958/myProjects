package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果确认邮件通知合作者状态，用于记录用户最后确认成果时间.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_CFM_CPMAIL_STAT")
public class PubCfmCpMailStat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 932031912171604710L;
  // 人员ID
  private Long psnId;
  // 发送次数，允许尝试3次
  private Integer num = 0;
  // 运行状态，0待发通知，1正在运行，9发送错误
  private Integer status = 0;
  // 最后确认时间
  private Date cfmAt;
  // 最后发送时间
  private Date opAt;

  public PubCfmCpMailStat() {
    super();
  }

  public PubCfmCpMailStat(Long psnId) {
    super();
    this.psnId = psnId;
    this.num = 0;
    this.status = 0;
    this.cfmAt = new Date();
    this.opAt = new Date();
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "NUM")
  public Integer getNum() {
    return num;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "CFM_AT")
  public Date getCfmAt() {
    return cfmAt;
  }

  @Column(name = "OP_AT")
  public Date getOpAt() {
    return opAt;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setNum(Integer num) {
    this.num = num;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCfmAt(Date cfmAt) {
    this.cfmAt = cfmAt;
  }

  public void setOpAt(Date opAt) {
    this.opAt = opAt;
  }

}
