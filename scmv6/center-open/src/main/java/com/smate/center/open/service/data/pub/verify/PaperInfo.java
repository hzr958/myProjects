package com.smate.center.open.service.data.pub.verify;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.utils.string.StringUtils;

/**
 * @author aijiangbin
 * @create 2018-11-12 16:29
 **/
public class PaperInfo {
  public String keyCode = ""; // 唯一标识
  public String title = "";// 成果标题
  public String doi = "";// 成果doi
  @JsonIgnore
  public Boolean doiCompare = false; //
  public String journalName = "";// 成果期刊名字
  @JsonIgnore
  public Boolean journalNameCompare = false; //
  public String authorNames = "";// 成果作者 ，英文分号分隔
  @JsonIgnore
  public Boolean authorNamesCompare = false; //
  public String publishYear = "";// 成果发表年份
  @JsonIgnore
  public Boolean publishYearCompare = false; //
  public String fundingInfo = "";// 项目标注/项目资助号
  @JsonIgnore
  public Boolean fundingInfoCompare = false; //

  @JsonIgnore
  public String participantNames = ""; //参与人

  public Boolean getDoiCompare() {
    return doiCompare;
  }

  public void setDoiCompare(Boolean doiCompare) {
    this.doiCompare = doiCompare;
  }

  public Boolean getJournalNameCompare() {
    return journalNameCompare;
  }

  public void setJournalNameCompare(Boolean journalNameCompare) {
    this.journalNameCompare = journalNameCompare;
  }

  public Boolean getAuthorNamesCompare() {
    return authorNamesCompare;
  }

  public void setAuthorNamesCompare(Boolean authorNamesCompare) {
    this.authorNamesCompare = authorNamesCompare;
  }

  public Boolean getPublishYearCompare() {
    return publishYearCompare;
  }

  public void setPublishYearCompare(Boolean publishYearCompare) {
    this.publishYearCompare = publishYearCompare;
  }

  public Boolean getFundingInfoCompare() {
    return fundingInfoCompare;
  }

  public void setFundingInfoCompare(Boolean fundingInfoCompare) {
    this.fundingInfoCompare = fundingInfoCompare;
  }

  public String getKeyCode() {
    if (StringUtils.isNotBlank(keyCode)) {
      keyCode = keyCode.trim();
    }
    return keyCode;
  }

  public void setKeyCode(String keyCode) {
    this.keyCode = keyCode;
  }

  public String getTitle() {
    if (StringUtils.isNotBlank(title)) {
      title = title.trim();
    }
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
    if(StringUtils.isNotBlank(journalName)){
      journalName = journalName.trim().toLowerCase();
    }
    return journalName;
  }

  public void setJournalName(String journalName) {
    this.journalName = journalName;
  }

  public String getAuthorNames() {
    if (StringUtils.isNotBlank(authorNames)) {
      authorNames =  PubDetailVoUtil.dealAuthorNames(authorNames) ;
    }
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getPublishYear() {
    if(StringUtils.isNotBlank(publishYear)){
      publishYear = publishYear.trim();
    }
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getFundingInfo() {
    if(StringUtils.isNotBlank(fundingInfo)){
      fundingInfo = fundingInfo.trim();
    }
    return fundingInfo;
  }

  public void setFundingInfo(String fundingInfo) {
    this.fundingInfo = fundingInfo;
  }

  public String getParticipantNames() {
    return participantNames;
  }

  public void setParticipantNames(String participantNames) {
    this.participantNames = participantNames;
  }
}
