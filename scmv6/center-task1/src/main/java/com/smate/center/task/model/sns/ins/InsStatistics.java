package com.smate.center.task.model.sns.ins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 机构统计数表
 * 
 * @author wsn
 *
 */
@Entity
@Table(name = "V_INS_STATISTICS")
public class InsStatistics implements Serializable {

  private static final long serialVersionUID = -2343752767883022561L;

  private Integer pubSum;// 成果总数
  private Integer prjSum; // 项目总数
  private Integer psnSum; // 人员总数
  private Long insId; // 机构ID
  private Integer likeSum; // 赞总数
  private Integer shareSum; // 分享总数
  private String zhName; // 机构中文名称
  private String enName; // 机构英文名称

  public InsStatistics() {
    super();
  }

  public InsStatistics(Integer pubSum, Integer prjSum, Integer psnSum, Long insId, Integer likeSum, Integer shareSum,
      String zhName, String enName) {
    super();
    this.pubSum = pubSum;
    this.prjSum = prjSum;
    this.psnSum = psnSum;
    this.insId = insId;
    this.likeSum = likeSum;
    this.shareSum = shareSum;
    this.zhName = zhName;
    this.enName = enName;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "LIKE_SUM")
  public Integer getLikeSum() {
    return likeSum;
  }

  public void setLikeSum(Integer likeSum) {
    this.likeSum = likeSum;
  }

  @Column(name = "SHARE_SUM")
  public Integer getShareSum() {
    return shareSum;
  }

  public void setShareSum(Integer shareSum) {
    this.shareSum = shareSum;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "PUB_SUM")
  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  @Column(name = "PRJ_SUM")
  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  @Column(name = "PSN_SUM")
  public Integer getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(Integer psnSum) {
    this.psnSum = psnSum;
  }

}
