package com.smate.sie.core.base.utils.model.admin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.smate.sie.core.base.utils.model.psn.SieInsPersonPk;

/**
 * 
 * @author hd
 *
 */
@Embeddable
public class SieInsUnitAdminPk implements Serializable {


  private static final long serialVersionUID = 7101551021368492024L;

  private Long insId;

  private Long unitId;


  public SieInsUnitAdminPk() {
    super();
  }



  public SieInsUnitAdminPk(Long insId, Long unitId) {
    super();
    this.insId = insId;
    this.unitId = unitId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SieInsUnitAdminPk other = (SieInsUnitAdminPk) obj;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (unitId == null) {
      if (other.unitId != null)
        return false;
    } else if (!unitId.equals(other.unitId))
      return false;
    return true;
  }


}
