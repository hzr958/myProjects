package com.smate.web.dyn.model.pub.rol;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SNS同步成果统计.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "SNS_SYNC_PUB_STATISTICS")
public class SnsSyncPubStatistics implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6392670387407119068L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SNS_SYNC_PUB_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// PK
  @Column(name = "SIE_PUB_ID")
  private Long siePubId;// sie成果id
  @Column(name = "SNS_PUB_ID")
  private Long snsPubId;// sns成果id
  @Column(name = "READ_NUM")
  private Long readNum = 0l;// 阅读次数
  @Column(name = "AWARD_NUM")
  private Long awardNum = 0l;// 赞次数
  @Column(name = "SHARE_NUM")
  private Long shareNum = 0l;// 分享次数
  @Column(name = "DOWNLOAD_NUM")
  private Long downloadNum = 0l;// 下载次数

  public SnsSyncPubStatistics() {

  }

  public SnsSyncPubStatistics(Long siePubId, Long snsPubId) {
    this.siePubId = siePubId;
    this.snsPubId = snsPubId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSiePubId() {
    return siePubId;
  }

  public void setSiePubId(Long siePubId) {
    this.siePubId = siePubId;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
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
