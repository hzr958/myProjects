package com.smate.web.psn.action.setting;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.web.psn.form.PsnSettingForm;

/**
 * 个人设置 action
 * 
 * @author tsz
 **/

@Results({@Result(name = "main", location = "/WEB-INF/jsp/psnsetting/base/main.jsp")})
public class PersonSettingAction extends ActionSupport implements ModelDriven<PsnSettingForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -7496914505522794654L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private PsnSettingForm form;

  @Value("${domainscm}")
  private String domainscm;

  /**
   * 
   * 个人设置主页
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/main")
  public String initPrivacySettings() throws Exception {
    return "main";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnSettingForm();
    }
  }

  @Override
  public PsnSettingForm getModel() {
    return form;
  }

  public String getDomainscm() {
    return domainscm;
  }

  public void setDomainscm(String domainscm) {
    this.domainscm = domainscm;
  }

}
