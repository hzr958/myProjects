package com.smate.core.base.utils.model.egtexpert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 单位人员角色表 .
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Embeddable
public class EgtExpertInsRoleId implements java.io.Serializable {

  private static final long serialVersionUID = 5609609162130630424L;

  // Fields
  private long userId;
  private long insId;
  private long roleId;

  public EgtExpertInsRoleId() {}

  public EgtExpertInsRoleId(long userId, long insId, long roleId) {
    this.userId = userId;
    this.insId = insId;
    this.roleId = roleId;
  }

  // Property accessors

  @Column(name = "USER_ID")
  public long getUserId() {
    return this.userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  @Column(name = "INS_ID")
  public long getInsId() {
    return this.insId;
  }

  public void setInsId(long insId) {
    this.insId = insId;
  }

  // Constructors

  @Column(name = "ROLE_ID")
  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (insId ^ (insId >>> 32));
    result = prime * result + (int) (roleId ^ (roleId >>> 32));
    result = prime * result + (int) (userId ^ (userId >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof EgtExpertInsRoleId))
      return false;
    EgtExpertInsRoleId castOther = (EgtExpertInsRoleId) other;

    return (this.getInsId() == castOther.getInsId() && this.getUserId() == castOther.getUserId());

  }

}
