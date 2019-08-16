package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 社交机器人配置信息表
 * 
 * @author 叶星源
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "ROBOT_MAN_CONFIG")
public class SieRobotManConfig implements Serializable {

  @Id
  @Column(name = "config_id")
  private Integer id; // 主键

  @Column(name = "robot_num")
  private Long robotNum; // 机器人数量
  @Column(name = "view_prj")
  private Long viewPrj; // 阅读项目
  @Column(name = "view_pub")
  private Long viewPub;// 阅读成果数
  @Column(name = "view_pat")
  private Long viewPat;// 阅读专利数
  @Column(name = "rate_read")
  private Long rateRead;// 阅读概率数
  @Column(name = "rate_share")
  private Long rateShare;// 分享概率数
  @Column(name = "rate_download")
  private Long rateDownload;// 下载概率数
  @Column(name = "rate_citation")
  private Long rateCitation;// 引用概率数
  @Column(name = "rate_index_share")
  private Long rateIndexShare;// 首页分享概率数
  // @Column(name = "status")
  // private Long status; // 状态，0不启用，1启用

  public SieRobotManConfig() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public Long getRobotNum() {
    return robotNum;
  }

  public Long getViewPrj() {
    return viewPrj;
  }

  public Long getViewPub() {
    return viewPub;
  }

  public Long getViewPat() {
    return viewPat;
  }

  public Long getRateRead() {
    return rateRead;
  }

  public Long getRateShare() {
    return rateShare;
  }

  public Long getRateDownload() {
    return rateDownload;
  }

  public Long getRateCitation() {
    return rateCitation;
  }

  public Long getRateIndexShare() {
    return rateIndexShare;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setRobotNum(Long robotNum) {
    this.robotNum = robotNum;
  }

  public void setViewPrj(Long viewPrj) {
    this.viewPrj = viewPrj;
  }

  public void setViewPub(Long viewPub) {
    this.viewPub = viewPub;
  }

  public void setViewPat(Long viewPat) {
    this.viewPat = viewPat;
  }

  public void setRateRead(Long rateRead) {
    this.rateRead = rateRead;
  }

  public void setRateShare(Long rateShare) {
    this.rateShare = rateShare;
  }

  public void setRateDownload(Long rateDownload) {
    this.rateDownload = rateDownload;
  }

  public void setRateCitation(Long rateCitation) {
    this.rateCitation = rateCitation;
  }

  public void setRateIndexShare(Long rateIndexShare) {
    this.rateIndexShare = rateIndexShare;
  }


  /*
   * public Long getStatus() { return status; }
   * 
   * public void setStatus(Long status) { this.status = status; }
   */
}
