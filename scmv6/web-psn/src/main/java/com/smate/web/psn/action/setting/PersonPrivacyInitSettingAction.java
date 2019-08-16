package com.smate.web.psn.action.setting;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.setting.UserSettings;
import com.smate.web.psn.service.setting.UserSettingsService;

/**
 * 人员隐私设置
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "init-privacy", location = "/WEB-INF/jsp/psnsetting/privacy/privacy_set.jsp")})
public class PersonPrivacyInitSettingAction extends ActionSupport implements ModelDriven<PsnSettingForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -7496914505522794654L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private PsnSettingForm form;


  @Autowired
  private UserSettingsService userSettingsService;

  /**
   * 响应隐私设置请求.
   * 
   * @author MJG
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxprivacyinit")
  public String initPrivacySettings() throws Exception {

    form.setUserSettings(userSettingsService.getPrivacyConfig());
    return "init-privacy";

  }

  @Action("/psnweb/psnsetting/privacy/ajaxsaveprivacyconfig")
  public String ajaxSavePrivacyCofig() throws Exception {
    try {
      String dataJson = form.getUserSettings().getPrivacyConfig();
      userSettingsService.savePrivacyConfig(dataJson);
      Struts2Utils.renderJson("{\"action\":\"success\"}", "encoding:UTF-8");

    } catch (Exception e) {

      Struts2Utils.renderJson("{\"action\":\"failure\"}", "encoding:UTF-8");
      logger.error("隐私配置保存失败！");
    }
    return null;
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



}
