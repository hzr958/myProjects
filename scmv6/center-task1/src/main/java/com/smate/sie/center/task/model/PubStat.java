package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果统计表
 * 
 * @author ztg
 */
@Entity
@Table(name = "ST_PUB")
public class PubStat implements Serializable {

  private static final long serialVersionUID = -7213899916197158857L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  // 阅读
  @Column(name = "READ_NUM", columnDefinition = "INT default 0")
  private Long readNum = 0L;
  // 下载
  @Column(name = "DOWNLOAD_NUM", columnDefinition = "INT default 0")
  private Long downloadNum = 0L;
  // 引用
  @Column(name = "CITE_NUM", columnDefinition = "INT default 0")
  private Long citeNum = 0L;

  @Column(name = "SHARE_NUM", columnDefinition = "INT default 0")
  private Long shareNum = 0L;

  @Column(name = "AWARD_NUM", columnDefinition = "INT default 0")
  private Long awardNum = 0L;


  public PubStat() {
    super();
  }

  public PubStat(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public PubStat(Long pubId, Long readNum, Long downloadNum, Long citeNum, Long shareNum, Long awardNum) {
    super();
    this.pubId = pubId;
    this.readNum = readNum;
    this.downloadNum = downloadNum;
    this.citeNum = citeNum;
    this.shareNum = shareNum;
    this.awardNum = awardNum;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getReadNum() {
    return readNum;
  }

  public void setReadNum(Long readNum) {
    this.readNum = readNum;
  }

  public Long getDownloadNum() {
    return downloadNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

  public Long getCiteNum() {
    return citeNum;
  }

  public void setCiteNum(Long citeNum) {
    this.citeNum = citeNum;
  }

  public Long getShareNum() {
    return shareNum;
  }

  public void setShareNum(Long shareNum) {
    this.shareNum = shareNum;
  }

  public Long getAwardNum() {
    return awardNum;
  }

  public void setAwardNum(Long awardNum) {
    this.awardNum = awardNum;
  }


}
