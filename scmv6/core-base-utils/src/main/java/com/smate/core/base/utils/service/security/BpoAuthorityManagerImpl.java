package com.smate.core.base.utils.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.AuthorityDao;
import com.smate.core.base.utils.dao.security.bpo.BpoInsPortalDao;
import com.smate.core.base.utils.dao.security.bpo.BpoInsRoleDao;
import com.smate.core.base.utils.dao.security.bpo.BpoRoleDao;
import com.smate.core.base.utils.model.bpo.BpoInsPortal;
import com.smate.core.base.utils.model.menu.Authority;
import com.smate.core.base.utils.model.security.InsRole;



/**
 * 授权管理类.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("bpoAuthorityManager")
@Transactional(rollbackFor = Exception.class)
public class BpoAuthorityManagerImpl implements AuthorityManager {
  @Autowired
  private AuthorityDao authorityDao;

  @Autowired
  private BpoInsRoleDao insRoleDao;

  @Autowired
  private BpoRoleDao roleDao;

  @Autowired
  private BpoInsPortalDao insPortalDao;

  /**
   * 获得用户所有角色的权限.按 INS获得.
   */
  public Collection<GrantedAuthority> userInsGrantedAuthorities(long userId, Long insId) {
    Set<GrantedAuthority> grantedAuthoritys = new HashSet<GrantedAuthority>();

    List<InsRole> roles = insRoleDao.getInsRoleWithUserId(userId, insId);

    for (InsRole role : roles) {
      for (Authority authority : roleDao.get(role.getId().getRoleId()).getAuthorities()) {
        grantedAuthoritys.add(new SimpleGrantedAuthority(authority.getName()));
      }
    }
    return grantedAuthoritys;
  }

  /**
   * 获得用户所有角色的权限.
   */
  public Collection<GrantedAuthority> obtainGrantedAuthorities(Long userId, Integer roleId, Long insId) {

    Collection<GrantedAuthority> grantedAuthoritys = new ArrayList<GrantedAuthority>();
    if (roleId == null) {

      List<InsRole> roles = insRoleDao.getInsRoleWithUserId(userId, insId);

      for (InsRole role : roles) {
        for (Authority authority : roleDao.get(role.getId().getRoleId()).getAuthorities()) {
          grantedAuthoritys.add(new SimpleGrantedAuthority(authority.getName()));
        }
      }
    } else {

      for (Authority authority : roleDao.get(Long.valueOf(roleId)).getAuthorities()) {
        grantedAuthoritys.add(new SimpleGrantedAuthority(authority.getName()));
      }
    }

    // 如果用户没有在该站点没有角色，默认授予 A_AUTHORITY，针对未注册的，但又需要登录的页面使用.
    if (grantedAuthoritys.size() == 0) {
      grantedAuthoritys.add(new SimpleGrantedAuthority("A_AUTHORITY"));
    }

    return grantedAuthoritys;

  }

  /**
   * 通过域名获得人当前所在的单位.
   */
  public long getCurrentInsId(String domain) {

    long insId = 0;
    BpoInsPortal insPortal = insPortalDao.getEntity(domain);
    if (insPortal != null) {
      insId = insPortal.getInsId();
    }
    return insId;
  }

  /**
   * 通过 单位 id 获得所在域名.
   */
  public String getDomainByInsId(long insId) {

    String domain = null;
    BpoInsPortal insPortal = insPortalDao.get(insId);
    if (insPortal != null) {
      domain = insPortal.getDomain();
    }
    return domain;
  }

  /**
   * 通过 单位 id 获得首页.
   */
  public String getIndexPageByInsId(long insId) {

    String indexpage = null;
    BpoInsPortal insPortal = insPortalDao.get(insId);
    if (insPortal != null) {
      indexpage = insPortal.getIndexPage();
    }
    if (indexpage == null) {
      indexpage = "index";
    }
    return indexpage;
  }

}
