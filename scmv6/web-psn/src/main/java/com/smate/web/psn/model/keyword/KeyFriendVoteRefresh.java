package com.smate.web.psn.model.keyword;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 好友具有相同投票关键词统计刷新.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "KEY_FRIEND_VOTE_REFRESH")
public class KeyFriendVoteRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3654780858255480090L;
  private Long id;
  private Long psnId;
  private Long friendPsnId;
  private int needRefresh;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KEY_FRIEND_REFRESH", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FRIEND_PSN_ID")
  public Long getFriendPsnId() {
    return friendPsnId;
  }

  public void setFriendPsnId(Long friendPsnId) {
    this.friendPsnId = friendPsnId;
  }

  @Column(name = "NEED_REFRESH")
  public int getNeedRefresh() {
    return needRefresh;
  }

  public void setNeedRefresh(int needRefresh) {
    this.needRefresh = needRefresh;
  }

}
