package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果更新.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KPI_REFRESH_PUB")
public class KpiRefreshPub implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 4754288606825207122L;
  private Long pubId;
  // 是否是删除成果
  private Integer isDel = 0;
  // 最后更新时间
  private Date lastDate;

  public KpiRefreshPub() {
    super();
  }

  public KpiRefreshPub(Long pubId) {
    super();
    this.pubId = pubId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "IS_DEL")
  public Integer getIsDel() {
    return isDel;
  }

  @Column(name = "LAST_DATE")
  public Date getLastDate() {
    return lastDate;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

}
