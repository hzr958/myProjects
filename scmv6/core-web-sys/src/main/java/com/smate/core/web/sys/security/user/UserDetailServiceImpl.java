package com.smate.core.web.sys.security.user;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.AuthorityDao;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.UserInfo;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalDomain;
import com.smate.core.base.utils.service.security.AuthorityManager;
import com.smate.core.web.sys.security.cache.UserCacheService;


/**
 * 实现spring security的UserDetailsService接口,获取用户Detail信息.
 * 
 * @author tsz
 */
@Service("userDetailsService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private AuthorityDao authorityDao;
  @Autowired
  private UserCacheService userCacheService;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private AuthorityManager authorityManager;

  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

    Long userId = null;
    Integer role = null;
    // 如果只有psn_id传送过来，查询所有权限，如果还带入了roleId，则只查询该角色的权限.
    if (NumberUtils.isNumber(userName)) {
      userId = Long.valueOf(userName);
    } else {
      userId = Long.valueOf(StringUtils.split(userName, '|')[0]);
      role = Integer.valueOf(StringUtils.split(userName, '|')[1]);
    }

    Long insId = null;
    if (TheadLocalDomain.getDomain() != null) {
      InsPortal insPortal = insPortalDao.getEntity(TheadLocalDomain.getDomain());
      if (insPortal != null) {
        insId = insPortal.getInsId();
      }
    } else {
      insId = SecurityUtils.getCurrentInsId();
    }
    // 获得用户所有角色的权限.
    Collection<GrantedAuthority> grantedAuths = authorityManager.obtainGrantedAuthorities(userId, role, insId);

    // 以下属性,暂时全部设为true.
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;
    boolean enabled = true;

    // 从缓存读取用户数据
    User user = userCacheService.get(userId);

    UserInfo userdetail = new UserInfo(userId.toString(), user.getPassword(), enabled, accountNonExpired,
        credentialsNonExpired, accountNonLocked, grantedAuths, user.getNodeId());

    return userdetail;
  }

}
