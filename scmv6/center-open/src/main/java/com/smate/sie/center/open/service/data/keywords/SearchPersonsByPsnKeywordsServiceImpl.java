package com.smate.sie.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.consts.ConstRegionDao;
import com.smate.center.open.dao.profile.PsnScienceAreaDao;
import com.smate.center.open.model.consts.ConstRegion;
import com.smate.center.open.model.profile.PsnScienceArea;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.constant.KeywordConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 根据科研人员的个人关键词找人
 * 
 * @author sjzhou
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SearchPersonsByPsnKeywordsServiceImpl extends ThirdDataTypeBase {
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PersonDao personDao;

  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();

    Object data = parameter.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        parameter.putAll(dataMap);
      }
    }
    if (parameter.get(KeywordConstants.KEYWORDS) == null) {
      logger.error("根据关键词检索专家，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799  根据关键词检索专家，关键词列表 keywordList 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.KEYWORDS).toString())) {
      logger.error("根据关键词检索专家，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799  根据关键词检索专家，关键词列表 keywordList 不能为空 ");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    QueryFields queryFields = new QueryFields();
    String keywords = "\"" + parameter.get(KeywordConstants.KEYWORDS).toString().replaceAll("^;+", "")
        .replaceAll(";+$", "").replaceAll(";", "\"\"") + "\"";
    queryFields.setKeywords(keywords);
    queryFields.setSearchString("");
    try {
      Map<String, Object> solrMap = solrIndexService.queryPersonsByPsnKw(0, 20, queryFields);
      fillPersonsData(solrMap, dataList);
    } catch (SolrServerException e) {
      logger.error("根据关键词检索专家数据出错", e);
      return errorMap("根据关键词检索专家数据出错", parameter, e.toString());
    }

    return successMap("根据关键词检索专家数据成功", dataList);
  }

  private void fillPersonsData(Map<String, Object> solrMap, List<Map<String, Object>> dataList) {
    String items = (String) solrMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
    boolean flag = JacksonUtils.isJsonString(items);
    if (flag) {
      List<Map<String, Object>> itemsList = JacksonUtils.jsonToList(items);
      if (itemsList != null && itemsList.size() > 0) {
        for (Map<String, Object> map : itemsList) {
          Map<String, Object> dataMap = new HashMap<String, Object>();
          if (map.get("psnId") != null && NumberUtils.isNumber(map.get("psnId").toString())) {
            Long psnId = NumberUtils.toLong(map.get("psnId").toString());
            dataMap.put("psnId", psnId);
            if (map.get("psnName") != null) {
              dataMap.put("psnName", map.get("psnName").toString());
            } else {
              dataMap.put("psnName", "");
            }
            if (map.get("enPsnName") != null) {
              dataMap.put("enPsnName", map.get("enPsnName").toString());
            } else {
              dataMap.put("enPsnName", "");
            }

            if (map.get("title") != null) {// 职称
              dataMap.put("title", map.get("title").toString());
            } else {
              dataMap.put("title", "");
            }

            if (map.get("zhInsName") != null) {// 单位名称
              dataMap.put("zhInsName", map.get("zhInsName").toString());
            } else {
              dataMap.put("zhInsName", "");
            }

            if (map.get("enInsName") != null) {
              dataMap.put("enInsName", map.get("enInsName").toString());
            } else {
              dataMap.put("enInsName", "");
            }
            PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
            if (psnStatistics != null) {
              dataMap.put("psnPubCount", psnStatistics.getPubSum() != null ? psnStatistics.getPubSum() : 0);
              dataMap.put("psnPrjCount", psnStatistics.getPrjSum() != null ? psnStatistics.getPrjSum() : 0);
              dataMap.put("psnPatentCount", psnStatistics.getPrjSum() != null ? psnStatistics.getPatentSum() : 0);
            }
            // 人员详情短地址
            if (map.get("psnShortUrl") != null) {
              dataMap.put("psnShortUrl", map.get("psnShortUrl").toString());
            } else {
              PsnProfileUrl psnUrl = psnProfileUrlDao.get(psnId);
              if (psnUrl != null && StringUtils.isNotBlank(psnUrl.getPsnIndexUrl())) {
                dataMap.put("psnShortUrl", domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnUrl.getPsnIndexUrl());
              }
            }
            // 人员头像
            dataMap.put("avatars", personDao.getPsnImgByObjectId(psnId));
            // 获取人员科技领域列表
            List<PsnScienceArea> list = psnScienceAreaDao.queryScienceArea(psnId, 1);
            if (CollectionUtils.isNotEmpty(list)) {
              StringBuffer psnZhArea = new StringBuffer();
              StringBuffer psnEnArea = new StringBuffer();

              psnZhArea.append(list.get(0).getScienceArea());
              psnEnArea.append(list.get(0).getEnScienceArea());
              for (int i = 1; i < list.size(); i++) {
                psnZhArea.append(";");
                psnZhArea.append(list.get(i).getScienceArea());
                psnEnArea.append(";");
                psnEnArea.append(list.get(i).getEnScienceArea());
              }
              dataMap.put("psnZhScienceArea", psnZhArea.toString());
              dataMap.put("psnEnScienceArea", psnEnArea.toString());
            } else {
              dataMap.put("psnZhScienceArea", "");
              dataMap.put("psnEnScienceArea", "");
            }
            String zhPsnRegionName = "";
            String enPsnRegionName = "";
            if (personDao.getPsnRegionIdByObjectId(psnId) != null) {
              Long psnRegionId = personDao.getPsnRegionIdByObjectId(psnId);
              // 根据地区ID和上一级地区ID构建人员所在地区信息
              int count = 0;
              while (psnRegionId != null) {
                count++;
                if (count > 5) {
                  break;
                }
                ConstRegion cre = constRegionDao.findRegionNameById(psnRegionId);
                if (cre != null) {
                  psnRegionId = cre.getSuperRegionId();
                  if (StringUtils.isNotBlank(zhPsnRegionName)) {
                    zhPsnRegionName = cre.getZhName() + ", " + zhPsnRegionName;
                  } else {
                    zhPsnRegionName = cre.getZhName();
                  }
                  if (StringUtils.isNotBlank(enPsnRegionName)) {
                    enPsnRegionName = cre.getEnName() + ", " + enPsnRegionName;
                  } else {
                    enPsnRegionName = cre.getEnName();
                  }
                } else {
                  break;
                }
              }
            } else {
              zhPsnRegionName = "中国";
              enPsnRegionName = "China";
            }
            dataMap.put("psnZhRegionName", zhPsnRegionName);
            dataMap.put("psnEnRegionName", enPsnRegionName);

            dataList.add(dataMap);
          }

        }

      }
    }
  }

}
