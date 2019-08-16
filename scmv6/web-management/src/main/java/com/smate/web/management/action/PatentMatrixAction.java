package com.smate.web.management.action;

import java.util.Map;

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
 * 专利矩阵
 * 
 * @author LJ
 *
 */

@Results({@Result(name = "matrixmain", location = "/WEB-INF/patent/patent_matrix_main.jsp"),
    @Result(name = "categoryLeft", location = "/WEB-INF/patent/subject_category.jsp"),
    @Result(name = "matrixinfo", location = "/WEB-INF/patent/patent_matrix_info.jsp")

})


public class PatentMatrixAction extends ActionSupport implements ModelDriven<PatentForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -2160711348639897333L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private PatentForm form;
  private String mmId;
  private String selectedId;

  @Autowired
  private PatentService patentService;

  @Action("/scmmanagement/patent/matrixmain")
  public String pantentMaint() throws Exception {
    mmId = Struts2Utils.getParameter("mmId");
    selectedId = Struts2Utils.getParameter("selectedId");
    if (StringUtils.isEmpty(mmId) || "null".equals(mmId)) {
      mmId = "mm7";
    }

    if (StringUtils.isEmpty(selectedId) || "null".equals(selectedId)) {
      selectedId = "selectmd1";
    }
    return "matrixmain";

  }

  @Action("/scmmanagement/patent/ajaxcategoryLeft")
  public String ajaxPantentLeft() throws Exception {
    // 左菜单
    return "categoryLeft";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/patent/ajaxmatrixinfo")
  public String ajaxPantentInfo() throws Exception {
    try {
      Map<String, String> matrixData = patentService.getMatrixData(form);
      form.setMdata(matrixData.get("mdata"));
      form.setMeasures(matrixData.get("measures"));
      form.setPlanning(matrixData.get("planning"));
      form.setCmax(matrixData.get("max"));
    } catch (Exception e) {
      logger.error("获取矩阵数据出错！", e);
    }

    return "matrixinfo";
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
