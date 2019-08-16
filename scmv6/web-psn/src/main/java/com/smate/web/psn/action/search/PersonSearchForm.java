package com.smate.web.psn.action.search;

import java.io.Serializable;
import java.util.List;

import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员检索Form
 * 
 * @author zk
 *
 */
public class PersonSearchForm implements Serializable {

  private static final long serialVersionUID = 1830848312383039496L;
  private String searchName; // 检索人名信息
  private String searchKey; // 检索关键词信息
  private List<PsnInfo> psnInfoList;
  private Integer pageSize = 20;// 每页显示十条
  private Integer pageNo = 1; // 第几页,从0开始

  public String getSearchName() {
    return searchName;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

}
