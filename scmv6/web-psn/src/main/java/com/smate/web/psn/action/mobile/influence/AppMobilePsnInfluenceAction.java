package com.smate.web.psn.action.mobile.influence;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.influence.InfluenceForm;
import com.smate.web.psn.service.psninfluence.PsnInfluenceService;

@Results({@Result(name = "main", location = "/WEB-INF/jsp/influence/app/app_psn_influence_main.jsp")})
public class AppMobilePsnInfluenceAction extends ActionSupport implements ModelDriven<InfluenceForm>, Preparable {
  private static final long serialVersionUID = -1197189579131344312L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private InfluenceForm form;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnInfluenceService psnInfluenceService;

  @Action("/app/psnweb/outside/mobile/influence")
  public String psnInfluenceMain() {
    form.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    form.setDomainscm(domainscm);
    return "main";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new InfluenceForm();
    }
  }

  @Override
  public InfluenceForm getModel() {
    return form;
  }

}
