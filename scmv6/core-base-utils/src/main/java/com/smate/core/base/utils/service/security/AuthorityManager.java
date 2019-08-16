package com.smate.core.base.utils.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;


/**
 * 授权信息获得接口.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface AuthorityManager {

  /**
   * 获得用户所有角色的权限.按 INS获得.
   * 
   * @param userId
   */
  Collection<GrantedAuthority> obtainGrantedAuthorities(Long userId, Integer roleId, Long insId);

  /**
   * 通过域名获得人当前所在的单位.
   * 
   * @param 域名
   */
  long getCurrentInsId(String domain);

  /**
   * 通过 单位 id 获得所在域名.
   */
  public String getDomainByInsId(long insId);

  /**
   * 通过 单位 id 获得当前单位的域名.
   */
  // public String getIndexPageByInsId(long insId);

}
