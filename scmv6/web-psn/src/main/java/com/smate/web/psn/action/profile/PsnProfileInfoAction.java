package com.smate.web.psn.action.profile;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.service.profile.PsnAliasService;

/**
 * 获取人员部分信息
 * 
 * @author wsn
 * @date 2018年8月11日
 */
public class PsnProfileInfoAction extends ActionSupport implements ModelDriven<PersonProfileForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private PersonProfileForm form;
  @Autowired
  private PsnAliasService psnAliasService;

  /**
   * 获取人员别名
   * 
   * @throws Exception
   */
  @Action("/psnweb/psnalias/initjson")
  public String ajaxInitPsnAliasFRDB() throws Exception {
    String dbCodes = form.getDbCode();
    String psnName = form.getName();
    Long psnId = SecurityUtils.getCurrentUserId();
    String json = null;
    try {
      if (StringUtils.isNotBlank(dbCodes) && StringUtils.isNotBlank(psnName)) {
        json = this.psnAliasService.getAllPsnAliasToJson(psnId, psnName, dbCodes);
      } else {
        json = "{\"result\":\"error\"}";
      }
    } catch (Exception e) {
      logger.error(e.toString() + "数据取出错误!");
      Struts2Utils.renderJson("{\"result\":\"error\"}", "encoding:UTF-8");
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  /**
   * 更新人员别名
   * 
   * @throws Exception
   */
  @Action("/psnweb/psnalias/ajaxupdate")
  public void ajaxUpdatePsnAlias() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    String dbCode = Struts2Utils.getParameter("dbcode");
    String psnName = Struts2Utils.getParameter("psnName");
    String psnAliasNames = Struts2Utils.getParameter("psnAliasNames");
    String json = "";
    try {
      json = psnAliasService.saveOrDeletePsnAlias(psnId, psnName, dbCode, psnAliasNames);
    } catch (Exception e) {
      logger.error("数据存储出错了", e);
    }
    Struts2Utils.renderJson("{\"result\":\"success\",\"data\":" + json + "}", "encoding:UTF-8");
  }

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
}
