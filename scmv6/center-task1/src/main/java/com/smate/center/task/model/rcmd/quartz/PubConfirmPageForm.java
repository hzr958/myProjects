package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.model.sns.quartz.PublicationList;

public class PubConfirmPageForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1500055972557960539L;

  private PubConfirmRolPub pubConfirmDetail;
  private List<PubConfirmRolPubMember> pubConfirmMembers;
  private PublicationList publicationList;
  private Integer seqNo;
  private Long insPubId;
  private Long insId;
  private Integer citedTimes;

  public PubConfirmPageForm() {}

  public PubConfirmPageForm(Long dtId, Float assignScore, Long rolPubId, PubConfirmRolPub detail, Integer seqNo,
      Long insId) {
    this.pubConfirmDetail = detail;
    this.pubConfirmDetail.setDtId(dtId);
    this.pubConfirmDetail.setAssignScore(assignScore);
    this.insPubId = rolPubId;
    this.seqNo = seqNo;
    this.insId = insId;
  }

  public PublicationList getPublicationList() {
    return publicationList;
  }

  public void setPublicationList(PublicationList publicationList) {
    this.publicationList = publicationList;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public PubConfirmRolPub getPubConfirmDetail() {
    return pubConfirmDetail;
  }

  public List<PubConfirmRolPubMember> getPubConfirmMembers() {
    return pubConfirmMembers;
  }

  public void setPubConfirmDetail(PubConfirmRolPub pubConfirmDetail) {
    this.pubConfirmDetail = pubConfirmDetail;
  }

  public void setPubConfirmMembers(List<PubConfirmRolPubMember> pubConfirmMembers) {
    this.pubConfirmMembers = pubConfirmMembers;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Long getInsPubId() {
    return insPubId;
  }

  public void setInsPubId(Long insPubId) {
    this.insPubId = insPubId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

}
