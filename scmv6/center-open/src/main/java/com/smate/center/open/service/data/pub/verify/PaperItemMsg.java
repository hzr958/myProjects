package com.smate.center.open.service.data.pub.verify;

/**
 * @author aijiangbin
 * @create 2018-11-20 14:39
 **/
public class PaperItemMsg {
  /**
   * 1 通过 2 不通过 3 不确定
   */
  public String keyCode = "";
  public String title = "";
  public String doi = "";
  public String journalName = "";
  public String authorNames = "";
  public String publishYear = "";
  public String fundingInfo = "";
  public String otherErrorMsg = "";

  public String getOtherErrorMsg() {
    return otherErrorMsg;
  }

  public void setOtherErrorMsg(String otherErrorMsg) {
    this.otherErrorMsg = otherErrorMsg;
  }

  public String getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(String keyCode) {
    this.keyCode = keyCode;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getJournalName() {
    return journalName;
  }

  public void setJournalName(String journalName) {
    this.journalName = journalName;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getFundingInfo() {
    return fundingInfo;
  }

  public void setFundingInfo(String fundingInfo) {
    this.fundingInfo = fundingInfo;
  }
}
