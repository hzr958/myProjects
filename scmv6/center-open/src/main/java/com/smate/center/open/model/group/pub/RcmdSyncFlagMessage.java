package com.smate.center.open.model.group.pub;

import java.util.HashMap;
import java.util.Map;

/**
 * 人员冗余信息刷新消息.
 * 
 * @author zzx
 * 
 */
public class RcmdSyncFlagMessage {

  /**
   * 
   */

  private Long psnId;
  private Integer workFlag = 0;
  private Integer nameFlag = 0;
  private Integer emailFlag = 0;
  private Integer taughtFlag = 0;
  private Integer eduFlag = 0;
  private Integer experienceFlag = 0;
  private Integer additinfoFlag = 0;
  private Integer contactFlag = 0;
  private Integer privacyFlag = 0;
  private Integer attFlag = 0;
  private Integer psnSetFlag = 0;
  private Integer pubFlag = 0;
  private Integer kwztFlag = 0;
  private Integer insFlag = 0;
  private Integer friendFlag = 0;
  private Integer groupFlag = 0;
  private Integer refcFlag = 0;
  private Map<Long, Integer> pubs;
  private Map<Long, Integer> refcs;
  private Map<Long, Integer> groups;

  public RcmdSyncFlagMessage() {
    super();
  }

  public RcmdSyncFlagMessage(Long psnId) {
    super();
    this.psnId = psnId;
  }

  /**
   * 构造实例.
   * 
   * @param psnId
   * @return
   */
  public static RcmdSyncFlagMessage getInstance(Long psnId) {

    RcmdSyncFlagMessage msg = new RcmdSyncFlagMessage(psnId);
    return msg;
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

  public Integer getRefcFlag() {
    return refcFlag;
  }

  public void setRefcFlag(Integer refcFlag) {
    this.refcFlag = refcFlag;
  }

  public Map<Long, Integer> getPubs() {
    return pubs;
  }

  public void setPubs(Map<Long, Integer> pubs) {
    this.pubs = pubs;
  }

  public Map<Long, Integer> getRefcs() {
    return refcs;
  }

  public void setRefcs(Map<Long, Integer> refcs) {
    this.refcs = refcs;
  }

  public Map<Long, Integer> getGroups() {
    return groups;
  }

  public void setGroups(Map<Long, Integer> groups) {
    this.groups = groups;
  }

  public void setPub(Long pubId, int isDel) {
    if (this.pubs == null) {
      this.pubs = new HashMap<Long, Integer>();
    }
    this.pubs.put(pubId, isDel);
    this.pubFlag = 1;
  }

  public void setRefc(Long refcId, int isDel) {
    if (this.refcs == null) {
      this.refcs = new HashMap<Long, Integer>();
    }
    this.refcs.put(refcId, isDel);
    this.refcFlag = 1;
  }

  public void setGoup(Long groupId, int isDel) {
    if (this.groups == null) {
      this.groups = new HashMap<Long, Integer>();
    }
    this.groups.put(groupId, isDel);
    this.groupFlag = 1;
  }

}
