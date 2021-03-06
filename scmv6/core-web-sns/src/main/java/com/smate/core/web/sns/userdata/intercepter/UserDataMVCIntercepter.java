package com.smate.core.web.sns.userdata.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.cache.CacheConst;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalInsId;
import com.smate.core.base.utils.service.security.InsRoleService;
import com.smate.core.web.sns.cache.UserDataCacheService;
import com.smate.core.web.sns.menu.service.MainInitialService;
import com.smate.core.web.sns.menu.service.PersonQueryService;
import com.smate.core.web.sns.userdata.intercepter.model.UserData;

/**
 * 用户信息拦截器 spring mvc专用.
 * 
 * @author tsz
 *
 * @date 2018年8月9日
 */
public class UserDataMVCIntercepter implements HandlerInterceptor {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserDataCacheService userDataCacheService;
  @Autowired
  private PersonQueryService personQueryService;
  @Autowired
  private MainInitialService mainInitialService;
  @Autowired
  private InsRoleService insRoleService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    UserData userData = null;
    if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId().longValue() != 0L) {
      Object temp = userDataCacheService.get(CacheConst.NEW_USER_DATA_CACHE, request.getSession().getId());
      if (temp != null) {
        userData = (UserData) temp;
      } else {
        userData = new UserData();
        // 没有缓存 读取数据 存缓存
        fillterUserName(userData);
        userDataCacheService.put(CacheConst.NEW_USER_DATA_CACHE, 60 * 10, request.getSession().getId(), userData);
      }
      fillterMenuId(userData, request);
      Long rolInsId = NumberUtils.createLong(request.getParameter("switchInsId"));
      if (rolInsId == null || rolInsId == 0L) {
        Long temp1 = NumberUtils.createLong(request.getParameter("rolInsId"));
        if (temp1 != null && temp1 != 0L) {
          rolInsId = temp1;
        }
      }
      // 从老系统传过来的 去除老系统后可以运送去掉
      Object object = request.getSession().getAttribute("userRolData-rolId");
      if (object != null && (rolInsId == null || rolInsId == 0L)) {
        rolInsId = NumberUtils.createLong(object.toString());
      }
      // 是否存在当前rolInsId
      if (rolInsId != null && (userData.getRolInsId() == null || !userData.getRolInsId().equals(rolInsId))) {
        userData.setRolInsId(rolInsId);
      }
      if (userData.getRolInsId() == null || userData.getRolInsId() == 0L) {
        clearInsInfo(userData);
      } else {
        fillterInsInfo(userData, request);
      }
      TheadLocalInsId.setInsId(userData.getRolInsId());
      userDataCacheService.put(CacheConst.NEW_USER_DATA_CACHE, 60 * 20, request.getSession().getId(), userData);
    }
    request.setAttribute("userData", userData);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    // TODO Auto-generated method stub
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // TODO Auto-generated method stub
  }

  /**
   * 获取人员 名字头像信息.
   * 
   */
  private void fillterUserName(UserData userData) {
    try {
      Person person = personQueryService.getPersonBaseInfo(SecurityUtils.getCurrentUserId());
      String locale = person.getLocalLanguage();
      if (StringUtils.isBlank(locale)) {
        locale = LocaleContextHolder.getLocale().toString();
      }
      String name = null;
      if ("en_US".equals(locale)
          && (StringUtils.isNotBlank(person.getLastName()) || StringUtils.isNotBlank(person.getFirstName()))) {
        name = StringUtils.trim(person.getFirstName() + " " + person.getLastName());
        name = HtmlUtils.subString(name, 11, "... ");
        userData.setEnUsername(name);
        userData.setUsername(person.getName());
      } else {
        name =
            StringUtils.isBlank(person.getName()) ? StringUtils.trim(person.getLastName() + " " + person.getFirstName())
                : person.getName();
        name = HtmlUtils.subString(name, 27, "... ");
        userData.setUsername(name);
        String enUsername = StringUtils.trim(person.getFirstName() + " " + person.getLastName());
        enUsername = HtmlUtils.subString(enUsername, 11, "... ");
        userData.setEnUsername(enUsername);
      }
      userData.setAvatars(person.getAvatars());
      userData.setDes3PsnId(Des3Utils.encodeToDes3(person.getPersonId().toString()));
    } catch (Exception e) {
      logger.error("sns初始化 人员数据报错 !!!", e);
    }
  }

  /**
   * 构建单位信息.
   * 
   */
  private void fillterInsInfo(UserData userData, HttpServletRequest request) {
    try {
      String[] insLogo = mainInitialService.loadMainLogo(userData.getRolInsId());
      String rolTitle = null;
      if ("zh".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
        rolTitle = insLogo[0] == null ? insLogo[2] : insLogo[0];
      } else {
        rolTitle = insLogo[2] == null ? insLogo[0] : insLogo[2];
      }
      userData.setRolTitle(rolTitle);
      userData.setRolLogoUrl(insLogo[1]);
      userData.setRolDomain(insLogo[3]);
      userData.setSys("SIE6");
      // ins_portal改为视图了，没有new_domain字段了
      // SIE新系统标志是SIE6
      // Cookie sysCookie = Struts2Utils.getCookie(request, "SYS");
      // if (sysCookie != null && "SIE6".equals(sysCookie.getValue())) {
      // userData.setSys("SIE6");
      // if (insLogo.length > 4 && StringUtils.isNotBlank(insLogo[4])) {
      // userData.setRolDomain(insLogo[4]);
      // }
      // }
      // 判断角色 是否有多个角色
      // 是否有多角色，否则需要切换角色功能
      userData
          .setRolMultiRole(insRoleService.hasSieMultiRole(userData.getRolInsId(), SecurityUtils.getCurrentUserId()));
    } catch (Exception e) {
      logger.error("ROL_LOGO_URL error", e);
    }
  }

  private void clearInsInfo(UserData userData) {
    userData.setRolTitle(null);
    userData.setRolLogoUrl(null);
    userData.setRolDomain(null);
    userData.setSys("SNS");
  }

  /**
   * 构建菜单ID.
   */
  private void fillterMenuId(UserData userData, HttpServletRequest request) {
    // 根据连接来定位
    int menuId = 0;
    String url = request.getRequestURL().toString();
    if (StringUtils.isBlank(url)) {
      return;
    }
    if (url.indexOf("pub/papermain") > 0) {
      menuId = 5;
    } else if (url.indexOf("pub/pubrecommendmain") > 0) {
      menuId = 5;
    } else if (url.indexOf("psnweb/friend") > 0) {
      menuId = 3;
    } else if (url.indexOf("psnweb/application") > 0) {
      menuId = 5;
    } else if (url.indexOf("psnweb/myfile") > 0) {
      menuId = 5;
    } else if (url.indexOf("psnweb") > 0) {
      menuId = 2;
    } else if (url.indexOf("/pub/") > 0) {
      menuId = 2;
    } else if (url.indexOf("groupweb") > 0) {
      menuId = 4;
    } else if (url.indexOf("groupweb") > 0) {
      menuId = 4;
    } else if (url.indexOf("dynweb/main") > 0) {
      menuId = 1;
    } else if (url.indexOf("/prjweb/fund/main") > 0) {
      menuId = 5;
    } else if (url.indexOf("/prjweb/ins/") > 0) {
      menuId = 5;
    } else if (url.indexOf("/prjweb/project/prjmain") > 0) {
      menuId = 5;
    }
    userData.setMenuId(menuId);
  }
}
