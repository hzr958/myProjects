package com.smate.center.oauth.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.smate.core.base.utils.mobile.SmateMobileUtils;

/**
 * SCM-14486-移动端地址跳转功能
 * 
 * @author zzx
 *
 */
@Component
public class BrowserRedirectFilter implements Filter {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private List<String> midUriList;
  private List<String> pcUriList;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    midUriList = new ArrayList<String>();
    pcUriList = new ArrayList<String>();
    // 首页
    midUriList.add("/dynweb/mobile/dynshow");
    pcUriList.add("/dynweb/main");
    // 个人主页
    midUriList.add("/psnweb/mobile/homepage");
    pcUriList.add("/psnweb/homepage/show");
    // 站外个人主页
    midUriList.add("/psnweb/mobile/outhome");
    pcUriList.add("/psnweb/outside/homepage");
    // 个人主页-短地址P
    midUriList.add("/psnweb/mobile/homepage");
    pcUriList.add("/WEB-INF/jsp/psnprofile/psn_homepage_main.jsp");
    // 个人主页-短地址P
    midUriList.add("/psnweb/mobile/homepage");
    pcUriList.add("/WEB-INF/jsp/outsidehomepage/outside_psn_homepage_main.jsp");
    // 联系
    midUriList.add("/psnweb/mobile/relationmain");
    pcUriList.add("/psnweb/friend/main");
    // 消息
    midUriList.add("/psnweb/mobile/msgbox");
    pcUriList.add("/dynweb/showmsg/msgmain");
    // 我的成果
    midUriList.add("/pub/querylist/psn");
    pcUriList.add("/psnweb/homepage/show?module=pub");
    // 成果详情
    midUriList.add("/pub/details/snsnonext");
    pcUriList.add("/pubweb/outside/details");
    // 成果详情
    // midUriList.add("/pubweb/wechat/findpubxml");
    // pcUriList.add("/pubweb/outside/details");
    // 成果详情
    midUriList.add("/pub/details/snsnonext");
    pcUriList.add("/pubweb/details/show");
    // 我的项目
    midUriList.add("/prjweb/wechat/prjmain");
    pcUriList.add("/psnweb/homepage/show?module=prj");
    // 我的好友
    midUriList.add("/psnweb/mobile/friendlist");
    pcUriList.add("/psnweb/friend/main");
    // 基金
    midUriList.add("/prjweb/wechat/findfunds");
    pcUriList.add("/prjweb/fund/main");
    // 基金详情
    midUriList.add("/prjweb/wechat/findfundsxml");
    pcUriList.add("/prjweb/funddetails/show");
    // 基金站外详情
    midUriList.add("/prjweb/wechat/findfundsxml");
    pcUriList.add("/prjweb/outside/showfund");
    // 忘记密码
    midUriList.add("/oauth/mobile/pwd/forget/index");
    pcUriList.add("/oauth/pwd/forget/index");
    // 注册
    midUriList.add("/oauth/mobile/register");
    pcUriList.add("/oauth/pc/register");
    // 项目详情
    midUriList.add("/prjweb/wechat/findprjxml");
    pcUriList.add("/prjweb/outside/project/detailsshow");

