package com.smate.web.management.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.FundThirdForm;
import com.smate.web.management.service.other.BpoFundService;
import com.smate.web.management.service.other.FundThirdService;

/**
 * 基金信息Action
 * 
 * @author YHX
 *
 */
@Results({@Result(name = "fund_third_maint", location = "/WEB-INF/other/fund_third_maint.jsp"),
    @Result(name = "fund_third_maint_left", location = "/WEB-INF/other/fund_third_maint_left.jsp"),
    @Result(name = "fund_third_maint_list", location = "/WEB-INF/other/fund_third_maint_list.jsp"),
    @Result(name = "fund_third_maint_details", location = "/WEB-INF/other/fund_third_maint_details.jsp"),
    @Result(name = "ajaxCategory", location = "/WEB-INF/other/fund_thickbox_category.jsp")})
public class FundThirdAction extends ActionSupport implements ModelDriven<FundThirdForm>, Preparable {
  private static final long serialVersionUID = 512318240450623825L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private FundThirdForm form;
  @Autowired
  private FundThirdService fundThirdService;
  private List<Map<String, Object>> agencyRegionList = null;// 机构所在省市
  private ConstFundCategory fundCategory;
  private List<Map<String, Object>> countryList;// 所有国家和地区
  private List<Map<String, Object>> degreeList = null;// 学位
  private List<Map<String, Object>> titleList = null;// 职称
  @Autowired
  private BpoFundService bpoFundService;

  // ===========基金机会列表=============
  @Action("/scmmanagement/fund/third/main")
  public String FundThirdMaint() throws Exception {
    return "fund_third_maint";

  }

  @Action("/scmmanagement/fund/ajaxFundThirdLeft")
  public String ajaxFundThirdLeft() throws Exception {
    try {

    } catch (Exception e) {
      logger.error("获取基金机会左菜单出现异常", e);
    }
    return "fund_third_maint_left";
  }

  @Action("/scmmanagement/fund/ajaxFundThirdMaintList")
  public String ajaxFundThirdList() throws Exception {
    // 获取基金机构列表数据
    try {
      fundThirdService.findFundThirdList(form);
    } catch (Exception e) {
      logger.info("获取基金机会列表数据出现错误！", e);
    }
    return "fund_third_maint_list";
  }

  @Action("/scmmanagement/fund/ajaxFundDetails")
  public String ajaxFundDetails() throws Exception {
    try {
      fundThirdService.viewFundThirdDetails(form);
    } catch (Exception e) {
      logger.info("获取基金机会详情出现错误！", e);
    }
    return "fund_third_maint_details";

  }

  @Action("/scmmanagement/fund/ajaxFundCheck")
  public String ajaxFundCheck() throws Exception {
    try {
      if (StringUtils.isNotBlank(form.getIds())) {
        fundCategory = fundThirdService.checkFundThird(NumberUtils.toLong(form.getIds()));
        agencyRegionList = bpoFundService.getConstRegionList(null);
        countryList = bpoFundService.getAllCountryAndRegion();
        degreeList = bpoFundService.getDegreeList();
        titleList = bpoFundService.getTitleList();
      }
    } catch (Exception e) {
      logger.error("审核基金机会出错,ids=" + form.getIds(), e);
    }
    return "ajaxCategory";
  }

  @Action("/scmmanagement/fund/ajaxFundDel")
  public String ajaxFundDel() throws Exception {
    try {
      if (StringUtils.isNotBlank(form.getIds())) {
        String[] fundIdStrs = form.getIds().split(",");
        for (String fundIdStr : fundIdStrs) {
          if (StringUtils.isNotBlank(fundIdStr)) {
            fundThirdService.deleteFundThird(NumberUtils.toLong(fundIdStr));
          }
        }
        Struts2Utils.renderJson("true", "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("基金机会删除操作出错,fundIds=" + form.getIds(), e);
    }
    return null;
  }


  @Override
  public FundThirdForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    form = new FundThirdForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }

  public List<Map<String, Object>> getAgencyRegionList() {
    return agencyRegionList;
  }

  public void setAgencyRegionList(List<Map<String, Object>> agencyRegionList) {
    this.agencyRegionList = agencyRegionList;
  }

  public ConstFundCategory getFundCategory() {
    return fundCategory;
  }

  public void setFundCategory(ConstFundCategory fundCategory) {
    this.fundCategory = fundCategory;
  }

  public List<Map<String, Object>> getCountryList() {
    return countryList;
  }

  public void setCountryList(List<Map<String, Object>> countryList) {
    this.countryList = countryList;
  }

  public List<Map<String, Object>> getDegreeList() {
    return degreeList;
  }

  public void setDegreeList(List<Map<String, Object>> degreeList) {
    this.degreeList = degreeList;
  }

  public List<Map<String, Object>> getTitleList() {
    return titleList;
  }

  public void setTitleList(List<Map<String, Object>> titleList) {
    this.titleList = titleList;
  }
}
