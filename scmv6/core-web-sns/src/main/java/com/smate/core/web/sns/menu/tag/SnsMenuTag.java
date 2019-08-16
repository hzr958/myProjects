package com.smate.core.web.sns.menu.tag;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.menu.UserRolData;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.web.sns.cache.UserDataCacheService;
import com.smate.core.web.sns.consts.SnsConst;
import com.smate.core.web.sns.menu.model.MenuItemBean;
import com.smate.core.web.sns.menu.service.MenuClientService;
import com.smate.core.web.sns.menu.utils.MenuUtils;
import com.smate.core.web.sns.menu.utils.NsfcMenuUtils;
import com.smate.core.web.sns.menu.utils.SnsMenuUtils;

/**
 * 科研之友菜单标签.
 * 
 * @author mjg
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class SnsMenuTag extends BodyTagSupport {

  protected static final String MENU_COOKIE_NAME = "menuId";
  private static final long serialVersionUID = 4923619449908172794L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 当前活动的菜单.
   */
  int menuId = 1;
  /**
   * 菜单还是导航条 "menu" "nav".
   */
  String type = "menu";
  /**
   * 系统标示.
   */
  String app = "sns";

  @Override
  public int doStartTag() throws JspException {
    try {

      pageContext.getOut().write(getMenuStr(pageContext));

      return EVAL_BODY_INCLUDE;
    } catch (Exception e) {
      logger.error("科研之友菜单标签", e);
      throw new JspException(e.getMessage());
    }
  }

  /**
   * 获取菜单内容.
   * 
   * @param pageContext
   * @return
   * @throws SysServiceException
   */
  @SuppressWarnings({"rawtypes"})
  private String getMenuStr(PageContext pageContext) throws SysServiceException {
    Integer roleId = null;
    // 当前操作的域名
    // TODO 所有域名 先取配置文件中配置的 tsz_2015.06.17
    String scmDomain = (String) pageContext.getServletContext().getAttribute("domainscm");
    ServletRequest request = pageContext.getRequest();
    // 是否存在当前rolInsId, 该rolInsId作为切换ROL样式时使用的参数
    // springBean读取
    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
    // web层缓存，包括菜单、用户临时数据.
    // TODO 改造获取UserRolData对象的方式
    UserDataCacheService menuCache = (UserDataCacheService) ctx.getBean("userDataCacheService");
    // 登录的用户界面数据
    String temp = null;
    try {
      temp = (String) menuCache.get(SnsConst.USER_DATA_CACHE, "UserRolData" + SecurityUtils.getCurrentUserId());
    } catch (Exception e1) {
      temp = null;
    }
    UserRolData userRolData = temp == null ? null : JacksonUtils.jsonObject(temp, UserRolData.class);
    // TODO 该方法为临时方法 以后改造 tsz_2015.06.17
    HttpServletRequest request1 = (HttpServletRequest) pageContext.getRequest();
    String url = request1.getServletPath();

    // 从cache中得到当前登录人的切换的rolInsId.
    Long rolInsId = userRolData.getRolInsId() == null ? 0 : (Long) userRolData.getRolInsId();
    MenuClientService menuClientService = (MenuClientService) ctx.getBean("menuClientService");
    // 当前语言 SCM-9092
    String locale = LocaleContextHolder.getLocale().toString();
    // 如果是sie协会版，则本地语言环境一定是英语
    if (userRolData.getSieVersion() == 1) {
      locale = "en_US";
    }
    // 获取个人菜单数据.
    Map userInfo =
        MapBuilder.getInstance().put("ins_id", rolInsId).put("role_id", roleId).put("locale", locale).getMap();
    // 获得当前选定的菜单id.
    menuId = this.getMenuId(request, menuClientService, userRolData, userInfo);
    // 机构主页
    if (url.indexOf("/inspg") >= 0) {
      menuId = 4;
    }
    userRolData.setMenuId(menuId);
    // 解析菜单内容并拼装成字符串.
    String menuItemStr = null;
    // 修改了科研之友菜单的生成方式_MJG_SCM-5269.
    MenuItemBean menuItem = menuClientService.getMenuItem(userInfo);
    if (rolInsId.longValue() == 2565 || rolInsId.longValue() == 2566) {
      scmDomain = userRolData.getRolDomain();
    }
    if ("menu".equalsIgnoreCase(type)) {
      if (rolInsId != null && (rolInsId.longValue() == 2565 || rolInsId.longValue() == 2566)) {
        // 基金委科研在线/成果在线的菜单加载. TSZ_2014.11.04_NFSCSCM-111
        menuItemStr = NsfcMenuUtils.showNsfcHTMLMenu(menuItem, menuId, scmDomain, scmDomain, rolInsId);
      } else {
        // 科研之友
        menuItemStr = SnsMenuUtils.showSnsHTMLMenu(menuItem, menuId, scmDomain, scmDomain, rolInsId);
      }
    } else {
      menuItemStr = MenuUtils.showNewHTMLNav(menuItem, menuId, scmDomain, scmDomain, rolInsId);
    }
    String userRolDataString = JacksonUtils.jsonObjectSerializer(userRolData);
    menuCache.put(SnsConst.USER_DATA_CACHE, 1200, "UserRolData" + SecurityUtils.getCurrentUserId(), userRolDataString);
    return menuItemStr;
  }

  /**
   * 获取选定菜单ID.
   * 
   * @param request
   * @param userRolData
   * @param menuItem
   * @return
   * @throws SysServiceException
   */
  @SuppressWarnings("rawtypes")
  private Integer getMenuId(ServletRequest request, MenuClientService menuClientService, UserRolData userRolData,
      Map userInfo) throws SysServiceException {
    String reqUri = ((HttpServletRequest) request).getRequestURI();
    // 获取当前请求地址的完整请求信息_MJG_2013-04-16_SCM-2272.
    String reqParam = ((HttpServletRequest) request).getQueryString();// 请求地址栏的参数.

    String pId = ((HttpServletRequest) request).getParameter("menuId");
    Object mid = ((HttpServletRequest) request).getAttribute("menuId");
    // 获取请求属性
    if (StringUtils.isBlank(pId)) {
      if (mid != null) {
        pId = mid.toString();
      }
    }
    // 如果是登录进入系统，则不追加请求参数_MJG_2013-04-17_SCM-2304.
    boolean isLoginPage = (reqUri.indexOf("/scmwebsns/main") >= 0);
    if (StringUtils.isNotBlank(reqParam) && !isLoginPage) {
      // 如果是登录后菜单跳转，则不追加请求参数与数据库中菜单配置记录进行匹配_MJG_2013-06-25_SCM-2812.
      boolean isForwardPage = false;
      isForwardPage = ((reqParam.indexOf("ticket") >= 0) && (reqParam.indexOf("isFirst") >= 0));
      if (!isLoginPage && !isForwardPage) {
        reqUri = reqUri + "?" + reqParam;
      }
    }

    // 修改菜单的加载逻辑(优先选择根据url匹配菜单ID；其次考虑如果匹配不到，再获取其menuId参数值进行赋值；再次如果没有提供menuId参数，则获取userRolData的菜单ID)_mjg_scm-5206.
    // 根据url反向解析menuId
    String url = reqUri;// ((HttpServletRequest)
    // request).getRequestURI();
    // liangguokeng
    if (url.indexOf(".action") > 0) {
      url = url.substring(0, url.indexOf(".action"));
    } else if (url.indexOf("?") > 0) {
      url = url.substring(0, url.indexOf("?"));
    }
    Integer id = menuClientService.getMenuIdByUrl(url, userInfo);

    if (id != null) {// 根据URL找到menuId了
      if (NumberUtils.isNumber(pId) && Integer.valueOf(pId).intValue() != id.intValue()) {
        menuId = Integer.valueOf(pId);
      } else {
        menuId = id;
      }
    } else {

      if (NumberUtils.isNumber(pId)) {
        menuId = Integer.valueOf(pId);
        id = menuId;
      } else if (userRolData != null) {
        // 从userRolData获得选定菜单menuId.
        menuId = userRolData.getMenuId();
        id = menuId;
      }
      //
      if (id == null) {
        // 从广西，海南，中山，基金委科研在线，成果在线跳转到科研之友的成果类链接，菜单定位到成果菜单_MJG_SCM-5218<此举是临时逻辑需待生产机发布后修正为正常菜单匹配逻辑>.
        String reqUrl = ((HttpServletRequest) request).getRequestURL().toString();
        if (StringUtils.isNotBlank(reqUrl) && userRolData.getRolInsId() != null
            && userRolData.getRolInsId().longValue() > 0) {
          if (url.contains("/publication/collect") || url.contains("/publication/enter")
              || url.contains("/publication/edit") || url.contains("/pubconfirm/publist")
              || url.contains("publication/import/importFile")) {
            id = 1300;
            menuId = 1300;
          }
        }
      }
      // 判断 如果为机构主页 链接 直接定位到应用菜单下
      // 看到此逻辑时，要淡定，统一改造再完善
      Integer rolInsId = Integer.valueOf(userInfo.get("ins_id").toString());
      if (rolInsId == 0 || rolInsId == null) {
        if (url.indexOf("/groupweb") >= 0) {
          menuId = 3;
        } else if (url.indexOf("/pubweb") >= 0) {
          menuId = 1300;
          // 如果在成果在线环境下 ，第二次以上重复编辑成果时，菜单也要显示
          if (url.indexOf("/publication/edit") >= 0 || url.indexOf("/publication/enter") >= 0) {
            menuId = handleRonlineMenuId(reqParam, menuId);
          }
        }
      } else {
        // 成果在线
        if (url.indexOf("/ronline/main") >= 0) {
          menuId = 9999;
        } else {
          // 在成果在线环境下 ，第二次以上重复编辑成果时，菜单也要显示
          if (menuId == 0 || menuId == 9999) {
            menuId = 12;
          }
          if (url.indexOf("/prjfinal/prjrptlist") >= 0) {
            // 防止菜单id不正确
            menuId = 12;
          }
          // zk 说可以去掉
          if (url.indexOf("/pubweb/") > 0) {
            menuId = 1300;
          }
          if (url.indexOf("/groupweb") >= 0) {
            menuId = 31;
          }
        }
      }
    }
    return menuId;
  }

  /**
   * 如果参数中带了from，则认定来自成果在线
   * 
   * @param reqParam
   * @param menuId
   */
  private Integer handleRonlineMenuId(String reqParam, Integer menuId) {

    if (reqParam.indexOf("from=finalReport") >= 0) {
      menuId = 501;
    } else if (reqParam.indexOf("from=reschReport") >= 0) {
      menuId = 505;
    } else if (reqParam.indexOf("from=proposal") >= 0) {
      menuId = 502;
    } else if (reqParam.indexOf("from=conPrj") >= 0) {
      menuId = 507;
    }
    return menuId;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
