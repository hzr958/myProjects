package com.smate.center.open.model.nsfc;

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
 * 
 */
@Entity
@Table(name = "NSFCROL2_SYS_USER_ROLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NsfcInsRole implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6162735366481144101L;
  private NsfcInsRoleId id;
  private String roleName;
  private List unitRoles;

  public NsfcInsRole() {
    super();
  }

  public NsfcInsRole(long userId, long insId, long rolId) {
    super();
    this.id = new NsfcInsRoleId(userId, insId, rolId);
  }

  public NsfcInsRole(NsfcInsRoleId id, String roleName) {
    super();
    this.id = id;
    this.roleName = roleName;
  }

  /**
   * @param id the id to set
   */
  public void setId(NsfcInsRoleId id) {
    this.id = id;
  }

  /**
   * @return the id
   */
  @Id
  @AttributeOverrides({@AttributeOverride(name = "insId", column = @Column(name = "INS_ID")),
      @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
      @AttributeOverride(name = "rolId", column = @Column(name = "ROLE_ID"))})
  public NsfcInsRoleId getId() {
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
