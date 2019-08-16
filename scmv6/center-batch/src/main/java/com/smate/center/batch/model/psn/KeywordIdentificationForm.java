package com.smate.center.batch.model.psn;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.smate.core.base.utils.model.security.Person;

public class KeywordIdentificationForm implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1970805346041221403L;
  private Long psnId;
  private List<Person> friendList;
  private Long keywordId;// psn_discipline_key 主键
  private String keyword;
  private Long count;

  public Long getPsnId() {
    return psnId;
  }

  public Long getKeywordId() {
    return keywordId;
  }

  public String getKeyword() {
    return keyword;
  }

  public Long getCount() {
    return count;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKeywordId(Long keywordId) {
    this.keywordId = keywordId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public List<Person> getFriendList() {
    return friendList;
  }

  public void setFriendList(List<Person> friendList) {
    this.friendList = friendList;
  }

  public boolean isIdentif(Long psnId) {
    if (CollectionUtils.isNotEmpty(friendList)) {
      for (Person p : friendList) {
        if (p.getPersonId().equals(psnId)) {
          return true;
        }
      }
    }
    return false;
  }
}
