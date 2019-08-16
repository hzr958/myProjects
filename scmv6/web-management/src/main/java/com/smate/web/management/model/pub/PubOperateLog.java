package com.smate.web.management.model.pub;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 管理系统成果操作日志表
 * 
 * @author yhx
 *
 */
@Entity
@Table(name = "V_PUB_OPERATE_LOG")
public class PubOperateLog {

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_OPERATE_LOG", sequenceName = "V_SEQ_PUB_OPERATE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_OPERATE_LOG")
  @Column(name = "ID")
  private Long id;// 主键
  @Column(name = "OP_PSN_ID")
  private Long opPsnId;// 操作人
  @Column(name = "PUB_ID")
  private Long pubId;// 成果id
  @Column(name = "OP_TYPE")
  private Long opType;// 操作类型 0删除
  @Column(name = "DESC_MSG")
  private String descMsg;// 描述
  @Column(name = "CREATE_DATE")
  private Date createDate;// 操作时间

  public PubOperateLog() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOpPsnId() {
    return opPsnId;
  }

  public void setOpPsnId(Long opPsnId) {
    this.opPsnId = opPsnId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getOpType() {
    return opType;
  }

  public void setOpType(Long opType) {
    this.opType = opType;
  }

  public String getDescMsg() {
    return descMsg;
  }

  public void setDescMsg(String descMsg) {
    this.descMsg = descMsg;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public PubOperateLog(Long id, Long opPsnId, Long pubId, Long opType, String descMsg, Date createDate) {
    super();
    this.id = id;
    this.opPsnId = opPsnId;
    this.pubId = pubId;
    this.opType = opType;
    this.descMsg = descMsg;
    this.createDate = createDate;
  }

  public PubOperateLog(Long opPsnId, Long pubId, Long opType, String descMsg, Date createDate) {
    super();
    this.opPsnId = opPsnId;
    this.pubId = pubId;
    this.opType = opType;
    this.descMsg = descMsg;
    this.createDate = createDate;
  }

}
