package com.smate.core.base.utils.model.security;

import java.io.Serializable;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 单位角色.
 * 
 * 使用JPA annotation定义ORM关系. 使用Hibernate annotation定义二级缓存.
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "SYS_USER_ROLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InsRole implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6162735366481144101L;
  private InsRoleId id;
  private String roleName;
  private List unitRoles;

  public InsRole() {
    super();
  }

  public InsRole(long userId, long insId, long rolId) {
    super();
    this.id = new InsRoleId(userId, insId, rolId);
  }

  public InsRole(InsRoleId id, String roleName) {
    super();
    this.id = id;
    this.roleName = roleName;
  }

  /**
   * @param id the id to set
   */
  public void setId(InsRoleId id) {
    this.id = id;
  }

  /**
   * @return the id
   */
  @Id
  @AttributeOverrides({@AttributeOverride(name = "insId", column = @Column(name = "INS_ID")),
      @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
      @AttributeOverride(name = "rolId", column = @Column(name = "ROLE_ID"))})
  public InsRoleId getId() {
    return id;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Transient
  public String getRoleName() {
    return roleName;
  }

  /**
   * @return the unitRoles
   */
  @Transient
  public List getUnitRoles() {
    return unitRoles;
  }

  /**
   * @param unitRoles the unitRoles to set
   */
  public void setUnitRoles(List unitRoles) {
    this.unitRoles = unitRoles;
  }

}
