package com.smate.core.base.utils.model.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.smate.core.base.utils.model.IdEntity;

/**
 * 受保护的资源映射. 链接权限
 * 
 * @author mjg
 * @since 2014-07-23.
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "SYS_RESOURCE_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceMapping extends IdEntity {

  private static final long serialVersionUID = 1L;
  private Long resourceId;// 资源ID.
  private Long roleId;// 角色ID.
  private String url;// 链接地址.

  @Column(name = "RESOURCE_ID")
  public Long getResourceId() {
    return resourceId;
  }

  public void setResourceId(Long resourceId) {
    this.resourceId = resourceId;
  }

  @Column(name = "ROLE_ID")
  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
