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
@Table(name = "V_PUB_SCIENCE_AREA")
public class PubScienceAreaPO implements Serializable {

  private static final long serialVersionUID = -2550269580485384201L;

  @Id
  @SequenceGenerator(name = "SEQ_PUB_SCIENCE_AREA_ID", sequenceName = "SEQ_PUB_SCIENCE_AREA_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_SCIENCE_AREA_ID")

  @Column(name = "ID")
  private Long id; // 逻辑主键ID

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "SCIENCE_AREA_ID")
  private Long scienceAreaId; // 科技领域id


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

  public Long getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Long scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  @Override
  public String toString() {
    return "PubScienceAreaPO{" + "pubId='" + pubId + '\'' + ", scienceAreaId='" + scienceAreaId + '\'' + '}';
  }
}
