package com.smate.core.base.utils.model.rol;

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
public class SieInsRole implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6162735366481144101L;
  private SieInsRoleId id;
  private String roleName;
  private List unitRoles;

  public SieInsRole() {
    super();
  }

  public SieInsRole(long userId, long insId, long rolId) {
    super();
    this.id = new SieInsRoleId(userId, insId, rolId);
  }

  public SieInsRole(SieInsRoleId id, String roleName) {
    super();
    this.id = id;
    this.roleName = roleName;
  }

  /**
   * @param id the id to set
   */
  public void setId(SieInsRoleId id) {
    this.id = id;
  }

  /**
   * @return the id
   */
  @Id
  @AttributeOverrides({@AttributeOverride(name = "insId", column = @Column(name = "INS_ID")),
      @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
      @AttributeOverride(name = "rolId", column = @Column(name = "ROLE_ID"))})
  public SieInsRoleId getId() {
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
