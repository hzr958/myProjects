package com.smate.center.task.model.sns.psn;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ETEMPLATE_INFLUENCE_COUNT")
public class ETemplateInfluenceCount implements Serializable {

  /**
   * 科研影响力
   */
  private static final long serialVersionUID = -3221079783093382952L;
  private Long psnId;
  private Long readCount;
  private Long monthReadCount;
  private Long pubCount;
  private Long monthPubCount;
  private Long awardCount;
  private Long monthAwardCount;
  private Long downloadCount;
  private Long monthDownloadCount;
  private Long shareCount;
  private Long monthShareCount;
  private Long citedTimesCount;
  private Long monthCitedTimesCount;
  private Integer hindex;
  private Integer status;
  private Date createDate;

  @Id
  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "read_count  ")
  public Long getReadCount() {
    return readCount;
  }

  public void setReadCount(Long readCount) {
    this.readCount = readCount;
  }

  @Column(name = "month_read_count")
  public Long getMonthReadCount() {
    return monthReadCount;
  }

  public void setMonthReadCount(Long monthReadCount) {
    this.monthReadCount = monthReadCount;
  }

  @Column(name = "pub_count")
  public Long getPubCount() {
    return pubCount;
  }

  public void setPubCount(Long pubCount) {
    this.pubCount = pubCount;
  }

  @Column(name = "month_pub_count")
  public Long getMonthPubCount() {
    return monthPubCount;
  }

  public void setMonthPubCount(Long monthPubCount) {
    this.monthPubCount = monthPubCount;
  }

  @Column(name = "award_count")
  public Long getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Long awardCount) {
    this.awardCount = awardCount;
  }

  @Column(name = "month_award_count")
  public Long getMonthAwardCount() {
    return monthAwardCount;
  }

  public void setMonthAwardCount(Long monthAwardCount) {
    this.monthAwardCount = monthAwardCount;
  }

  @Column(name = "download_count")
  public Long getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(Long downloadCount) {
    this.downloadCount = downloadCount;
  }

  @Column(name = "month_download_count")
  public Long getMonthDownloadCount() {
    return monthDownloadCount;
  }

  public void setMonthDownloadCount(Long monthDownloadCount) {
    this.monthDownloadCount = monthDownloadCount;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "create_date")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "share_count")
  public Long getShareCount() {
    return shareCount;
  }

  public void setShareCount(Long shareCount) {
    this.shareCount = shareCount;
  }

  @Column(name = "month_share_count")
  public Long getMonthShareCount() {
    return monthShareCount;
  }

  public void setMonthShareCount(Long monthShareCount) {
    this.monthShareCount = monthShareCount;
  }

  @Column(name = "cited_times_count")
  public Long getCitedTimesCount() {
    return citedTimesCount;
  }

  public void setCitedTimesCount(Long citedTimesCount) {
    this.citedTimesCount = citedTimesCount;
  }

  @Column(name = "month_cited_times_count")
  public Long getMonthCitedTimesCount() {
    return monthCitedTimesCount;
  }

  public void setMonthCitedTimesCount(Long monthCitedTimesCount) {
    this.monthCitedTimesCount = monthCitedTimesCount;
  }

  @Column(name = "hindex")
  public Integer getHindex() {
    return hindex;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex;
  }

}
