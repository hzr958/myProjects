package com.smate.center.batch.service.pub.mq;


/**
 * 获取基准库文献ID消息.
 * 
 * @author liqinghua
 * 
 */
public class GetPdwhIdMessage {

  /**
   * 
   */
  private static final long serialVersionUID = -2463062468731145002L;
  // 成果id
  private Long pubId;
  // 人员ID
  private Long psnId;
  // 单位ID
  private Long insId;
  // 文献库id
  private Integer dbId;
  // 标题hash
  private Long titleHash;
  // 专利号id
  private Long patentHash;
  // 组合hash
  private Long unitHash;
  // isi、scopus、ei库id
  private Long sourceIdHash;

  public GetPdwhIdMessage() {
    super();
  }

  public GetPdwhIdMessage(Long pubId, Long psnId, Long insId, Integer dbId, Long titleHash, Long patentHash,
      Long unitHash, Long sourceIdHash, Integer fromNodeId) {
    this.pubId = pubId;
    this.psnId = psnId;
    this.insId = insId;
    this.dbId = dbId;
    this.titleHash = titleHash;
    this.patentHash = patentHash;
    this.unitHash = unitHash;
    this.sourceIdHash = sourceIdHash;
  }

  public GetPdwhIdMessage(Long pubId, Long psnId, Integer dbId, Long titleHash, Long patentHash, Long unitHash,
      Long sourceIdHash, Integer fromNodeId) {
    this.pubId = pubId;
    this.psnId = psnId;
    this.dbId = dbId;
    this.titleHash = titleHash;
    this.patentHash = patentHash;
    this.unitHash = unitHash;
    this.sourceIdHash = sourceIdHash;
  }

  public Integer getDbId() {
    return dbId;
  }

  public Long getTitleHash() {
    return titleHash;
  }

  public Long getPatentHash() {
    return patentHash;
  }

  public Long getUnitHash() {
    return unitHash;
  }

  public Long getSourceIdHash() {
    return sourceIdHash;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setPatentHash(Long patentHash) {
    this.patentHash = patentHash;
  }

  public void setUnitHash(Long unitHash) {
    this.unitHash = unitHash;
  }

  public void setSourceIdHash(Long sourceIdHash) {
    this.sourceIdHash = sourceIdHash;
  }

  public Long getPubId() {
    return pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
