package com.smate.web.psn.model.friend;

import java.io.Serializable;

/**
 * 移动端-----“联系”页面Action用form
 *
 * @author wsn
 *
 */
public class PersonalRelationForm implements Serializable {

  private Long psnId;// 人员ID
  private String des3PsnId; // 加密的人员ID
  private Long friendCount = 0L; // 好友数
  private String searchString;

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

  public Long getFriendCount() {
    return friendCount;
  }

  public void setFriendCount(Long friendCount) {
    this.friendCount = friendCount;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

}
