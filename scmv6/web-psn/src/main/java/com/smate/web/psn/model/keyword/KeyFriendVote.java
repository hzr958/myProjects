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
 * 统计好友具有相同关键词投票.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "KEY_FRIEND_VOTE")
public class KeyFriendVote implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1669635182396136493L;
  private Long id;
  private Long psnId;
  private Long friendPsnId;
  private int sameVotes;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KEY_FRIEND_VOTE", allocationSize = 1)
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

  @Column(name = "SAME_VOTES")
  public int getSameVotes() {
    return sameVotes;
  }

  public void setSameVotes(int sameVotes) {
    this.sameVotes = sameVotes;
  }
}
