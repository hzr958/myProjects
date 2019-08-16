package com.smate.web.prj.model.common;

import java.io.Serializable;

import com.smate.core.base.utils.common.HtmlUtils;

public class GroupPsnNodeForm implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1633121141931318930L;

  // 群组ID
  private Long groupId;

  // 群组名称
  private String groupName;
  // 加密群组ID
  private String des3GroupId;

  // 节点
  private Integer nodeId;

  // 学科领域
  private String disciplines;

  // 群组分类
  private String groupCategory;

  // 公开类型[O=开放,H=半开放,P=保密]
  private String openType = "H";
  // 群组成果权限类型[1=所有成员,0=管理员]
  private String pubViewType;
  // 群组中的角色[1=创建人,2=管理员, 3=组员][冗余]
  private String groupRole = "3";
  // 成果、文献、文件是否可以加入群组.1=可以，0=不可以
  private Integer isCanAdd = 1;

  // 是否支持群组成果[1=是,0=否]
  private String isPubView = "1";

  // 是否支持群组参考文献[1=是,0=否]
  private String isRefView = "1";

  private String escapeGroupName;

  private Integer count;

  public Integer getIsCanAdd() {
    return isCanAdd;
  }

  public void setIsCanAdd(Integer isCanAdd) {
    this.isCanAdd = isCanAdd;
  }

  public Long getGroupId() {
    return groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public String getDisciplines() {
    return disciplines;
  }

  public String getGroupCategory() {
    return groupCategory;
  }

  public String getOpenType() {
    return openType;
  }

  public String getPubViewType() {
    return pubViewType;
  }

  public String getGroupRole() {
    return groupRole;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public void setPubViewType(String pubViewType) {
    this.pubViewType = pubViewType;
  }

  public void setGroupRole(String groupRole) {
    this.groupRole = groupRole;
  }

  public String getIsPubView() {
    return isPubView;
  }

  public String getIsRefView() {
    return isRefView;
  }

  public void setIsPubView(String isPubView) {
    this.isPubView = isPubView;
  }

  public void setIsRefView(String isRefView) {
    this.isRefView = isRefView;
  }

  public String getEscapeGroupName() {
    if (this.groupName != null)
      escapeGroupName = HtmlUtils.toHtml(this.groupName);
    return escapeGroupName;
  }

  public void setEscapeGroupName(String escapeGroupName) {
    this.escapeGroupName = escapeGroupName;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

}
