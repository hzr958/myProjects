package com.smate.web.group.form;

import java.util.ArrayList;
import java.util.List;

import com.smate.core.base.utils.model.Page;
import com.smate.web.group.model.group.psn.PsnInfo;


/**
 * 群组显示推荐和检索人员
 * 
 * @author YJ
 */
public class GroupShareForm {

  private Long psnId;// 用户ID
  private String des3PsnId;// 加密的用户ID
  private Long groupId;// 群组ID
  private String des3GroupId;// 加密的群组ID
  private Integer searchType; // 检索类型 0代表群组人员检索 1代表好友检索
  private String searchKey; // 检索关键字
  private String orderBy = "date"; // 排序； date排序，name排序
  private List<PsnInfo> psnInfoList;// 页面显示的封装的好友信息list
  private String type; // sxj 要换页面 就在这里加
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

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public Integer getSearchType() {
    return searchType;
  }

  public void setSearchType(Integer searchType) {
    this.searchType = searchType;
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

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public Boolean getFromAttention() {
    return fromAttention;
  }

  public void setFromAttention(Boolean fromAttention) {
    this.fromAttention = fromAttention;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
