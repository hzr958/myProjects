package com.smate.center.task.model.sns.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PubAssginMatchContext implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4062098660027208304L;
  private Long pdwhPubId;
  private Long psnId;
  private Long pubyear;
  private List<String> emailList;// pdwh_pub_xml 中的Email
  private String keywords;// 包含pdwh_pub_xml中zh_keywords，en_keywords
  private Integer emailResult = 0;// 邮件匹配结果
  private Integer nameType = 0;// 全名匹配结果/简称匹配结果
  private Integer InitNameMatch = 0;// 简称匹配结果
  private Integer insResult = 0;// 单位匹配结果
  private Integer friendResult = 0;// 好友匹配结果
  private Integer keywordsResult;// 关键词匹配结果
  private String matchedEmail;// 匹配上的邮箱
  private String matchedFullName;// 匹配上的全称
  private String matchedInitName;// 匹配上的简称
  private Long matchedInsId;// 匹配上的InsId
  private String pubMemberName;
  private Long pubMemberId;

  private List<Long> rePsnIds = new ArrayList<Long>();

  public String getMatchedEmail() {
    return matchedEmail;
  }

  public void setMatchedEmail(String matchedEmail) {
    this.matchedEmail = matchedEmail;
  }

  public String getMatchedFullName() {
    return matchedFullName;
  }

  public void setMatchedFullName(String matchedFullName) {
    this.matchedFullName = matchedFullName;
  }

  public String getMatchedInitName() {
    return matchedInitName;
  }

  public void setMatchedInitName(String matchedInitName) {
    this.matchedInitName = matchedInitName;
  }

  public Long getMatchedInsId() {
    return matchedInsId;
  }

  public void setMatchedInsId(Long matchedInsId) {
    this.matchedInsId = matchedInsId;
  }

  public PubAssginMatchContext() {
    super();
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public List<String> getEmailList() {
    return emailList;
  }

  public void setEmailList(List<String> emailList) {
    this.emailList = emailList;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getEmailResult() {
    return emailResult;
  }

  public void setEmailResult(Integer emailResult) {
    this.emailResult = emailResult;
  }

  public Integer getNameType() {
    return nameType;
  }

  public void setNameType(Integer nameType) {
    this.nameType = nameType;
  }

  public Integer getInitNameMatch() {
    return InitNameMatch;
  }

  public void setInitNameMatch(Integer initNameMatch) {
    InitNameMatch = initNameMatch;
  }

  public Integer getInsResult() {
    return insResult;
  }

  public void setInsResult(Integer insResult) {
    this.insResult = insResult;
  }

  public Integer getFriendResult() {
    return friendResult;
  }

  public void setFriendResult(Integer friendResult) {
    this.friendResult = friendResult;
  }

  public Integer getKeywordsResult() {
    return keywordsResult;
  }

  public void setKeywordsResult(Integer keywordsResult) {
    this.keywordsResult = keywordsResult;
  }

  public List<Long> getRePsnIds() {
    return rePsnIds;
  }

  public void setRePsnIds(List<Long> rePsnIds) {
    this.rePsnIds = rePsnIds;
  }

  public Long getPubyear() {
    return pubyear;
  }

  public void setPubyear(Long pubyear) {
    this.pubyear = pubyear;
  }


  public String getPubMemberName() {
    return pubMemberName;
  }

  public void setPubMemberName(String pubMemberName) {
    this.pubMemberName = pubMemberName;
  }

  public Long getPubMemberId() {
    return pubMemberId;
  }

  public void setPubMemberId(Long pubMemberId) {
    this.pubMemberId = pubMemberId;
  }

}
