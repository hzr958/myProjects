package com.smate.center.task.model.rol.quartz;

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
 * 成果确认邮件通知合作者的成果ID.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_CFM_CPMAIL_PUB")
public class PubCfmCpMailPub implements Serializable {

  // 主键
  private Long id;
  // 人员ID
  private Long psnId;
  // 单位ID
  private Long insId;
  // 成果ID
  private Long pubId;
  // 确认时间
  private Date cfmAt;
  // 是否发送过好友推荐0未发送，1已发送,3未找到合作者
  private Integer status = 0;

  public PubCfmCpMailPub() {
    super();
  }

  public PubCfmCpMailPub(Long psnId, Long insId, Long pubId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.pubId = pubId;
    this.cfmAt = new Date();
    this.status = 0;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_CFM_CPMAIL_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "CFM_AT")
  public Date getCfmAt() {
    return cfmAt;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setCfmAt(Date cfmAt) {
    this.cfmAt = cfmAt;
  }
}
