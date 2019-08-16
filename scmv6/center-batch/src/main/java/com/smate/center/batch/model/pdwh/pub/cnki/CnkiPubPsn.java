package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果人员关系表(对科研之友成果关系进行冗余).
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "CNKI_PUB_PSN")
public class CnkiPubPsn implements Serializable {

  private static final long serialVersionUID = 9102973536139077586L;
  private Long id;// 地址ID.
  private Long pubId;// 成果ID.
  private Long psnId;// 人员ID.
  private Long source;// 数据来源：1-基准库自动匹配；2-ROL匹配；3-个人在线导入.
  private Long score;// 对于ROL匹配到的，有分数，推荐合作者的时候可以进行排序.
  private Integer result;// 匹配结果：0-待确认；1-已确认；2-拒绝.

  public CnkiPubPsn() {
    super();
  }

  public CnkiPubPsn(Long pubId, Integer result) {
    super();
    this.pubId = pubId;
    this.result = result;
  }

  public CnkiPubPsn(Long id, Long pubId, Long psnId, Long source, Long score, Integer result) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.psnId = psnId;
    this.source = source;
    this.score = score;
    this.result = result;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_PSN", allocationSize = 1)
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

  @Column(name = "SOURCE")
  public Long getSource() {
    return source;
  }

  @Column(name = "SCORE")
  public Long getScore() {
    return score;
  }

  @Column(name = "RESULT")
  public Integer getResult() {
    return result;
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

  public void setSource(Long source) {
    this.source = source;
  }

  public void setScore(Long score) {
    this.score = score;
  }

  public void setResult(Integer result) {
    this.result = result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CnkiPubPsn other = (CnkiPubPsn) obj;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (pubId == null) {
      if (other.pubId != null)
        return false;
    } else if (!pubId.equals(other.pubId))
      return false;
    return true;
  }

}
