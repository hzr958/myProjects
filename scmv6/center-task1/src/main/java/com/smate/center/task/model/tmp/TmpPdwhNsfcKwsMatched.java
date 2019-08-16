package com.smate.center.task.model.tmp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TMP_PDWH_NSFC_KWS_MATCHED")
public class TmpPdwhNsfcKwsMatched {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(sequenceName = "SEQ_TMP_PDWH_NSFC_KWS_MATCHED", allocationSize = 1, name = "SEQ_STORE")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long id;
  @Column(name = "KEYWORD_ID") // pdwh_pub_keyword_split表id
  private Long keywordId;
  @Column(name = "KEYWORD")
  private String keyword;
  @Column(name = "KEYWORD_HASH")
  private Long keywordHash;
  @Column(name = "RESEARCH_CODE")
  private Long researchCode;
  @Column(name = "RESEARCH_DIRECTION_NAME")
  private String researchDirectionName;
  @Column(name = "APPLICATION_CODE")
  private String applicationCode;
  @Column(name = "MGMT_DISCIPLINE_CODE")
  private String mgmtDisciplineCode;
  @Column(name = "MGMT_DISCIPLINE_NAME")
  private String mgmtDisciplineName;
  @Column(name = "APPLICATION_CODE_NAME")
  private String applicationCodeName;
  @Column(name = "STATUS")
  private Integer status;
  @Column(name = "KW_LANGUAGE")
  private Integer KeywordLanguage;

  public TmpPdwhNsfcKwsMatched() {
    super();
  }

  public TmpPdwhNsfcKwsMatched(Long keywordId, String keyword, Long keywordHash, Long researchCode,
      String researchDirectionName, String applicationCode, String mgmtDisciplineCode, String mgmtDisciplineName,
      String applicationCodeName, Integer status, Integer keywordLanguage) {
    super();
    this.keywordId = keywordId;
    this.keyword = keyword;
    this.keywordHash = keywordHash;
    this.researchCode = researchCode;
    this.researchDirectionName = researchDirectionName;
    this.applicationCode = applicationCode;
    this.mgmtDisciplineCode = mgmtDisciplineCode;
    this.mgmtDisciplineName = mgmtDisciplineName;
    this.applicationCodeName = applicationCodeName;
    this.status = status;
    KeywordLanguage = keywordLanguage;
  }

  public Integer getKeywordLanguage() {
    return KeywordLanguage;
  }

  public void setKeywordLanguage(Integer keywordLanguage) {
    KeywordLanguage = keywordLanguage;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getKeywordId() {
    return keywordId;
  }

  public void setKeywordId(Long keywordId) {
    this.keywordId = keywordId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Long getKeywordHash() {
    return keywordHash;
  }

  public void setKeywordHash(Long keywordHash) {
    this.keywordHash = keywordHash;
  }

  public Long getResearchCode() {
    return researchCode;
  }

  public void setResearchCode(Long researchCode) {
    this.researchCode = researchCode;
  }

  public String getResearchDirectionName() {
    return researchDirectionName;
  }

  public void setResearchDirectionName(String researchDirectionName) {
    this.researchDirectionName = researchDirectionName;
  }

  public String getApplicationCode() {
    return applicationCode;
  }

  public void setApplicationCode(String applicationCode) {
    this.applicationCode = applicationCode;
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
