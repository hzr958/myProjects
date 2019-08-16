package com.smate.sie.core.base.utils.model.admin;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 部门管理员
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "INS_UNIT_ADMIN")
public class SieInsUnitAdmin {

  private SieInsUnitAdminPk pk;

  private Long psnId;


  public SieInsUnitAdmin() {
    super();
  }


  public SieInsUnitAdmin(SieInsUnitAdminPk pk, Long psnId) {
    super();
    this.pk = pk;
    this.psnId = psnId;
  }

  public SieInsUnitAdmin(Long insId, Long unitId, Long psnId) {
    super();
    this.pk = new SieInsUnitAdminPk(insId, unitId);
    this.psnId = psnId;
  }


  @EmbeddedId
  public SieInsUnitAdminPk getPk() {
    return pk;
  }

  public void setPk(SieInsUnitAdminPk pk) {
    this.pk = pk;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }



}
