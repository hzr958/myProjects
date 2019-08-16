package com.smate.web.dyn.model.rsycrcmd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推荐系统同步人员信息标记.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "RCMD_SYNC_PSNINFO")
public class RcmdSyncPsnInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3806240603654825676L;

  // 主键
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "WORK_FLAG")
  private Integer workFlag = 0;
  @Column(name = "NAME_FLAG")
  private Integer nameFlag = 0;
  @Column(name = "EMAIL_FLAG")
  private Integer emailFlag = 0;
  @Column(name = "TAUGHT_FLAG")
  private Integer taughtFlag = 0;
  @Column(name = "EDU_FLAG")
  private Integer eduFlag = 0;
  @Column(name = "EXPERIENCE_FLAG")
  private Integer experienceFlag = 0;
  @Column(name = "ADDITINFO_FLAG")
  private Integer additinfoFlag = 0;
  @Column(name = "CONTACT_FLAG")
  private Integer contactFlag = 0;
  @Column(name = "PRIVACY_FLAG")
  private Integer privacyFlag = 0;
  @Column(name = "ATT_FLAG")
  private Integer attFlag = 0;
  @Column(name = "PSNSET_FLAG")
  private Integer psnSetFlag = 0;
  @Column(name = "PUB_FLAG")
  private Integer pubFlag = 0;
  @Column(name = "KWZT_FLAG")
  private Integer kwztFlag = 0;
  @Column(name = "INS_FLAG")
  private Integer insFlag = 0;
  @Column(name = "FRIEND_FLAG")
  private Integer friendFlag = 0;
  @Column(name = "GROUP_FLAG")
  private Integer groupFlag = 0;
  @Column(name = "REFC_FLAG")
  private Integer refcFlag = 0;
  @Column(name = "STATUS")
  private Integer status = 0;

  public RcmdSyncPsnInfo() {
    super();
  }

  public RcmdSyncPsnInfo(Long psnId) {
    super();
    this.psnId = psnId;
    this.status = 0;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getWorkFlag() {
    return workFlag;
  }

  public void setWorkFlag(Integer workFlag) {
    this.workFlag = workFlag;
  }

  public Integer getNameFlag() {
    return nameFlag;
  }

  public void setNameFlag(Integer nameFlag) {
    this.nameFlag = nameFlag;
  }

  public Integer getEmailFlag() {
    return emailFlag;
  }

  public void setEmailFlag(Integer emailFlag) {
    this.emailFlag = emailFlag;
  }

  public Integer getTaughtFlag() {
    return taughtFlag;
  }

  public void setTaughtFlag(Integer taughtFlag) {
    this.taughtFlag = taughtFlag;
  }

  public Integer getEduFlag() {
    return eduFlag;
  }

  public void setEduFlag(Integer eduFlag) {
    this.eduFlag = eduFlag;
  }

  public Integer getExperienceFlag() {
    return experienceFlag;
  }

  public void setExperienceFlag(Integer experienceFlag) {
    this.experienceFlag = experienceFlag;
  }

  public Integer getAdditinfoFlag() {
    return additinfoFlag;
  }

  public void setAdditinfoFlag(Integer additinfoFlag) {
    this.additinfoFlag = additinfoFlag;
  }

  public Integer getContactFlag() {
    return contactFlag;
  }

  public void setContactFlag(Integer contactFlag) {
    this.contactFlag = contactFlag;
  }

  public Integer getPrivacyFlag() {
    return privacyFlag;
  }

  public void setPrivacyFlag(Integer privacyFlag) {
    this.privacyFlag = privacyFlag;
  }

  public Integer getAttFlag() {
    return attFlag;
  }

  public void setAttFlag(Integer attFlag) {
    this.attFlag = attFlag;
  }

  public Integer getPsnSetFlag() {
    return psnSetFlag;
  }

  public void setPsnSetFlag(Integer psnSetFlag) {
    this.psnSetFlag = psnSetFlag;
  }

  public Integer getPubFlag() {
    return pubFlag;
  }

  public void setPubFlag(Integer pubFlag) {
    this.pubFlag = pubFlag;
  }

  public Integer getKwztFlag() {
    return kwztFlag;
  }

  public void setKwztFlag(Integer kwztFlag) {
    this.kwztFlag = kwztFlag;
  }

  public Integer getInsFlag() {
    return insFlag;
  }

  public void setInsFlag(Integer insFlag) {
    this.insFlag = insFlag;
  }

  public Integer getFriendFlag() {
    return friendFlag;
  }

  public void setFriendFlag(Integer friendFlag) {
    this.friendFlag = friendFlag;
  }

  public Integer getGroupFlag() {
    return groupFlag;
  }

  public void setGroupFlag(Integer groupFlag) {
    this.groupFlag = groupFlag;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getRefcFlag() {
    return refcFlag;
  }

  public void setRefcFlag(Integer refcFlag) {
    this.refcFlag = refcFlag;
  }

}
