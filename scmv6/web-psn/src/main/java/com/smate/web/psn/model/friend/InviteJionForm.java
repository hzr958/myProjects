package com.smate.web.psn.model.friend;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 群组成员-邀请成员加入form
 * 
 * @author lhd
 */
@SuppressWarnings("rawtypes")
public class InviteJionForm {

  private Long psnId;// 用户ID
  private String des3PsnId;// 加密的用户ID
  private Long groupId;// 群组ID
  private String des3GroupId;// 加密的群组ID
  private List<PsnInfo> psnInfoList;// 页面显示的封装的好友信息list
  private Page page = new Page();// 分页信息
  private String type; // sxj 要换页面 就在这里加
  private String searchKey; // 检索关键字
  private String orderBy = "date"; // 排序； date排序，name排序
  private Boolean fromAttention = false; // = true 说明请求是来至关注页面的请求
  List<Long> excludePsnIds = new ArrayList<>();

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Boolean getFromAttention() {
    return fromAttention;
  }

  public void setFromAttention(Boolean fromAttention) {
    this.fromAttention = fromAttention;
  }

  public List<Long> getExcludePsnIds() {
    return excludePsnIds;
  }

  public void setExcludePsnIds(List<Long> excludePsnIds) {
    this.excludePsnIds = excludePsnIds;
  }



}
