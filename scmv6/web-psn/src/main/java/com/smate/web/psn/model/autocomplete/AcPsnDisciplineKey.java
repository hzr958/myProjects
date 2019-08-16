package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 个人关键词自动提示model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "NSFC_PSN_DISCIPLINE_KEY")
public class AcPsnDisciplineKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 15205812813114102L;
  private Long id;
  private Long psnDisId;
  private String zhKeyWord;
  private String enKeyWord;
  private String keyWord;
  private int status;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSNDIS_ID")
  public Long getPsnDisId() {
    return psnDisId;
  }

  public void setPsnDisId(Long psnDisId) {
    this.psnDisId = psnDisId;
  }

  @Column(name = "KEY_WORDS")
  public String getZhKeyWord() {
    return zhKeyWord;
  }

  public void setZhKeyWord(String zhKeyWord) {
    this.zhKeyWord = zhKeyWord;
  }

  @Column(name = "EN_KEY_WORDS")
  public String getEnKeyWord() {
    return enKeyWord;
  }

  public void setEnKeyWord(String enKeyWord) {
    this.enKeyWord = enKeyWord;
  }

  @Transient
  public String getKeyWord() {
    return keyWord;
  }

  public void setKeyWord(String keyWord) {
    this.keyWord = keyWord;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
