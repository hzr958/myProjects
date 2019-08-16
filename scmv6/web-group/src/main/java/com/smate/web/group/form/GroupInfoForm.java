package com.smate.web.group.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.GroupStatistics;
import com.smate.web.group.model.group.invit.GroupInvitePsn;
import com.smate.web.group.model.group.psn.PsnInfo;
import com.smate.web.group.model.group.pub.GroupPubs;

/**
 * 群组信息Form
 * 
 * @author zk
 *
 */
public class GroupInfoForm {

  private String des3GroupId; // 加密群组id
  private Long groupId; // 群组id
  private String GroupCode; // 群组code:openId+"_"+groupId的MD5值
  private String des3PsnId; // 加密人员id
  private Long psnId; // 人员id
  @SuppressWarnings("rawtypes")
  private Page page = new Page();
  private String searchKey; // 检索框
  private GroupPsn groupPsn;
  private Person person; // 当前人的信息
  private GroupStatistics groupStatistics;
  private GroupInvitePsn groupInvitePsn;
  private List<GroupInvitePsn> groupInvitePsns;
  private List<GroupPubs> groupPubs;

  private List<PsnInfo> psnInfoList;// 群组成员列表
  // 排序处理sortType：1. group_rol asc,invite_psn_id desc 2.加入时间 默认为1
  private Integer sortType;
  private Integer isAdmin = 0;// 是否为群组管理员,0：否，1：是
  private Integer currentPsnGroupRole = 3;// 当前人的群组角色 1:创建人 2:管理员 3:组员 默认是组员；
  private Integer currentPsnGroupRoleStatus = 0; // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
  private String oldToGroupPending;// 群组成员模块-从老系统页面点击"n名待审核"跳转到新页面的"申请中"列表 标识
  private Integer backType;// 返回 标识，用于群组详情跳转相应功能（3：群组成果） (31:简介 ， 32人员 ， 33 ，成果 ，34文件)
  private String groupOpenType;// 如果是“O”则是非群组成员访问群组，隐藏某些操作
  /**
   * ajb 点击返回 群组成果编辑，返回的 专属参数，
   */
  private String screenRecords = "";//
  private String screenPubTypes = "";//
  private String screenYears = "";//
  private String groupPubEdit = "";// 标识群组是是否群组成果编辑
  // private String orderBy = "";//
  // private String searchKey = "";//
  // private String pageNo = "";//
  // private String pageSize = "";//
  private String groupCategory;// 群组研究领域
  private String toModule;// 到达某个模块 简介brief ， 成员member ， pub , file等
  private String submodule; // 子模块 member 成员 ， apply 申请

  private Long currentPsnId;// 当前人的psnId

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3PsnId);
      if (des3Str == null) {
        return psnId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public Long getGroupId() {
    if (groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3GroupId);
      if (des3Str == null) {
        return groupId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public GroupPsn getGroupPsn() {
    return groupPsn;
  }

  public void setGroupPsn(GroupPsn groupPsn) {
    this.groupPsn = groupPsn;
  }

  public GroupStatistics getGroupStatistics() {
    return groupStatistics;
  }

  public void setGroupStatistics(GroupStatistics groupStatistics) {
    this.groupStatistics = groupStatistics;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public Integer getSortType() {
    return sortType;
  }

  public void setSortType(Integer sortType) {
    this.sortType = sortType;
  }

  public List<GroupInvitePsn> getGroupInvitePsns() {
    return groupInvitePsns;
  }

  public void setGroupInvitePsns(List<GroupInvitePsn> groupInvitePsns) {
    this.groupInvitePsns = groupInvitePsns;
  }

  public List<GroupPubs> getGroupPubs() {
    return groupPubs;
  }

  public void setGroupPubs(List<GroupPubs> groupPubs) {
    this.groupPubs = groupPubs;
  }

  public Integer getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(Integer isAdmin) {
    this.isAdmin = isAdmin;
  }

  public Integer getCurrentPsnGroupRole() {
    return currentPsnGroupRole;
  }

  public void setCurrentPsnGroupRole(Integer currentPsnGroupRole) {
    this.currentPsnGroupRole = currentPsnGroupRole;
  }

  public String getOldToGroupPending() {
    return oldToGroupPending;
  }

  public void setOldToGroupPending(String oldToGroupPending) {
    this.oldToGroupPending = oldToGroupPending;
  }

  public Integer getBackType() {
    return backType;
  }

  public void setBackType(Integer backType) {
    this.backType = backType;
  }

  public Integer getCurrentPsnGroupRoleStatus() {
    return currentPsnGroupRoleStatus;
  }

  public void setCurrentPsnGroupRoleStatus(Integer currentPsnGroupRoleStatus) {
    this.currentPsnGroupRoleStatus = currentPsnGroupRoleStatus;
  }

  public String getGroupCode() {
    return GroupCode;
  }

  public void setGroupCode(String groupCode) {
    GroupCode = groupCode;
  }

  public GroupInvitePsn getGroupInvitePsn() {
    return groupInvitePsn;
  }

  public void setGroupInvitePsn(GroupInvitePsn groupInvitePsn) {
    this.groupInvitePsn = groupInvitePsn;
  }

  public String getGroupOpenType() {
    return groupOpenType;
  }

  public void setGroupOpenType(String groupOpenType) {
    this.groupOpenType = groupOpenType;
  }

  public String getGroupCategory() {
    return groupCategory;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public String getToModule() {
    return toModule;
  }

  public void setToModule(String toModule) {
    this.toModule = toModule;
  }

  public String getSubmodule() {
    return submodule;
  }

  public void setSubmodule(String submodule) {
    this.submodule = submodule;
  }

  public String getScreenRecords() {
    return screenRecords;
  }

  public void setScreenRecords(String screenRecords) {
    this.screenRecords = screenRecords;
  }

  public String getScreenPubTypes() {
    return screenPubTypes;
  }

  public void setScreenPubTypes(String screenPubTypes) {
    this.screenPubTypes = screenPubTypes;
  }

  public String getScreenYears() {
    return screenYears;
  }

  public void setScreenYears(String screenYears) {
    this.screenYears = screenYears;
  }

  public String getGroupPubEdit() {
    return groupPubEdit;
  }

  public void setGroupPubEdit(String groupPubEdit) {
    this.groupPubEdit = groupPubEdit;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }


}
