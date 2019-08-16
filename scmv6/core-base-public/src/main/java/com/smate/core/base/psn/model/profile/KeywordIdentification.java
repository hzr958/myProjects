package com.smate.core.base.psn.model.profile;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "KEYWORD_IDENTIFICATION")
public class KeywordIdentification implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5013796733425847264L;
  private Long id;
  private Long psnId;
  private Long keywordId;
  private Long friendId;
  private Date opDate;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KWIDENTIFIC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KW_ID")
  public Long getKeywordId() {
    return keywordId;
  }

  @Column(name = "FRIEND_ID")
  public Long getFriendId() {
    return friendId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKeywordId(Long keywordId) {
    this.keywordId = keywordId;
  }

  public void setFriendId(Long friendId) {
    this.friendId = friendId;
  }

  @Column(name = "OP_DATE")
  public Date getOpDate() {
    return opDate;
  }

  public void setOpDate(Date opDate) {
    this.opDate = opDate;
  }

}
