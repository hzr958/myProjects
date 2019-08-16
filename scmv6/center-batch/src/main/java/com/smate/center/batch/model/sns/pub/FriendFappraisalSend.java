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
 * 个人对好友的评价表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIENDS_FAPPRAISAL_SEND")
public class FriendFappraisalSend implements Serializable {

  private static final long serialVersionUID = 8406216618944570415L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 好友的psnId
  private Long friendPsnId;

  // psnId desc3
  private String des3TempPsnId;
  // 好友姓名
  private String friendPsnName;
  // 评价好友工作单位
  private String friendWork;
  // 评价好友工作单位Id
  private Long friendWorkId;
  // 评价时选择与好友的关系（存放中文）
  private String relations;
  // 评价时选择与好友的关系（存放英文）
  private String relationsEn;
  // 评价内容
  private String content;
  // 评价时间
  private Date sendDate;
  private String psnName;

  public FriendFappraisalSend() {
    super();
    this.sendDate = new Date();
  }

  @Id
  @Column(name = "FAPPRAISAL_SEND_ID")
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

  @Column(name = "FRIEND_PSN_WORK")
  public String getFriendWork() {
    return friendWork;
  }

  public void setFriendWork(String friendWork) {
    this.friendWork = friendWork;
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

  @Column(name = "FRIEND_WORK_ID")
  public Long getFriendWorkId() {
    return friendWorkId;
  }

  public void setFriendWorkId(Long friendWorkId) {
    this.friendWorkId = friendWorkId;
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

}
