package com.smate.web.psn.action.pc.improve;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.service.profile.PsnInfoImproveService;

/**
 * 人员信息完善Action
 * 
 * @author WSN
 *
 *         2017年10月17日 上午10:47:05
 *
 */
public class PsnImproveInfoAction extends ActionSupport implements ModelDriven<PersonProfileForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -4780356344356856630L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private PersonProfileForm form;

  @Autowired
  private PsnInfoImproveService psnInfoImproveService;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonProfileForm();
    }

  }

  @Override
  public PersonProfileForm getModel() {
    return form;
  }

  /**
   * 注册用户完善关键词页面
   * 
   * @return
   */
  @Action("/psnweb/improve/keywords")
  public String improveRegisterKeyWords() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form = psnInfoImproveService.buildPsnImproveKeywordsInfo(form);
    } catch (Exception e) {
      logger.error("构建人员关键词息完善页面出错， psnId = " + form.getPsnId(), e);
    }
    return "register_improve_keywords";
  }

}
