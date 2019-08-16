package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果作者邮件表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "ISI_PUB_MATCH_EMAIL")
public class IsiPubMatchEmail implements Serializable {

  private static final long serialVersionUID = -8475081724907895783L;
  private Long id;// 主键.
  private Long pubId;// 成果ID.
  private String email;// 作者Email(小写).
  private Integer seqNo;// 用户序号.

  public IsiPubMatchEmail() {
    super();
  }

  public IsiPubMatchEmail(String email, Integer seqNo) {
    super();
    this.email = email;
    this.seqNo = seqNo;
  }

  public IsiPubMatchEmail(Long pubId, String email, Integer seqNo) {
    super();
    this.pubId = pubId;
    this.email = email;
    this.seqNo = seqNo;
  }

  public IsiPubMatchEmail(Long id, Long pubId, String email, Integer seqNo) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.email = email;
    this.seqNo = seqNo;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_MATCH_EMAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

}
