package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * GroupInvitePsnFriend复合主键.
 * 
 * @author zhuangyanming
 * 
 */
@Embeddable
public class GroupInvitePsnFriendPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2038591196457588052L;
  // 当前人的PSN_ID
  private Long psnId;
  // 好友的PSN_ID
  private Long friendPsnId;

  public GroupInvitePsnFriendPk() {

  }

  public GroupInvitePsnFriendPk(Long psnId, Long friendPsnId) {
    this.psnId = psnId;
    this.friendPsnId = friendPsnId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "FRIEND_PSN_ID")
  public Long getFriendPsnId() {
    return friendPsnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setFriendPsnId(Long friendPsnId) {
    this.friendPsnId = friendPsnId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((friendPsnId == null) ? 0 : friendPsnId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GroupInvitePsnFriendPk other = (GroupInvitePsnFriendPk) obj;
    if (friendPsnId == null) {
      if (other.friendPsnId != null)
        return false;
    } else if (!friendPsnId.equals(other.friendPsnId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }

}
