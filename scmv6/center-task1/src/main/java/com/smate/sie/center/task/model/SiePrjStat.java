package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author ztg
 * @since 2018年7月23日
 * @descript 项目统计
 */
@Entity
@Table(name = "ST_PRJ")
public class SiePrjStat {
  @Id
  @Column(name = "prj_id")
  private Long prjId;
  @Column(name = "pub_sum") // 成果数
  private Integer pubSum = 0;//
  // 阅读
  @Column(name = "READ_NUM")
  private Long readNum = 0L;
  // 下载
  @Column(name = "DOWNLOAD_NUM")
  private Long downloadNum = 0L;
  // 引用
  @Column(name = "CITE_NUM")
  private Long citeNum = 0L;

  @Column(name = "SHARE_NUM")
  private Long shareNum = 0L;

  @Column(name = "AWARD_NUM") // 赞数
  private Long awardNum = 0L;

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
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
