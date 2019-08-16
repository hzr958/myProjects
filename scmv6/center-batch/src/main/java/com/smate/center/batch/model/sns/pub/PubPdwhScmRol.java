package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TMP_PUB_PDWH")
public class PubPdwhScmRol implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4979936278690326484L;

  private Long pubId;// snsPubId
  private Integer from;// 来源1 sns,2 rol
  private Integer status;
  // 成果处理状态：0.待处理，1.处理成功，4.没有获取到pub_pdwh表中数据，5.没有获取到基准库中对应成果id，6.对应publication表中数据为空
  private Integer matchCounts = 0;// 匹配次数，最多为5次

  public PubPdwhScmRol() {
    super();
  }

  public PubPdwhScmRol(Long pubId, Integer from, Integer status, Integer matchCounts) {
    super();
    this.pubId = pubId;
    this.from = from;
    this.status = status;
    this.matchCounts = matchCounts;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "PUB_FROM")
  public Integer getFrom() {
    return from;
  }

  public void setFrom(Integer from) {
    this.from = from;
  }

  @Column(name = "MATCH_COUNTS")
  public Integer getMatchCounts() {
    return matchCounts;
  }

  public void setMatchCounts(Integer matchCounts) {
    this.matchCounts = matchCounts;
  }

}
