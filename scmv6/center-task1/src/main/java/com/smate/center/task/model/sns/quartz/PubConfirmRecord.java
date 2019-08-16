package com.smate.center.task.model.sns.quartz;

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
 * 成果确认记录表.
 * 
 * @author lqh
 * 
 */
@Entity
@Table(name = "PUB_CONFIRM_RECORD")
public class PubConfirmRecord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -213682892762303668L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_CONFIRM_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "ROL_PUB_ID")
  private Long rolPubId;
  @Column(name = "SNS_PUB_ID")
  private Long snsPubId;
  @Column(name = "SYNC_AT")
  private Date syncAt;
  // 是否同步标记推荐系统成功,0未同步，1同步成功，9同步失败
  @Column(name = "SYNC_RCMD")
  private Integer syncRcmd = 0;

  public PubConfirmRecord() {
    super();
  }

  public PubConfirmRecord(Long id, Long psnId, Long insId, Long rolPubId, Long snsPubId, Date syncAt,
      Integer syncRcmd) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.insId = insId;
    this.rolPubId = rolPubId;
    this.snsPubId = snsPubId;
    this.syncAt = syncAt;
    this.syncRcmd = syncRcmd;
  }

  public PubConfirmRecord(Long psnId, Long insId, Long rolPubId, Long snsPubId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.rolPubId = rolPubId;
    this.snsPubId = snsPubId;
    this.syncAt = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getRolPubId() {
    return rolPubId;
  }

  public void setRolPubId(Long rolPubId) {
    this.rolPubId = rolPubId;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public Date getSyncAt() {
    return syncAt;
  }

  public void setSyncAt(Date syncAt) {
    this.syncAt = syncAt;
  }

  public Integer getSyncRcmd() {
    return syncRcmd;
  }

  public void setSyncRcmd(Integer syncRcmd) {
    this.syncRcmd = syncRcmd;
  }

}
