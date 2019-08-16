package com.smate.web.psn.action.mobile.data.psninfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.service.profile.PsnHomepageService;

/**
 * 获取人员信息
 * 
 * @author wsn
 * @date Apr 29, 2019
 */
public class PsnDataAction extends ActionSupport implements ModelDriven<PersonProfileForm>, Preparable {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private PersonProfileForm form;
  @Autowired
  private PsnHomepageService psnHomepageService;

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
   * 移动端 编辑人员主页头部信息
   */
  @SuppressWarnings("unchecked")
  @Action("/psndata/mobile/names")
  public void findPsnNames() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(form.getPsnId())) {
        form = psnHomepageService.getPsnNameInfo(form);
        map.put("result", "success");
        map.put("data", form);
      } else {
        map.put("result", "error");
        map.put("errMsg", "psnId is null");
      }
    } catch (Exception e) {
      logger.error("移动端获取人员主页头部信息出错, psnId = " + form.getPsnId(), e);
      map.put("result", "error");
      map.put("errMsg", e.getClass());
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }


  /**
   * 移动端 编辑人员主页头部信息
   */
  @SuppressWarnings("unchecked")
  @Action("/psndata/account")
  public void findPsnAccount() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(form.getPsnId())) {
        String psnAccount = psnHomepageService.findPsnAccount(form.getPsnId());
        map.put("status", "success");
        map.put("account", Objects.toString(psnAccount, ""));
      } else {
        map.put("status", "error");
        map.put("errMsg", "psnId is null");
      }
    } catch (Exception e) {
      logger.error("移动端获取人员主页头部信息出错, psnId = {}", form.getPsnId(), e);
      map.put("status", "error");
      map.put("errMsg", e.getClass());
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

}


