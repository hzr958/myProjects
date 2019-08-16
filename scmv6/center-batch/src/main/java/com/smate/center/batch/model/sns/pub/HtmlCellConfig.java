package com.smate.center.batch.model.sns.pub;

/**
 * @author yamingd 通过Xml构建成果页面表格Cell的内容，生成配置
 */
public class HtmlCellConfig {

  private boolean titleWithLink = true;
  private boolean showFulltextButton = true;
  private boolean showSourceUrlButton = true;
  private boolean showImpactFactors = true;
  private boolean showCitedTimes = true;

  private String citeDatePattern = "yyyy-MM-dd";

  /**
   * @return the titleWithLink
   */
  public boolean isTitleWithLink() {
    return titleWithLink;
  }

  /**
   * @param titleWithLink the titleWithLink to set
   */
  public void setTitleWithLink(boolean titleWithLink) {
    this.titleWithLink = titleWithLink;
  }

  /**
   * @return the showFulltextButton
   */
  public boolean isShowFulltextButton() {
    return showFulltextButton;
  }

  /**
   * @param showFulltextButton the showFulltextButton to set
   */
  public void setShowFulltextButton(boolean showFulltextButton) {
    this.showFulltextButton = showFulltextButton;
  }

  /**
   * @return the showSourceUrlButton
   */
  public boolean isShowSourceUrlButton() {
    return showSourceUrlButton;
  }

  /**
   * @param showSourceUrlButton the showSourceUrlButton to set
   */
  public void setShowSourceUrlButton(boolean showSourceUrlButton) {
    this.showSourceUrlButton = showSourceUrlButton;
  }

  /**
   * @return the showImpactFactors
   */
  public boolean isShowImpactFactors() {
    return showImpactFactors;
  }

  /**
   * @param showImpactFactors the showImpactFactors to set
   */
  public void setShowImpactFactors(boolean showImpactFactors) {
    this.showImpactFactors = showImpactFactors;
  }

  /**
   * @return the citeDatePattern
   */
  public String getCiteDatePattern() {
    return citeDatePattern;
  }

  /**
   * @param citeDatePattern the citeDatePattern to set
   */
  public void setCiteDatePattern(String citeDatePattern) {
    this.citeDatePattern = citeDatePattern;
  }

  /**
   * @return the showCitedTimes
   */
  public boolean isShowCitedTimes() {
    return showCitedTimes;
  }

  /**
   * @param showCitedTimes the showCitedTimes to set
   */
  public void setShowCitedTimes(boolean showCitedTimes) {
    this.showCitedTimes = showCitedTimes;
  }
}
