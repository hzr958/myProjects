package com.smate.core.base.pub.recommend.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 个人熟悉的学科关键字.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RECOMMEND_DISCIPLINE_KEY")
public class RecommendDisciplineKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2467297195382733818L;

  private Long id;
  // private Long keyId;
  private String keyWords;
  private Long psnId;// lgk ,拆分学科领域与关键词
  // private Integer status;//
  // zk,当前研究领域是有效,0无效（既删除的研究领域），１有效[兼容认同信息，用户保存的关键词记录不能直接删除，否则该关键词的认同信息就会失联]
  private Date updateDate;// 更新时间

  public RecommendDisciplineKey() {
    super();
  }

  public RecommendDisciplineKey(String keyWords) {
    super();
    this.keyWords = keyWords;
  }

  public RecommendDisciplineKey(Long psnId, String keyWords) {
    super();
    this.keyWords = keyWords;
    this.psnId = psnId;
  }

  public RecommendDisciplineKey(Long psnId, String keyWords, Date updateDate) {
    super();
    this.keyWords = keyWords;
    this.psnId = psnId;
    this.updateDate = updateDate;
  }

  public RecommendDisciplineKey(String keyWords, Long psnId) {
    super();
    this.keyWords = keyWords;
    this.psnId = psnId;
  }

  public RecommendDisciplineKey(Long id, String keyWords, Long psnId) {
    super();
    this.id = id;
    this.keyWords = keyWords;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_REC_DISCIPLINE_KEY", allocationSize = 1)
  public Long getId() {
    return id;
  }

  @Column(name = "KEY_WORDS")
  public String getKeyWords() {
    return keyWords;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
