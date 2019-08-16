package com.smate.web.psn.action;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

public class LoginToCXCAction extends ActionSupport {

  private static final long serialVersionUID = -7195572820070377389L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Value("${login.to.cxc}")
  private String CXCUrl;
  @Value("${login.to.mcxc}")
  private String mCXCUrl;

  @Action("/psnweb/login/tocxc")
  public String testLoginToCXC() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    try {
      String targetUrl = Struts2Utils.getParameter("target_url");
      String redirectUrl = CXCUrl;
      boolean isMobile = SmateMobileUtils.isMobileBrowser(Struts2Utils.getRequest().getHeader("user-agent"));
      // 获取动态 openid
      String dynOpenid = DigestUtils.md5Hex(UUID.randomUUID().toString() + currentPsnId);
      cacheService.put("DYN_OPENID_CACHE", 3 * 60, dynOpenid + "_" + "cba4b03f", currentPsnId);
      if (isMobile) {
        redirectUrl = mCXCUrl;
      }
      // 目标地址不为空则拼接上目标地址
      if (StringUtils.isNotBlank(targetUrl)) {
        redirectUrl += "?return_url=" + targetUrl + "&openId=" + dynOpenid;
      } else {
        redirectUrl += "?openId=" + dynOpenid;
      }
      Struts2Utils.getResponse().sendRedirect(redirectUrl);
    } catch (IOException e) {
      logger.error("跳转到创新城出错, psnId = " + currentPsnId, e);
    }
    return null;
  }
}
