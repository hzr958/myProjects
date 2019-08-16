package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 关键词自动提示搜索.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "NSFC_DISCIPLINE_KEY")
public class AcDisciplineKey implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -6890538035304246815L;
  private Long id;
  private String zhKeyWord;
  private String enKeyWord;
  private String keyWord;
  private Long disId;
  private Long psnId;
  private int status;

  @Id
  @Column(name = "KEY_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "KEY_WORD")
  public String getZhKeyWord() {
    return zhKeyWord;
  }

  public void setZhKeyWord(String zhKeyWord) {
    this.zhKeyWord = zhKeyWord;
  }

  @Column(name = "EN_KEY_WORD")
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

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
