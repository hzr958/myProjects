package com.smate.web.management.model.institution.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 对比设置.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "KPI_INS_SETTING")
public class KpiInsSetting implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2665260299360929374L;
  private Long insId;
  private Integer statOpen;
  private Integer contOpen;

  public KpiInsSetting() {
    super();
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "STAT_OPEN")
  public Integer getStatOpen() {
    return statOpen;
  }

  public void setStatOpen(Integer statOpen) {
    this.statOpen = statOpen;
  }

  @Column(name = "CONT_OPEN")
  public Integer getContOpen() {
    return contOpen;
  }

  public void setContOpen(Integer contOpen) {
    this.contOpen = contOpen;
  }
}
