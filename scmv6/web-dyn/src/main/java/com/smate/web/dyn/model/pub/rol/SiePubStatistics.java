package com.smate.web.dyn.model.pub.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SIE成果统计.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "SIE_PUB_STATISTICS")
public class SiePubStatistics implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3995375485968449657L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;// 被阅读的成果id
  @Column(name = "READ_NUM")
  private Long readNum = 0l;// 阅读次数
  @Column(name = "AWARD_NUM")
  private Long awardNum = 0l;// 赞次数
  @Column(name = "DOWNLOAD_NUM")
  private Long downloadNum = 0l;// 下载次数

  public SiePubStatistics() {
    super();
  }

  public SiePubStatistics(Long pubId) {
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

  public Long getDownloadNum() {
    return downloadNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

}
