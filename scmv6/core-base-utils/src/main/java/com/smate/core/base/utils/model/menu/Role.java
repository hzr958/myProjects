package com.smate.core.base.utils.model.menu;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.smate.core.base.utils.common.ReflectionUtils;
import com.smate.core.base.utils.model.IdEntity;

/**
 * 角色.
 * 
 * 使用JPA annotation定义ORM关系. 使用Hibernate annotation定义二级缓存.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "SYS_ROLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 8563096835601408349L;

  private String name;

  private String authNames;

  private Set<Authority> authorities = new LinkedHashSet<Authority>(); // 有序的关联对象集合

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "SYS_ROLE_AUTHORITIE", joinColumns = {@JoinColumn(name = "ROLE_ID")},
      inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID")})
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  @Transient
  public String getAuthNames() {
    return this.authNames;
  }

  public void setAuthNames() {
    this.authNames = ReflectionUtils.convertElementPropertyToString(authorities, "displayName", ", ");
  }

  @Transient
  @SuppressWarnings("unchecked")
  public List<Long> getAuthIds() {
    return ReflectionUtils.convertElementPropertyToList(authorities, "id");
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
