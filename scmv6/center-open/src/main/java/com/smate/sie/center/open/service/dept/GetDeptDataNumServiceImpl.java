package com.smate.sie.center.open.service.dept;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.center.open.utils.PageDataUtils;
import com.smate.sie.core.base.utils.dao.ins.Sie6InsDisciplineEconomicDao;
import com.smate.sie.core.base.utils.dao.ins.SieConstInsTypeDao;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegionDao;
import com.smate.sie.core.base.utils.dao.insguid.SieInsGuidDao;
import com.smate.sie.core.base.utils.dao.statistics.SieInsStatisticsDao;
import com.smate.sie.core.base.utils.model.ins.Sie6InsDisciplineEconomic;
import com.smate.sie.core.base.utils.model.ins.SieConstInsType;
import com.smate.sie.core.base.utils.model.ins.SieInsRegion;
import com.smate.sie.core.base.utils.model.statistics.SieInsStatistics;

@Transactional(rollbackFor = Exception.class)
public class GetDeptDataNumServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private SieInsGuidDao sieInsGuidDao;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;
  @Autowired
  private SieInsStatisticsDao sieInsStatisticsDao;
  @Autowired
  private SieInsRegionDao sieInsRegionDao;
  @Autowired
  private SieConstRegionDao sieConstRegionDao;
  @Autowired
  private SieConstInsTypeDao sieConstInsTypeDao;
  @Autowired
  private Sie6InsDisciplineEconomicDao sie6InsDisciplineEconomicDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 校验分页参数是否合法 不合法给定默认值
    PageDataUtils.correctPageData(paramet);
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      return BuildUniformErrorReturn(temp, paramet);
    }
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      return BuildUniformErrorReturn(temp, paramet);
    }
    String regionStr = ObjectUtils.isEmpty(dataMap.get("region_id")) ? "" : dataMap.get("region_id").toString().trim();
    if (StringUtils.isNotBlank(regionStr)) {
      String[] regions = StringUtils.split(regionStr, ",|，");
      for (String str : regions) {
        if (!NumberUtils.isDigits(str)) {
          return BuildUniformErrorReturn(temp, paramet);
        }
      }
    }
    String natureStr = ObjectUtils.isEmpty(dataMap.get("nature_id")) ? "" : dataMap.get("nature_id").toString().trim();
    if (StringUtils.isNotBlank(natureStr)) {
      String[] natures = StringUtils.split(natureStr, ",|，");
      for (String nature : natures) {
        if (!NumberUtils.isDigits(nature)) {
          return BuildUniformErrorReturn(temp, paramet);
        }
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 构造统一错误MSG返回码
   * 
   * @param temp
   * @return
   */
  private Map<String, Object> BuildUniformErrorReturn(Map<String, Object> temp, Map<String, Object> parame) {
    temp = super.errorMap(OpenMsgCodeConsts.SCM_999, parame, "");
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> params = new HashMap<String, Object>();
    Map<String, Object> addReturnMap = new HashMap<String, Object>();
    Page<Sie6Institution> page = new Page<Sie6Institution>();
    // 构造入参
    params = this.BuildParams(paramet);
    // 获取分页参数
    page = (Page<Sie6Institution>) params.get("page");
    // 查询机构列表
    page = this.doBusiness(params, page);
    // 查询所属行业信息,机构类别,所属地区信息
    addReturnMap = this.doOtherInfo(addReturnMap, params);
    // 构造返回参数
    temp = this.buildReturnData(page, temp, addReturnMap);
    return temp;
  }

  /**
   * 查询所属行业信息,机构类别,所属地区信息
   * 
   * @param params
   * @return
   */
  private Map<String, Object> doOtherInfo(Map<String, Object> addReturnMap, Map<String, Object> params) {
    List<Map<String, Object>> encodeList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> natureList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();
    params = this.buildSerchParams(params);
    encodeList = sie6InstitutionDao.getInsCountForEncode(params);
    natureList = sie6InstitutionDao.getInsCountForNature(params);
    regionList = sie6InstitutionDao.getInsCountForRegion(params);
    addReturnMap.put("economic", encodeList);
    addReturnMap.put("natures", natureList);
    addReturnMap.put("regions", regionList);
    return addReturnMap;
  }

  /**
   * 构造查询参数
   * 
   * @param params
   * @return
   */
  private Map<String, Object> buildSerchParams(Map<String, Object> params) {
    if ((boolean) params.get("naturesExist")) {
      String natureStr = params.get("natureStr").toString();
      String[] natures = StringUtils.split(natureStr, ",|，");
      List<Long> natureIdList = new ArrayList<>();
      for (String str : natures) {
        natureIdList.add(Long.valueOf(str));
      }
      params.put("natureIdList", natureIdList);
    }
    if ((boolean) params.get("ecoIdsExist")) {
      String ecoIdStr = params.get("ecoIdStr").toString();
      String[] ecoIds = StringUtils.split(ecoIdStr, ",|，");
      List<String> ecoIdList = new ArrayList<>();
      for (String str : ecoIds) {
        ecoIdList.add(str);
      }
      params.put("ecoIdList", ecoIdList);
    }
    if ((boolean) params.get("regionIdExist")) {
      String regionIdStr = params.get("regionIdStr").toString();
      String[] regionIds = StringUtils.split(regionIdStr, ",|，");
      List<Long> regionIdList = new ArrayList<>();
      for (String str : regionIds) {
        regionIdList.add(Long.valueOf(str));
      }
      params.put("regionIdList", regionIdList);
    }
    return params;
  }

  /**
   * 构造返回参数
   * 
   * @param page
   * @param temp
   * @param addReturnMap
   * @return
   */
  private Map<String, Object> buildReturnData(Page<Sie6Institution> page, Map<String, Object> temp,
      Map<String, Object> addReturnMap) {
    // 处理返回的数据
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    returnMap.put("page_no", org.apache.commons.lang.ObjectUtils.toString(page.getPageNo()));
    returnMap.put("page_size", org.apache.commons.lang.ObjectUtils.toString(page.getPageSize()));
    returnMap.put("total_pages", org.apache.commons.lang.ObjectUtils.toString(page.getTotalPages()));
    returnMap.put("total_count", org.apache.commons.lang.ObjectUtils.toString(page.getTotalCount()));
    returnMap.put("economic", addReturnMap.get("economic"));
    returnMap.put("natures", addReturnMap.get("natures"));
    returnMap.put("regions", addReturnMap.get("regions"));
    for (Sie6Institution ins : page.getResult()) {
      Long insId = ins.getId();
      // 查询统计数更新时间
      ins.setStUpdateDate(sieInsStatisticsDao.getStUpdateDateById(insId));
      Sie6InsPortal portal =
          ObjectUtils.isEmpty(sie6InsPortalDao.get(insId)) ? new Sie6InsPortal() : sie6InsPortalDao.get(insId);
      SieInsStatistics sieInsStatistics =
          ObjectUtils.isEmpty(sieInsStatisticsDao.get(insId)) ? new SieInsStatistics() : sieInsStatisticsDao.get(insId);
      SieInsRegion sieInsRegion =
          ObjectUtils.isEmpty(sieInsRegionDao.get(insId)) ? new SieInsRegion() : sieInsRegionDao.get(insId);
      // 增加国民经济行业代码
      Sie6InsDisciplineEconomic insEconomic =
          ObjectUtils.isEmpty(sie6InsDisciplineEconomicDao.getEconomicByInsId(insId)) ? new Sie6InsDisciplineEconomic()
              : sie6InsDisciplineEconomicDao.getEconomicByInsId(insId);
      Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
      dataMap.put("prjNum", sieInsStatistics.getPrjSum());
      dataMap.put("unitNum", sieInsStatistics.getUnitNum());
      dataMap.put("patentNum", sieInsStatistics.getPtSum());
      dataMap.put("psnNum", sieInsStatistics.getPsnSum());
      dataMap.put("thesisNum", sieInsStatistics.getPubSum());
      dataMap.put("domainUrl", StringUtils.isEmpty(portal.getDomain()) ? "" : portal.getDomain());
      dataMap.put("insName", StringUtils.isEmpty(ins.getZhName()) ? "" : ins.getZhName());
      dataMap.put("ins_id", ins.getId());
      dataMap.put("ins_guid", getInsGuid(ins.getId()));
      dataMap.put("nature", ins.getNature());
      dataMap.put("nature_name", getNatureName(ins.getNature()));
      dataMap.put("country_id", sieInsRegion.getCountryId());
      dataMap.put("country_name", getRegionName(sieInsRegion.getCountryId()));
      dataMap.put("prv_id", sieInsRegion.getPrvId());
      dataMap.put("prv_name", getRegionName(sieInsRegion.getPrvId()));
      dataMap.put("cy_id", sieInsRegion.getCyId());
      dataMap.put("cy_name", getRegionName(sieInsRegion.getCyId()));
      dataMap.put("dis_id", sieInsRegion.getDisId());
      dataMap.put("dis_name", getRegionName(sieInsRegion.getDisId()));
      // 增加国民经济行业代码和名称的返回
      dataMap.put("eco_id", insEconomic.getEcoCode());
      dataMap.put("eco_name", insEconomic.getEcoZhName());
      dataMap.put("contact_person", ins.getContactPerson());
      dataMap.put("contact_tel", ins.getTel());
      dataMap.put("contact_mobile", ins.getMobile());
      dataMap.put("contact_email", ins.getContactEmail());
      dataMap.put("uniform_id1", ins.getUniformId1());
      dataMap.put("uniform_id2", ins.getUniformId2());
      dataMap.put("logo", portal.getLogo());
      dataMap.put("is_validate", "");// 是否实名认证
      dataMap.put("update_date", formateDateToString(ins.getUpdateDate()));// 单位更新时间
      dataMap.put("st_update_date", formateDateToString(ins.getStUpdateDate()));// 统计数更新时间
      dataList.add(dataMap);
    }
    returnMap.put("institution", dataList);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, returnMap);
    return temp;
  }

  /**
   * 获取机构列表
   * 
   * @param params
   * @param page
   * @return
   */
  private Page<Sie6Institution> doBusiness(Map<String, Object> params, Page<Sie6Institution> page) {
    List<Sie6Institution> insIdList = new ArrayList<>();
    // 查询单位信息，根据所传参数作为查询条件（机构guid，机构名称，机构地域编号，机构类型，所属行业）
    insIdList = sie6InstitutionDao.getInsListByConditions(params, page);
    page.setResult(insIdList);
    return page;
  }

  /**
   * 构造入参
   * 
   * @param data
   * @return
   */
  private Map<String, Object> BuildParams(Map<String, Object> paramet) {
    Object mapData = paramet.get(OpenConsts.MAP_DATA);
    Map<String, Object> data = JacksonUtils.jsonToMap(mapData.toString());
    Map<String, Object> params = new HashMap<String, Object>();
    // 获取参数
    String orgGuid = ObjectUtils.isEmpty(data.get("org_guid")) ? "" : data.get("org_guid").toString().trim();
    String orgName =
        ObjectUtils.isEmpty(data.get("org_name")) ? "" : data.get("org_name").toString().trim().replace(" ", "");
    // 获取机构类型参数
    String natureStr = ObjectUtils.isEmpty(data.get("nature_id")) ? "" : data.get("nature_id").toString().trim();
    // 获取机构行业参数
    String ecoIdStr = ObjectUtils.isEmpty(data.get("eco_id")) ? "" : data.get("eco_id").toString().trim();
    // 获取regionId(地区编号)
    String regionIdStr = ObjectUtils.isEmpty(data.get("region_id")) ? "" : data.get("region_id").toString().trim();
    // 获取分页参数
    Integer pageNo = getIntNumber(paramet.get("page_no"));
    Integer pageSize = getIntNumber(paramet.get("page_size"));
    Page<Sie6Institution> page = new Page<Sie6Institution>();
    // 设置分页参数
    page.setPageSize(pageSize);
    page.setPageNo(pageNo);
    page.setTotalCount(0);
    params.put("page", page);
    boolean orgGuidExist = StringUtils.isNotBlank(orgGuid), orgNameExist = StringUtils.isNotBlank(orgName),
        regionIdExist = StringUtils.isNotBlank(regionIdStr), naturesExist = StringUtils.isNotBlank(natureStr),
        ecoIdsExist = StringUtils.isNotBlank(ecoIdStr);
    params = buildParamBoolMap(params, "orgGuidExist", orgGuidExist);
    params = buildParamBoolMap(params, "orgNameExist", orgNameExist);
    params = buildParamBoolMap(params, "naturesExist", naturesExist);
    params = buildParamBoolMap(params, "ecoIdsExist", ecoIdsExist);
    params = buildParamBoolMap(params, "regionIdExist", regionIdExist);
    if (orgGuidExist) {
      params = buildParam(params, "orgGuid", orgGuid);
    }
    if (orgNameExist) {
      params = buildParam(params, "orgName", orgName);
    }
    if (StringUtils.isNotBlank(natureStr)) {
      params = buildParams(params, "natureStr", natureStr);
    }
    if (StringUtils.isNotBlank(ecoIdStr)) {
      params = buildParams(params, "ecoIdStr", ecoIdStr);
    }
    if (StringUtils.isNotBlank(regionIdStr)) {
      params = buildParams(params, "regionIdStr", regionIdStr);
    }
    return params;
  }



  /**
   * 构造字符数组传参
   * 
   * @param params
   * @param str
   * @param strs
   * @return
   */
  private Map<String, Object> buildParams(Map<String, Object> params, String str, String strs) {
    params.put(str, strs);
    return params;
  }

  /**
   * 构造字符传参
   * 
   * @param params
   * @param str
   * @param val
   * @return
   */
  private Map<String, Object> buildParam(Map<String, Object> params, String str, String val) {
    params.put(str, val);
    return params;
  }

  /**
   * 构造布尔型传参
   * 
   * @param str
   * @param bool
   */
  private Map<String, Object> buildParamBoolMap(Map<String, Object> params, String str, boolean bool) {
    params.put(str, bool);
    return params;
  }

  /**
   * 获取格式化后的正整形数字
   * 
   * @param object
   * @return
   */
  private Integer getIntNumber(Object object) {
    Integer num = null;
    if (ObjectUtils.isEmpty(object)) {
      num = 1;
    } else {
      String numStr = object.toString().trim();
      if (numStr.length() > 10) {
        long a = Long.valueOf(numStr.substring(0, 10));
        if (a > Integer.MAX_VALUE) {
          num = Integer.MAX_VALUE;
        } else {
          num = (int) a;
        }
      } else {
        num = Integer.valueOf(numStr);
      }
    }
    return num;
  }

  /**
   * 获取机构地区名称
   * 
   * @param regionId
   * @return
   */
  private Object getRegionName(Long regionId) {
    return NumberUtils.isNullOrZero(regionId) ? null
        : ObjectUtils.isEmpty(sieConstRegionDao.get(regionId)) ? "" : sieConstRegionDao.get(regionId).getZhName();
  }

  /**
   * 获取机构guid
   * 
   * @param id
   * @return
   */
  private Object getInsGuid(Long id) {
    return ObjectUtils.isEmpty(sieInsGuidDao.get(id)) ? "" : sieInsGuidDao.get(id).getGuid();
  }

  /**
   * 获取机构类型名称
   * 
   * @param nature
   * @return
   */
  private Object getNatureName(Long nature) {
    String natureName = "";
    SieConstInsType sieConstInsType = sieConstInsTypeDao.get(nature);
    if (sieConstInsType != null) {
      natureName = sieConstInsType.getZhName();
    }
    return natureName;
  }

  /**
   * 将Date类型数据转换为String字符串（格式yyyy-MM-dd 24hh:mi:ss）
   * 
   * @param date
   * @return string
   */
  private String formateDateToString(Date date) {
    if (date == null) {
      return "";
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = formatter.format(date);
    return dateString;
  }

}
