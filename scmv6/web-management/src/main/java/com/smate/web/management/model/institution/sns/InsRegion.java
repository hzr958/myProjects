package com.smate.web.management.model.institution.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 高校所在地表，当常量表使用.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "INS_REGION")
public class InsRegion implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 258328865523305390L;
  private Long insId;
  // 机构省份ID（region_id）
  private Long prvId;
  // 机构市级地区ID（region_id）
  private Long cyId;

  public InsRegion() {
    super();
  }

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
