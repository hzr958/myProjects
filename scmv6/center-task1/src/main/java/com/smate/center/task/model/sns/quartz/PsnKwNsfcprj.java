package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "psn_kw_nsfcprj")
public class PsnKwNsfcprj implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7032023157502438293L;
  private Long id;
  private Long psnId;
  private Long prjCode;
  private String keyword;
  private String keywordTxt;
  private Integer type;
  private String zhKw;
  private String zhKwTxt;
  private Integer zhKwLen;
  private String enKw;
  private String enKwTxt;
  private Integer enKwLen;
  private Integer prjYear;

  public PsnKwNsfcprj(Long id, Long psnId, Long prjCode, String keyword, String keywordTxt, Integer type, String zhKw,
      String zhKwTxt, Integer zhKwLen, String enKw, String enKwTxt, Integer enKwLen, Integer prjYear) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.prjCode = prjCode;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
    this.type = type;
    this.zhKw = zhKw;
    this.zhKwTxt = zhKwTxt;
    this.zhKwLen = zhKwLen;
    this.enKw = enKw;
    this.enKwTxt = enKwTxt;
    this.enKwLen = enKwLen;
    this.prjYear = prjYear;
  }

  @Id
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "prj_code")
  public Long getPrjCode() {
    return prjCode;
  }

  @Column(name = "keyword")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "keyword_txt")
  public String getKeywordTxt() {
    return keywordTxt;
  }

  @Column(name = "type")
  public Integer getType() {
    return type;
  }

  @Column(name = "zh_kw")
  public String getZhKw() {
    return zhKw;
  }

  @Column(name = "zh_kw_txt")
  public String getZhKwTxt() {
    return zhKwTxt;
  }

  @Column(name = "zh_kw_len")
  public Integer getZhKwLen() {
    return zhKwLen;
  }

  @Column(name = "en_kw")
  public String getEnKw() {
    return enKw;
  }

  @Column(name = "en_kw_txt")
  public String getEnKwTxt() {
    return enKwTxt;
  }

  @Column(name = "en_kw_len")
  public Integer getEnKwLen() {
    return enKwLen;
  }

  @Column(name = "prj_year")
  public Integer getPrjYear() {
    return prjYear;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPrjCode(Long prjCode) {
    this.prjCode = prjCode;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKeywordTxt(String keywordTxt) {
    this.keywordTxt = keywordTxt;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setZhKw(String zhKw) {
    this.zhKw = zhKw;
  }

  public void setZhKwTxt(String zhKwTxt) {
    this.zhKwTxt = zhKwTxt;
  }

  public void setZhKwLen(Integer zhKwLen) {
    this.zhKwLen = zhKwLen;
  }

  public void setEnKw(String enKw) {
    this.enKw = enKw;
  }

  public void setEnKwTxt(String enKwTxt) {
    this.enKwTxt = enKwTxt;
  }

  public void setEnKwLen(Integer enKwLen) {
    this.enKwLen = enKwLen;
  }

  public void setPrjYear(Integer prjYear) {
    this.prjYear = prjYear;
  }
}
