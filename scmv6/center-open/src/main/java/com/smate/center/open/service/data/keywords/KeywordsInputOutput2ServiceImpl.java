package com.smate.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.smate.center.open.dao.consts.ConstRegionDao;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.constant.KeywordConstants;
import com.smate.core.base.utils.json.JacksonUtils;

@Transactional(rollbackFor = Exception.class)
public class KeywordsInputOutput2ServiceImpl extends ThirdDataTypeBase {
  @Autowired
  private SolrIndexService solrIndexService;
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

    if (parameter.get(KeywordConstants.START_YEAR) == null) {
      logger.error("关键词省份投入产出趋势分析，分析周期开始年份 startYear 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_797, parameter,
          "SCM_797  关键词省份投入产出趋势分析，服务参数     分析周期开始年份 startYear不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.START_YEAR).toString())) {
      logger.error("关键词省份投入产出趋势分析，分析周期开始年份 startYear 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_797, parameter,
          "SCM_797  关键词省份投入产出趋势分析，服务参数     分析周期开始年份 startYear不能为空 ");
      return temp;
    } else if (!NumberUtils.isNumber(parameter.get(KeywordConstants.START_YEAR).toString())) {
      logger.error("关键词省份投入产出趋势分析 ，分析周期开始年份 startYear格式不对 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_800, parameter,
          "SCM_800  关键词省份投入产出趋势分析 ，服务参数     分析周期开始年份 startYear格式不对");
      return temp;
    }
    if (parameter.get(KeywordConstants.END_YEAR) == null) {
      logger.error("关键词省份投入产出趋势分析，分析周期结束年份 endyear 不能为空 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_798, parameter, "SCM_798 关键词省份投入产出趋势分析，服务参数    分析周期结束年份 endyear不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.END_YEAR).toString())) {
      logger.error("关键词省份投入产出趋势分析，分析周期结束年份 endyear 不能为空 ");
      temp =
          super.errorMap(OpenMsgCodeConsts.SCM_798, parameter, "SCM_798 关键词省份投入产出趋势分析，服务参数    分析周期结束年份 endyear不能为空 ");
      return temp;
    } else if (!NumberUtils.isNumber(parameter.get(KeywordConstants.END_YEAR).toString())) {
      logger.error("根据关键词检索项目 ，分析周期结束年份 endyear格式不对 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_801, parameter, "SCM_801  根据关键词检索项目 ，服务参数     分析周期结束年份 endyear格式不对");
      return temp;
    }
    if (parameter.get(KeywordConstants.INPUT_OUTPUT_TYPE) == null) {
      logger.error("关键词省份投入产出趋势分析，投入产出type 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_796, parameter, "SCM_796   关键词省份投入产出趋势分析，投入产出type 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.INPUT_OUTPUT_TYPE).toString())) {
      logger.error("关键词省份投入产出趋势分析，投入产出type 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_796, parameter, "SCM_796   关键词省份投入产出趋势分析，投入产出type 不能为空 ");
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
      logger.error("关键词省份投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 关键词省份投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      return temp;
    } else if (StringUtils.isBlank(parameter.get(KeywordConstants.KEYWORDS).toString())) {
      logger.error("关键词省份投入产出趋势分析，关键词列表 keywordList 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_799, parameter, "SCM_799 关键词省份投入产出趋势分析，关键词列表 keywordList 不能为空 ");
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
      Integer type = Integer.parseInt(parameter.get(KeywordConstants.INPUT_OUTPUT_TYPE).toString());
      this.InPutOutputTrend(keywords, yearList, dataList, type);
    } catch (SolrServerException e) {
      logger.error("根据关键词计算省份投入产出趋势取数据出错", e);
      return errorMap("根据关键词计算省份投入产出趋势取数据出错", parameter, e.toString());
    }

    return successMap("根据关键词计算省份投入产出趋势取数据成功", dataList);
  }

  private List<Map<String, Object>> InPutOutputTrend(String keywords, List<Integer> yearList,
      List<Map<String, Object>> dataList, Integer type) throws SolrServerException {

    List<Long> regionIds = constRegionDao.getAllProvinceId();
    List<Map<String, Long>> regionCountList = new ArrayList<Map<String, Long>>();
    for (Long regionId : regionIds) {
      if (1 == type) {// 论文
        regionCountList.add(solrIndexService.queryPubCountByRegionId(keywords, regionId));
      } else if (2 == type) {// 专利
        regionCountList.add(solrIndexService.queryPatCountByRegionId(keywords, regionId));
      } else if (3 == type) {// 项目
        regionCountList.add(solrIndexService.queryPrjCountByRegionId(keywords, regionId));
      } else if (4 == type) {// 技术合同（暂时没有）
        regionCountList.add(solrIndexService.queryTechCountByRegionId(keywords, regionId));
      }

    }

    /*
     * regionCountTmpList [{rsCount=0, provinceId=220000}, {rsCount=0, provinceId=640000}, {rsCount=0,
     * provinceId=650000}, {rsCount=0, provinceId=330000}, {rsCount=0, provinceId=610000}, {rsCount=0,
     * provinceId=620000}, {rsCount=0, provinceId=210000}, {rsCount=0, provinceId=230000}, {rsCount=0,
     * provinceId=310000}, {rsCount=0, provinceId=320000}, {rsCount=0, provinceId=110000}, {rsCount=0,
     * provinceId=120000}]
     */

    // 对regionCountTmpList进行排序

    Collections.sort(regionCountList, new Comparator<Map<String, Long>>() {
      @Override
      public int compare(Map<String, Long> o1, Map<String, Long> o2) {
        return o2.get("rsCount").compareTo(o1.get("rsCount"));
      }
    });

    for (int i = 0; i < 8; i++) {
      Map<String, Object> dataMap = new HashMap<String, Object>();
      ArrayList<Long> countData = new ArrayList<Long>();
      for (Integer year : yearList) {
        Long totalCount = null;
        if (1 == type) {// 论文
          totalCount = solrIndexService.queryCountPub(keywords, year,
              Long.parseLong(regionCountList.get(i).get("provinceId").toString()));
        } else if (2 == type) {// 专利
          totalCount = solrIndexService.queryCountPat(keywords, year,
              Long.parseLong(regionCountList.get(i).get("provinceId").toString()));
        } else if (3 == type) {// 项目
          totalCount = solrIndexService.queryCountPrj(keywords, year,
              Long.parseLong(regionCountList.get(i).get("provinceId").toString()));
        } else if (4 == type) {
          totalCount = solrIndexService.queryCountTech(keywords, year,
              Long.parseLong(regionCountList.get(i).get("provinceId").toString()));
        }
        countData.add(totalCount);
      }
      dataMap.put("provinceId", Long.parseLong(regionCountList.get(i).get("provinceId").toString()));
      dataMap.put("countData", countData);
      dataMap.put("yearList", yearList);
      dataList.add(dataMap);
    }

    return dataList;
  }

}
