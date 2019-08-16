package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 清理任务提示，用户信息.
 * 
 * @author liqinghua
 * 
 */
public class ClearTaskNoticeUserInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5216246911269496127L;
  private Long insId;
  private Integer roleId;
  private Long unitId;

  public ClearTaskNoticeUserInfo() {
    super();
  }

  public ClearTaskNoticeUserInfo(Long insId, Integer roleId, Long unitId) {
    super();
    this.insId = insId;
    this.roleId = roleId;
    this.unitId = unitId;
  }

  public static ClearTaskNoticeUserInfo getInstance(Long insId, Integer roleId, Long unitId) {
    return new ClearTaskNoticeUserInfo(insId, roleId, unitId);
  }

  public Long getInsId() {
    return insId;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

}
