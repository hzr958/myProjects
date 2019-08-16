package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 好友对个人的评价表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIENDS_FAPPRAISAL_REC")
public class FriendFappraisalRec implements Serializable {

  private static final long serialVersionUID = 358302368183107637L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // psnId desc3
  private String des3TempPsnId;
  // 人员工作经历id
  private Long psnWorkId;
  // 好友的psnId
  private Long friendPsnId;
  // 好友姓名
  private String friendPsnName;
  // 好友英文姓名
  private String friendPsnEnName;
  // 被评价的工作单位
  private String psnWork;
  // 评价时选择与好友的关系(中文)
  private String relations;
  // 评价时选择与好友的关系（英文）
  private String relationsEn;
  // 好友对个人的评价内容
  private String content;
  // 评价时间
  private Date sendDate;
  // 评价人头像
  private String friendHead;
  // 公开状态，null待处理，1已公开，0未公开
  private Integer appStatus;

  private String psnName;
  private String friendPsnTitle;
  /** 关系，显示时使用. */
  private String viewRelations;

  public FriendFappraisalRec() {
    super();
    this.sendDate = new Date();
  }

  @Id
  @Column(name = "FAPPRAISAL_REC_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIENDS_FAPPRAISAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FRIEND_PSN_ID")
  public Long getFriendPsnId() {
    return friendPsnId;
  }

  public void setFriendPsnId(Long friendPsnId) {
    this.friendPsnId = friendPsnId;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "FRIEND_PSN_NAME")
  public String getFriendPsnName() {
    return friendPsnName;
  }

  public void setFriendPsnName(String friendPsnName) {
    this.friendPsnName = friendPsnName;
  }

  @Column(name = "FRIEND_PSN_EN_NAME")
  public String getFriendPsnEnName() {
    return friendPsnEnName;
  }

  public void setFriendPsnEnName(String friendPsnEnName) {
    this.friendPsnEnName = friendPsnEnName;
  }

  @Column(name = "PSN_WORK")
  public String getPsnWork() {
    return psnWork;
  }

  public void setPsnWork(String psnWork) {
    this.psnWork = psnWork;
  }

  @Column(name = "RELATIONS")
  public String getRelations() {
    return relations;
  }

  public void setRelations(String relations) {
    this.relations = relations;
  }

  @Column(name = "SEND_DATE")
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  @Column(name = "PSN_WORK_ID")
  public Long getPsnWorkId() {
    return psnWorkId;
  }

  public void setPsnWorkId(Long psnWorkId) {
    this.psnWorkId = psnWorkId;
  }

  @Column(name = "FRIEND_HEAD")
  public String getFriendHead() {
    return friendHead;
  }

  public void setFriendHead(String friendHead) {
    this.friendHead = friendHead;
  }

  @Column(name = "APP_STATUS")
  public Integer getAppStatus() {
    return appStatus;
  }

  public void setAppStatus(Integer appStatus) {
    this.appStatus = appStatus;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public String getDes3TempPsnId() {
    if (this.friendPsnId != null && des3TempPsnId == null) {
      des3TempPsnId = ServiceUtil.encodeToDes3(this.friendPsnId.toString());
    }
    return des3TempPsnId;
  }

  public void setDes3TempPsnId(String des3TempPsnId) {
    this.des3TempPsnId = des3TempPsnId;
  }

  /**
   * @return the psnName
   */
  @Transient
  public String getPsnName() {
    return psnName;
  }

  /**
   * @param psnName the psnName to set
   */
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "RELATIONS_EN")
  public String getRelationsEn() {
    return relationsEn;
  }

  public void setRelationsEn(String relationsEn) {
    this.relationsEn = relationsEn;
  }

  @Transient
  public String getFriendPsnTitle() {
    return friendPsnTitle;
  }

  public void setFriendPsnTitle(String friendPsnTitle) {
    this.friendPsnTitle = friendPsnTitle;
  }

  @Transient
  public String getViewRelations() {
    return viewRelations;
  }

  public void setViewRelations(String viewRelations) {
    this.viewRelations = viewRelations;
  }

}
