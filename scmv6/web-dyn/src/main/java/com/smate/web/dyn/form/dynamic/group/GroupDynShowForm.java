package com.smate.web.dyn.form.dynamic.group;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicAwards;

/**
 * 群组动态显示Form
 * 
 * @author zzx
 *
 */
public class GroupDynShowForm {
  private Long currentPsnId;// 当前人PsnId
  private String des3CurrentPsnId;// 当前人PsnId
  private Integer currentPsnRole;// 当前人角色
  private String currentAvatars;// 当前用户的头像地址
  private String locale;// 语言类型
  private String flag;// 请求标识 more 为返回list 否则 返回main
  private Long dynId; // 动态id
  private String des3DynId;// 加密动态id
  private String dynIds;// 动态id集
  private Long groupId;// 群组id
  private String des3GroupId;// 加密群组id
  private Integer firstResult = 0;// 查询的开始位置
  private Integer maxResults = 10;// 查询的最大数量
  private List<GroupDynShowInfo> groupDynShowInfoList; // 群组动态显示类列表
  private List<GroupDynCommentsShowInfo> groupDynCommentsShowInfoList; // 群组动态评论显示类列表
  private List<GroupDynamicAwards> groupDynamicAwardsList; // 赞信息
  private GroupDynShowInfo grpDynShowInfo; // 单个动态信息
  private GroupDynCommentsShowInfo grpDynCommentsShowInfo; // 单个动态的评论信息
  private GroupDynamicAwards grpDynAwards; // 单个动态的赞信息
  private Integer databaseType = 2;
  private Page page = new Page();
  private Integer showNew = 0; // 只显示 新动态 1 是 0否
  private Map<String, Object> jsonDynInfo; // json格式的群组动态信息
  private boolean showJsonDynInfo; // 是否需要json格式群组动态信息

  public Integer getFirstResult() {
    return firstResult;
  }

  public void setFirstResult(Integer firstResult) {
    this.firstResult = firstResult;
  }

  public Long getDynId() {
    if (dynId == null && StringUtils.isNotBlank(des3DynId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3DynId);
      if (des3Str == null) {
        return dynId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Integer getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {
    this.maxResults = maxResults;
  }

  public List<GroupDynShowInfo> getGroupDynShowInfoList() {
    return groupDynShowInfoList;
  }

  public void setGroupDynShowInfoList(List<GroupDynShowInfo> groupDynShowInfoList) {
    this.groupDynShowInfoList = groupDynShowInfoList;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
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

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public List<GroupDynCommentsShowInfo> getGroupDynCommentsShowInfoList() {
    return groupDynCommentsShowInfoList;
  }

  public void setGroupDynCommentsShowInfoList(List<GroupDynCommentsShowInfo> groupDynCommentsShowInfoList) {
    this.groupDynCommentsShowInfoList = groupDynCommentsShowInfoList;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public String getCurrentAvatars() {
    return currentAvatars;
  }

  public void setCurrentAvatars(String currentAvatars) {
    this.currentAvatars = currentAvatars;
  }

  public List<GroupDynamicAwards> getGroupDynamicAwardsList() {
    return groupDynamicAwardsList;
  }

  public void setGroupDynamicAwardsList(List<GroupDynamicAwards> groupDynamicAwardsList) {
    this.groupDynamicAwardsList = groupDynamicAwardsList;
  }

  public String getDynIds() {
    return dynIds;
  }

  public void setDynIds(String dynIds) {
    this.dynIds = dynIds;
  }

  public String getDes3DynId() {
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }

  public Long getCurrentPsnId() {
    if (currentPsnId != null && currentPsnId != 0) {
      return currentPsnId;
    } else {
      this.currentPsnId = SecurityUtils.getCurrentUserId();
      return currentPsnId;
    }

  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public Integer getCurrentPsnRole() {
    return currentPsnRole;
  }

  public void setCurrentPsnRole(Integer currentPsnRole) {
    this.currentPsnRole = currentPsnRole;
  }

  public Integer getShowNew() {
    return showNew;
  }

  public void setShowNew(Integer showNew) {
    this.showNew = showNew;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getDes3CurrentPsnId() {
    if (StringUtils.isBlank(des3CurrentPsnId)) {
      des3CurrentPsnId = Des3Utils.encodeToDes3(getCurrentPsnId().toString());
    }
    return des3CurrentPsnId;
  }

  public void setDes3CurrentPsnId(String des3CurrentPsnId) {
    this.des3CurrentPsnId = des3CurrentPsnId;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public GroupDynShowInfo getGrpDynShowInfo() {
    return grpDynShowInfo;
  }

  public void setGrpDynShowInfo(GroupDynShowInfo grpDynShowInfo) {
    this.grpDynShowInfo = grpDynShowInfo;
  }

  public GroupDynCommentsShowInfo getGrpDynCommentsShowInfo() {
    return grpDynCommentsShowInfo;
  }

  public void setGrpDynCommentsShowInfo(GroupDynCommentsShowInfo grpDynCommentsShowInfo) {
    this.grpDynCommentsShowInfo = grpDynCommentsShowInfo;
  }

  public GroupDynamicAwards getGrpDynAwards() {
    return grpDynAwards;
  }

  public void setGrpDynAwards(GroupDynamicAwards grpDynAwards) {
    this.grpDynAwards = grpDynAwards;
  }

  public Map<String, Object> getJsonDynInfo() {
    return jsonDynInfo;
  }

  public void setJsonDynInfo(Map<String, Object> jsonDynInfo) {
    this.jsonDynInfo = jsonDynInfo;
  }

  public boolean getShowJsonDynInfo() {
    return showJsonDynInfo;
  }

  public void setShowJsonDynInfo(boolean showJsonDynInfo) {
    this.showJsonDynInfo = showJsonDynInfo;
  }



}
