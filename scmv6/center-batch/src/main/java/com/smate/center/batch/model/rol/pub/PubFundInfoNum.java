package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果项目信息，抽取出连续数字最少5位，最大18位（long值最大9223372036854775）.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_FUNDINFO_NUM")
public class PubFundInfoNum implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2704933999175783546L;

  private Long id;
  private Long pubId;
  private Long insId;
  private Long fundNum;

  public PubFundInfoNum() {
    super();
  }

  public PubFundInfoNum(Long pubId, Long insId, Long fundNum) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.fundNum = fundNum;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_FUNDINFO_NUM", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "FUND_NUM")
  public Long getFundNum() {
    return fundNum;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setFundNum(Long fundNum) {
    this.fundNum = fundNum;
  }

}
