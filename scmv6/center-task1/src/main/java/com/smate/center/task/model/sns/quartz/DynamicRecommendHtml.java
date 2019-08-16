package com.smate.center.task.model.sns.quartz;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 推荐动态内容表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "DYNAMIC_RECOMMEND_HTML")
public class DynamicRecommendHtml {

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_RECOMMEND_HTML", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "DYN_RE_TYPE")
  private int dynReType;
  @Column(name = "DYN_HTML")
  private String dynHtml;
  /**
   * 保存英文版推荐动态的内容的字段
   */
  @Column(name = "DYN_HTML_EN")
  private String dynHtmlEn;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public DynamicRecommendHtml() {
    super();
  }

  public DynamicRecommendHtml(Long id, Long psnId, int dynReType, String dynHtml, String dynHtmlEn, Date createDate,
      Date updateDate) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.dynReType = dynReType;
    this.dynHtml = dynHtml;
    this.dynHtmlEn = dynHtmlEn;
    this.createDate = createDate;
    this.updateDate = updateDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public int getDynReType() {
    return dynReType;
  }

  public void setDynReType(int dynReType) {
    this.dynReType = dynReType;
  }

  public String getDynHtml() {
    return dynHtml;
  }

  public void setDynHtml(String dynHtml) {
    this.dynHtml = dynHtml;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getDynHtmlEn() {
    return dynHtmlEn;
  }

  public void setDynHtmlEn(String dynHtmlEn) {
    this.dynHtmlEn = dynHtmlEn;
  }

}
