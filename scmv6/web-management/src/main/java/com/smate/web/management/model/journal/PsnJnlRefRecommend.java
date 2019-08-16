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
 * 论文->适合期刊-按论文推荐期刊新算法.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_JNL_REFREC_RECOMMEND")
public class PsnJnlRefRecommend implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 20469156823559277L;
  private Long id;
  private Long psnId;
  private Long jnlId;
  private String des3JnlId;
  // 推荐得分
  private Double score;
  // 期刊名称
  private String jnlTitileEn;
  private String jnlTitleXx;
  private String titleViwe;
  /** 收录. */
  private String dbCodes;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_JNL_REF_RECOMMEND", allocationSize = 1)
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

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
