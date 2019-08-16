package com.smate.center.task.single.service.pub;

import java.io.Serializable;

/**
 * 论文推荐动态参数实体.
 * 
 * @author mjg
 * 
 */
public class DynRecommendThesisForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1305153408688200140L;
  private Long pubId;
  private String des3Id;
  private Long psnId;
  private Integer dbId;
  private String zhTitle;
  private String enTitle;
  private String authorNames;
  private String zhBriefDesc;
  private String enBriefDesc;

  public DynRecommendThesisForm() {
    super();
  }

  public DynRecommendThesisForm(Long pubId, String des3Id, Long psnId, Integer dbId, String zhTitle, String enTitle,
      String authorNames, String zhBriefDesc, String enBriefDesc) {
    super();
    this.pubId = pubId;
    this.des3Id = des3Id;
    this.psnId = psnId;
    this.dbId = dbId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.zhBriefDesc = zhBriefDesc;
    this.enBriefDesc = enBriefDesc;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getZhBriefDesc() {
    return zhBriefDesc;
  }

  public void setZhBriefDesc(String zhBriefDesc) {
    this.zhBriefDesc = zhBriefDesc;
  }

  public String getEnBriefDesc() {
    return enBriefDesc;
  }

  public void setEnBriefDesc(String enBriefDesc) {
    this.enBriefDesc = enBriefDesc;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }
}
