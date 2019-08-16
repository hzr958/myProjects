package com.smate.sie.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.constant.KeywordConstants;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.sie.center.open.dao.dept.Sie6InsPsnDao;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegionDao;
import com.smate.sie.core.base.utils.dao.statistics.SieInsStatisticsDao;
import com.smate.sie.core.base.utils.model.ins.SieInsRegion;
import com.smate.sie.core.base.utils.model.statistics.SieInsStatistics;

/**
 * hd
 */
public class SearchInsByPaperKeywordsServiceImpl extends ThirdDataTypeBase {
  @Value("${solr.server.url}")
  private String serverUrl;
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private Sie6InsPsnDao sie6InsPsnDao;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;
  @Autowired
  private SieInsRegionDao sieInsRegionDao;
  @Autowired
  private SieConstRegionDao sieConstRegionDao;
  @Autowired
  private SieInsStatisticsDao sieInsStatisticsDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(parameter, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    if (serviceData != null) {
      parameter.putAll(serviceData);
    }
    if (parameter.get(KeywordConstants.KEYWORDS) == null) {
      logger.error("根据关键词检索相关作者所在的单位，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 = 根据关键词检索相关作者所在的单位，关键词列表 keywordList 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.KEYWORDS).toString())) {
      logger.error("根据关键词检索相关作者所在的单位，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 = 根据关键词检索相关作者所在的单位，关键词列表 keywordList 不能为空 ");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    QueryFields queryFields = new QueryFields();
    String keywords = "\"" + parameter.get(KeywordConstants.KEYWORDS).toString().replaceAll("^;+", "")
        .replaceAll(";+$", "").replaceAll(";", "\"\"") + "\"";
    queryFields.setKeywords(keywords);
    queryFields.setSearchString("");
    try {
      List<Long> insIdsList = new ArrayList<Long>();
      Map<String, Object> solrMap = solrIndexService.queryPersonsByPsnKw(0, 100, queryFields);
      fillPersonsData(solrMap, insIdsList);
      fllInsData(insIdsList, dataList);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
    } catch (SolrServerException e) {
      logger.error("根据关键词检索相关作者所在的单位数据出错", e);
      return errorMap("根据关键词检索相关作者所在的单位数据出错", parameter, e.toString());
    }

    return successMap("根据关键词检索相关作者所在的单位数据成功", dataList);
  }

  @SuppressWarnings({"unchecked", "deprecation"})
  private void fillPersonsData(Map<String, Object> mapData, List<Long> insIdsList) {
    try {
      Set<Long> psnIdsSet = new HashSet<Long>();
      String items = (String) mapData.get(SolrIndexSerivceImpl.RESULT_ITEMS);
      boolean flag = JacksonUtils.isJsonString(items);
      if (flag) {
        List<Map<String, Object>> itemsList = JacksonUtils.jsonToList(items);
        if (itemsList != null && itemsList.size() > 0) {
          for (Map<String, Object> map : itemsList) {
            if (map.get("psnId") != null && NumberUtils.isNumber(map.get("psnId").toString())) {
              psnIdsSet.add(NumberUtils.toLong(map.get("psnId").toString()));
            }
          }
        }
        if (!psnIdsSet.isEmpty()) {
          List<Long> psnIdsList = new ArrayList<Long>(psnIdsSet);
          if (CollectionUtils.isNotEmpty(psnIdsList)) {
            insIdsList.addAll(sie6InsPsnDao.getInsIdBypsnId(psnIdsList));
          }
        }

      }

    } catch (Exception e) {
      logger.error("根据关键词检索相关作者所在的单位，获取关键词对应的成果Id出错", e);
    }

  }

  private void fllInsData(List<Long> inss, List<Map<String, Object>> dataList) {
    try {
      if (CollectionUtils.isNotEmpty(inss)) {
        List<SieInsStatistics> insTen = sieInsStatisticsDao.getTopTenInsId(inss);
        for (SieInsStatistics inst : insTen) {
          Long insId = inst.getInsId();
          if (insId != null) {
            Sie6Institution ins = ObjectUtils.isEmpty(sie6InstitutionDao.get(insId)) ? new Sie6Institution()
                : sie6InstitutionDao.get(insId);
            Sie6InsPortal portal =
                ObjectUtils.isEmpty(sie6InsPortalDao.get(insId)) ? new Sie6InsPortal() : sie6InsPortalDao.get(insId);
            SieInsRegion sieInsRegion =
                ObjectUtils.isEmpty(sieInsRegionDao.get(insId)) ? new SieInsRegion() : sieInsRegionDao.get(insId);
            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            dataMap.put("ins_id", ins.getId());
            dataMap.put("ins_name", StringUtils.isEmpty(ins.getZhName()) ? "" : ins.getZhName());
            dataMap.put("pub_sun", inst.getPubSum());
            dataMap.put("nature", ins.getNature());
            dataMap.put("nature_name", getNatureName(ins.getNature()));
            dataMap.put("prv_id", sieInsRegion.getPrvId());
            dataMap.put("prv_name", getRegionName(sieInsRegion.getPrvId()));
            dataMap.put("domain", StringUtils.isEmpty(portal.getDomain()) ? "" : portal.getDomain());
            dataList.add(dataMap);
          }
        }
      }

    } catch (Exception e) {
      logger.error("根据关键词检索相关作者所在的单位，获取单位数据出错", e);
    }
  }

  private Object getNatureName(Long nature) {
    // 1高校，2研究中心，3资助机构，4企业，5出版社，6协会，7医院，99其他
    String natureName = "";
    if (nature == null) {
      return natureName;
    }
    Integer i = Integer.valueOf(nature + "");
    switch (i) {
      case 1:
        natureName = "高校";
        break;
      case 2:
        natureName = "研究中心";
        break;
      case 3:
        natureName = "资助机构";
        break;
      case 4:
        natureName = "企业";
        break;
      case 5:
        natureName = "出版社";
        break;
      case 6:
        natureName = "协会";
        break;
      case 7:
        natureName = "医院";
        break;
      case 99:
        natureName = "其他";
        break;
      default:
        natureName = "";
    }
    return natureName;
  }

  private Object getRegionName(Long regionId) {
    if (Objects.isNull(regionId) || regionId.equals(0L)) {
      return null;
    } else {
      return ObjectUtils.isEmpty(sieConstRegionDao.get(regionId)) ? "" : sieConstRegionDao.get(regionId).getZhName();
    }
  }
}
