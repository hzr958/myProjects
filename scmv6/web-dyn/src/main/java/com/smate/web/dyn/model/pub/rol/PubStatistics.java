package com.smate.web.dyn.model.pub.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果统计.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "PUB_STATISTICS")
public class PubStatistics implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5566871438112034066L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;// 被阅读的成果id
  @Column(name = "READ_NUM")
  private Long readNum = 0l;// 阅读次数
  @Column(name = "AWARD_NUM")
  private Long awardNum = 0l;// 赞次数
  @Column(name = "SHARE_NUM")
  private Long shareNum = 0l;// 分享次数
  @Column(name = "DOWNLOAD_NUM")
  private Long downloadNum;// 下载量

  public PubStatistics(Long pubId, Long readNum, Long awardNum, Long shareNum, Long downloadNum) {
    super();
    this.pubId = pubId;
    this.readNum = readNum;
    this.awardNum = awardNum;
    this.shareNum = shareNum;
    this.downloadNum = downloadNum;
  }

  public PubStatistics() {
    super();
  }

  public PubStatistics(Long pubId) {
    super();
    this.pubId = pubId;
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

  public Long getAwardNum() {
    return awardNum;
  }

  public void setAwardNum(Long awardNum) {
    this.awardNum = awardNum;
  }

  public Long getShareNum() {
    return shareNum;
  }

  public void setShareNum(Long shareNum) {
    this.shareNum = shareNum;
  }

  public Long getDownloadNum() {
    return downloadNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

}
