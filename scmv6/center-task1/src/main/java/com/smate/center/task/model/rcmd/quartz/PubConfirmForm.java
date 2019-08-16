package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.Page;

public class PubConfirmForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5286405887128211855L;

  private String dtIds;
  private Long dtId;
  private Long insId;
  private String confirmIds;
  private String confirmData;
  private String insName;
  private List<Object[]> pubMembers;
  private String queryPubMember;
  private String removePubMember;
  private String forwardUrl;
  private List<PubConfirmInsForm> pubConfirmInsForms;
  private Page<PubConfirmPageForm> mainPage;
  private Integer publishYear;
  private Map<Integer, Long> pubYearMap;
  private Long dupPubId;
  private Integer dupAction;
  private Integer dupType;
  private Long confirmPmId;
  private Integer confirmSeqNo;
  private Integer confirmType;
  private String psnEnName;
  private String psnZhName;
  private Long lastShowPubId = 0L;
  private Long lastShowDtId = 0L;
  private Float lastShowScore = 0F;
  private Integer curShowCount = 0;
  /** 是否邀请合作者. */
  private Integer inviteCoAuthors = 1;
  private String rolPubIds;
  private String allConfirmPubIds;
  private Long psnId; // 微信成果 确认 psnid
  private String mark = "0"; // 1为微信请求

  public String getAllConfirmPubIds() {
    return allConfirmPubIds;
  }

  public void setAllConfirmPubIds(String allConfirmPubIds) {
    this.allConfirmPubIds = allConfirmPubIds;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getConfirmData() {
    return confirmData;
  }

  public void setConfirmData(String confirmData) {
    this.confirmData = confirmData;
  }

  public String getConfirmIds() {
    return confirmIds;
  }

  public void setConfirmIds(String confirmIds) {
    this.confirmIds = confirmIds;
  }

  public String getDtIds() {
    return dtIds;
  }

  public void setDtIds(String dtIds) {
    this.dtIds = dtIds;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public List<Object[]> getPubMembers() {
    return pubMembers;
  }

  public String getQueryPubMember() {
    return queryPubMember;
  }

  public String getRemovePubMember() {
    return removePubMember;
  }

  public void setPubMembers(List<Object[]> pubMembers) {
    this.pubMembers = pubMembers;
  }

  public void setQueryPubMember(String queryPubMember) {
    this.queryPubMember = queryPubMember;
  }

  public void setRemovePubMember(String removePubMember) {
    this.removePubMember = removePubMember;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public List<PubConfirmInsForm> getPubConfirmInsForms() {
    return pubConfirmInsForms;
  }

  public void setPubConfirmInsForms(List<PubConfirmInsForm> pubConfirmInsForms) {
    this.pubConfirmInsForms = pubConfirmInsForms;
  }

  public Page<PubConfirmPageForm> getMainPage() {
    return mainPage;
  }

  public void setMainPage(Page<PubConfirmPageForm> mainPage) {
    this.mainPage = mainPage;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Map<Integer, Long> getPubYearMap() {
    return pubYearMap;
  }

  public void setPubYearMap(Map<Integer, Long> pubYearMap) {
    this.pubYearMap = pubYearMap;
  }

  public Long getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(Long dupPubId) {
    this.dupPubId = dupPubId;
  }

  public Integer getDupAction() {
    return dupAction;
  }

  public void setDupAction(Integer dupAction) {
    this.dupAction = dupAction;
  }

  public Integer getDupType() {
    return dupType;
  }

  public void setDupType(Integer dupType) {
    this.dupType = dupType;
  }

  public Long getConfirmPmId() {
    return confirmPmId;
  }

  public void setConfirmPmId(Long confirmPmId) {
    this.confirmPmId = confirmPmId;
  }

  public Integer getConfirmSeqNo() {
    return confirmSeqNo;
  }

  public void setConfirmSeqNo(Integer confirmSeqNo) {
    this.confirmSeqNo = confirmSeqNo;
  }

  public Integer getConfirmType() {
    return confirmType;
  }

  public void setConfirmType(Integer confirmType) {
    this.confirmType = confirmType;
  }

  public Long getDtId() {
    return dtId;
  }

  public void setDtId(Long dtId) {
    this.dtId = dtId;
  }

  public String getPsnEnName() {
    return psnEnName;
  }

  public void setPsnEnName(String psnEnName) {
    this.psnEnName = psnEnName;
  }

  public String getPsnZhName() {
    return psnZhName;
  }

  public void setPsnZhName(String psnZhName) {
    this.psnZhName = psnZhName;
  }

  public Long getLastShowPubId() {
    return lastShowPubId;
  }

  public void setLastShowPubId(Long lastShowPubId) {
    this.lastShowPubId = lastShowPubId;
  }

  public Long getLastShowDtId() {
    return lastShowDtId;
  }

  public void setLastShowDtId(Long lastShowDtId) {
    this.lastShowDtId = lastShowDtId;
  }

  public Float getLastShowScore() {
    return lastShowScore;
  }

  public void setLastShowScore(Float lastShowScore) {
    this.lastShowScore = lastShowScore;
  }

  public Integer getCurShowCount() {
    return curShowCount;
  }

  public void setCurShowCount(Integer curShowCount) {
    this.curShowCount = curShowCount;
  }

  public Integer getInviteCoAuthors() {
    return inviteCoAuthors;
  }

  public void setInviteCoAuthors(Integer inviteCoAuthors) {
    this.inviteCoAuthors = inviteCoAuthors;
  }

  public String getRolPubIds() {
    return rolPubIds;
  }

  public void setRolPubIds(String rolPubIds) {
    this.rolPubIds = rolPubIds;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

}
