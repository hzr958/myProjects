package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 个人好友分组.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_IN_GROUP")
public class FriendInGroups implements Serializable {

  private static final long serialVersionUID = -917725504459148322L;
  // pk
  private Long id;
  // 好友ID
  private Long friendId;
  // 好友所属小组ID
  private Long groupId;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_IN_GROUP", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FRIEND_ID")
  public Long getFriendId() {
    return friendId;
  }

  public void setFriendId(Long friendId) {
    this.friendId = friendId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
