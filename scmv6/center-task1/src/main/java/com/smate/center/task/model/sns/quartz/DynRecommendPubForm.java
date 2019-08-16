package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

/**
 * 成果推荐动态参数实体.
 * 
 * @author mjg
 * 
 */
public class DynRecommendPubForm implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 8749773617954999887L;
  private Long psnId;
  private Long pubId;
  private String pubTitle;
  private Long insId;
  private Long dtId;
  private String authors;
  private String source;
  private String des3Id;

  public DynRecommendPubForm() {
    super();
  }

  public DynRecommendPubForm(Long psnId, Long pubId, String pubTitle, Long insId, Long dtId, String authors,
      String des3Id) {
    super();
    this.psnId = psnId;
    this.pubId = pubId;
    this.pubTitle = pubTitle;
    this.insId = insId;
    this.dtId = dtId;
    this.authors = authors;
    this.des3Id = des3Id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getPubTitle() {
    return pubTitle;
  }

  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getDtId() {
    return dtId;
  }

  public void setDtId(Long dtId) {
    this.dtId = dtId;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
