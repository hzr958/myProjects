package com.smate.web.psn.action.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.AttPersonInfo;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.setting.UserSettings;
import com.smate.web.psn.service.setting.UserSettingsService;

/**
 * 人员关注设置
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "attention_set", location = "/WEB-INF/jsp/psnsetting/attention/attention_set.jsp"),
    @Result(name = "attention_psn_list", location = "/WEB-INF/jsp/psnsetting/attention/attention_psn_list.jsp")})
public class PersonAttentionSettingAction extends ActionSupport implements ModelDriven<PsnSettingForm>, Preparable {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private PsnSettingForm form;

  @Autowired
  private UserSettingsService userSettingsService;

  /**
   * 
   */
  private static final long serialVersionUID = -7496914505522794654L;

  @Action("/psnweb/psnsetting/ajaxattentioninit")
  public String initAttSettings() throws Exception {
    try {
      // UserSettings userSettings = userSettingsService.getAttConfig();
      Long psnId = SecurityUtils.getCurrentUserId();
      Long count = userSettingsService.getAllAttPsnCount(psnId);
      form.getUserSettings().setTotalCount(count);

    } catch (Exception e) {
      logger.error("读取配置信息出现异常！");
    }
    return "attention_set";
  }

  /**
   * 读取加载关注人员列表.
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxLoadAttPersonList")
  public String ajaxLoadAttPersonList() throws Exception {

    try {
      List<AttPersonInfo> attPersonInfoList = userSettingsService.loadAttPersonList(form.getUserSettings());
      form.setAttPersonInfoList(attPersonInfoList);
    } catch (Exception e) {

      logger.error("读取人员关注列表信息失败！", e);
    }
    return "attention_psn_list";
  }

  @Action("/psnweb/psnsetting/ajaxCancelDynAttPerson")
  public String ajaxCancelDynAttPerson() throws Exception {
    Map<String, String> json = new HashMap<String, String>(2);
    try {
      UserSettings userSettings = new UserSettings();
      userSettingsService.getAttPersonId(form);
      if (form.getAttPersonId() != null) {
        userSettings.setCancelId(form.getAttPersonId());
        userSettingsService.cancelAttPerson(userSettings);
        json.put("result", "success");
      } else {
        json.put("result", "error");
      }
    } catch (Exception e) {
      json.put("result", "error");
      logger.error("取消关注操作失败！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  @Action("/psnweb/psnsetting/ajaxCancelAttPerson")
  public String ajaxCancelAttPerson() throws Exception {
    Map<String, String> json = new HashMap<String, String>(2);
    try {
      UserSettings userSettings = form.getUserSettings();
      userSettingsService.cancelAttPerson(userSettings);
      json.put("result", "success");
    } catch (Exception e) {
      json.put("result", "error");
      logger.error("取消关注操作失败！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  @Action("/psnweb/psnsetting/ajaxAttPsnCount")
  public String ajaxLoadAttPsnCount() throws Exception {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long attPsnCount = userSettingsService.getAllAttPsnCount(psnId);
      Struts2Utils.renderJson("{\"action\":\"success\",\"attCount\":" + attPsnCount + "}", "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("读取关注人员总数出现异常！");
    }
    return null;

  }

  @Action("/psnweb/psnsetting/ajaxSaveAttFrd")
  public String ajaxSaveAttFrd() throws Exception {
    String des3PsnIds = form.getDes3PsnIds();
    List<Long> psnIdList = new ArrayList<>();
    if (StringUtils.isEmpty(des3PsnIds) || des3PsnIds.matches("[,\\s]*")) {
      logger.error("非法参数传入！");
      Struts2Utils.renderJson("{\"action\":\"failure\"}", "encoding:UTF-8");
      return null;
    }
    try {
      String[] des3PsnIdsArr = des3PsnIds.split(",");
      for (String des3PsnId : des3PsnIdsArr) {
        String psnId = ServiceUtil.decodeFromDes3(des3PsnId);
        if (StringUtils.isBlank(psnId) || SecurityUtils.getCurrentUserId().equals(new Long(psnId))) {
          continue;
        }
        if (NumberUtils.isNumber(psnId)) {
          psnIdList.add(Long.parseLong(psnId));
        }
      }
      // -1保存失败;0该该人已经被关注;>0保存成功，不需更新。
      long result = userSettingsService.saveAttFrdList(psnIdList);
      if (result == -1) {
        Struts2Utils.renderJson("{\"action\":\"failure\"}", "encoding:UTF-8");
      } else if (result > 0) {
        Struts2Utils.renderJson("{\"action\":\"success\", \"attPsnId\":\"" + result + "\"}", "encoding:UTF-8");
      }

    } catch (Exception e) {
      logger.error("保存关注好友信息失败！", e);
      Struts2Utils.renderJson("{\"action\":\"failure\"}", "encoding:UTF-8");

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
