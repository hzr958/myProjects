package com.smate.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.consts.ConstRegionDao;
import com.smate.center.open.dao.institution.InstitutionDao;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.constant.KeywordConstants;
import com.smate.core.base.utils.json.JacksonUtils;

@Transactional(rollbackFor = Exception.class)
public class SearchInsByKeywordsServiceImpl extends ThirdDataTypeBase {
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ConstRegionDao constRegionDao;

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
      logger.error("根据关键词检索机构，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 = 根据关键词检索机构，关键词列表 keywordList 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.KEYWORDS).toString())) {
      logger.error("根据关键词检索机构，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 = 根据关键词检索机构，关键词列表 keywordList 不能为空 ");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    List<Long> regionId = new ArrayList<Long>();
    if (parameter.get(KeywordConstants.PROVINCEID) != null
        && StringUtils.isNotBlank(parameter.get(KeywordConstants.PROVINCEID).toString())) {
      regionId = constRegionDao.getReginIds(Long.valueOf(parameter.get(KeywordConstants.PROVINCEID).toString()));
    }
    try {
      List<Map<String, Object>> insList =
          institutionDao.searchInstitution(parameter.get(KeywordConstants.KEYWORDS).toString(), regionId);
      for (Map<String, Object> map : insList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("insId", map.get("insId"));
        dataMap.put("zhName", map.get("zhName") != null ? map.get("zhName").toString() : "");
        dataMap.put("enName", map.get("enName") != null ? map.get("zhName").toString() : "");
        dataMap.put("zhAddress", map.get("zhAddress") != null ? map.get("zhAddress").toString() : "");
        dataMap.put("enAddress", map.get("enAddress") != null ? map.get("enAddress").toString() : "");
        dataMap.put("url", map.get("url") != null ? map.get("url").toString() : "");
        dataList.add(dataMap);
      }
    } catch (Exception e) {
      logger.error("根据关键词检索机构数据出错", e);
      return errorMap("根据关键词检索机构数据出错", parameter, e.toString());
    }
    return successMap("根据关键词检索机构数据成功", dataList);
  }

}
