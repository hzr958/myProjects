package com.smate.web.psn.action.mobile.site;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.wechat.WechatBaseAction;

/**
 * 切换站点
 *
 * @author wsn
 *
 */
@Results({@Result(name = "siteList", location = "/WEB-INF/jsp/mobile/site/site_list.jsp")})
public class SiteListViewAction extends WechatBaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private Long psnId;

  /**
   * 站点页面
   * 
   * @return
   */
  @Action("/psnweb/mobile/sitelistview")
  public String loadPsnList() {
    psnId = SecurityUtils.getCurrentUserId();
    return "siteList";
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
