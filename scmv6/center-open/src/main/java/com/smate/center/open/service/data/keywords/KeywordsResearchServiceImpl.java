package com.smate.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.constant.KeywordConstants;
import com.smate.core.base.utils.json.JacksonUtils;

@Transactional(rollbackFor = Exception.class)
public class KeywordsResearchServiceImpl extends ThirdDataTypeBase {
  @Autowired
  private SolrIndexService solrIndexService;

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

    if (parameter.get(KeywordConstants.START_YEAR) == null) {
      logger.error("关键词科研趋势分析，分析周期开始年份 startYear 不能为空 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_797, parameter, "SCM_797  关键词科研趋势分析，服务参数     分析周期开始年份 startYear不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.START_YEAR).toString())) {
      logger.error("关键词科研趋势分析，分析周期开始年份 startYear 不能为空 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_797, parameter, "SCM_797  关键词科研趋势分析，服务参数     分析周期开始年份 startYear不能为空 ");
      return temp;
    } else if (!NumberUtils.isNumber(parameter.get(KeywordConstants.START_YEAR).toString())) {
      logger.error("根据关键词检索项目 ，分析周期开始年份 startYear格式不对 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_800, parameter, "SCM_800  根据关键词检索项目 ，服务参数     分析周期开始年份 startYear格式不对");
      return temp;
    }
    if (parameter.get(KeywordConstants.END_YEAR) == null) {
      logger.error("关键词科研趋势分析，分析周期结束年份 endyear 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_798, parameter, "SCM_798 关键词科研趋势分析，服务参数    分析周期结束年份 endyear不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.END_YEAR).toString())) {
      logger.error("关键词科研趋势分析，分析周期结束年份 endyear 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_798, parameter, "SCM_798 关键词科研趋势分析，服务参数    分析周期结束年份 endyear不能为空 ");
      return temp;
    } else if (!NumberUtils.isNumber(parameter.get(KeywordConstants.END_YEAR).toString())) {
      logger.error("根据关键词检索项目 ，分析周期结束年份 endyear格式不对 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_801, parameter, "SCM_801  根据关键词检索项目 ，服务参数     分析周期结束年份 endyear格式不对");
      return temp;
    }
    Integer startYear = Integer.parseInt(parameter.get(KeywordConstants.START_YEAR).toString());
    Integer endYear = Integer.parseInt(parameter.get(KeywordConstants.END_YEAR).toString());
    if (startYear < 1900) {
      logger.error("根据关键词检索项目 ，分析周期开始年份不在有效范围内，最小1900年");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_803, parameter, "SCM_803  根据关键词检索项目 ，分析周期开始年份不在有效范围内，最小1900年");
      return temp;
    }
    if (startYear > endYear) {
      logger.error("根据关键词检索项目 ，分析周期结束年份必须大于开始年份 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_802, parameter, "SCM_802  根据关键词检索项目 ，服务参数    分析周期结束年份必须大于开始年份");
      return temp;
    }
    if (parameter.get(KeywordConstants.KEYWORDS) == null) {
      logger.error("关键词科研趋势分析，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 关键词科研趋势分析，关键词列表 keywordList 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.KEYWORDS).toString())) {
      logger.error("关键词科研趋势分析，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 关键词科研趋势分析，关键词列表 keywordList 不能为空 ");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    Integer startYear = Integer.parseInt(parameter.get(KeywordConstants.START_YEAR).toString());
    Integer endYear = Integer.parseInt(parameter.get(KeywordConstants.END_YEAR).toString());
    List<Integer> yearList = new ArrayList<Integer>();
    for (int i = 0; i <= endYear - startYear; i++) {
      yearList.add(startYear + i);
    }
    String[] keywordSplit = parameter.get(KeywordConstants.KEYWORDS).toString().split(";");
    try {
      this.scientificResearch(keywordSplit, yearList, dataMap, dataList);
    } catch (SolrServerException e) {
      logger.error("根据关键词计算科研趋势取数据出错", e);
      return errorMap("根据关键词计算科研趋势取数据出错", parameter, e.toString());
    }
    return successMap("根据关键词计算科研趋势取数据成功", dataList);

  }

  private List<Map<String, Object>> scientificResearch(String[] keywordSplit, List<Integer> yearList,
      Map<String, Object> dataMap, List<Map<String, Object>> dataList) throws SolrServerException {
    for (String kw : keywordSplit) {
      Map<String, Object> map = new HashMap<String, Object>();
      ArrayList<Long> countByYears = new ArrayList<Long>();
      for (Integer year : yearList) {
        Long count = solrIndexService.queryPubCount(kw, year);
        countByYears.add(count);
      }
      map.put("name", kw);
      map.put("data", countByYears);
      dataList.add(map);
    }
    return dataList;
  }

}
