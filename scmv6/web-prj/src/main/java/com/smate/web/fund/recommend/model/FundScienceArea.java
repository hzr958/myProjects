package com.smate.web.fund.recommend.model;

import java.io.Serializable;

/**
 * 推荐基金的科技领域
 * 
 * @author WSN
 *
 *         2017年8月21日 下午4:29:28
 *
 */
public class FundScienceArea implements Serializable {

  private Long psnId; // 人员ID
  private Long scienceAreaId; // 科技领域ID
  private String zhTitle; // 中文标题
  private String enTitle; // 英文标题
  private String showTitle; // 显示的科技领域名称
  private String searchKey; // 检索用的，包括该科技领域和其一级科技领域

  public FundScienceArea() {
    super();
  }

  public FundScienceArea(Long psnId, Long scienceAreaId, String zhTitle, String enTitle, String showTitle) {
    super();
    this.psnId = psnId;
    this.scienceAreaId = scienceAreaId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.showTitle = showTitle;
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

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Long scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  public String getShowTitle() {
    return showTitle;
  }

  public void setShowTitle(String showTitle) {
    this.showTitle = showTitle;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

}
