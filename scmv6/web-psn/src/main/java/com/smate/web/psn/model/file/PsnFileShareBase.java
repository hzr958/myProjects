package com.smate.web.psn.model.file;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人文件分享主表
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_PSN_FILE_SHARE_BASE")
public class PsnFileShareBase implements Serializable {

  private static final long serialVersionUID = -3479630081395088944L;

  @Id
  @Column(name = "ID")
  private Long id;

  /**
   * 分享人id
   */
  @Column(name = "SHARER_ID")
  private Long sharerId;

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
  @Column(name = "share_Content_Rel")
  private String shareContentRel; // 分享内容 关系记录 记录站内信关系id 以便取消关注 多个用逗号隔开

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

  public PsnFileShareBase(Long id, Long sharerId, Date createDate, Date updateDate, Integer status) {
    super();
    this.id = id;
    this.sharerId = sharerId;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.status = status;
  }

  public PsnFileShareBase() {
    super();
  }

  public String getShareContentRel() {
    return shareContentRel;
  }

  public void setShareContentRel(String shareContentRel) {
    this.shareContentRel = shareContentRel;
  }

}
