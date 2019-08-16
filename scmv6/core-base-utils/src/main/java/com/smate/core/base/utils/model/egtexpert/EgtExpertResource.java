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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.smate.core.base.utils.common.ReflectionUtils;
import com.smate.core.base.utils.model.IdEntity;


/**
 * 受保护的资源. 链接权限配置
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "SYS_RESOURCE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EgtExpertResource extends IdEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 7860993998110304877L;
  public static final String QUERY_BY_URL_TYPE = "FROM Resource WHERE resourceType=? ORDER BY orderNum ASC";
  public static final String QUERY_BY_MENU_TYPE =
      "FROM Resource WHERE resourceType=? AND parentId=? ORDER BY orderNum ASC";

  // resourceType常量
  public static final String URL_TYPE = "url";
  public static final String MENU_TYPE = "menu";

  private String resourceType; // 资源类型
  private String name; // 资源名称
  private String value; // 资源标识

  private int orderNum; // 资源排序字段

  private Integer parentId; // 上级菜单
  private String target;// 菜单目标
  private int lavels;// 菜单层次

  private String authNames;

  private Integer status;

  private Set<EgtExpertAuthority> authorities = new LinkedHashSet<EgtExpertAuthority>(); // 可访问该资源的授权

  @Column(name = "RESOURCE_TYPE")
  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  @Column(name = "VALUE")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /**
   * 
   * @return 菜单名称
   */
  @Column(name = "NAME")
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "ORDER_NUM")
  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  @Column(name = "TARGET")
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * @param 设置上级菜单ID
   */
  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  /**
   * @return 上级菜单ID
   */
  @Column(name = "PARENT_ID")
  public Integer getParentId() {
    return parentId;
  }

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "SYS_RESOURCE_AUTHORITIE", joinColumns = {@JoinColumn(name = "RESOURCE_ID")},
      inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID")})
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  public Set<EgtExpertAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<EgtExpertAuthority> authorities) {
    this.authorities = authorities;
  }

  @Transient
  public String getAuthNames() {
    return this.authNames;
  }

  public void setAuthNames() {
    this.authNames = ReflectionUtils.convertElementPropertyToString(authorities, "name", ",");
  }

  public void setLavels(int lavels) {
    this.lavels = lavels;
  }

  @Column(name = "LAVELS")
  public int getLavels() {
    return lavels;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
