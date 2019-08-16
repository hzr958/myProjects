package com.smate.center.batch.service.pub.mq;

/**
 * 人员统计信息JMS model.
 * 
 * @author zk
 * 
 */
public class PsnStatisticsMessage {

  /**
   * 
   */
  private Long psnId;
  private Integer pubSum;
  private Integer citedSum;
  private Integer hindex;
  private Integer zhSum;
  private Integer enSum;
  private Integer prjSum;
  private Integer frdSum = 0;
  private Integer groupSum = 0;
  private Integer pubAwardSum = 0;
  private Integer patentSum = 0;
  // 待认领成果数
  private Integer pcfPubSum = 0;
  // 成果全文推荐总数_MJG_SCM-5991.
  private Integer pubFullTextSum = 0;
  // 服务器节点
  private Integer fromNodeId = 1;

  public Long getPsnId() {
    return psnId;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public Integer getCitedSum() {
    return citedSum;
  }

  public Integer getHindex() {
    return hindex;
  }

  public Integer getZhSum() {
    return zhSum;
  }

  public Integer getEnSum() {
    return enSum;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public void setCitedSum(Integer citedSum) {
    this.citedSum = citedSum;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex;
  }

  public void setZhSum(Integer zhSum) {
    this.zhSum = zhSum;
  }

  public void setEnSum(Integer enSum) {
    this.enSum = enSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getFrdSum() {
    return frdSum;
  }

  public Integer getGroupSum() {
    return groupSum;
  }

  public void setFrdSum(Integer frdSum) {
    this.frdSum = frdSum;
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

  public Integer getFromNodeId() {
    return fromNodeId;
  }

  public void setFromNodeId(Integer fromNodeId) {
    this.fromNodeId = fromNodeId;
  }

}
