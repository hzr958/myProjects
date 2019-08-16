package com.smate.center.merge.model.sns.person;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员信息统计表.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PSN_STATISTICS")
public class PsnStatistics implements Serializable {
  private static final long serialVersionUID = -5138572151813757305L;
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PUB_SUM")
  private Integer pubSum = 0;
  @Column(name = "CITED_SUM")
  private Integer citedSum = 0;
  @Column(name = "HINDEX")
  private Integer hindex = 0;
  @Column(name = "ZH_SUM")
  private Integer zhSum = 0;
  @Column(name = "EN_SUM")
  private Integer enSum = 0;
  @Column(name = "PRJ_SUM")
  private Integer prjSum = 0;
  @Column(name = "FRD_SUM")
  private Integer frdSum = 0;
  @Column(name = "GROUP_SUM")
  private Integer groupSum = 0;
  @Column(name = "PUB_AWARD_SUM")
  private Integer pubAwardSum = 0;
  @Column(name = "PATENT_SUM")
  private Integer patentSum = 0;
  // 待认领成果数
  @Column(name = "PCFPUB_SUM")
  private Integer pcfPubSum = 0;
  // 成果全文推荐总数_MJG_SCM-5991.
  @Column(name = "PUB_FULLTEXT_SUM")
  private Integer pubFullTextSum = 0;
  // 公开成果数
  @Column(name = "OPEN_PUB_SUM")
  private Integer openPubSum = 0;
  // 公开项目数
  @Column(name = "OPEN_PRJ_SUM")
  private Integer openPrjSum = 0;
  // 访问（阅读）数
  @Column(name = "VISIT_SUM")
  private Integer visitSum = 0;

  public PsnStatistics() {
    super();
  }

  /**
   * 全部参数构造函数. 2018年9月18日
   */
  public PsnStatistics(Long psnId, Integer pubSum, Integer citedSum, Integer hindex, Integer zhSum, Integer enSum,
      Integer prjSum, Integer frdSum, Integer groupSum, Integer pubAwardSum, Integer patentSum, Integer pcfPubSum,
      Integer pubFullTextSum, Integer openPubSum, Integer openPrjSum, Integer visitSum) {
    super();
    this.psnId = psnId;
    this.pubSum = pubSum;
    this.citedSum = citedSum;
    this.hindex = hindex;
    this.zhSum = zhSum;
    this.enSum = enSum;
    this.prjSum = prjSum;
    this.frdSum = frdSum;
    this.groupSum = groupSum;
    this.pubAwardSum = pubAwardSum;
    this.patentSum = patentSum;
    this.pcfPubSum = pcfPubSum;
    this.pubFullTextSum = pubFullTextSum;
    this.openPubSum = openPubSum;
    this.openPrjSum = openPrjSum;
    this.visitSum = visitSum;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getCitedSum() {
    return citedSum;
  }

  public void setCitedSum(Integer citedSum) {
    this.citedSum = citedSum;
  }

  public Integer getHindex() {
    return hindex;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex;
  }

  public Integer getZhSum() {
    return zhSum;
  }

  public void setZhSum(Integer zhSum) {
    this.zhSum = zhSum;
  }

  public Integer getEnSum() {
    return enSum;
  }

  public void setEnSum(Integer enSum) {
    this.enSum = enSum;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getFrdSum() {
    return frdSum;
  }

  public void setFrdSum(Integer frdSum) {
    this.frdSum = frdSum;
  }

  public Integer getGroupSum() {
    return groupSum;
  }

  public void setGroupSum(Integer groupSum) {
    this.groupSum = groupSum;
  }

  public Integer getPubAwardSum() {
    return pubAwardSum;
  }

  public void setPubAwardSum(Integer pubAwardSum) {
    this.pubAwardSum = pubAwardSum;
  }

  public Integer getPatentSum() {
    return patentSum;
  }

  public void setPatentSum(Integer patentSum) {
    this.patentSum = patentSum;
  }

  public Integer getPcfPubSum() {
    return pcfPubSum;
  }

  public void setPcfPubSum(Integer pcfPubSum) {
    this.pcfPubSum = pcfPubSum;
  }

  public Integer getPubFullTextSum() {
    return pubFullTextSum;
  }

  public void setPubFullTextSum(Integer pubFullTextSum) {
    this.pubFullTextSum = pubFullTextSum;
  }

  public Integer getOpenPubSum() {
    return openPubSum;
  }

  public void setOpenPubSum(Integer openPubSum) {
    this.openPubSum = openPubSum;
  }

  public Integer getOpenPrjSum() {
    return openPrjSum;
  }

  public void setOpenPrjSum(Integer openPrjSum) {
    this.openPrjSum = openPrjSum;
  }

  public Integer getVisitSum() {
    return visitSum;
  }

  public void setVisitSum(Integer visitSum) {
    this.visitSum = visitSum;
  }
}
