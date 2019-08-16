package com.smate.center.task.model.tmp.sns;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JZBGSJ_MODIFIED")
public class JzbgsjModified {
  @Id
  @Column(name = "PRJ_ID")
  private Long prjId;
  @Column(name = "FUNDINFO")
  private String fundInfo;
  @Column(name = "NSFC_DISCODE")
  private String nsfcDiscode;
  @Column(name = "PARTICIPANTS")
  private String participants;
  @Column(name = "PRJ_OWNER")
  private String prjOwner;
  @Column(name = "SCM_PSN_ID")
  private Long scmPsnId;
  @Column(name = "OPEN_ID")
  private String openId;
  @Column(name = "GUID")
  private String guid;
  @Column(name = "INS_NAME")
  private String insName;
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  @Column(name = "EN_TITLE")
  private String enTitle;
  @Column(name = "ZH_ABSTRACT")
  private String zhAbstract;
  @Column(name = "EN_ABSTRACT")
  private String enAbstract;
  @Column(name = "ZH_KW")
  private String zhKw;
  @Column(name = "EN_KW")
  private String enKw;
  @Column(name = "PRJ_TYPE")
  private String prjType;
  @Column(name = "SUB_TYPE_BRIEF")
  private String subTypeBrief;
  @Column(name = "REMARK")
  private String remark;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "START_DATE")
  private Date startDate;
  @Column(name = "END_DATE")
  private Date endDate;
  @Column(name = "SCM_DISCODE")
  private String scmDiscode;
  @Column(name = "SCM_DISCODE_1")
  private String scmDiscode1;

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public String getNsfcDiscode() {
    return nsfcDiscode;
  }

  public void setNsfcDiscode(String nsfcDiscode) {
    this.nsfcDiscode = nsfcDiscode;
  }

  public String getParticipants() {
    return participants;
  }

  public void setParticipants(String participants) {
    this.participants = participants;
  }

  public String getPrjOwner() {
    return prjOwner;
  }

  public void setPrjOwner(String prjOwner) {
    this.prjOwner = prjOwner;
  }

  public Long getScmPsnId() {
    return scmPsnId;
  }

  public void setScmPsnId(Long scmPsnId) {
    this.scmPsnId = scmPsnId;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
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

  public String getZhAbstract() {
    return zhAbstract;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  public String getEnAbstract() {
    return enAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public String getZhKw() {
    return zhKw;
  }

  public void setZhKw(String zhKw) {
    this.zhKw = zhKw;
  }

  public String getEnKw() {
    return enKw;
  }

  public void setEnKw(String enKw) {
    this.enKw = enKw;
  }

  public String getPrjType() {
    return prjType;
  }

  public void setPrjType(String prjType) {
    this.prjType = prjType;
  }

  public String getSubTypeBrief() {
    return subTypeBrief;
  }

  public void setSubTypeBrief(String subTypeBrief) {
    this.subTypeBrief = subTypeBrief;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getScmDiscode() {
    return scmDiscode;
  }

  public void setScmDiscode(String scmDiscode) {
    this.scmDiscode = scmDiscode;
  }

  public String getScmDiscode1() {
    return scmDiscode1;
  }

  public void setScmDiscode1(String scmDiscode1) {
    this.scmDiscode1 = scmDiscode1;
  }

}
