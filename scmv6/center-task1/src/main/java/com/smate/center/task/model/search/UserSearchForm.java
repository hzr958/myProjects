package com.smate.center.task.model.search;

import java.io.Serializable;
import java.util.List;

import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;



/**
 * 用户检索form.
 * 
 * @author liqinghua
 * 
 */
public class UserSearchForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6677186987400675720L;

  private String userInfo;
  private Integer resultSize = 0;
  private Integer searchSize = 0;
  private Integer startIndex = 0;
  private List<Person> userList;
  private String des3CpsnId;
  private List<UserSearchResultForm> searchResultList;
  private String userIns;// 用户单位信息.
  private boolean isMore;

  public String getUserInfo() {
    return userInfo;
  }

  public Integer getSearchSize() {
    return searchSize;
  }

  public List<Person> getUserList() {
    return userList;
  }

  public void setUserInfo(String userInfo) {
    this.userInfo = userInfo;
  }

  public void setSearchSize(Integer searchSize) {
    this.searchSize = searchSize;
  }

  public void setUserList(List<Person> userList) {
    this.userList = userList;
  }

  public Integer getResultSize() {
    return resultSize;
  }

  public Integer getStartIndex() {
    return startIndex;
  }

  public void setResultSize(Integer resultSize) {
    this.resultSize = resultSize;
  }

  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }

  public String getDes3CpsnId() {
    if (des3CpsnId == null) {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId != null && psnId != 0) {
        des3CpsnId = ServiceUtil.encodeToDes3(psnId.toString());
      } else {
        des3CpsnId = "";
      }
    }
    return des3CpsnId;
  }

  public List<UserSearchResultForm> getSearchResultList() {
    return searchResultList;
  }

  public void setSearchResultList(List<UserSearchResultForm> searchResultList) {
    this.searchResultList = searchResultList;
  }

  public String getUserIns() {
    return userIns;
  }

  public void setUserIns(String userIns) {
    this.userIns = userIns;
  }

  public boolean isMore() {
    return isMore;
  }

  public void setMore(boolean isMore) {
    this.isMore = isMore;
  }

}
