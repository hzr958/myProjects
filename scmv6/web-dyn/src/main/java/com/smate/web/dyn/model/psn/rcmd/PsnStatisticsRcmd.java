package com.smate.web.dyn.model.psn.rcmd;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员信息统计表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_STATISTICS")
public class PsnStatisticsRcmd implements Serializable {

  /**
   * 
   */
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

  public PsnStatisticsRcmd() {}

  public PsnStatisticsRcmd(Long psnId, Integer hindex) {
    this.psnId = psnId;
    this.hindex = hindex;
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
}
