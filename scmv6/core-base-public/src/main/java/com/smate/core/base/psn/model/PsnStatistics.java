package com.smate.core.base.psn.model;

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

  public PsnStatistics(Long psnId, Integer hindex) {
    this.psnId = psnId;
    this.hindex = hindex;
  }

  public PsnStatistics(Integer pubSum, Integer prjSum) {
    super();
    this.pubSum = pubSum;
    this.prjSum = prjSum;
  }

  public PsnStatistics(Integer pubSum, Integer prjSum, Integer openPubSum, Integer openPrjSum) {
    super();
    this.pubSum = pubSum;
    this.prjSum = prjSum;
    this.openPubSum = openPubSum;
    this.openPrjSum = openPrjSum;
  }

  public PsnStatistics(Integer pubSum, Integer prjSum, Integer patentSum) {
    super();
    this.pubSum = pubSum;
    this.prjSum = prjSum;
    this.patentSum = patentSum;
  }

  public PsnStatistics(Long psnId, Integer citedSum, Integer hindex, Integer visitSum, Integer friendSum) {
    super();
    this.psnId = psnId;
    this.citedSum = citedSum;
    this.hindex = hindex;
    this.visitSum = visitSum;
    this.frdSum = friendSum;
  }

  public PsnStatistics(Long psnId, Integer citedSum, Integer hindex, Integer visitSum, Integer friendSum,
      Integer pubAwardSum) {
    super();
    this.psnId = psnId;
    this.citedSum = citedSum;
    this.hindex = hindex;
    this.visitSum = visitSum;
    this.frdSum = friendSum;
    this.pubAwardSum = pubAwardSum;
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
