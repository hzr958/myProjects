package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专利相关统计
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "ST_PAT")
public class PatStat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6396663444019953809L;
  // 专利主键
  private Long patId;
  // 阅读数
  private Long readNum = 0L;
  // 下载数
  private Long downloadNum = 0L;
  // 转化金额
  private Double tranAmount = 0d;
  // 引用
  private Long citeNum = 0L;
  // 分享
  private Long shareNum = 0L;
  // 赞
  private Long awardNum = 0L;
  // 转化数
  private Long tranNum = 0L;

  public PatStat() {
    super();
  }

  public PatStat(Long patId) {
    super();
    this.patId = patId;
  }

  public PatStat(Long patId, Long readNum, Long downloadNum, Long citeNum, Long shareNum, Long awardNum) {
    super();
    this.patId = patId;
    this.readNum = readNum;
    this.downloadNum = downloadNum;
    this.citeNum = citeNum;
    this.shareNum = shareNum;
    this.awardNum = awardNum;
  }

  @Id
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  @Column(name = "READ_NUM", columnDefinition = "INT default 0")
  public Long getReadNum() {
    return readNum;
  }

  public void setReadNum(Long readNum) {
    this.readNum = readNum;
  }

  @Column(name = "DOWNLOAD_NUM", columnDefinition = "INT default 0")
  public Long getDownloadNum() {
    return downloadNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

  @Column(name = "TRANSFER_AMOUNT", columnDefinition = "INT default 0")
  public Double getTranAmount() {
    return tranAmount;
  }

  public void setTranAmount(Double tranAmount) {
    this.tranAmount = tranAmount;
  }

  @Column(name = "CITE_NUM", columnDefinition = "INT default 0")
  public Long getCiteNum() {
    return citeNum;
  }

  public void setCiteNum(Long citeNum) {
    this.citeNum = citeNum;
  }

  @Column(name = "SHARE_NUM", columnDefinition = "INT default 0")
  public Long getShareNum() {
    return shareNum;
  }

  public void setShareNum(Long shareNum) {
    this.shareNum = shareNum;
  }

  @Column(name = "AWARD_NUM", columnDefinition = "INT default 0")
  public Long getAwardNum() {
    return awardNum;
  }

  public void setAwardNum(Long awardNum) {
    this.awardNum = awardNum;
  }

  @Column(name = "TRANSFER_NUM", columnDefinition = "INT default 0")
  public Long getTranNum() {
    return tranNum;
  }

  public void setTranNum(Long tranNum) {
    this.tranNum = tranNum;
  }

}
