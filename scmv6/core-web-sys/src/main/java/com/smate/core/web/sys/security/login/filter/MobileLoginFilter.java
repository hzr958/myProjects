package com.smate.core.web.sys.security.login.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.app.service.AppAuthTokenService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.wechat.OAuth2Service;
import com.smate.core.base.utils.wechat.WeChatRelationService;
import com.smate.core.web.sys.security.cache.UserCacheService;

/**
 * 移动端用户登录Filter
 * 
 * @author zk
 *
 */
public class MobileLoginFilter implements Filter {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AppAuthTokenService appAuthTokenService;
  @Value("${domainoauth}")
  private String oauthDomain; // 认证中心域名
  @Value("${domainscm}")
  private String snsDomain;// 科研之友域名
  // 忽略的地址
  private static final List<String> skipUrl = new ArrayList<String>() {
    private static final long serialVersionUID = 1583143577978103965L;
    {
      add("/node-web5/psnweb/mobile/morePersonKeywords");
      add("/node-web5/psnweb/mobile/outhome");
      add("/node-web5/psnweb/mobile/otherfriendlist");
      // add("/node-web2/pubweb/wechat/findotherpubs");需要用到自己的psnid
      add("/node-web3/prjweb/wechat/findotherprjs");
      add("/node-web2/pubweb/mobile/pubdetail");
      add("/node-web2/pubweb/wechat/ajaxgetpubinfo");
      add("/node-web2/pubweb/mobile/search/pdwhpaper");
      add("/node-web2/pubweb/mobile/search/pdwhpatent");
      add("/node-web2/pubweb/mobile/search/main");
      add("/node-web2/pubweb/mobile/findpdwhpubxml");
      add("/node-web6/dynamicprodece");
      add("/node-web5/psnweb/search/ajaxlist");
      add("/node-web5/psnweb/search/ajaxPsnOtherInfos");
      add("/node-web5/psnweb/mobile/search");
      add("/node-web5/psnweb/mobile/ajaxavatarurls");
      add("/node-web5/psnweb/mobile/ajaxlist");
      add("/node-web5/psnweb/search");
      add("/node-web6/dynweb/dynamic/ajaxrealtime");
      add("/node-web6/dynweb/grpdyn/outside/ajaxshow");
      add("/node-web6/dynweb/grpdyn/outside/ajaxgetsamplecomment");
      add("/node-web6/dynweb/grpdyn/outside/ajaxdiscusscommentlist");

    }
  };

  public MobileLoginFilter() {
    super();
  }

  @Override
  public void destroy() {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpSession session = ((HttpServletRequest) request).getSession();
    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    // 获取当前人员id为空，则尝试从人员缓存中获取
    this.tryFindPsnIdByCache(httpRequest, ctx);
    logger.debug("正在进入移动端用户登录Filter...");
    // 如果请求url带有/mobile字样
    boolean needIf = httpRequest.getRequestURI().indexOf("/mobile") >= 0 ? true : false;
    // 如果请求url带有/wechat字样
    needIf = needIf ? true : httpRequest.getRequestURI().indexOf("/wechat") >= 0 ? true : false;
    // 如果请求url带有/dynamic字样
    needIf = needIf ? true : httpRequest.getRequestURI().indexOf("/dynweb") >= 0 ? true : false;
    needIf = false;
    if (needIf) {
      if (SecurityUtils.getCurrentUserId() == 0 && !skipUrl.contains(httpRequest.getRequestURI())) {
        // 存在微信unionId
        if (session.getAttribute("wxUnionId") != null
            && StringUtils.isNotBlank(session.getAttribute("wxUnionId").toString())) {
          this.tryFindPsnIdByWxUnionId(session.getAttribute("wxUnionId").toString(), request, httpRequest, httpResponse,
              ctx, session);
        }
        if (!(TheadLocalPsnId.getPsnId() > 0) && session.getAttribute("wxOpenId") != null
            && StringUtils.isNotBlank(session.getAttribute("wxOpenId").toString())) {
          logger.info("正在进入移动端用户登录Filter,存在wxOpenId且不能获取到psnid,尝试通过wxopenid获取psnId");
          this.tryFindPsnIdByWxOpenId(session.getAttribute("wxOpenId").toString(), request, httpRequest, httpResponse,
              ctx, session);
          if (SecurityUtils.getCurrentUserId() == 0) {
            // 绑定或登录后，一定会有人员id,如未有，则为重定向，不需要往下执行
            return;
          }
        } else if (StringUtils.isNotBlank(request.getParameter("code"))) {
          // 存在微信网页授权code
          logger.info("正在进入移动端用户登录Filter,存在微信网页授权code有且不能获取到psnid,尝试通过微信网页授权code获取psnId");
          this.tryFindPsnIdByWxCode(request, httpRequest, httpResponse, ctx, session);
          if (SecurityUtils.getCurrentUserId() == 0) {
            // 绑定或登录后，一定会有人员id,如未有，则为重定向，不需要往下执行
            return;
          }
        } else {
          // 没有登录，则跳转至移动端登录页面
          logger.info("正在进入移动端用户登录Filter,不能获取到psnid,跳转至移动端登录页面");
          this.sendRedirectToLogin(request, httpRequest, httpResponse, session);
          return;
        }
      }
    } else {
      logger.debug("正在进入移动端用户登录Filter,因存在于忽略列表," + httpRequest.getRequestURI() + "不做处理");
    }
    chain.doFilter(request, response);
  }

