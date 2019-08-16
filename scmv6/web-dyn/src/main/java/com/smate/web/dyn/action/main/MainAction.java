package com.smate.web.dyn.action.main;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.main.MainForm;
import com.smate.web.dyn.service.main.ShowMainService;

/**
 * 科研之友--主页Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "main", location = "/WEB-INF/jsp/main/main.jsp"),
    @Result(name = "main_shortcuts", location = "/WEB-INF/jsp/main/main_shortcuts.jsp")})
public class MainAction extends ActionSupport implements Preparable, ModelDriven<MainForm> {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MainForm form;
  @Resource
  private ShowMainService showMainService;

  /**
   * 科研之友--主页main
   * 
   * @return
   */
  @Action("/dynweb/main")
  public String main() {
    try {
      showMainService.main(form);
    } catch (Exception e) {
      logger.error("进入科研之友--主页main出错,form=" + form, e);
    }
    return "main";
  }

  /**
   * 科研之友--移动端主页main
   * 
   * @return
   */
  @Action("mobile/dynweb/main")
  public String mobileMain() {
    try {
      showMainService.main(form);
    } catch (Exception e) {
      logger.error("进入科研之友--移动端主页main出错,form=" + form, e);
    }
    return "main";
  }

  @Action("/dynweb/main/ajaxgetdynshortcuts")
  public String dynShortcuts() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // form.setIp(Struts2Utils.getRemoteAddr());
      // showMainService.mainShortcuts(form);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取首页常用功能出错", e);
    }
    return "main_shortcuts";
  }

  @Action("/dynweb/main/ajaxgetmenuitem")
  public String dynMenuItem() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setIp(Struts2Utils.getRemoteAddr());
      showMainService.mainShortcuts(form);
      map.put("result", "success");
      map.put("status", form.getStatus());
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取首页应用菜单出错", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public MainForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new MainForm();
    }

  }

}
