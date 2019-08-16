package com.smate.center.task.model.tmp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * nsfc临时关键词类
 * 
 * @author LIJUN
 * @date 2018年4月12日
 */
@Entity
@Table(name = "TMP_NSFC_KEYWORDS")
public class TmpNsfcKeywords {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(sequenceName = "SEQ_TMP_NSFC_KEYWORDS", allocationSize = 1, name = "SEQ_STORE")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long id;
  @Column(name = "RESEARCH_CODE")
  private Long researchCode;
  @Column(name = "APPLICATION_CODE")
  private String applicationCode;
  @Column(name = "KEYWORDS")
  private String keywords;
  @Column(name = "KEYWORDS_HASH")
  private Long keywordsHash;
  @Column(name = "MGMT_DISCIPLINE_CODE")
  private String mgmtDisciplineCode;
  @Column(name = "MGMT_DISCIPLINE_NAME")
  private String mgmtDisciplineName;
  @Column(name = "APPLICATION_CODE_NAME")
  private String applicationCodeName;
  @Column(name = "RESEARCH_DIRECTION_NAME")
  private String researchDirectionName;
  @Column(name = "KEYWORD_ORDER")
  private Integer keywordOrder;
  @Column(name = "MATCH_ORDER")
  private Integer matchOrder;
  @Column(name = "FLAG")
  private Integer flag;

  public TmpNsfcKeywords() {
    super();
  }

  public Long getKeywordsHash() {
    return keywordsHash;
  }

  public TmpNsfcKeywords(Long id, String keywords) {
    super();
    this.id = id;
    this.keywords = keywords;
  }

  public TmpNsfcKeywords(Long id, Long researchCode, String applicationCode, String keywords, Long keywordsHash,
      String mgmtDisciplineCode, String mgmtDisciplineName, String applicationCodeName, String researchDirectionName,
      Integer keywordOrder, Integer matchOrder, Integer flag) {
    super();
    this.id = id;
    this.researchCode = researchCode;
    this.applicationCode = applicationCode;
    this.keywords = keywords;
    this.keywordsHash = keywordsHash;
    this.mgmtDisciplineCode = mgmtDisciplineCode;
    this.mgmtDisciplineName = mgmtDisciplineName;
    this.applicationCodeName = applicationCodeName;
    this.researchDirectionName = researchDirectionName;
    this.keywordOrder = keywordOrder;
    this.matchOrder = matchOrder;
    this.flag = flag;
  }

  public void setKeywordsHash(Long keywordsHash) {
    this.keywordsHash = keywordsHash;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getResearchCode() {
    return researchCode;
  }

  public void setResearchCode(Long researchCode) {
    this.researchCode = researchCode;
  }

  public String getApplicationCode() {
    return applicationCode;
  }

  public void setApplicationCode(String applicationCode) {
    this.applicationCode = applicationCode;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getMgmtDisciplineCode() {
    return mgmtDisciplineCode;
  }

  public void setMgmtDisciplineCode(String mgmtDisciplineCode) {
    this.mgmtDisciplineCode = mgmtDisciplineCode;
  }

  public String getMgmtDisciplineName() {
    return mgmtDisciplineName;
  }

  public void setMgmtDisciplineName(String mgmtDisciplineName) {
    this.mgmtDisciplineName = mgmtDisciplineName;
  }

  public String getApplicationCodeName() {
    return applicationCodeName;
  }

  public void setApplicationCodeName(String applicationCodeName) {
    this.applicationCodeName = applicationCodeName;
  }

  public String getResearchDirectionName() {
    return researchDirectionName;
  }

  public void setResearchDirectionName(String researchDirectionName) {
    this.researchDirectionName = researchDirectionName;
  }

  public Integer getKeywordOrder() {
    return keywordOrder;
  }

  public void setKeywordOrder(Integer keywordOrder) {
    this.keywordOrder = keywordOrder;
  }

  public Integer getMatchOrder() {
    return matchOrder;
  }

  public void setMatchOrder(Integer matchOrder) {
    this.matchOrder = matchOrder;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

}
