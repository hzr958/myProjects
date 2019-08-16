package com.smate.web.dyn.model.pdwhpub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author zjh 成果引用次数表
 *
 */
@Entity
@Table(name = "PDWH_PUB_CITED_TIMES")
public class PdwhPubCitedTimes implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2297723976704566732L;
  private Long pdwhPubId;// 成果id
  private Integer citedTimes;// 引用次数
  private Date updateDate;// 更新的日期
  private Integer type;// 手动更新1，后台更新0

  public PdwhPubCitedTimes() {
    super();
  }

  public PdwhPubCitedTimes(Long pdwhPubId, Integer citedTimes, Date updateDate, Integer type) {
    super();
    this.pdwhPubId = pdwhPubId;
    this.citedTimes = citedTimes;
    this.updateDate = updateDate;
    this.type = type;
  }

  @Id
  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "CITED_TIMES")
  public int getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "TYPE")
  public int getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }


}
