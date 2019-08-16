package com.smate.web.management.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.analysis.KeywordsDistrForm;

/**
 * 研究领域关键机构，人员，论文分析
 * 
 * @author Administrator
 *
 */

@Results({@Result(name = "keyModulesmain", location = "/WEB-INF/keywords/keyModules_analysis_main.jsp"),
    @Result(name = "categoryLeft", location = "/WEB-INF/patent/subject_category_keyanalysis.jsp"),
    @Result(name = "keyModulesinfo11", location = "/WEB-INF/keywords/keyModules_analysis_info11.jsp"),
    @Result(name = "keyModulesinfo12", location = "/WEB-INF/keywords/keyModules_analysis_info12.jsp"),
    @Result(name = "keyModulesinfo13", location = "/WEB-INF/keywords/keyModules_analysis_info13.jsp"),
    @Result(name = "keyModulesinfo21", location = "/WEB-INF/keywords/keyModules_analysis_info21.jsp"),
    @Result(name = "keyModulesinfo22", location = "/WEB-INF/keywords/keyModules_analysis_info22.jsp"),
    @Result(name = "keyModulesinfo23", location = "/WEB-INF/keywords/keyModules_analysis_info23.jsp"),
    @Result(name = "keyModulesinfo31", location = "/WEB-INF/keywords/keyModules_analysis_info31.jsp"),
    @Result(name = "keyModulesinfo32", location = "/WEB-INF/keywords/keyModules_analysis_info32.jsp"),
    @Result(name = "keyModulesinfo33", location = "/WEB-INF/keywords/keyModules_analysis_info33.jsp"),
    @Result(name = "keyModulesinfo41", location = "/WEB-INF/keywords/keyModules_analysis_info41.jsp"),
    @Result(name = "keyModulesinfo42", location = "/WEB-INF/keywords/keyModules_analysis_info42.jsp"),
    @Result(name = "keyModulesinfo43", location = "/WEB-INF/keywords/keyModules_analysis_info43.jsp"),
    @Result(name = "keyModulesinfo51", location = "/WEB-INF/keywords/keyModules_analysis_info51.jsp"),
    @Result(name = "keyModulesinfo52", location = "/WEB-INF/keywords/keyModules_analysis_info52.jsp"),
    @Result(name = "keyModulesinfo53", location = "/WEB-INF/keywords/keyModules_analysis_info53.jsp"),
    @Result(name = "keyModulesinfo61", location = "/WEB-INF/keywords/keyModules_analysis_info61.jsp"),
    @Result(name = "keyModulesinfo62", location = "/WEB-INF/keywords/keyModules_analysis_info62.jsp"),
    @Result(name = "keyModulesinfo63", location = "/WEB-INF/keywords/keyModules_analysis_info63.jsp"),
    @Result(name = "keyModulesinfo71", location = "/WEB-INF/keywords/keyModules_analysis_info71.jsp"),
    @Result(name = "keyModulesinfo72", location = "/WEB-INF/keywords/keyModules_analysis_info72.jsp"),
    @Result(name = "keyModulesinfo73", location = "/WEB-INF/keywords/keyModules_analysis_info73.jsp"),
    @Result(name = "keyModulesinfo81", location = "/WEB-INF/keywords/keyModules_analysis_info81.jsp"),
    @Result(name = "keyModulesinfo82", location = "/WEB-INF/keywords/keyModules_analysis_info82.jsp"),
    @Result(name = "keyModulesinfo83", location = "/WEB-INF/keywords/keyModules_analysis_info83.jsp")

})
public class ResearchAreasKeyModulesAction extends ActionSupport implements ModelDriven<KeywordsDistrForm>, Preparable {

  /**
   */
  private static final long serialVersionUID = -8619006274817480106L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private KeywordsDistrForm form;
  private String mmId;
  private String selectedId;

  @Action("/scmmanagement/keywordsdistr/keyModulesmain")
  public String pantentMaint() throws Exception {
    mmId = Struts2Utils.getParameter("mmId");
    selectedId = Struts2Utils.getParameter("selectedId");
    if (StringUtils.isEmpty(mmId) || "null".equals(mmId)) {
      mmId = "mm4";
    }

    if (StringUtils.isEmpty(selectedId) || "null".equals(selectedId)) {
      selectedId = "selectmd1";
    }
    return "keyModulesmain";

  }

  @Action("/scmmanagement/keywordsdistr/ajaxkwcategoryLeft")
  public String ajaxPantentLeft() throws Exception {
    // 左菜单
    return "categoryLeft";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/keywordsdistr/ajaxkeyModulesinfo")
  public String ajaxPantentInfo() throws Exception {
    Integer modId = form.getModId();
    Integer mId = form.getmId();
    if (modId == null || mId == null) {
      return "keyModulesinfo11";
    } else {
      String pageId = String.valueOf(mId + 1) + String.valueOf(modId);
      String pageName = "keyModulesinfo" + pageId;
      return pageName;
    }
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new KeywordsDistrForm();
    }

  }

  @Override
  public KeywordsDistrForm getModel() {
    return form;
  }

  public String getMmId() {
    return mmId;
  }

  public void setMmId(String mmId) {
    this.mmId = mmId;
  }

  public String getSelectedId() {
    return selectedId;
  }

  public void setSelectedId(String selectedId) {
    this.selectedId = selectedId;
  }

}
