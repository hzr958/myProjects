package com.smate.web.prj.vo;

import java.io.Serializable;

/**
 * 待导入项目VO
 * 
 * @author wsn
 * @date Dec 17, 2018
 */
public class PendingImportPrjVO implements Serializable {

  private static final long serialVersionUID = 5911829096231203104L;

  private String showTitle; // 列表显示的title
  private String showAuthor; // 列表中显示的作者
  private String showBriefDesc; // 显示的编目信息
  private String prjType; // 项目类型
  private String dupValue; // 查重后选中选项
  private String sourceUrl; // 来源URL
  private String sourceDbCode; // 来源DB id
  private Integer authorMatch; // 是否匹配上作者

  public String getShowTitle() {
    return showTitle;
  }

  public void setShowTitle(String showTitle) {
    this.showTitle = showTitle;
  }

  public String getShowAuthor() {
    return showAuthor;
  }

  public void setShowAuthor(String showAuthor) {
    this.showAuthor = showAuthor;
  }

  public String getShowBriefDesc() {
    return showBriefDesc;
  }

  public void setShowBriefDesc(String showBriefDesc) {
    this.showBriefDesc = showBriefDesc;
  }

  public String getPrjType() {
    return prjType;
  }

  public void setPrjType(String prjType) {
    this.prjType = prjType;
  }

  public String getDupValue() {
    return dupValue;
  }

  public void setDupValue(String dupValue) {
    this.dupValue = dupValue;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getSourceDbCode() {
    return sourceDbCode;
  }

  public void setSourceDbCode(String sourceDbCode) {
    this.sourceDbCode = sourceDbCode;
  }

  public Integer getAuthorMatch() {
    return authorMatch;
  }

  public void setAuthorMatch(Integer authorMatch) {
    this.authorMatch = authorMatch;
  }



}
