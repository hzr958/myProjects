package com.smate.center.open.model.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组关键词
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "GROUP_KEYWORDS")
public class GroupKeywords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8732270528270847295L;
  private Long id; // 主键
  private Long groupId; // 群组id
  private String keyword; // 群组关键词
  private Long keyHash; // 关键词hash

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Column(name = "KW_HASH")
  public Long getKeyHash() {
    return keyHash;
  }

  public void setKeyHash(Long keyHash) {
    this.keyHash = keyHash;
  }



}
