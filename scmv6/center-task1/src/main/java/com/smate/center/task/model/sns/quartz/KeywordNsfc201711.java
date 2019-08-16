package com.smate.center.task.model.sns.quartz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NSFC 201711关键词信息
 * 
 * @author LIJUN
 *
 */
@Entity
@Table(name = "KEYWORD_NSFC_201711")
public class KeywordNsfc201711 {
  private Long applicationId;// 申请号
  private Integer year;// 申请年份
  private String applicationCode1;// 申请代码1
  private String applicationCode2;// 申请代码2
  private String applicationType;// 申请类型
  private String manageCode;// 管理代码
  private String keywordsZh;// 中文关键词
  private String keywordsEn;// 英文关键词
  private String researchDirection;// 研究方向
  private Integer evaluationResult;// 是否批准

  @Id
  @Column(name = "APPLICATION_ID")
  public Long getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(Long applicationId) {
    this.applicationId = applicationId;
  }

  @Column(name = "YEAR")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Column(name = "APPLICATION_CODE1")
  public String getApplicationCode1() {
    return applicationCode1;
  }

  public void setApplicationCode1(String applicationCode1) {
    this.applicationCode1 = applicationCode1;
  }

  @Column(name = "APPLICATION_CODE2")
  public String getApplicationCode2() {
    return applicationCode2;
  }

  public void setApplicationCode2(String applicationCode2) {
    this.applicationCode2 = applicationCode2;
  }

  @Column(name = "APPLICATION_TYPE")
  public String getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }

  @Column(name = "MANAGE_CODE")
  public String getManageCode() {
    return manageCode;
  }

  public void setManageCode(String manageCode) {
    this.manageCode = manageCode;
  }

  @Column(name = "KEYWORDS_ZH")
  public String getKeywordsZh() {
    return keywordsZh;
  }

  public void setKeywordsZh(String keywordsZh) {
    this.keywordsZh = keywordsZh;
  }

  @Column(name = "KEYWORDS_EN")
  public String getKeywordsEn() {
    return keywordsEn;
  }

  public void setKeywordsEn(String keywordsEn) {
    this.keywordsEn = keywordsEn;
  }

  @Column(name = "RESEARCH_DIRECTION")
  public String getResearchDirection() {
    return researchDirection;
  }

  public void setResearchDirection(String researchDirection) {
    this.researchDirection = researchDirection;
  }

  @Column(name = "EVALUATION_RESULT")
  public Integer getEvaluationResult() {
    return evaluationResult;
  }

  public void setEvaluationResult(Integer evaluationResult) {
    this.evaluationResult = evaluationResult;
  }

}