    midUriList.add("/prjweb/wechat/findprjxml");
    pcUriList.add("/prjweb/project/detailsshow");
    // 忘记密码
    midUriList.add("/oauth/mobile/pwd/reset/index");
    pcUriList.add("/oauth/pwd/reset/index");
    // 忘记密码
    midUriList.add("/oauth/mobile/pwd/reset/toreset");
    pcUriList.add("/oauth/pwd/reset/toreset");
    // 全站检索--论文
    midUriList.add("/pub/paper/search");
    pcUriList.add("/pub/search/pdwhpaper");
    // 全站检索--专利
    midUriList.add("/pub/patent/search");
    pcUriList.add("/pub/search/pdwhpatent");
    // 全站检索--人员
    midUriList.add("/psnweb/mobile/search");
    pcUriList.add("/pub/search/psnsearch");
    // 发现群组
    midUriList.add("/grp/mobile/findgroupmain");
    pcUriList.add("/groupweb/mygrp/main?model=rcmdGrp");
    // 我的群组
    midUriList.add("/grp/mobile/mygroupmain");
    pcUriList.add("/groupweb/mygrp/main");
    // 群组首页
    midUriList.add("/grp/outside/main");
    pcUriList.add("/groupweb/grpinfo/main?model=discuss");
    midUriList.add("/grp/main");
    pcUriList.add("/groupweb/grpinfo/outside/main");
    midUriList.add("/grp/outside/main");
    pcUriList.add("/groupweb/grpinfo/outside/main");
    // 群组成员
    midUriList.add("/grp/outside/member/main");
    pcUriList.add("/groupweb/grpinfo/main?model=member");
    midUriList.add("/grp/member/main");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=member");
    midUriList.add("/grp/outside/member/main");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=member");
    // 群组成果
    midUriList.add("/grp/outside/mobile/grppubmain?showPrjPub=1");
    pcUriList.add("/groupweb/grpinfo/main?model=pub");
    midUriList.add("/grp/mobile/grppubmain?showPrjPub=1");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=pub");
    midUriList.add("/grp/outside/mobile/grppubmain?showPrjPub=1");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=pub");
    // 群组文献
    midUriList.add("/grp/outside/mobile/grppubmain?showRefPub=1");
    pcUriList.add("/groupweb/grpinfo/main?model=projectRef");
    midUriList.add("/grp/mobile/grppubmain?showRefPub=1");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=projectRef");
    midUriList.add("/grp/outside/mobile/grppubmain?showRefPub=1");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=projectRef");
    // 群组文件
    midUriList.add("/grp/outside/main/grpfilemain");
    pcUriList.add("/groupweb/grpinfo/main?model=file");
    midUriList.add("/grp/main/grpfilemain");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=file");
    midUriList.add("/grp/outside/main/grpfilemain");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=file");
    // 群组课件
    midUriList.add("/grp/outside/main/grpfilemain?courseFileType=2");
    pcUriList.add("/groupweb/grpinfo/main?model=curware");
    midUriList.add("/grp/main/grpfilemain?courseFileType=2");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=curware");
    midUriList.add("/grp/outside/main/grpfilemain?courseFileType=2");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=curware");
    // 群组作业
    midUriList.add("/grp/outside/main/grpfilemain?workFileType=1");
    pcUriList.add("/groupweb/grpinfo/main?model=work");
    midUriList.add("/grp/main/grpfilemain?workFileType=1");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=work");
    midUriList.add("/grp/outside/main/grpfilemain?workFileType=1");
    pcUriList.add("/groupweb/grpinfo/outside/main?model=work");
    // 科研验证
    midUriList.add("/dynweb/mobile/dynshow");
    pcUriList.add("/application/validate/maint");
  }

  public static void main(String[] args) {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    // 获取uri
    String uri = httpServletRequest.getServletPath();
    String param = httpServletRequest.getQueryString() == null ? "" : httpServletRequest.getQueryString();
    // 检查是否是需要拦截的请求
    int indexPcUri = getIndex(pcUriList, uri, param);
    int indexMidUri = getIndex(midUriList, uri, param);

    if (indexPcUri >= 0 || indexMidUri >= 0) {// A-检查是否是需要拦截的请求-是-则检查当前系统环境是否匹配
      try {
        // 判断当前系统环境是否是移动端
        boolean isMid = judgmentIsMid(httpServletRequest);
        boolean isWechat = SmateMobileUtils.isWeChatBrowser(httpServletRequest.getHeader("User-Agent"));
        if ((isMid || isWechat) && indexPcUri >= 0) {// B-系统是移动端或是微信浏览器，请求是pc端，则自动跳转移动页面
          String midUri = midUriList.get(indexPcUri);
          httpServletResponse
              .sendRedirect(domainMobile + midUri + (midUri.indexOf("?") < 0 ? ("?" + param) : ("&" + param)));
        } else if (!isMid && !isWechat && indexMidUri >= 0) {// B-系统是pc端且不是微信浏览器，请求是移动端，则自动跳转pc页面
          String pcUri = pcUriList.get(indexMidUri);
          httpServletResponse
              .sendRedirect(domainscm + pcUri + (pcUri.indexOf("?") < 0 ? ("?" + param) : ("&" + param)));
        } else {// B-系统和请求一致，则跳过
          chain.doFilter(request, response);
        }
      } catch (Exception e) {
        logger.error("过滤器-移动端地址跳转功能出错", e);
        chain.doFilter(request, response);
      }
    } else {// A-检查是否是需要拦截的请求-否-则直接跳过
      chain.doFilter(request, response);
    }
  }

  /**
   * 获取当前url在存放的list中的位置，如果存放的参数，需要匹配
   * 
   * @param uriList
   * @param currentUri
   * @param currentParam
   * @return
   */
  private int getIndex(List<String> uriList, String currentUri, String currentParam) {
    int index = -1;
    Pattern uriPat = Pattern.compile("^" + currentParam + "(\\?.*$|$)");
    for (int i = 0; i < uriList.size(); i++) {
      String uri = uriList.get(i);
      if (uriPat.matcher(uri).matches()) {// 匹配了uri，下面匹配参数
        String[] uriStr = uri.split("\\?");
        String paramStr = uriStr.length > 1 ? uriStr[1] : "";
        // 带有参数的，需要存放的url的参数都要匹配到当前url的参数，存放的url带有参数而当前的url没有参数，认为不匹配上
        if (StringUtils.isNotBlank(paramStr) && StringUtils.isNotBlank(currentParam)) {// 带有参数的，需要存放的url的参数都要匹配到当前url的参数，
          boolean compare = true;
          for (String param : paramStr.split("&")) {
            if (!compare) {
              break;
            }
            compare = false;
            if (currentParam.contains(param)) {
              compare = true;
            }
          }
          index = compare ? i : index;
        } else if (StringUtils.isBlank(paramStr)) {
          index = i;
        }
      }
    }
    return index;
  }

  /**
   * 判断当前系统是不是移动端
   * 
   * @param httpServletRequest
   * @return
   */
  private boolean judgmentIsMid(HttpServletRequest httpServletRequest) {
    String ua = httpServletRequest.getHeader("User-Agent");
    String regex =
        "ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini";
    Pattern p = Pattern.compile(regex);
    return p.matcher(ua.toLowerCase()).find();
  }

  @Override
  public void destroy() {}

}
