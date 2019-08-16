package com.smate.core.web.sns.menu.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.menu.UserRolData;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalDomain;
import com.smate.core.base.utils.security.TheadLocalSessionId;
import com.smate.core.base.utils.service.security.AuthorityManager;
import com.smate.core.web.sns.cache.UserDataCacheService;
import com.smate.core.web.sns.consts.SnsConst;
import com.smate.core.web.sns.menu.service.InsPortalManager;
import com.smate.core.web.sns.menu.service.PersonQueryService;

/**
 * 获取登录 人员信息拦截器
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class SnsMenuFilter implements Filter {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public SnsMenuFilter() {
    super();

  }

  /**
   * 
   * 处理菜单呈现与顶部html的内容呈现.
   * 
   * 
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    // 新系统 停用改拦截
    chain.doFilter(request, response);

    HttpSession session = ((HttpServletRequest) request).getSession();
    logger.debug("正在进入新系统...");
    request.setAttribute("snsDomain", session.getServletContext().getAttribute("domainscm").toString());
    TheadLocalSessionId.setSessionId(session.getId());
    if (SecurityUtils.getCurrentUserId() == 0L) {
      chain.doFilter(request, response);
      return;
    }
    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
    // 获得当前操作的域名
    String domain = TheadLocalDomain.getDomain();
    // 读取来自基金委、RO的跳转
    String from = ((HttpServletRequest) request).getParameter("from");
    String menuIdStr = ((HttpServletRequest) request).getParameter("menuId");
    // 获取sessionMenu封装数据
    UserDataCacheService menuCache = (UserDataCacheService) ctx.getBean("userDataCacheService");
    InsPortalManager insPortalManager = (InsPortalManager) ctx.getBean("insPortalManager");
    boolean swithRolIns = false;// 成果在线标识，防止菜单乱串
    // 从老系统传过来的
    String snsRolId = (String) session.getAttribute("userRolData-rolId");
    Long rolInsId = null;
    // 登录的用户界面数据
    String temp = null;
    try {
      temp = (String) menuCache.get(SnsConst.USER_DATA_CACHE, "UserRolData" + SecurityUtils.getCurrentUserId());
    } catch (Exception e1) {
      temp = null;
    }
    UserRolData userRolData = temp == null ? null : JacksonUtils.jsonObject(temp, UserRolData.class);
    if (userRolData == null) {
      userRolData = new UserRolData();
      userRolData.setRolInsId(rolInsId);
      swithRolIns = true;
    }
    if (snsRolId != null) {
      userRolData.setRolInsId(Long.valueOf(snsRolId));
      rolInsId = userRolData.getRolInsId();
    }
    if (StringUtils.isNotBlank(from)) {
      userRolData.setFrom(from);
    }
    // 是否切换单位

    HttpServletRequest request1 = (HttpServletRequest) request;
    String url = request1.getRequestURI();
    this.setCurrentContext(url, userRolData);
    // 是否存在当前rolInsId
    if (request.getParameter("switchInsId") != null) {
      rolInsId = NumberUtils.createLong(request.getParameter("switchInsId"));
    }
    if (request.getParameter("rolInsId") != null) {
      rolInsId = NumberUtils.createLong(request.getParameter("rolInsId"));
    }
    if (rolInsId == null) {
      rolInsId = 0L;
    }

    // 通过当前请求的域名 来判断 单位 tsz
    if ((rolInsId == null || rolInsId == 0L)
        && ((HttpServletRequest) request).getServerName().indexOf(".nsfc.gov.cn") >= 0) {
      rolInsId = 2566L;
    }
    // 通过当前请求的域名 来判断 单位 tsz
    if (rolInsId != null && rolInsId != 0L
        && ((HttpServletRequest) request).getServerName().indexOf(".scholarmate.com") >= 0) {
      clearInsInfo(request, domain, userRolData, 0L);
    }

    // 默认跳到成果在线
    if ((rolInsId == null || rolInsId == 0) && url.indexOf("/ronline") >= 0) {
      rolInsId = 2566L;
      userRolData.setRolInsId(rolInsId);
      swithRolIns = true;
    }
    // 通过当前请求的域名 来判断 单位 tsz
    if ((rolInsId == null || rolInsId == 0L) && request1.getServerName().indexOf(".nsfc.gov.cn") >= 0) {
      rolInsId = 2566L;
      userRolData.setRolInsId(rolInsId);
      swithRolIns = true;
    }

    // 跳回科研之友
    // 来自成果在线,不会去老系统，所以老系统的rolid还是跳转之前的
    if (!swithRolIns && (StringUtils.isNotBlank(from) || (rolInsId != null && rolInsId > 0))) {
      userRolData.setFrom(from);
      if (isFromNsfcSys(from)) {
        rolInsId = 2566L;
        userRolData.setRolInsId(rolInsId);
      } else {
        userRolData.setRolInsId(rolInsId);
      }
      swithRolIns = true;
    }
    // 菜单属于成果在线
    if (!swithRolIns && StringUtils.isNotBlank(menuIdStr)) {
      Integer menuId = Integer.valueOf(menuIdStr);
      if (menuId == 502 || menuId == 507 || menuId == 505 || menuId == 501) {
        rolInsId = 2566L;
        userRolData.setMenuId(menuId);
        userRolData.setRolInsId(rolInsId);
        swithRolIns = true;
      }
    }

    /**
     * 切换单位的title信息，比如切换到清华大学/基金委成果在线
     */
    if (rolInsId == 0) {
      userRolData.setRolInsId(rolInsId);
      userRolData.setRolDomain(domain);
    }
    if (userRolData.getRolDomain() != null) {
      domain = userRolData.getRolDomain();
    }
    if (rolInsId > 0) {
      swithRolIns = true;
      userRolData.setRolInsId(rolInsId);
      AuthorityManager authorityManager = (AuthorityManager) ctx.getBean("authorityManager");
      userRolData.setRolDomain(authorityManager.getDomainByInsId(rolInsId));
      try {
        // version字段已废弃
        // InsPortal insPortal = insPortalManager.getInsPortalByInsId(rolInsId);
        // if (insPortal != null) {
        // userRolData.setSieVersion(Long.valueOf(insPortal.getVersion()));
        // session.setAttribute("sieVersion", insPortal.getVersion());
        // } else {
        // }
        userRolData.setSieVersion(0L);
        session.removeAttribute("sieVersion");
      } catch (Exception e) {
        logger.error("获取人员所有单位Url失败", e);
      }
    }
    if (rolInsId == 0) {
      // 清空单位信息
      clearInsInfo(request, domain, userRolData, rolInsId);
    } else if (swithRolIns) {
      // 只有在切换时读取一次
      domain = SnsMenuData.fillInsInfo(request, ctx, domain, userRolData, rolInsId, logger);
    }

    // *************end 获取当前菜单数据
    // ************start 人员所有单位列表
    try {
      // 单位个数，如果不为null，则表示已经读取过portal列表，防止多次读取portal
      if (userRolData.getPortalCount() == null) {

        List<InsPortal> portal = insPortalManager.findUserRolUrl(SecurityUtils.getCurrentUserId(), null);
        if (portal != null && portal.size() > 0) {
          setWebContext(portal);
          userRolData.setPortal(portal);
          userRolData.setPortalCount(portal.size());
        } else {
          userRolData.setPortalCount(0);
        }
      }
    } catch (Exception e) {
      logger.error("获取人员所有单位Url失败", e);
    }

    fillterUserName(userRolData, ctx);

    // *********end 人员所有单位列表
    // 60分钟过期？
    // 改成村字符串
    String userRolDataString = JacksonUtils.jsonObjectSerializer(userRolData);
    menuCache.put(SnsConst.USER_DATA_CACHE, 1200, "UserRolData" + SecurityUtils.getCurrentUserId(), userRolDataString);
    request.setAttribute("userRolData", userRolData);
    chain.doFilter(request, response);
  }

  private void setCurrentContext(String requestUrl, UserRolData userRolData) {
    if (requestUrl.indexOf("inspg") > 0) {
      userRolData.setCurrentContext("inspg");
    } else {
      userRolData.setCurrentContext("");
    }
  }

  /**
   * 通过from参数判断是否来源来基金委成果在线
   * 
   * @param from
   * @return
   */
  private boolean isFromNsfcSys(String from) {
    if ("finalReport".equals(from) || "reschReport".equals(from) || "proposal".equals(from) || "conPrj".equals(from)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 清空单位信息.
   * 
   * @param request
   * @param domain
   * @param userRolData
   * @param rolInsId
   * @throws UnsupportedEncodingException
   */
  private void clearInsInfo(ServletRequest request, String domain, UserRolData userRolData, Long rolInsId)
      throws UnsupportedEncodingException {
    // 切换系统清空单位Logo
    if (userRolData.getRolLogoUrl() != null) {
      userRolData.setRolLogoUrl(null);
      userRolData.setRolTitle(null);
      userRolData.setRolInsId(rolInsId);
      userRolData.setFromIsis(null);
    }
    userRolData.setMenuId(0);
    String snsContext =
        ((HttpServletRequest) request).getSession().getServletContext().getAttribute("snsContext").toString();
    ((HttpServletRequest) request).getSession().setAttribute("logoutindex",
        java.net.URLEncoder.encode(domain + snsContext + "/index", "UTF-8"));
  }

  private void fillterUserName(UserRolData userRolData, WebApplicationContext ctx) {

    /**
     * 添加写入用户默认姓名
     */

    try {
      // 如果用户名为空，则查询用户名，注意，基本信息编辑那里修改后会将姓名缓存清空
      // PersonBaseAction.ajaxSave
      // 新旧系统 userRolData 缓存 不一样问题 导致 修改姓名 显示不一样
      if (StringUtils.isBlank(userRolData.getUsername()) || StringUtils.isBlank(userRolData.getFirstName())) {
        PersonQueryService personQueryService = (PersonQueryService) ctx.getBean("personQueryService");
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
          userRolData.setEnUsername(name);
          userRolData.setUsername(person.getName());
        } else {
          name = StringUtils.isBlank(person.getName())
              ? StringUtils.trim(person.getLastName() + " " + person.getFirstName())
              : person.getName();
          name = HtmlUtils.subString(name, 27, "... ");
          userRolData.setUsername(name);
          String enUsername = StringUtils.trim(person.getFirstName() + " " + person.getLastName());
          enUsername = HtmlUtils.subString(enUsername, 11, "... ");
          userRolData.setEnUsername(enUsername);
        }
        userRolData.setFirstName(person.getAvatars());
        userRolData.setLastName(person.getLastName());
        userRolData.setDes3PsnId(Des3Utils.encodeToDes3(person.getPersonId().toString()));
      }
    } catch (Exception e) {
      logger.warn("读取人员名称失败", e);
    }
  }

  public void init(FilterConfig conf) throws ServletException {

  }

  public void destroy() {

  }

  private void setWebContext(List<InsPortal> portals) throws Exception {

    if (CollectionUtils.isEmpty(portals)) {
      return;
    }
    for (InsPortal insPortal : portals) {
      insPortal.setWebCtx("scmwebrol");
    }

  }

}
