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
import com.smate.web.management.model.patent.PatentForm;

/**
 * 专利地图
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "mapmain", location = "/WEB-INF/patent/patent_map_main.jsp"),
    @Result(name = "categoryLeft", location = "/WEB-INF/patent/subject_category.jsp"),
    @Result(name = "mapinfo", location = "/WEB-INF/patent/patent_map_info.jsp")

})

public class PatentMapAction extends ActionSupport implements ModelDriven<PatentForm>, Preparable {

  private static final long serialVersionUID = 3980713080197229633L;

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private PatentForm info;
  private Integer mId;
  private String mmId;
  private String selectedId;

  @Action("/scmmanagement/patent/mapmain")
  public String patentMaint() throws Exception {
    mmId = Struts2Utils.getParameter("mmId");
    selectedId = Struts2Utils.getParameter("selectedId");
    if (StringUtils.isEmpty(mmId) || "null".equals(mmId)) {
      mmId = "mm6";
    }

    if (StringUtils.isEmpty(selectedId) || "null".equals(selectedId)) {
      selectedId = "selectmd1";
    }
    return "mapmain";

  }

  @Action("/scmmanagement/patent/ajaxcategoryLeft")
  public String ajaxpatentLeft() throws Exception {
    // 左菜单
    return "categoryLeft";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/patent/ajaxmapinfo")
  public String ajaxpatentInfo() throws Exception {
    if (mId == 0) {
      info.setTsvDataName("earthScience.tsv");
    } else if (mId == 1) {
      info.setTsvDataName("industryAndEngineering.tsv");
    } else if (mId == 2) {
      info.setTsvDataName("management.tsv");
    } else if (mId == 3) {
      info.setTsvDataName("chemistry.tsv");
    } else if (mId == 4) {
      info.setTsvDataName("bioScience.tsv");
    } else if (mId == 5) {
      info.setTsvDataName("mathematicalScience.tsv");
    } else if (mId == 6) {
      info.setTsvDataName("information.tsv");
    } else if (mId == 7) {
      info.setTsvDataName("medicine.tsv");
    } else {
      info.setTsvDataName("earthScience.tsv");
    }

    return "mapinfo";
  }

  @Override
  public void prepare() throws Exception {
    if (info == null) {
      info = new PatentForm();
    }

  }

  @Override
  public PatentForm getModel() {
    return info;
  }

  public Integer getmId() {
    return mId;
  }

  public void setmId(Integer mId) {
    this.mId = mId;
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
