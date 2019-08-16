package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "PROJECT_CATEGORY_NSFC_2018")
public class PubKeywordsSubsetsClassificationRs implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7477434755284708370L;
  private Long id;
  private String approveCode;
  private String nsfcCategory;
  private Integer seqNo;
  private Integer language;
  private Integer original;

  public PubKeywordsSubsetsClassificationRs() {
    super();
  }

  public PubKeywordsSubsetsClassificationRs(String approveCode, String nsfcCategory, Integer seqNo, Integer language,
      Integer original) {
    super();
    this.approveCode = approveCode;
    this.nsfcCategory = nsfcCategory;
    this.seqNo = seqNo;
    this.language = language;
    this.original = original;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORD_SUBSET", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "APPROVE_CODE")
  public String getApproveCode() {
    return approveCode;
  }

  public void setApproveCode(String approveCode) {
    this.approveCode = approveCode;
  }

  @Column(name = "NSFC_CATEGORY")
  public String getNsfcCategory() {
    return nsfcCategory;
  }

  public void setNsfcCategory(String nsfcCategory) {
    this.nsfcCategory = nsfcCategory;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "ORIGINAL")
  public Integer getOriginal() {
    return original;
  }

  public void setOriginal(Integer original) {
    this.original = original;
  }

}
