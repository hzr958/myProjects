package com.smate.core.base.utils.model.egtexpert;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.smate.core.base.utils.model.IdEntity;


/**
 * 权限.
 * 
 * 
 */
/**
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "SYS_AUTHORITIE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EgtExpertAuthority extends IdEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 3513834520615138884L;
  private String name;
  private String displayName;
  private Set<EgtExpertResource> resources = new LinkedHashSet<EgtExpertResource>(); // 对应权限下的资源
  private Set<EgtExpertRole> roles = new LinkedHashSet<EgtExpertRole>();;

  /**
   * 
   * @return name.
   * 
   */
  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  /**
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return
   */
  @Column(name = "DISPLAY_NAME")
  public String getDisplayName() {
    return displayName;
  }

  /**
   * @param displayName
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * 
   * @return
   * 
   */
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "SYS_RESOURCE_AUTHORITIE", joinColumns = {@JoinColumn(name = "AUTHORITY_ID")},
      inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID")})
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  public Set<EgtExpertResource> getResources() {
    return resources;
  }

  /**
   * @param resources
   */
  public void setResources(Set<EgtExpertResource> resources) {
    this.resources = resources;
  }

  // 多对多定义，cascade操作避免定义CascadeType.REMOVE
  /**
   * @return
   */
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  // 中间表定义,表名采用自定义命名规则
  @JoinTable(name = "SYS_ROLE_AUTHORITIE", joinColumns = {@JoinColumn(name = "AUTHORITY_ID")},
      inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
  // Fecth策略定义
  @Fetch(FetchMode.SUBSELECT)
  // 集合按id排序.
  @OrderBy("id")
  // 集合中对象id的缓存.
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public Set<EgtExpertRole> getRoles() {

    return roles;
  }

  /**
   * @param roles
   */
  public void setRoles(Set<EgtExpertRole> roles) {
    this.roles = roles;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
