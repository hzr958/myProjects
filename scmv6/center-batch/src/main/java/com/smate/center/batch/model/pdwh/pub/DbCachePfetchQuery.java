package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 在线导入成果，查询基准库ID消息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "DBCACHE_PFETCH_QUERY")
public class DbCachePfetchQuery implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4542547270440742225L;

  private Long id;
  private Long pubId;
  private Long psnId;
  private Long insId;
  private Integer fromNodeId = 1;
  // 网站ID
  private Integer dbId;
  // " zh_title|en_title"组合，生成的hash标识
  private Long titleHash;
  // "year| original| author"组合，生成的hash标识
  private Long unitHash;
  // PATENT_NO的hash标识
  private Long patentHash;
  // Source_id的hash
  private Long sourceIdHash;
  // 创建时间
  private Date createTime = new Date();
  // 查询次数，最多5次
  private Integer queryNum = 0;
  // 查询到的基准库ID
  private Long queryId;
  // 0等待查询，1已查询到，9查询不到（查询次数超过3次）
  private Integer result = 0;
  // 是否发送
  private Integer isSend = 0;

  public DbCachePfetchQuery() {
    super();
  }

  public DbCachePfetchQuery(Long pubId, Long psnId, Long insId, Integer dbId, Long titleHash, Long unitHash,
      Long patentHash, Long sourceIdHash) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.insId = insId;
    this.dbId = dbId;
    this.titleHash = titleHash;
    this.unitHash = unitHash;
    this.patentHash = patentHash;
    this.sourceIdHash = sourceIdHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DBCACHE_PFETCH_QUERY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "FROM_NODE_ID")
  public Integer getFromNodeId() {
    return fromNodeId;
  }

  @Column(name = "DBID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  @Column(name = "UNIT_HASH")
  public Long getUnitHash() {
    return unitHash;
  }

  @Column(name = "PATENT_HASH")
  public Long getPatentHash() {
    return patentHash;
  }

  @Column(name = "SOURCE_ID_HASH")
  public Long getSourceIdHash() {
    return sourceIdHash;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  @Column(name = "QUERY_NUM")
  public Integer getQueryNum() {
    return queryNum;
  }

  @Column(name = "QUERY_ID")
  public Long getQueryId() {
    return queryId;
  }

  @Column(name = "RESULT")
  public Integer getResult() {
    return result;
  }

  @Column(name = "IS_SEND")
  public Integer getIsSend() {
    return isSend;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setFromNodeId(Integer fromNodeId) {
    this.fromNodeId = fromNodeId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setUnitHash(Long unitHash) {
    this.unitHash = unitHash;
  }

  public void setPatentHash(Long patentHash) {
    this.patentHash = patentHash;
  }

  public void setSourceIdHash(Long sourceIdHash) {
    this.sourceIdHash = sourceIdHash;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public void setQueryNum(Integer queryNum) {
    this.queryNum = queryNum;
  }

  public void setQueryId(Long queryId) {
    this.queryId = queryId;
  }

  public void setResult(Integer result) {
    this.result = result;
  }

  public void setIsSend(Integer isSend) {
    this.isSend = isSend;
  }

}
