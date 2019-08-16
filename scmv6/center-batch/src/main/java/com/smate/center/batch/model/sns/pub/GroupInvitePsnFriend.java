package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 好友变化监听表，用于好友的群组.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_INVITE_PSNFRIEND")
public class GroupInvitePsnFriend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2131203274098931014L;

  private GroupInvitePsnFriendPk id;

  @EmbeddedId
  public GroupInvitePsnFriendPk getId() {
    return id;
  }

  public void setId(GroupInvitePsnFriendPk id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
