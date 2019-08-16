package com.smate.core.web.sys.security.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.service.profile.CommonUserService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.dao.security.AuthorityDao;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.bpo.BpoInsPortalDao;
import com.smate.core.base.utils.dao.security.egtexpert.EgtExpertInsPortalDao;
import com.smate.core.base.utils.dao.security.gxrol.GxRolInsPortalDao;
import com.smate.core.base.utils.dao.security.hnrol.HnRolInsPortalDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.dao.security.role.SieInsPortalDao;
import com.smate.core.base.utils.dao.security.zsrol.ZsRolInsPortalDao;
import com.smate.core.base.utils.model.UserInfo;
import com.smate.core.base.utils.model.bpo.BpoInsPortal;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.egtexpert.EgtExpertInsPortal;
import com.smate.core.base.utils.model.gxrol.GxRolInsPortal;
import com.smate.core.base.utils.model.hnrol.HnRolInsPortal;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.core.base.utils.model.rol.SieInsPortal;
import com.smate.core.base.utils.model.zsrol.ZsRolInsPortal;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.security.AuthorityManager;
import com.smate.core.web.sys.security.cache.UserCacheService;

/**
 * 实现spring security的UserDetailsService接口,获取用户Detail信息.
 * 
 * @author tsz
 */
@Service("scmUserDetailsService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ScmUserDetailsServiceImpl implements ScmUserDetailsService {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  AuthorityDao authorityDao;
  @Autowired
  private CommonUserService commonUserService;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private SieInsPortalDao sieInsPortalDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;
  @Autowired
  private BpoInsPortalDao bpoInsPortalDao;
  @Autowired
  private GxRolInsPortalDao gxRolInsPortalDao;
  @Autowired
  private ZsRolInsPortalDao zsRolInsPortalDao;
  @Autowired
  private HnRolInsPortalDao hnRolInsPortalDao;
  @Autowired
  private EgtExpertInsPortalDao egtExpertInsPortalDao;
  @Autowired
  private AuthorityManager snsAuthorityManager;
  @Autowired
  private AuthorityManager sieAuthorityManager;
  @Autowired
  private AuthorityManager sie6AuthorityManager;
  @Autowired
  private AuthorityManager bpoAuthorityManager;
  @Autowired
  private AuthorityManager gxrolAuthorityManager;
  @Autowired
  private AuthorityManager zsrolAuthorityManager;
  @Autowired
  private AuthorityManager hnrolAuthorityManager;
  @Autowired
  private AuthorityManager egtExpertAuthorityManager;
  private UserCacheService userCacheService; // 权限缓存 服务

  @Override
  public UserDetails loadUserFromSys(String userName, String sys, String url)
      throws UsernameNotFoundException, DataAccessException {
    Long userId = null;
    Long insId = null;
    Integer role = null;
    // 如果只有psn_id传送过来，查询所有权限，如果还带入了roleId，则只查询该角色的权限.
    try {
      if (NumberUtils.isNumber(userName)) {
        userId = Long.valueOf(userName);
      } else {
        userId = Long.valueOf(StringUtils.split(userName, '|')[0]);
        insId = Long.valueOf(StringUtils.split(userName, '|')[1]);
        role = Integer.valueOf(StringUtils.split(userName, '|')[2]);
        if (role == -1) {
          role = null;
        }
      }
      // 获取要跳转到的系统的domain
      url = this.getDomain(url);
      if (StringUtils.isNotBlank(url)) {
        insId = this.findInsIdBySysAndDomain(sys, url);
      } else {
        insId = SecurityUtils.getCurrentInsId() == null ? 0L : SecurityUtils.getCurrentInsId();
      }
      Collection<GrantedAuthority> grantedAuths = null;

      if (StringUtils.isNotBlank(sys)) {
        if ("SIE".equals(sys)) {
          grantedAuths = sieAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("SIE6".equals(sys)) {
          grantedAuths = sie6AuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("SNS".equals(sys) || "WCI".equals(sys)) {
          grantedAuths = snsAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("BPO".equals(sys)) {
          grantedAuths = bpoAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("GXROL".equals(sys)) {
          grantedAuths = gxrolAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("ZSROL".equals(sys)) {
          grantedAuths = zsrolAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("HNROL".equals(sys)) {
          grantedAuths = hnrolAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else if ("EGTEXPERT".equals(sys)) {
          grantedAuths = egtExpertAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        } else {
          grantedAuths = snsAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
        }
      } else {
        grantedAuths = snsAuthorityManager.obtainGrantedAuthorities(userId, role, insId);
      }
      // 获得用户所有角色的权限.
      // 不一样 的系统 取不一样的服务 （权限是按库来查的）
      // 以下属性,暂时全部设为true.
      boolean accountNonExpired = true;
      boolean credentialsNonExpired = true;
      boolean accountNonLocked = true;
      boolean enabled = true;

      // 从缓存读取用户数据
      // User user = userCacheService.get(userId);
      User user = commonUserService.findUserById(userId);
      UserInfo userInfo = new UserInfo(userId.toString(), user.getPassword(), enabled, accountNonExpired,
          credentialsNonExpired, accountNonLocked, grantedAuths, user.getNodeId());
      userInfo.setInsId(insId == 0 ? null : insId);
      userInfo.setRoleId(role);
      userInfo.setSys(sys);
      return userInfo;
    } catch (Exception e) {
      logger.error("捕获到异常，userId=" + userId + ", insId=" + insId + ", role=" + role + ", sys=" + sys + e);
      throw new UsernameNotFoundException(e.toString());
    }
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return loadUserFromSys(username, null, null);
  }

  @Override
  public Long findInsIdBySysAndDomain(String sys, String domain) throws DataAccessException {
    Long insId = null;
    try {
      if (StringUtils.isNotBlank(sys)) {
        if ("SIE".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            SieInsPortal insPortal = sieInsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        } else if ("SIE6".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            Sie6InsPortal insPortal = sie6InsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        } else if ("BPO".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            BpoInsPortal insPortal = bpoInsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        } else if ("GXROL".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            GxRolInsPortal insPortal = gxRolInsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        } else if ("ZSROL".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            ZsRolInsPortal insPortal = zsRolInsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        } else if ("HNROL".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            HnRolInsPortal insPortal = hnRolInsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        } else if ("EGTEXPERT".equals(sys)) {
          if (StringUtils.isNotBlank(domain)) {
            EgtExpertInsPortal insPortal = egtExpertInsPortalDao.getEntity(domain);
            if (insPortal != null) {
              insId = insPortal.getInsId();
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("获取系统insId出错", e);
    }
    return insId == null ? 0L : insId;
  }

  @Override
  public Map<String, Object> buildUserDetailsMap(String sessionId, String targetUrl, String sys,
      UserDetails userDetails) throws Exception {
    Object detailsObj = userCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    Map<String, Object> detailsMap = null;
    if (detailsObj != null) {
      detailsMap = (Map<String, Object>) detailsObj;
    } else {
      detailsMap = new HashMap<String, Object>();
    }
    String domain = this.getDomain(targetUrl);
    if (StringUtils.isBlank(domain)) {
      logger.info("从跳转的目标页面获取域名为空，get domain from targetUrl is null");
    }
    Long insId = this.findInsIdBySysAndDomain(sys, domain);
    detailsMap.remove(sys + "|" + insId);
    detailsMap.put(sys + "|" + insId, userDetails);
    return detailsMap;
  }

  public String getDomain(String url) throws Exception {
    if (StringUtils.isNotBlank(url)) {
      if (url.indexOf("http://") > -1) {
        url = url.replace("http://", "");
      }
      if (url.indexOf("https://") > -1) {
        url = url.replace("https://", "");
      }
      if (url.indexOf("/") > -1) {
        url = url.substring(0, url.indexOf("/"));
      }
    }
    return url;
  }

  public void setUserCacheService(UserCacheService userCacheService) {
    this.userCacheService = userCacheService;
  }

}
