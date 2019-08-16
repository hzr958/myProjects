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
 * @author zzx
 *
 */
@Entity
@Table(name = "PATENT_CATEGORY_NSFC")
public class PatentCategpruNsfc implements Serializable {
  private static final long serialVersionUID = 7477434755284708370L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORD_SUBSET", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "APPROVE_CODE")
  private String approveCode;
  @Column(name = "NSFC_CATEGORY")
  private String nsfcCategory;
  @Column(name = "SEQ_NO")
  private Integer seqNo;
  @Column(name = "LANGUAGE")
  private Integer language;
  @Column(name = "ORIGINAL")
  private Integer original;

  public PatentCategpruNsfc() {
    super();
  }

  public PatentCategpruNsfc(String approveCode, String nsfcCategory, Integer seqNo, Integer language,
      Integer original) {
    super();
    this.approveCode = approveCode;
    this.nsfcCategory = nsfcCategory;
    this.seqNo = seqNo;
    this.language = language;
    this.original = original;
  }

  public PatentCategpruNsfc(Long id, String approveCode, String nsfcCategory, Integer seqNo, Integer language,
      Integer original) {
    super();
    this.id = id;
    this.approveCode = approveCode;
    this.nsfcCategory = nsfcCategory;
    this.seqNo = seqNo;
    this.language = language;
    this.original = original;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getApproveCode() {
    return approveCode;
  }

  public void setApproveCode(String approveCode) {
    this.approveCode = approveCode;
  }

  public String getNsfcCategory() {
    return nsfcCategory;
  }

  public void setNsfcCategory(String nsfcCategory) {
    this.nsfcCategory = nsfcCategory;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  public Integer getOriginal() {
    return original;
  }

  public void setOriginal(Integer original) {
    this.original = original;
  }


}
