package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "JXSTC_PRP_INFO_TEMP")
public class JxstcPrpInfoTemp implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PRP_CODE")
  private Long prpCode;//
  @Column(name = "POS_CODE")
  private Long posCode;//
  @Column(name = "SUMMARY")
  private String summary;//
  @Column(name = "ZH_TITLE")
  private String zhTitle;//
  @Column(name = "KEY_WORDS")
  private String keyWords;//

  public JxstcPrpInfoTemp() {
    super();
  }

  public JxstcPrpInfoTemp(Long prpCode, Long posCode, String summary, String zhTitle, String keyWords) {
    super();
    this.prpCode = prpCode;
    this.posCode = posCode;
    this.summary = summary;
    this.zhTitle = zhTitle;
    this.keyWords = keyWords;
  }

  public Long getPrpCode() {
    return prpCode;
  }

  public void setPrpCode(Long prpCode) {
    this.prpCode = prpCode;
  }

  public Long getPosCode() {
    return posCode;
  }

  public void setPosCode(Long posCode) {
    this.posCode = posCode;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

}
