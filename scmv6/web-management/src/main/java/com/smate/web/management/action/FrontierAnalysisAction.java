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
 * 前沿分析
 * 
 * @author LJ
 *
 */
@Results({@Result(name = "frontiermain", location = "/WEB-INF/keywords/frontier_analysis_main.jsp"),
    @Result(name = "categoryLeft", location = "/WEB-INF/patent/subject_category.jsp"),
    @Result(name = "frontierinfo0", location = "/WEB-INF/keywords/frontier_analysis_info.jsp"),
    @Result(name = "frontierinfo1", location = "/WEB-INF/keywords/frontier_analysis_info_1.jsp"),
    @Result(name = "frontierinfo2", location = "/WEB-INF/keywords/frontier_analysis_info_2.jsp"),
    @Result(name = "frontierinfo3", location = "/WEB-INF/keywords/frontier_analysis_info_3.jsp"),
    @Result(name = "frontierinfo4", location = "/WEB-INF/keywords/frontier_analysis_info_4.jsp"),
    @Result(name = "frontierinfo5", location = "/WEB-INF/keywords/frontier_analysis_info_5.jsp"),
    @Result(name = "frontierinfo6", location = "/WEB-INF/keywords/frontier_analysis_info_6.jsp"),
    @Result(name = "frontierinfo7", location = "/WEB-INF/keywords/frontier_analysis_info_7.jsp")

})
public class FrontierAnalysisAction extends ActionSupport implements ModelDriven<KeywordsDistrForm>, Preparable {

  /**
   */
  private static final long serialVersionUID = -8619006274817480106L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private KeywordsDistrForm form;
  private String mmId;
  private String selectedId;

  @Action("/scmmanagement/keywordsdistr/frontiermain")
  public String pantentMaint() throws Exception {
    mmId = Struts2Utils.getParameter("mmId");
    selectedId = Struts2Utils.getParameter("selectedId");

    if (StringUtils.isEmpty(mmId) || "null".equals(mmId)) {
      mmId = "mm3";
    }

    if (StringUtils.isEmpty(selectedId) || "null".equals(selectedId)) {
      selectedId = "selectmd1";
    }
    return "frontiermain";

  }

  @Action("/scmmanagement/keywordsdistr/ajaxcategoryLeft")
  public String ajaxPantentLeft() throws Exception {
    // 左菜单
    return "categoryLeft";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/keywordsdistr/ajaxfrontierinfo")
  public String ajaxPantentInfo() throws Exception {
    Integer mId = form.getmId();
    String pageName = "frontierinfo";
    if (mId != null && mId <= 7 && mId >= 0) {
      pageName = pageName + String.valueOf(mId);
    }
    return pageName;

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
