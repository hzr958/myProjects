package com.smate.web.dyn.action.parseurl;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.service.parseurl.ParseLinkeService;

/**
 * 解析url
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "testUrl", location = "/WEB-INF/jsp/mobile/urltest.jsp")})
public class ParseLinkeAction {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String shareUrl;
  @Autowired
  private ParseLinkeService parseLinkeService;

  /**
   * 解析url获取meta中的og:title和og:image
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxresolve")
  public String ajaxURLResolve() {
    try {
      Map<String, String> map = parseLinkeService.resolveUrl(shareUrl);
      if (map == null) {
        return null;
      }
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (IOException e) {
      logger.error("解析url出错，url=" + shareUrl, e);
    }
    return null;
  }

  public String getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }
}
