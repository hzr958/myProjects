package com.smate.web.management.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.analysis.KeywordSplit;
import com.smate.web.management.model.analysis.PubInfo;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;
import com.smate.web.management.model.other.fund.ConstFundAgency;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.FundForm;
import com.smate.web.management.model.other.fund.FundLeftMenu;
import com.smate.web.management.service.analysis.KeywordsDicService;
import com.smate.web.management.service.other.BpoFundService;
import com.smate.web.management.service.other.ConstDisciplineManage;
import com.smate.web.management.service.other.FundThirdService;

/**
 * 资助机构Action
 * 
 * @author LJ
 *
 */
@Results({@Result(name = "redirectAction", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "fund_agency_maint", location = "/WEB-INF/other/fund_agency_maint.jsp"),
    @Result(name = "categoryMaint", location = "/WEB-INF/other/fund_category_maint.jsp"),
    @Result(name = "fund_agency_maint_left", location = "/WEB-INF/other/fund_agency_maint_left.jsp"),
    @Result(name = "fund_agency_maint_list", location = "/WEB-INF/other/fund_agency_maint_list.jsp"),
    @Result(name = "ajaxCategoryLeft", location = "/WEB-INF/other/fund_category_maint_left.jsp"),
    @Result(name = "ajaxCategoryMaintList", location = "/WEB-INF/other/fund_category_maint_list.jsp"),
    @Result(name = "ajaxCategory", location = "/WEB-INF/other/fund_thickbox_category.jsp"),
    @Result(name = "ajaxAgency", location = "/WEB-INF/other/fund_thickbox_agency.jsp")})
public class FundingAgenciesAction extends ActionSupport implements ModelDriven<FundForm>, Preparable {
  private static final long serialVersionUID = 512318240450623825L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private FundForm form;
  private Page page = new Page(10);
  private List<FundLeftMenu> leftList = null;
  private List<Map<String, Object>> agencyTypeList = null;// 机构类别
  private List<Map<String, Object>> agencyRegionList = null;// 机构所在省市
  private List<Map<String, Object>> agencyCityList = null;// 机构所在地市
  private List<Map<String, Object>> agencyList = null;// 基金机构
  private List<Map<String, Object>> languageList = null;// 类别语言
  private List<Map<String, Object>> degreeList = null;// 学位
  private List<Map<String, Object>> titleList = null;// 职称
  private FileUploadSimple fileUploadSimple;
  ConstFundCategory category = null;// 对比类别---修改
  ConstFundCategory parentCategory = null; // 对比类别---BPO
  private String SAVE = "save"; // 批准(新增)
  private String UPDATE = "update"; // 更新
  private ConstFundAgency fundAgency;
  private ConstFundCategory fundCategory;
  private List<Map<String, Object>> countryList;// 所有国家和地区
  private List<Map<String, Object>> provinceList;// 省
  private List<Map<String, Object>> cityList;// 市
  private String agencyIds;// 选中的资助机构id
  private String categoryIds;// 选中的资助类别id
  @Autowired
  private BpoFundService bpoFundService;
  @Autowired
  private KeywordsDicService KeywordsDicService;
  @Autowired
  private ConstDisciplineManage constDisciplineManage;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  private String auditType;
  @Autowired
  private FundThirdService fundThirdService;


  // ===========基金机构列表=============
  @Action("/scmmanagement/fund/agency/main")
  public String agencyMaint() throws Exception {
    return "fund_agency_maint";

  }