  // 获取当前人员id为空，则尝试从人员缓存中获取(用户在移动端登录后)
  private void tryFindPsnIdByCache(HttpServletRequest httpRequest, WebApplicationContext ctx) {
    // if(SecurityUtils.getCurrentUserId()==0){
    // 获取人员信息
    UserCacheService userCacheService = (UserCacheService) ctx.getBean("userCacheService");
    if (userCacheService != null) {
      String sessionId = httpRequest.getParameter("SID");
      if (StringUtils.isBlank(sessionId)) {
        String sid = (String) httpRequest.getSession().getAttribute("oauthSid");
        if (StringUtils.isNotBlank(sid)) {
          sessionId = sid;
        } else {
          sessionId = httpRequest.getSession().getId();
        }
      } else {
        httpRequest.getSession().setAttribute("oauthSid", sessionId);
      }
      Object object = userCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);

      if (object != null) {
        Map<String, Object> detailsMap = (Map<String, Object>) object;
        Object cachedDetail = detailsMap.get("SNS" + "|" + 0);
        if (cachedDetail == null) {
          cachedDetail = detailsMap.get("WCI" + "|" + 0);
        }
        if (cachedDetail != null) {
          UserDetails uDetails = (UserDetails) cachedDetail;
          if (uDetails != null && uDetails.getUsername() != null) {
            TheadLocalPsnId.setPsnId(Long.valueOf(uDetails.getUsername()));
          } else {
            TheadLocalPsnId.setPsnId(0L);
          }
        }
      }
    } else {

    }
    // }
  }

  // 获取当前人员id为空，则尝试从微信微信网页授权code获取
  private void tryFindPsnIdByWxCode(ServletRequest request, HttpServletRequest httpRequest,
      HttpServletResponse httpResponse, WebApplicationContext ctx, HttpSession session) throws IOException {

    try {
      OAuth2Service oAuth2Service = (OAuth2Service) ctx.getBean("oAuth2Service");
      if (oAuth2Service == null) {
        logger.error(
            "在移动端用户登录Filter，微信网页授权code不为空，code=" + request.getParameter("code") + ",但获取OAuth2Service对象为空，跳转至移动端登录页面");
        this.sendRedirectToLogin(request, httpRequest, httpResponse, session);
      }
      // 获取微信psnid
      // -------------- 此处为获取微信openid的方式，需zk或相关负责人补充账号绑定的检查 xys begin
      // --------------
      String weChatOpenId = oAuth2Service.getWeChatOpenId(request.getParameter("code"));
      String weChatToken = oAuth2Service.getWeChatToken();
      Map<String, Object> weChatInfo = oAuth2Service.getWeChatInfo(weChatToken, weChatOpenId);
      String wxUnionId = weChatInfo.get("unionid") == null ? null : weChatInfo.get("unionid").toString();
      if (StringUtils.isNotBlank(wxUnionId)) {
        // 存放wxopenid
        session.setAttribute("wxUnionId", wxUnionId);
        session.setAttribute("wxOpenId", weChatOpenId);
        this.tryFindPsnIdByWxUnionId(wxUnionId, request, httpRequest, httpResponse, ctx, session);
        if (!(TheadLocalPsnId.getPsnId() > 0)) {
          this.tryFindPsnIdByWxOpenId(weChatOpenId, request, httpRequest, httpResponse, ctx, session);
        }
      }
    } catch (Exception e) {
      logger.error("在移动端用户登录Filter，尝试通过微信网页授权code获取psnid出错,跳转至移动端登录页面", e);
      this.sendRedirectToLogin(request, httpRequest, httpResponse, session);
    }
  }

  // 尝试通过wxopenid获取人员id
  private void tryFindPsnIdByWxOpenId(String wxOpenId, ServletRequest request, HttpServletRequest httpRequest,
      HttpServletResponse httpResponse, WebApplicationContext ctx, HttpSession session) throws IOException {
    // 获取人员id
    WeChatRelationService weChatRelationService = (WeChatRelationService) ctx.getBean("weChatRelationService");
    if (weChatRelationService != null) {
      Long psnId = weChatRelationService.querypsnIdByWeChatOpenid(wxOpenId);
      if (psnId != null) {
        TheadLocalPsnId.setPsnId(psnId);
      } else {
        logger.info("在移动端用户登录Filter,通过微信openid(" + wxOpenId + ")获取不到对应psnid，跳转至微信绑定页面");
        this.sendRedirectToBind(request, httpRequest, httpResponse, session);
      }
    } else {
      logger.info("在移动端用户登录Filter,通过微信openid(" + wxOpenId + ")获取不到对应psnid但获取WeChatRelationService对象为空，跳转至移动端登录页面");
      this.sendRedirectToLogin(request, httpRequest, httpResponse, session);
    }
  }

  // 尝试通过wxunionid获取人员id
  private void tryFindPsnIdByWxUnionId(String wxUnionId, ServletRequest request, HttpServletRequest httpRequest,
      HttpServletResponse httpResponse, WebApplicationContext ctx, HttpSession session) throws IOException {
    // 获取人员id
    WeChatRelationService weChatRelationService = (WeChatRelationService) ctx.getBean("weChatRelationService");
    if (weChatRelationService != null) {
      Long psnId = weChatRelationService.querypsnIdByWeChatUnionid(wxUnionId);
      if (psnId != null) {
        TheadLocalPsnId.setPsnId(psnId);
      }
    }
  }

  /**
   * 设置重定向地址到移动端登录页面
   * 
   * @param httpRequest
   * @param request
   * @param response
   * @throws IOException
   */
  private void sendRedirectToLogin(ServletRequest request, HttpServletRequest httpRequest,
      HttpServletResponse httpResponse, HttpSession session) throws IOException {
    logger.info("在移动端用户登录Filter，跳转至移动端登录页面");
    String paramsStr = this.requestParams(request);
    // 需要处理/node-web2/pubweb/xxx中的node-web2,新系统都有这样的问题
    int startOff = httpRequest.getRequestURI().indexOf("/", 1);
    String service = session.getServletContext().getAttribute("domainscm").toString()
        + httpRequest.getRequestURI().substring(startOff);
    if (StringUtils.isNotBlank(paramsStr)) {
      service += "?" + paramsStr;
    }
    httpResponse.sendRedirect(session.getServletContext().getAttribute("domainoauth").toString()
        + "/oauth/mobile/index?service=" + Des3Utils.encodeToDes3(service));
  }

  /**
   * 设置重定向地址到微信端绑定页面
   * 
   * @param httpRequest
   * @param request
   * @param response
   * @param session
   * @throws IOException
   */
  private void sendRedirectToBind(ServletRequest request, HttpServletRequest httpRequest,
      HttpServletResponse httpResponse, HttpSession session) throws IOException {
    logger.info("在移动端用户登录Filter，跳转至微信端绑定页面");
    String paramsStr = this.requestParams(request);
    // 需要处理/node-web2/pubweb/xxx中的node-web2,新系统都有这样的问题
    int startOff = httpRequest.getRequestURI().indexOf("/", 1);
    String service = httpRequest.getRequestURI().substring(startOff);
    if (StringUtils.isNotBlank(paramsStr)) {
      service += "?" + paramsStr;
    }
    String bindUrl =
        session.getServletContext().getAttribute("domainscm").toString() + "/open/wechat/bind?des3WxOpenId=";
    httpResponse.sendRedirect(
        bindUrl + URLEncoder.encode(Des3Utils.encodeToDes3(session.getAttribute("wxOpenId").toString()), "utf-8")
            + "&des3WxUnionId="
            + URLEncoder.encode(Des3Utils.encodeToDes3(session.getAttribute("wxUnionId").toString()), "utf-8") + "&url="
            + URLEncoder.encode(Des3Utils.encodeToDes3(service), "utf-8"));
  }

  /**
   * 请求参数处理
   * 
   * @param request
   * @return
   */
  private String requestParams(ServletRequest request) {
    StringBuffer paramsStr = new StringBuffer();
    Map<String, String[]> paramsMap = request.getParameterMap();
    for (String paramsKey : paramsMap.keySet()) {
      paramsStr.append(paramsKey);
      paramsStr.append("=");
      paramsStr.append(paramsMap.get(paramsKey)[0]);
      paramsStr.append("&");
    }
    String resultStr = paramsStr.toString();
    if (StringUtils.isNotBlank(resultStr) && resultStr.indexOf("&") > 0) {
      return resultStr.substring(0, resultStr.length() - 1);
    } else {
      return "";
    }
  }

  @Override
  public void init(FilterConfig conf) throws ServletException {

  }

  public String getOauthDomain() {
    return oauthDomain;
  }

  public void setOauthDomain(String oauthDomain) {
    this.oauthDomain = oauthDomain;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }
}
