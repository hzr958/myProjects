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
public class KeywordsInputOutputServiceImpl extends ThirdDataTypeBase {
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
      logger.error("关键词投入产出趋势分析，分析周期开始年份 startYear 不能为空 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_797, parameter, "SCM_797  关键词投入产出趋势分析，服务参数     分析周期开始年份 startYear不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.START_YEAR).toString())) {
      logger.error("关键词投入产出趋势分析，分析周期开始年份 startYear 不能为空 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_797, parameter, "SCM_797  关键词投入产出趋势分析，服务参数     分析周期开始年份 startYear不能为空 ");
      return temp;
    } else if (!NumberUtils.isNumber(parameter.get(KeywordConstants.START_YEAR).toString())) {
      logger.error("根据关键词检索项目 ，分析周期开始年份 startYear格式不对 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_800, parameter, "SCM_800  根据关键词检索项目 ，服务参数     分析周期开始年份 startYear格式不对");
      return temp;
    }
    if (parameter.get(KeywordConstants.END_YEAR) == null) {
      logger.error("关键词投入产出趋势分析，分析周期结束年份 endyear 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_798, parameter, "SCM_798 关键词投入产出趋势分析，服务参数    分析周期结束年份 endyear不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.END_YEAR).toString())) {
      logger.error("关键词投入产出趋势分析，分析周期结束年份 endyear 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_798, parameter, "SCM_798 关键词投入产出趋势分析，服务参数    分析周期结束年份 endyear不能为空 ");
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
      logger.error("关键词投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 关键词投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.KEYWORDS).toString())) {
      logger.error("关键词投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 关键词投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Integer startYear = Integer.parseInt(parameter.get(KeywordConstants.START_YEAR).toString());
    Integer endYear = Integer.parseInt(parameter.get(KeywordConstants.END_YEAR).toString());
    List<Integer> yearList = new ArrayList<Integer>();
    for (int i = 0; i <= endYear - startYear; i++) {
      yearList.add(startYear + i);
    }
    String keywords = "\"" + parameter.get(KeywordConstants.KEYWORDS).toString().replaceAll("^;+", "")
        .replaceAll(";+$", "").replaceAll(";", "\"\"") + "\"";

    // 计算投入产出趋势
    try {
      this.InPutOutputTrend(keywords, yearList, dataList);
    } catch (SolrServerException e) {
      logger.error("根据关键词计算投入产出趋势取数据出错", e);
      return errorMap("根据关键词计算科研趋势取数据出错", parameter, e.toString());
    }

    return successMap("根据关键词计算投入产出趋势取数据成功", dataList);
  }

  private List<Map<String, Object>> InPutOutputTrend(String keywords, List<Integer> yearList,
      List<Map<String, Object>> dataList) throws SolrServerException {
    Map<String, Object> pubMap = new HashMap<String, Object>();
    Map<String, Object> prjMap = new HashMap<String, Object>();
    Map<String, Object> patMap = new HashMap<String, Object>();
    Map<String, Object> citeMap = new HashMap<String, Object>();
    Map<String, Object> techMap = new HashMap<String, Object>();
    ArrayList<Long> pubCountByYears = new ArrayList<Long>();// 论文
    ArrayList<Long> prjCountByYears = new ArrayList<Long>();// 项目
    ArrayList<Long> patCountByYears = new ArrayList<Long>();// 专利
    ArrayList<Long> citeCountByYears = new ArrayList<Long>();// 引用
    ArrayList<Long> techCountByYears = new ArrayList<Long>();// 技术合同

    for (Integer year : yearList) {
      Long pubCount = solrIndexService.queryPubCountByKeywords(keywords, year);
      pubCountByYears.add(pubCount);

      Long prjCount = solrIndexService.queryPrjCountByKeywords(keywords, year);
      prjCountByYears.add(prjCount);

      Long patCount = solrIndexService.queryPatCountByKeywords(keywords, year);
      patCountByYears.add(patCount);
      citeCountByYears.add(0L);
      techCountByYears.add(0L);

      /*
       * Long citeCount = solrIndexService.queryCiteCountByKeywords(keywords, year);
       * citeCountByYears.add(citeCount);
       * 
       * Long techCount = solrIndexService.queryTechCountByKeywords(keywords, year);
       * techCountByYears.add(techCount);
       */
    }
    pubMap.put("yearList", yearList);
    pubMap.put("type", "论文");
    pubMap.put("countData", pubCountByYears);
    dataList.add(pubMap);

    prjMap.put("yearList", yearList);
    prjMap.put("type", "项目");
    prjMap.put("countData", prjCountByYears);
    dataList.add(prjMap);

    patMap.put("yearList", yearList);
    patMap.put("type", "专利");
    patMap.put("countData", patCountByYears);
    dataList.add(patMap);

    citeMap.put("yearList", yearList);
    citeMap.put("type", "引用");
    citeMap.put("countData", citeCountByYears);
    dataList.add(citeMap);

    techMap.put("yearList", yearList);
    techMap.put("type", "技术合同");
    techMap.put("countData", techCountByYears);
    dataList.add(techMap);
    return dataList;
  }

}
