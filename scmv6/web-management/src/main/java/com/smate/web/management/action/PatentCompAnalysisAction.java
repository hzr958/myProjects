package com.smate.web.management.action;

import org.apache.commons.lang.StringUtils;
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
import com.smate.web.management.model.patent.PatentForm;
import com.smate.web.management.service.patent.PatentService;

/**
 * 专利数据对比分析
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "compmain", location = "/WEB-INF/patent/patent_analysis_main.jsp"),
    @Result(name = "categoryLeft", location = "/WEB-INF/patent/subject_category.jsp"),
    @Result(name = "compinfo", location = "/WEB-INF/patent/patent_analysis_info.jsp")

})
public class PatentCompAnalysisAction extends ActionSupport implements ModelDriven<PatentForm>, Preparable {

  /**
   */
  private static final long serialVersionUID = -8619006274817480106L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private PatentForm form;
  private String mmId;
  private String selectedId;

  @Autowired
  private PatentService patentService;

  @Action("/scmmanagement/patent/compmain")
  public String pantentMaint() throws Exception {
    mmId = Struts2Utils.getParameter("mmId");
    selectedId = Struts2Utils.getParameter("selectedId");
    if (StringUtils.isEmpty(mmId) || "null".equals(mmId)) {
      mmId = "mm5";
    }

    if (StringUtils.isEmpty(selectedId) || "null".equals(selectedId)) {
      selectedId = "selectmd1";
    }
    return "compmain";

  }

  @Action("/scmmanagement/patent/ajaxcategoryLeft")
  public String ajaxPantentLeft() throws Exception {
    // 左菜单
    return "categoryLeft";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/patent/ajaxcompinfo")
  public String ajaxPantentInfo() throws Exception {
    try {
      String data = patentService.getPatentCompAnalysisdata(form);
      form.setCdata(data);
    } catch (Exception e) {
      logger.error("获取专利统计对比数据出现错误！", e);
    }
    return "compinfo";
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PatentForm();
    }

  }

  @Override
  public PatentForm getModel() {
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
