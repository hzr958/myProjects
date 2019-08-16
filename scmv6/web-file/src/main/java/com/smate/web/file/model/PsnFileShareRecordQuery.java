package com.smate.web.file.model;

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
 * 个人文件分享记录
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_PSN_FILE_SHARE_RECORD")
public class PsnFileShareRecordQuery implements Serializable {

  private static final long serialVersionUID = -3479630081395088944L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_PSN_FILE_SHARE_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  /**
   * 分享人id
   */
  @Column(name = "SHARER_ID")
  private Long sharerId;

  /**
   * 接受人id 分号隔离
   */
  @Column(name = "RECEIVER_ID")
  private Long reveiverId;

  /**
   * 文件id 分号隔离
   */
  @Column(name = "FILE_ID")
  private Long fileId;

  /**
   * 创建时间
   */
  @Column(name = "CREATE_DATE")
  private Date createDate;

  /**
   * 更新时间
   */
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  /**
   * 状态 0=正常 ； 1=取消 ； 2=删除
   */
  @Column(name = "STATUS")
  private Integer status;

  /**
   * 站内信，消息关联表的id
   */
  @Column(name = "MSG_RELATION_ID")
  private Long msgRelationId;
  /**
   * 分享主表id
   */
  @Column(name = "SHARE_BASE_ID")
  private Long shareBaseId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSharerId() {
    return sharerId;
  }

  public void setSharerId(Long sharerId) {
    this.sharerId = sharerId;
  }

  public Long getReveiverId() {
    return reveiverId;
  }

  public void setReveiverId(Long reveiverId) {
    this.reveiverId = reveiverId;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getMsgRelationId() {
    return msgRelationId;
  }

  public void setMsgRelationId(Long msgRelationId) {
    this.msgRelationId = msgRelationId;
  }

  public Long getShareBaseId() {
    return shareBaseId;
  }

  public void setShareBaseId(Long shareBaseId) {
    this.shareBaseId = shareBaseId;
  }

  public PsnFileShareRecordQuery(Long id, Long sharerId, Long reveiverId, Long fileId, Date createDate, Date updateDate,
      Integer status, Long msgRelationId, Long shareBaseId) {
    super();
    this.id = id;
    this.sharerId = sharerId;
    this.reveiverId = reveiverId;
    this.fileId = fileId;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.status = status;
    this.msgRelationId = msgRelationId;
    this.shareBaseId = shareBaseId;
  }

  public PsnFileShareRecordQuery() {
    super();
  }

}
