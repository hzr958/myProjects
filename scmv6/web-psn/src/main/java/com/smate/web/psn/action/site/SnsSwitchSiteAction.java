package com.smate.web.psn.action.site;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.web.psn.service.site.SwitchSiteService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * 切换站点 ajb
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "switch_site", location = "/WEB-INF/jsp/site/switch-site.jsp")})
public class SnsSwitchSiteAction extends ActionSupport implements ModelDriven<UserRolDataForm>, Preparable {

  private static final long serialVersionUID = -8835910416516102177L;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainsns;
  private UserRolDataForm form;

  @Resource
  private SwitchSiteService switchSiteService;

  /**
   * 切换站点
   * 
   * @return
   */
  @Action("/psnweb/site/switchsite")
  public String switchSite() {
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        switchSiteService.findUserRolSite(form);
      }
    } catch (Exception e) {
      logger.error("切换站点异常psnId=" + form.getPsnId(), e);
    }
    return "switch_site";
  }

  public String getDomainsns() {
    return domainsns;
  }

  public void setDomainsns(String domainsns) {
    this.domainsns = domainsns;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new UserRolDataForm();
    }
  }

  @Override
  public UserRolDataForm getModel() {
    return form;
  }

  public UserRolDataForm getForm() {
    return form;
  }

  public void setForm(UserRolDataForm form) {
    this.form = form;
  }

}
