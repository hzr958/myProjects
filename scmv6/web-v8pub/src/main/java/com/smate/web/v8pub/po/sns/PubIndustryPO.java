package com.smate.web.v8pub.po.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SNS库成果科技领域
 * 
 * @author YJ
 *
 *         2018年8月6日
 */
@Entity
@Table(name = "V_PUB_INDUSTRY")
public class PubIndustryPO implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -6633637365545605549L;

  @Id
  @SequenceGenerator(name = "SEQ_PUB_INDUSTRY_ID", sequenceName = "SEQ_PUB_INDUSTRY_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_INDUSTRY_ID")

  @Column(name = "ID")
  private Long id; // 逻辑主键ID

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "INDUSTRY_CODE")
  private String industryCode; // 产业代码


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }



  public String getIndustryCode() {
    return industryCode;
  }

  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

  @Override
  public String toString() {
    return "PubIndustryPO{" + "pubId=" + pubId + ", industryCode=" + industryCode + '}';
  }
}
