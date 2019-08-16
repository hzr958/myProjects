package com.smate.web.psn.action;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 跳转到完善信息页面
 * 
 * @author Administrator
 *
 */

@Results({@Result(name = "improve", location = "/WEB-INF/jsp/mobile/ImproveWorkOrEducationHistory.jsp")})
public class ImprovePsnInfoAction extends ActionSupport {

  private static final long serialVersionUID = 1907028260167453355L;

  private String wxOpenId;

  @Autowired
  private SysDomainConst sysDomainConst;

  @Value("${domainMobile}")
  private String domainMobile; // 移动端域名

  private String wxUrl;

  @Value("${domainInnocity}")
  private String domainInnocity; // 创新城域名
  // 第三方判断(CXC_LOGIN:创新城)
  private String sysType;
  private String service;

  /**
   * 跳转到完善信息页面
   * 
   * @return
   */
  @Action("/psnweb/mobile/improveInfo")
  public String goToImprovePsnInfo() {
    try {
      Struts2Utils.getRequest().setAttribute("snsDomain", sysDomainConst.getSnsDomain());
      if (StringUtils.isNotBlank(wxUrl)) {
        wxUrl = URLEncoder.encode(wxUrl, "utf-8");
      }
    } catch (Exception e) {
    }
    return "improve";
  }

  public String getWxOpenId() {
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getWxUrl() {
    return wxUrl;
  }

  public void setWxUrl(String wxUrl) {
    this.wxUrl = wxUrl;
  }

  public String getDomainMobile() {
    return domainMobile;
  }

  public void setDomainMobile(String domainMobile) {
    this.domainMobile = domainMobile;
  }

  public String getSysType() {
    return sysType;
  }

  public void setSysType(String sysType) {
    this.sysType = sysType;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

}
