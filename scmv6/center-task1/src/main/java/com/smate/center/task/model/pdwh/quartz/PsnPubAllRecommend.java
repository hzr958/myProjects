package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员期刊推荐新算法，推荐出来的文献记录
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_PUBALL_RECOMMEND")
public class PsnPubAllRecommend implements Serializable {

  private static final long serialVersionUID = 1527339935789670989L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PUBALL_RECOMMEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;
  // 人员ID
  @Column(name = "PSN_ID")
  private Long psnId;
  // 成果ID
  @Column(name = "PUBALL_ID")
  private Long pubAllId;
  @Column(name = "SCORE")
  private Double score;
  @Column(name = "KEYWORDS")
  private String keywords;
  @Column(name = "REC_DATE")
  private Date recDate;
  // 文献语言1中文，2英文
  @Column(name = "LANGUAGE")
  private Integer language;
  @Transient
  private String pubTitle;// 成果标题.
  // 对应pubAll的dbId,用于邮件推荐
  @Transient
  private Integer dbId;
  @Transient
  private Long pubId;

  public PsnPubAllRecommend() {
    super();
  }

  public PsnPubAllRecommend(Long pubAllId, String keywords) {
    super();
    this.pubAllId = pubAllId;
    this.keywords = keywords;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Date getRecDate() {
    return recDate;
  }

  public void setRecDate(Date recDate) {
    this.recDate = recDate;
  }

  public String getPubTitle() {
    return pubTitle;
  }

  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((pubAllId == null) ? 0 : pubAllId.hashCode());
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
    PsnPubAllRecommend other = (PsnPubAllRecommend) obj;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (pubAllId == null) {
      if (other.pubAllId != null)
        return false;
    } else if (!pubAllId.equals(other.pubAllId))
      return false;
    return true;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

}