  @Action("/scmmanagement/fund/ajaxAgencyLeft")
  public String ajaxAgencyLeft() throws Exception {
    // 获取基金机构左菜单
    try {
      leftList = bpoFundService.getConstFundAgencyLeftMenu();
    } catch (Exception e) {
      logger.error("获取基金机构左菜单出现异常", e);
    }
    return "fund_agency_maint_left";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/fund/ajaxAgencyMaintList")
  public String ajaxAgencyMaintList() throws Exception {
    // 获取基金机构列表数据
    try {
      page = bpoFundService.findFundAgency(page, form);
    } catch (Exception e) {
      logger.info("获取基金机构列表数据出现错误！", e);
    }
    return "fund_agency_maint_list";
  }

  @Action("/scmmanagement/fund/ajaxCheckExistByName")
  public String ajaxCheckExistByName() throws Exception {// 检查新增基金机构是否重复
    try {
      String key = Struts2Utils.getParameter("key");
      ConstFundAgency fundAgencyByName = bpoFundService.getFundAgencyByName(key);
      Struts2Utils.renderJson(fundAgencyByName, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("", e);
      Struts2Utils.renderText("false", "encoding:UTF-8");
    }
    return null;
  }


  /**
   * 新增资助机构
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/fund/ajaxAgencyAdd")
  public String ajaxAgencyAdd() throws Exception {
    agencyTypeList = bpoFundService.getFundAgencyTypeList();
    agencyRegionList = bpoFundService.getConstRegionList(null);
    countryList = bpoFundService.getAllCountryAndRegion();
    return "ajaxAgency";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/fund/ajaxGetCity")
  public String ajaxGetCity() throws Exception {// 根据省id获取地市
    try {
      String idstr = Struts2Utils.getParameter("id");
      if (StringUtils.isNotBlank(idstr)) {
        List<Map<String, Object>> agencyCityList = bpoFundService.getConstRegionList(NumberUtils.toLong(idstr));
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(agencyCityList), "encoding:UTF-8");
      }
    } catch (Exception e) {
      Struts2Utils.renderJson("false", "encoding:UTF-8");
      logger.error("", e);
    }
    return null;
  }

  @Action("/scmmanagement/fund/ajaxAgencyEdit")
  public String ajaxAgencyEdit() throws Exception {// 编辑资助机构
    try {
      String idstr = Struts2Utils.getParameter("id");
      if (StringUtils.isNotBlank(idstr))
        fundAgency = bpoFundService.getConstFundAgency(NumberUtils.toLong(idstr));
      agencyTypeList = bpoFundService.getFundAgencyTypeList();
      agencyRegionList = bpoFundService.getConstRegionList(null);
      countryList = bpoFundService.getAllCountryAndRegion();
      if (fundAgency.getAddrCoun() != null) {
        provinceList = bpoFundService.getConstRegionList(fundAgency.getAddrCoun());
      }
      if (fundAgency.getAddrPrv() != null) {
        cityList = bpoFundService.getConstRegionList(fundAgency.getAddrPrv());
      }
      if (fundAgency.getSuperRegionId() != null)
        agencyCityList = bpoFundService.getConstRegionList(fundAgency.getSuperRegionId());
    } catch (Exception e) {
      logger.error("", e);
    }
    this.setAuditType(UPDATE);
    return "ajaxAgency";
  }

  @Action("/scmmanagement/fund/ajaxCheckAgency")
  public String ajaxCheckAgency() throws Exception {// 检查新增基金机构是否重复
    try {
      if (fundAgency != null) {
        String auditType = Struts2Utils.getParameter("auditType");
        if (UPDATE.equals(auditType)) {
          if (fundAgency.getId() == null) {
            fundAgency.setId(fundAgency.getParentAgencyId());
          }
        }
        boolean result = bpoFundService.getConstFundAgency(fundAgency);
        Struts2Utils.renderText(ObjectUtils.toString(result), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("", e);
      Struts2Utils.renderText("false", "encoding:UTF-8");
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/fund/ajaxAgencySave")
  public String ajaxAgencySave() throws Exception {// 保存资助机构
    try {
      String auditType = Struts2Utils.getParameter("auditType");
      fundAgency.setInsId(0L);
      fundAgency.setPsnId(SecurityUtils.getCurrentUserId());
      fundAgency.setStatus(0);
      if (UPDATE.equals(auditType)) {// 更新
        bpoFundService.updateConstFundAgency(fundAgency);
      } else {// 批准(新增)
        bpoFundService.saveConstFundAgency(fundAgency);
      }
      if ("true".equalsIgnoreCase(Struts2Utils.getParameter("isCategoryByAddAgency"))) {
        agencyList = bpoFundService.getFundAgencyAll();
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(agencyList), "encoding:UTF-8");
      } else {
        Struts2Utils.renderJson("true", "encoding:UTF-8");
      }
      List<ConstFundCategory> fundCategoryList = bpoFundService.getConstFundCategoryAll(fundAgency.getId());
      for (ConstFundCategory constFundCategory : fundCategoryList) {
        this.saveOrUpdateSolrFundInfo(constFundCategory);
      }
    } catch (Exception e) {
      logger.error("", e);
      Struts2Utils.renderText("false", "encoding:UTF-8");
    }
    return null;
  }

  @Action("/scmmanagement/fund/ajaxAgencyDel")
  public String ajaxAgencyDel() throws Exception {// 删除资助机构
    try {
      if (StringUtils.isNotBlank(agencyIds)) {
        String[] agencyIdStrs = agencyIds.split(",");
        for (String agencyIdStr : agencyIdStrs) {
          if (StringUtils.isNotBlank(agencyIdStr)) {
            bpoFundService.deleteConstFundAgency(NumberUtils.toLong(agencyIdStr));
            List<ConstFundCategory> fundCategoryList =
                bpoFundService.getConstFundCategoryAll(Long.parseLong(agencyIdStr));
            for (ConstFundCategory constFundCategory : fundCategoryList) {
              this.saveOrUpdateSolrFundInfo(constFundCategory);
            }
          }
        }
        Struts2Utils.renderJson("true", "encoding:UTF-8");
      }
    } catch (Exception e) {
      Struts2Utils.renderJson("false", "encoding:UTF-8");
      logger.error("", e);
    }
    return null;
  }


  // =============基金类别列表==============
  @Action("/scmmanagement/fund/category/main")
  public String categoryMaint() throws Exception {
    return "categoryMaint";
  }

  @Action("/scmmanagement/fund/ajaxCategoryLeft")
  public String ajaxCategoryLeft() throws Exception {// 获取基金类别左菜单
    leftList = bpoFundService.getConstFundCategoryLeftMenu();
    return "ajaxCategoryLeft";
  }

  @SuppressWarnings("unchecked")
  @Action("/scmmanagement/fund/ajaxCategoryMaintList")
  public String ajaxCategoryMaintList() throws Exception {// 获取基金类别列表数据
    page = bpoFundService.findFundCategory(page, form);
    return "ajaxCategoryMaintList";
  }

  @Action("/scmmanagement/fund/ajaxCategoryAdd")
  public String ajaxCategoryAdd() throws Exception {// 新增资助类别
    agencyList = bpoFundService.getFundAgencyAll();
    languageList = bpoFundService.getLanguageList();
    degreeList = bpoFundService.getDegreeList();
    titleList = bpoFundService.getTitleList();
    countryList = bpoFundService.getAllCountryAndRegion();
    agencyRegionList = bpoFundService.getConstRegionList(null);
    return "ajaxCategory";
  }

  @Action("/scmmanagement/fund/ajaxCategoryEdit")
  public String ajaxCategoryEdit() throws Exception {// 编辑资助类别
    try {
      String idstr = Struts2Utils.getParameter("id");
      if (StringUtils.isNotBlank(idstr))
        fundCategory = bpoFundService.getConstFundCategory(NumberUtils.toLong(idstr));
      agencyList = bpoFundService.getFundAgencyAll();
      languageList = bpoFundService.getLanguageList();
      degreeList = bpoFundService.getDegreeList();
      titleList = bpoFundService.getTitleList();
      countryList = bpoFundService.getAllCountryAndRegion();
      agencyRegionList = bpoFundService.getConstRegionList(null);
      this.setAuditType(UPDATE);
    } catch (Exception e) {
      logger.error("", e);
    }
    return "ajaxCategory";
  }

  @Action("/scmmanagement/fund/ajaxCheckCategory")
  public String ajaxCheckCategory() throws Exception {// 检查新增基金类别是否重复
    try {
      boolean result = false;
      if (fundCategory != null) {
        String auditType = Struts2Utils.getParameter("auditType");
        if (UPDATE.equals(auditType)) {
          if (fundCategory.getId() == null) {
            fundCategory.setId(fundCategory.getParentCategoryId());
          }
        }
        result = this.bpoFundService.getConstFundCategory(fundCategory);
      }
      Struts2Utils.renderText(ObjectUtils.toString(result), "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("", e);
      Struts2Utils.renderText("false", "encoding:UTF-8");
    }
    return null;

  }

  // 通过学科代码查询学科领域JSON数据
  @Action("/scmmanagement/const/ajaxDiscipline")
  public String ajaxDiscipline() throws Exception {
    String jsonRes = "";
    try {
      String discCode = Struts2Utils.getParameter("discCode");
      jsonRes = constDisciplineManage.bpoFindDiscJsonData(discCode);
    } catch (Exception e) {
      String resultJson = "{\"result\":\"error\"}";
      Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
      return null;
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;
  }

  /**
   * 两种情况 批准-->新增-->获取id回存 更新-->修改
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/fund/ajaxCategorySave")
  public String ajaxCategorySave() throws Exception {// 保存资助类别
    try {
      String auditType = Struts2Utils.getParameter("auditType");
      fundCategory.setInsId(0L);
      fundCategory.setPsnId(SecurityUtils.getCurrentUserId());
      fundCategory.setStatus(0);

      String languageStr = Struts2Utils.getParameter("languageStr");
      if (StringUtils.isNotBlank(languageStr))
        fundCategory.setLanguage(NumberUtils.toLong(languageStr));
      if (StringUtils.isNotBlank(Struts2Utils.getParameter("startDateStr"))) {
        fundCategory.setStartDate(new SimpleDateFormat("yyyy/MM/dd").parse(Struts2Utils.getParameter("startDateStr")));
        fundCategory.setYear(
            NumberUtils.toLong(ObjectUtils.toString(new SimpleDateFormat("yyyy").format(fundCategory.getStartDate()))));
      }
      if (StringUtils.isNotBlank(Struts2Utils.getParameter("endDateStr"))) {
        fundCategory.setEndDate(new SimpleDateFormat("yyyy/MM/dd").parse(Struts2Utils.getParameter("endDateStr")));
        if (fundCategory.getYear() == null) {
          fundCategory.setYear(
              NumberUtils.toLong(ObjectUtils.toString(new SimpleDateFormat("yyyy").format(fundCategory.getEndDate()))));
        }
      }
      if (StringUtils.isNotBlank(Struts2Utils.getParameter("birthLeastStr")))
        fundCategory
            .setBirthLeast(new SimpleDateFormat("yyyy/MM/dd").parse(Struts2Utils.getParameter("birthLeastStr")));
      if (StringUtils.isNotBlank(Struts2Utils.getParameter("birthMaxStr")))
        fundCategory.setBirthMax(new SimpleDateFormat("yyyy/MM/dd").parse(Struts2Utils.getParameter("birthMaxStr")));
      String nameZh = Struts2Utils.getParameter("nameZh");
      String nameEn = Struts2Utils.getParameter("nameEn");
      // 在保存的事后设置 grant_name_zh和grant_name_en字段 TSZ_2014-1-24_ROL-788
      // 取出name_zh和name_en 类别名称最小的的类别 有可能是
      // ->分割的添加进grant_name_zh和grant_name_en字段
      if (StringUtils.isNotBlank(nameZh)) {
        if (nameZh.indexOf("->") > -1) {
          String gNameZh = nameZh.split("->")[nameZh.split("->").length - 1].trim();
          fundCategory.setGrantNameZh(gNameZh);
        } else {
          fundCategory.setGrantNameZh(nameZh);
        }
      }
      if (StringUtils.isNotBlank(nameEn)) {
        if (nameEn.indexOf("->") > -1) {
          String gNameEn = nameEn.split("->")[nameEn.split("->").length - 1].trim();
          fundCategory.setGrantNameEn(gNameEn);
        } else {
          fundCategory.setGrantNameEn(nameEn);
        }
      }
      if (UPDATE.equals(auditType)) {// 更新
        fundCategory = bpoFundService.updateConstFundCategory(fundCategory);
      } else {// 批准(新增)
        fundCategory.setCreateDate(new Date());
        fundCategory = bpoFundService.saveConstFundCategory(fundCategory);
      }
      this.saveOrUpdateSolrFundInfo(fundCategory);
      Long fundId = fundCategory.getFundId();
      if (fundId != null) {
        fundThirdService.updateFundThird(fundId, 1);
      }
      Struts2Utils.renderText("true", "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("", e);
      Struts2Utils.renderText("false", "encoding:UTF-8");
    }
    return null;
  }

  @SuppressWarnings({"unchecked"})
  private void saveOrUpdateSolrFundInfo(ConstFundCategory fundCategory) throws ServiceException {
    // 调接口删除基金信息
    if (fundCategory.getId() == null) {
      logger.info("solr更新基金信息fundId为空！");
      return;
    }
    // openServerUrl = "http://127.0.0.1:49080/center-open/scmopendata";
    Map<String, Object> restfulMap = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    restfulMap.put("openid", "99999999");
    restfulMap.put("token", "00000000fun2solr");
    dataMap.put("fund_id", fundCategory.getId());
    restfulMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(SERVER_URL, restfulMap, Object.class);

    if (obj != null) {
      Map<String, Object> map = JacksonUtils.jsonToMap(obj.toString());
      // 返回结果为ArrayList,详见open返回值说明
      List<Object> status = (List<Object>) map.get("result");
      try {
        Integer result = Integer.parseInt(String.valueOf(((Map<String, Object>) status.get(0)).get("return_status")));
        if (result == 1) {
          logger.info("solr更新基金索引信息成功，psnId = " + fundCategory.getId());
        } else {
          logger.info("solr更新基金索引信息失败，psnId = " + fundCategory.getId());
        }
      } catch (Exception e) {
        logger.error("solr更新基金索引信息失败，psnId = " + fundCategory.getId(), e);
      }
    }
  }

  @Action("/scmmanagement/fund/ajaxCategoryDel")
  public String ajaxCategoryDel() throws Exception {// 删除资助类别
    try {
      if (StringUtils.isNotBlank(categoryIds)) {
        String[] categoryIdStrs = categoryIds.split(",");
        for (String categoryIdStr : categoryIdStrs) {
          if (StringUtils.isNotBlank(categoryIdStr)) {
            bpoFundService.deleteConstFundCategory(NumberUtils.toLong(categoryIdStr));
            // 删除基金solr信息
            this.deleteSolrPsnInfo(NumberUtils.toLong(categoryIdStr));
          }
        }
        Struts2Utils.renderJson("true", "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  @Action("/scmmanagement/fund/ajaxGetDesKws")
  public String ajaxGetDesKws() throws Exception {
    try {
      String description = Struts2Utils.getParameter("description");
      PubInfo info = new PubInfo();
      String locale = LocaleContextHolder.getLocale().toString();
      if ("zh_CN".equals(locale)) {
        info.setZhAbs(description);
      } else {
        info.setEnAbs(description);
      }
      if (StringUtils.isNotBlank(description)) {
        List<KeywordSplit> splitList = KeywordsDicService.findPubKeywords(info);
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(splitList), "encoding:UTF-8");
      }

    } catch (Exception e) {
      logger.error("从类别描述提取关键词出错", e);
      Struts2Utils.renderJson("false", "encoding:UTF-8");
    }
    return null;
  }

  @Action("/scmmanagement/fund/ajaxMatchAgencyNameBykey")
  public void ajaxGetAgencyNameBykey() {
    String key = Struts2Utils.getParameter("key");
    List<ConstFundAgency> agencyIds = bpoFundService.getMatchAgencyByKey(key);
    Struts2Utils.renderJson(JacksonUtils.listToJsonStr(agencyIds), "encoding:UTF-8");
  }


  private void deleteSolrPsnInfo(Long constCategoryId) throws ServiceException {
    // 调接口删除基金信息
    if (constCategoryId == null) {
      logger.info("solr删除基金信息fundId为空！");
      return;
    }
    // openServerUrl = "http://127.0.0.1:49080/center-open/scmopendata";
    Map restfulMap = new HashMap<String, Object>();
    Map dataMap = new HashMap<String, Object>();
    restfulMap.put("openid", "99999999");
    restfulMap.put("token", "00000000fundsolr");
    dataMap.put("fund_id", constCategoryId);
    restfulMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(SERVER_URL, restfulMap, Object.class);

    if (obj != null) {
      Map<String, Object> map = JacksonUtils.jsonToMap(obj.toString());
      // 返回结果为ArrayList,详见open返回值说明
      List<Object> status = (List<Object>) map.get("result");
      try {
        Integer result = Integer.parseInt(String.valueOf(((Map<String, Object>) status.get(0)).get("return_status")));
        if (result == 1) {
          logger.info("solr删除基金索引信息成功，psnId = " + constCategoryId);
        } else {
          logger.info("solr删除基金索引信息失败，psnId = " + constCategoryId);
        }
      } catch (Exception e) {
        logger.error("solr删除基金索引信息失败，psnId = " + constCategoryId, e);
      }
    }

  }

  @SuppressWarnings("rawtypes")
  public Page getPage() {
    return page;
  }

  @SuppressWarnings("rawtypes")
  public void setPage(Page page) {
    this.page = page;
  }

  public List<FundLeftMenu> getLeftList() {
    return leftList;
  }

  public void setLeftList(List<FundLeftMenu> leftList) {
    this.leftList = leftList;
  }

  public List<Map<String, Object>> getAgencyRegionList() {
    return agencyRegionList;
  }

  public void setAgencyRegionList(List<Map<String, Object>> agencyRegionList) {
    this.agencyRegionList = agencyRegionList;
  }

  public List<Map<String, Object>> getAgencyCityList() {
    return agencyCityList;
  }

  public void setAgencyCityList(List<Map<String, Object>> agencyCityList) {
    this.agencyCityList = agencyCityList;
  }

  public List<Map<String, Object>> getAgencyList() {
    return agencyList;
  }

  public void setAgencyList(List<Map<String, Object>> agencyList) {
    this.agencyList = agencyList;
  }

  public List<Map<String, Object>> getCountryList() {
    return countryList;
  }

  public void setCountryList(List<Map<String, Object>> countryList) {
    this.countryList = countryList;
  }

  public List<Map<String, Object>> getProvinceList() {
    return provinceList;
  }

  public void setProvinceList(List<Map<String, Object>> provinceList) {
    this.provinceList = provinceList;
  }

  public List<Map<String, Object>> getCityList() {
    return cityList;
  }

  public void setCityList(List<Map<String, Object>> cityList) {
    this.cityList = cityList;
  }

  public String getAgencyIds() {
    return agencyIds;
  }

  public void setAgencyIds(String agencyIds) {
    this.agencyIds = agencyIds;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundForm();
    }
  }

  @Override
  public FundForm getModel() {
    return form;
  }

  public FundForm getForm() {
    return form;
  }

  public void setForm(FundForm form) {
    this.form = form;
  }

  public List<Map<String, Object>> getAgencyTypeList() {
    return agencyTypeList;
  }

  public void setAgencyTypeList(List<Map<String, Object>> agencyTypeList) {
    this.agencyTypeList = agencyTypeList;
  }

  public List<Map<String, Object>> getLanguageList() {
    return languageList;
  }

  public void setLanguageList(List<Map<String, Object>> languageList) {
    this.languageList = languageList;
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

  public ConstFundCategory getCategory() {
    return category;
  }

  public void setCategory(ConstFundCategory category) {
    this.category = category;
  }

  public ConstFundCategory getParentCategory() {
    return parentCategory;
  }

  public void setParentCategory(ConstFundCategory parentCategory) {
    this.parentCategory = parentCategory;
  }

  public ConstFundAgency getFundAgency() {
    return fundAgency;
  }

  public FileUploadSimple getFileUploadSimple() {
    return fileUploadSimple;
  }

  public void setFileUploadSimple(FileUploadSimple fileUploadSimple) {
    this.fileUploadSimple = fileUploadSimple;
  }

  public void setFundAgency(ConstFundAgency fundAgency) {
    this.fundAgency = fundAgency;
  }

  public ConstFundCategory getFundCategory() {
    return fundCategory;
  }

  public void setFundCategory(ConstFundCategory fundCategory) {
    this.fundCategory = fundCategory;
  }

  public String getCategoryIds() {
    return categoryIds;
  }

  public void setCategoryIds(String categoryIds) {
    this.categoryIds = categoryIds;
  }

  public String getAuditType() {
    return auditType;
  }

  public void setAuditType(String auditType) {
    this.auditType = auditType;
  }
}
