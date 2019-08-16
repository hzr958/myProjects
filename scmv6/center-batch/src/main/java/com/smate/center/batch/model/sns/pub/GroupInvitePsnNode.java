package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 群组与人员的邀请关系表(当前人所有节点上加入的群组).
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_INVITE_PSN_NODE")
public class GroupInvitePsnNode implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4578207751851316332L;
  private GroupInvitePsnNodePk id;

  // 节点
  private Integer nodeId;

  // 群组名称[冗余]
  private String groupName;
  // 群组名称首字母
  private String firstLetter;
  // 群组介绍[冗余]
  private String groupDescription;
  // 群组分类[冗余]
  private String groupCategory;
  // 群组分类名称
  private String categoryName;
  // 群组成员数[冗余]
  private Integer sumMembers = 0;
  // 待确认群组成员数[冗余]
  private Integer sumToMembers = 0;
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
  // 群组作业数
  private Integer sumWorks = 0;
  // 群组教学课件数
  private Integer sumMaterials = 0;

  // 公开类型[O=开放,H=半开放,P=保密][冗余]
  private String openType = "H";
  // 是否同意加入群组[1=是,0=否][冗余]
  private String isAccept = "1";

  // 群组中的角色[1=创建人,2=管理员, 3=组员][冗余]
  private String groupRole = "3";

  // 群组介绍截取
  private String groupDescriptionSub;

  // 项目组成员是否可以查看其他群组成员的科研项目和成果[1=是,0=否]
  private String isGroupMemberView = "1";
  // 是否支持讨论板[1=是,0=否]
  private String isDiscuss = "1";
  // 是否支持文件共享[1=是,0=否]
  private String isShareFile = "1";

  // 是否支持群组项目[1=是,0=否]
  private String isPrjView = "0";

  // 是否支持群组成果[1=是,0=否]
  private String isPubView = "0";

  // 是否支持群组参考文献[1=是,0=否]
  private String isRefView = "1";

  // 是否支持作业[1=是,0=否]
  private String isWorkView = "0";
  // 是否支持教学课件[1=是,0=否]
  private String isMaterialView = "0";
  // 群组创建日期
  private Date createDate;
  // 群组最后一次访问日期
  private Date lastVisitDate;
  // 群组访问统计
  private Long visitCount;

  // 群组和个人的关系（空：需要管理员确认 0：拒绝加入群组 1：确认加入 2：需要普通成员确认 3:完全没有关系）
  private String relatetion = "1";

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

  @Column(name = "GROUP_NAME_FIRST_LETTER")
  public String getFirstLetter() {
    return firstLetter;
  }

  @Column(name = "GROUP_DESCRIPTION")
  public String getGroupDescription() {
    return groupDescription;
  }

  @Column(name = "GROUP_CATEGORY")
  public String getGroupCategory() {
    return groupCategory;
  }

  @Transient
  public String getCategoryName() {
    return categoryName;
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

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "LAST_VISIT_DATE")
  public Date getLastVisitDate() {
    return lastVisitDate;
  }

  @Column(name = "VISIT_COUNT")
  public Long getVisitCount() {
    return visitCount;
  }

  public void setLastVisitDate(Date lastVisitDate) {
    this.lastVisitDate = lastVisitDate;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setFirstLetter(String firstLetter) {
    this.firstLetter = firstLetter;
  }

  public void setGroupDescription(String groupDescription) {
    this.groupDescription = groupDescription;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
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

  @Column(name = "OPEN_TYPE")
  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  @Column(name = "IS_ACCEPT")
  public String getIsAccept() {
    return isAccept;
  }

  public void setIsAccept(String isAccept) {
    this.isAccept = isAccept;
  }

  @Column(name = "GROUP_ROLE")
  public String getGroupRole() {
    return groupRole;
  }

  public void setGroupRole(String groupRole) {
    this.groupRole = groupRole;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "IS_GROUP_MEMBER_VIEW")
  public String getIsGroupMemberView() {
    return isGroupMemberView;
  }

  public void setIsGroupMemberView(String isGroupMemberView) {
    this.isGroupMemberView = isGroupMemberView;
  }

  @Column(name = "IS_DISCUSS")
  public String getIsDiscuss() {
    return isDiscuss;
  }

  public void setIsDiscuss(String isDiscuss) {
    this.isDiscuss = isDiscuss;
  }

  @Column(name = "IS_PRJ_VIEW")
  public String getIsPrjView() {
    return isPrjView;
  }

  public void setIsPrjView(String isPrjView) {
    this.isPrjView = isPrjView;
  }

  @Column(name = "IS_PUB_VIEW")
  public String getIsPubView() {
    return isPubView;
  }

  public void setIsPubView(String isPubView) {
    this.isPubView = isPubView;
  }

  @Column(name = "IS_REF_VIEW")
  public String getIsRefView() {
    return isRefView;
  }

  public void setIsRefView(String isRefView) {
    this.isRefView = isRefView;
  }

  @Column(name = "IS_SHARE_FILE")
  public String getIsShareFile() {
    return isShareFile;
  }

  public void setIsShareFile(String isShareFile) {
    this.isShareFile = isShareFile;
  }

  @Column(name = "SUM_TO_MEMBERS")
  public Integer getSumToMembers() {
    return sumToMembers;
  }

  public void setSumToMembers(Integer sumToMembers) {
    this.sumToMembers = sumToMembers;
  }

  @Column(name = "SUM_WORKS")
  public Integer getSumWorks() {
    return sumWorks;
  }

  @Column(name = "SUM_MATERIALS")
  public Integer getSumMaterials() {
    return sumMaterials;
  }

  @Column(name = "IS_WORK_VIEW")
  public String getIsWorkView() {
    return isWorkView;
  }

  @Column(name = "IS_MATERIAL_VIEW")
  public String getIsMaterialView() {
    return isMaterialView;
  }

  public void setSumWorks(Integer sumWorks) {
    this.sumWorks = sumWorks;
  }

  public void setSumMaterials(Integer sumMaterials) {
    this.sumMaterials = sumMaterials;
  }

  public void setIsWorkView(String isWorkView) {
    this.isWorkView = isWorkView;
  }

  public void setIsMaterialView(String isMaterialView) {
    this.isMaterialView = isMaterialView;
  }

  @Override
  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof GroupInvitePsnNode))
      return false;
    GroupInvitePsnNode otherGroupInvite = (GroupInvitePsnNode) other;

    return (this.id.getGroupId().longValue() == otherGroupInvite.getId().getGroupId().longValue());

  }

  @Transient
  public String getRelatetion() {
    return relatetion;
  }

  public void setRelatetion(String relatetion) {
    this.relatetion = relatetion;
  }

}
