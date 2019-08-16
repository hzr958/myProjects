package com.smate.core.base.utils.model.security;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 人员角色表 .
 * 
 * @author tsz
 * 
 */
@Embeddable
public class UserRoleId implements java.io.Serializable {

  private static final long serialVersionUID = 560960916213063043L;

  // Fields
  private long userId;
  private long rolId;

  public UserRoleId() {}

  public UserRoleId(long userId, long rolId) {
    this.userId = userId;
    this.rolId = rolId;
  }

  // Property accessors

  @Column(name = "USER_ID")
  public long getUserId() {
    return this.userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  // Constructors

  @Column(name = "ROLE_ID")
  public long getRolId() {
    return rolId;
  }

  public void setRolId(long rolId) {
    this.rolId = rolId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (rolId ^ (rolId >>> 32));
    result = prime * result + (int) (userId ^ (userId >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof UserRoleId))
      return false;
    UserRoleId castOther = (UserRoleId) other;

    return (this.getUserId() == castOther.getUserId());

  }

}
