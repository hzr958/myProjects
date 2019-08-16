package com.smate.web.psn.model.profile;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.model.keyword.PsnScienceArea;

public class PsnScienceAreaForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8745947220769014121L;

  private Long psnId;
  private List<Person> friendList;
  private PsnScienceArea psnScienceArea;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public List<Person> getFriendList() {
    return friendList;
  }

  public void setFriendList(List<Person> friendList) {
    this.friendList = friendList;
  }

  public PsnScienceArea getPsnScienceArea() {
    return psnScienceArea;
  }

  public void setPsnScienceArea(PsnScienceArea psnScienceArea) {
    this.psnScienceArea = psnScienceArea;
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
