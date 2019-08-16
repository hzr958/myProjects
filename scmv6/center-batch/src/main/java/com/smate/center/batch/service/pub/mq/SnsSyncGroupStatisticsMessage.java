package com.smate.center.batch.service.pub.mq;

import java.util.Date;
import java.util.List;

import com.smate.center.batch.enums.pub.SnsSyncGroupStatisActionEnum;

/**
 * 群组统计信息同步message
 * 
 * @author zyx
 * 
 */
public class SnsSyncGroupStatisticsMessage {

  // 群组ID
  private Long groupId;
  // 群组所在节点ID
  private Integer nodeId;
  // 群组用户PsnId
  private List<Long> memberPsnIdList;
  // 群组名称
  private String groupName;
  // 群组类别代码
  private String groupCategory;
  // 群组创建时期
  private Date createDate;
  // 群组成员数
  private Integer sumMemebers;
  // 群组活动数
  private Integer sumActivity;
  // 成果+文献+文件的数量
  private Integer sumBiz;
  // 群组访问统计
  private Long visitCount;
  private SnsSyncGroupStatisActionEnum action;

  public SnsSyncGroupStatisticsMessage() {
    super();
  }

  public SnsSyncGroupStatisticsMessage(Long groupId, Integer nodeId, SnsSyncGroupStatisActionEnum action) {
    this.groupId = groupId;
    this.nodeId = nodeId;
    this.action = action;
  }

  public SnsSyncGroupStatisticsMessage(Long groupId, Integer nodeId, String groupName, String groupCategory,
      Date createDate, Integer sumMemebers, Integer sumActivity, Integer sumBiz, Long visitCount,
      List<Long> memberPsnIdList, SnsSyncGroupStatisActionEnum action) {
    this.groupId = groupId;
    this.nodeId = nodeId;
    this.groupName = groupName;
    this.groupCategory = groupCategory;
    this.createDate = createDate;
    this.sumMemebers = sumMemebers;
    this.sumActivity = sumActivity;
    this.sumBiz = sumBiz;
    this.visitCount = visitCount;
    this.action = action;
    this.memberPsnIdList = memberPsnIdList;
  }

  public Long getGroupId() {
    return groupId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupCategory() {
    return groupCategory;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public Integer getSumMemebers() {
    return sumMemebers;
  }

  public Integer getSumActivity() {
    return sumActivity;
  }

  public Integer getSumBiz() {
    return sumBiz;
  }

  public Long getVisitCount() {
    return visitCount;
  }

  public SnsSyncGroupStatisActionEnum getAction() {
    return action;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setSumMemebers(Integer sumMemebers) {
    this.sumMemebers = sumMemebers;
  }

  public void setSumActivity(Integer sumActivity) {
    this.sumActivity = sumActivity;
  }

  public void setSumBiz(Integer sumBiz) {
    this.sumBiz = sumBiz;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }

  public void setAction(SnsSyncGroupStatisActionEnum action) {
    this.action = action;
  }

  public List<Long> getMemberPsnIdList() {
    return memberPsnIdList;
  }

  public void setMemberPsnIdList(List<Long> memberPsnIdList) {
    this.memberPsnIdList = memberPsnIdList;
  }

}
