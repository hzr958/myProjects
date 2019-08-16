package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果基金标注实体.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "PUB_FUNDINFO")
public class PubFundinfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4504547604486267248L;
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;// 成果id
  @Column(name = "OWNER_PSN_ID")
  private Long psnId;// 成果所有者id;
  @Column(name = "PUB_TYPE")
  private Integer typeId;// 成果类型 const_pub_type
  @Column(name = "FUNDINFO")
  private String fundinfo;// 基金标注

  public PubFundinfo() {
    super();
  }

  public PubFundinfo(Long pubId, Long psnId, Integer typeId, String fundinfo) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.typeId = typeId;
    this.fundinfo = fundinfo;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public String getFundinfo() {
    return fundinfo;
  }

  public void setFundinfo(String fundinfo) {
    this.fundinfo = fundinfo;
  }

}
