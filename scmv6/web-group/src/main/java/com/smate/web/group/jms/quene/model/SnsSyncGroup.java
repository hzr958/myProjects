package com.smate.web.group.jms.quene.model;

import java.util.Date;
import java.util.List;

import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.invit.GroupInvitePsn;
import com.smate.web.group.model.group.psn.GroupInvitePsnNode;
import com.smate.web.group.model.group.psn.GroupPsnNodeSum;

public class SnsSyncGroup {
  /**
  	 * 
  	 */
  private GroupPsn groupPsn;
  private GroupInvitePsn groupInvitePsn;
  private SnsSyncGroupActionEnum action;
  private Integer receiveNodeId;
  private List<GroupInvitePsnNode> gipnList;
  private Long sendPsnId;
  private Long receivePsnId;
  private List<GroupPsnNodeSum> groupSumList;
  private List<Long> psnList;
  private Long groupOwner;
  private Long groupId;
  private Date lastVisitDate;

  public SnsSyncGroup() {
    super();
  }

  public SnsSyncGroup(GroupPsn groupPsn, Integer fromNodeId, SnsSyncGroupActionEnum action) {
    this.action = action;
    this.groupPsn = groupPsn;
  }

  public SnsSyncGroup(GroupPsn groupPsn, List<Long> psnList, Integer fromNodeId, SnsSyncGroupActionEnum action) {
    this.action = action;
    this.groupPsn = groupPsn;
    this.psnList = psnList;
  }

  public SnsSyncGroup(GroupPsn groupPsn, Integer fromNodeId, List<GroupPsnNodeSum> groupSumList,
      SnsSyncGroupActionEnum action) {
    this.action = action;
    this.groupPsn = groupPsn;
    this.groupSumList = groupSumList;
  }

  public SnsSyncGroup(GroupPsn groupPsn, GroupInvitePsn groupInvitePsn, Integer fromNodeId,
      SnsSyncGroupActionEnum action) {
    this.action = action;
    this.groupPsn = groupPsn;
    this.groupInvitePsn = groupInvitePsn;
  }

  public SnsSyncGroup(Long sendPsnId, Long receivePsnId, Integer sendNodeId, Integer receiveNodeId,
      SnsSyncGroupActionEnum action) {
    this.sendPsnId = sendPsnId;
    this.receivePsnId = receivePsnId;
    this.receiveNodeId = receiveNodeId;
    this.action = action;
  }

  public SnsSyncGroup(List<GroupInvitePsnNode> gipnList, Long sendPsnId, Long receivePsnId, Integer fromNodeId,
      SnsSyncGroupActionEnum action) {
    this.gipnList = gipnList;
    this.sendPsnId = sendPsnId;
    this.receivePsnId = receivePsnId;
    this.action = action;
  }

  public SnsSyncGroup(Long groupOwner, Long groupId, Integer fromNodeId, SnsSyncGroupActionEnum action) {
    this.groupOwner = groupOwner;
    this.groupId = groupId;
  }

  public SnsSyncGroup(Long sendPsnId, Long groupId, Date lastVisitDate, Integer fromNodeId,
      SnsSyncGroupActionEnum action) {
    this.sendPsnId = sendPsnId;
    this.groupId = groupId;
    this.lastVisitDate = lastVisitDate;
    this.action = action;
  }

  public GroupPsn getGroupPsn() {
    return groupPsn;
  }

  public SnsSyncGroupActionEnum getAction() {
    return action;
  }

  public void setGroupPsn(GroupPsn groupPsn) {
    this.groupPsn = groupPsn;
  }

  public GroupInvitePsn getGroupInvitePsn() {
    return groupInvitePsn;
  }

  public void setGroupInvitePsn(GroupInvitePsn groupInvitePsn) {
    this.groupInvitePsn = groupInvitePsn;
  }

  public void setAction(SnsSyncGroupActionEnum action) {
    this.action = action;
  }

  public Integer getReceiveNodeId() {
    return receiveNodeId;
  }

  public void setReceiveNodeId(Integer receiveNodeId) {
    this.receiveNodeId = receiveNodeId;
  }

  public List<GroupInvitePsnNode> getGipnList() {
    return gipnList;
  }

  public void setGipnList(List<GroupInvitePsnNode> gipnList) {
    this.gipnList = gipnList;
  }

  public Long getSendPsnId() {
    return sendPsnId;
  }

  public Long getReceivePsnId() {
    return receivePsnId;
  }

  public void setSendPsnId(Long sendPsnId) {
    this.sendPsnId = sendPsnId;
  }

  public void setReceivePsnId(Long receivePsnId) {
    this.receivePsnId = receivePsnId;
  }

  public List<GroupPsnNodeSum> getGroupSumList() {
    return groupSumList;
  }

  public void setGroupSumList(List<GroupPsnNodeSum> groupSumList) {
    this.groupSumList = groupSumList;
  }

  public List<Long> getPsnList() {
    return psnList;
  }

  public void setPsnList(List<Long> psnList) {
    this.psnList = psnList;
  }

  public Long getGroupOwner() {
    return groupOwner;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupOwner(Long groupOwner) {
    this.groupOwner = groupOwner;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Date getLastVisitDate() {
    return lastVisitDate;
  }

  public void setLastVisitDate(Date lastVisitDate) {
    this.lastVisitDate = lastVisitDate;
  }

}
