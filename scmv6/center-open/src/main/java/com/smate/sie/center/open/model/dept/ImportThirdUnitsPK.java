package com.smate.sie.center.open.model.dept;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 第三方部门信息主键.
 * 
 * @author xys
 *
 */
@Embeddable
public class ImportThirdUnitsPK implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6341739378205217741L;

  @Column(name = "UNIT_ID")
  private String unitId;// 部门ID
  @Column(name = "INS_ID")
  private Long insId;// 单位ID

  public ImportThirdUnitsPK() {
    super();
  }

  public ImportThirdUnitsPK(String unitId, Long insId) {
    super();
    this.unitId = unitId;
    this.insId = insId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
