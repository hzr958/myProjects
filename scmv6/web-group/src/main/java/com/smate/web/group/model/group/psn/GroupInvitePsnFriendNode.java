package com.smate.web.group.model.group.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.group.service.group.GroupInvitePsnNodePk;

/**
 * 群组与人员的邀请关系表(当前人所有节点上好友加入的群组).
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_INVITE_PSNFRIEND_NODE")
public class GroupInvitePsnFriendNode implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8106567132214365464L;

  private GroupInvitePsnNodePk id;

  // 节点
  private Integer nodeId;

  // 群组名称[冗余]
  private String groupName;
  // 群组介绍[冗余]
  private String groupDescription;
  // 群组分类[冗余]
  private String groupCategory;
  // 群组成员数[冗余]
  private Integer sumMembers = 0;
  // 群组话题数[冗余]
  private Integer sumSubjects = 0;
  // 群组成果数[冗余]
  private Integer sumPubs = 0;
  // 群组项目数[冗余]
  private Integer sumPrjs = 0;
  // 群组文献数[冗余]
  private Integer sumRefs = 0;
  // 群组文件数[冗余]
  private Integer sumFiles = 0;
  // 群组拥有者的psn_id[冗余]
  private Long ownerPsnId;
  // 群组图片[冗余]
  private String groupImgUrl;
  // 好友人员ID（加入群组的人员ID），多个用逗号隔开
  private String psnIds;
  // 好友人员姓名（加入群组的人员ID），多个用逗号隔开
  private String psnNames;
  // 群组介绍截取
  private String groupDescriptionSub;
  // 公开类型[O=开放,H=半开放,P=保密][冗余]
  private String openType = "H";

  // 群组加密NodeId
  private String des3GroupNodeId;

  // 群组创建时间
  private Date createDate;

  @EmbeddedId
  public GroupInvitePsnNodePk getId() {
    return id;
  }

  public void setId(GroupInvitePsnNodePk id) {
    this.id = id;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Column(name = "GROUP_NAME")
  public String getGroupName() {
    return groupName;
  }

  @Column(name = "GROUP_DESCRIPTION")
  public String getGroupDescription() {
    return groupDescription;
  }

  @Column(name = "GROUP_CATEGORY")
  public String getGroupCategory() {
    return groupCategory;
  }

  @Column(name = "SUM_MEMBERS")
  public Integer getSumMembers() {
    return sumMembers;
  }

  @Column(name = "SUM_SUBJECTS")
  public Integer getSumSubjects() {
    return sumSubjects;
  }

  @Column(name = "SUM_PUBS")
  public Integer getSumPubs() {
    return sumPubs;
  }

  @Column(name = "SUM_PRJS")
  public Integer getSumPrjs() {
    return sumPrjs;
  }

  @Column(name = "SUM_REFS")
  public Integer getSumRefs() {
    return sumRefs;
  }

  @Column(name = "SUM_FILES")
  public Integer getSumFiles() {
    return sumFiles;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setGroupDescription(String groupDescription) {
    this.groupDescription = groupDescription;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public void setSumMembers(Integer sumMembers) {
    this.sumMembers = sumMembers;
  }

  public void setSumSubjects(Integer sumSubjects) {
    this.sumSubjects = sumSubjects;
  }

  public void setSumPubs(Integer sumPubs) {
    this.sumPubs = sumPubs;
  }

  public void setSumPrjs(Integer sumPrjs) {
    this.sumPrjs = sumPrjs;
  }

  public void setSumRefs(Integer sumRefs) {
    this.sumRefs = sumRefs;
  }

  public void setSumFiles(Integer sumFiles) {
    this.sumFiles = sumFiles;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  @Column(name = "GROUP_IMG_URL")
  public String getGroupImgUrl() {
    return groupImgUrl;
  }

  public void setGroupImgUrl(String groupImgUrl) {
    this.groupImgUrl = groupImgUrl;
  }

  @Column(name = "PSN_IDS")
  public String getPsnIds() {
    return psnIds;
  }

  @Transient
  public String getPsnNames() {
    return psnNames;
  }

  public void setPsnIds(String psnIds) {
    this.psnIds = psnIds;
  }

  public void setPsnNames(String psnNames) {
    this.psnNames = psnNames;
  }

  @Transient
  public String getGroupDescriptionSub() {

    if (StringUtils.isNotEmpty(groupDescription)) {
      int strLong = 200;
      boolean isLong = groupDescription.length() > strLong;
      groupDescriptionSub = isLong ? (groupDescription.substring(0, strLong) + "...") : groupDescription;
      if (!isLong) {
        groupDescription = null;
      }
    }

    return groupDescriptionSub;
  }

  public void setGroupDescriptionSub(String groupDescriptionSub) {
    this.groupDescriptionSub = groupDescriptionSub;
  }

  @Column(name = "OPEN_TYPE")
  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  @Transient
  public String getDes3GroupNodeId() {
    if (this.nodeId != null && des3GroupNodeId == null) {
      des3GroupNodeId = ServiceUtil.encodeToDes3(this.nodeId.toString());
    }
    return des3GroupNodeId;
  }

  public void setDes3GroupNodeId(String des3GroupNodeId) {
    if (this.nodeId == null && StringUtils.isNotBlank(des3GroupNodeId)) {
      this.nodeId = Integer.valueOf(ServiceUtil.decodeFromDes3(des3GroupNodeId));
    }
    this.des3GroupNodeId = des3GroupNodeId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
