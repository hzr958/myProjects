package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 推广邮件-根据会议论文推荐期刊新算法.
 * 
 * @author maojianguo
 * 
 */
@Entity
@Table(name = "PUB_CONF_JNL_RECOMMEND")
public class PubConfJnlRecommend implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 20469156823559277L;
  private Long id;
  private Long psnId;
  private Long pubId;
  private Long jnlId;
  private String des3JnlId;
  // 是否自己发表过：0未发表，1已发表
  private Integer isme;
  // 是否好友发表过：0未发表，1已发表
  private Integer isfrd;
  // 推荐度
  private Integer degrees;
  // 推荐得分
  private Double score;
  // 期刊名称
  private String jnlTitileEn;
  private String jnlTitleXx;
  private String titleViwe;
  /** 收录. */
  private String dbCodes;
  private String pubTitle;// 论文标题.

  public PubConfJnlRecommend() {
    super();
  }

  public PubConfJnlRecommend(Long id, Long psnId, Long pubId, Long jnlId, Integer degrees, String jnlTitileEn,
      String jnlTitleXx, String pubTitle) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.pubId = pubId;
    this.jnlId = jnlId;
    this.degrees = degrees;
    this.jnlTitleXx = jnlTitleXx;
    this.jnlTitileEn = jnlTitileEn;
    this.pubTitle = pubTitle;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_CONF_JNL_RECOMMEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "ISME")
  public Integer getIsme() {
    return isme;
  }

  public void setIsme(Integer isme) {
    this.isme = isme;
  }

  @Column(name = "ISFRD")
  public Integer getIsfrd() {
    return isfrd;
  }

  public void setIsfrd(Integer isfrd) {
    this.isfrd = isfrd;
  }

  @Column(name = "DEGREES")
  public Integer getDegrees() {
    if (this.score != null) {
      degrees = score < 3 ? 3 : score.intValue() > 5 ? 5 : score.intValue();
    }
    return degrees;
  }

  public void setDegrees(Integer degrees) {
    this.degrees = degrees;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  @Transient
  public String getJnlTitileEn() {
    return jnlTitileEn;
  }

  public void setJnlTitileEn(String jnlTitileEn) {
    this.jnlTitileEn = jnlTitileEn;
  }

  @Transient
  public String getJnlTitleXx() {
    return jnlTitleXx;
  }

  public void setJnlTitleXx(String jnlTitleXx) {
    this.jnlTitleXx = jnlTitleXx;
  }

  @Transient
  public String getDbCodes() {
    return dbCodes;
  }

  public void setDbCodes(String dbCodes) {
    this.dbCodes = dbCodes;
  }

  @Transient
  public String getTitleViwe() {
    return titleViwe;
  }

  public void setTitleViwe(String titleViwe) {
    this.titleViwe = titleViwe;
  }

  @Transient
  public String getDes3JnlId() {
    return des3JnlId;
  }

  public void setDes3JnlId(String des3JnlId) {
    this.des3JnlId = des3JnlId;
  }

  @Column(name = "PUB_TITLE")
  public String getPubTitle() {
    return pubTitle;
  }

  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
