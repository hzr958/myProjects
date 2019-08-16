package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 高校所在地表，当常量表使用.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "INS_REGION")
public class InsRegion implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -1387722413411951646L;
  private Long insId;
  // 机构省份ID（region_id）
  private Long prvId;
  // 机构市级地区ID（region_id）
  private Long cyId;
  // 机构市级地区辖区管理ID（例如南山区）
  private Long disId;

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

}
