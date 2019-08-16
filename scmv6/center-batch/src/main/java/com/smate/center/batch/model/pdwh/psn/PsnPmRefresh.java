package com.smate.center.batch.model.pdwh.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户个人信息更新标记表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_REFRESH")
public class PsnPmRefresh implements Serializable {

  private static final long serialVersionUID = 8095601500128047472L;
  private Long psnId;// 人员ID.
  private Integer nameFlag = 0;// 是否修改姓名.
  private Integer kwFlag = 0;// 是否修改自填关键词.
  private Integer emailFlag = 0;// 是否修改邮件.
  private Integer wkFlag = 0;// 是否修改工作经历.
  private Integer pubFlag = 0;// 是否修改成果.
  private Integer prjFlag = 0;// 是否修改项目.

  public PsnPmRefresh() {
    super();
  }

  public PsnPmRefresh(Long psnId, Integer nameFlag, Integer kwFlag, Integer emailFlag, Integer wkFlag, Integer pubFlag,
      Integer prjFlag) {
    super();
    this.psnId = psnId;
    this.nameFlag = nameFlag;
    this.kwFlag = kwFlag;
    this.emailFlag = emailFlag;
    this.wkFlag = wkFlag;
    this.pubFlag = pubFlag;
    this.prjFlag = prjFlag;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "NAME_FLAG")
  public Integer getNameFlag() {
    return nameFlag;
  }

  @Column(name = "KW_FLAG")
  public Integer getKwFlag() {
    return kwFlag;
  }

  @Column(name = "EMAIL_FLAG")
  public Integer getEmailFlag() {
    return emailFlag;
  }

  @Column(name = "WK_FLAG")
  public Integer getWkFlag() {
    return wkFlag;
  }

  @Column(name = "PUB_FLAG")
  public Integer getPubFlag() {
    return pubFlag;
  }

  @Column(name = "PRJ_FLAG")
  public Integer getPrjFlag() {
    return prjFlag;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setNameFlag(Integer nameFlag) {
    this.nameFlag = nameFlag;
  }

  public void setKwFlag(Integer kwFlag) {
    this.kwFlag = kwFlag;
  }

  public void setEmailFlag(Integer emailFlag) {
    this.emailFlag = emailFlag;
  }

  public void setWkFlag(Integer wkFlag) {
    this.wkFlag = wkFlag;
  }

  public void setPubFlag(Integer pubFlag) {
    this.pubFlag = pubFlag;
  }

  public void setPrjFlag(Integer prjFlag) {
    this.prjFlag = prjFlag;
  }

}
