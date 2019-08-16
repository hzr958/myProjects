package com.smate.sie.web.application.service.analysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.web.application.dao.analysis.KpiKeywordsTfCotfNDisDao;
import com.smate.sie.web.application.dao.analysis.KpiKeywordsTfCotfNKwDao;
import com.smate.sie.web.application.model.analysis.KpiKeywordsTfCotfNDis;
import com.smate.sie.web.application.model.analysis.KpiKeywordsTfCotfNKw;
import com.smate.sie.web.application.model.consts.ConstDisciplineNsfc;
import com.smate.sie.web.application.service.consts.ConstDisciplineNsfcService;

/**
 * 开题分析服务类
 * 
 * @author sjzhou
 *
 */
@Service("problemAnalysisService")
@Transactional(rollbackFor = Exception.class)
public class ProblemAnalysisServiceImpl implements ProblemAnalysisService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private KpiKeywordsTfCotfNDisDao kpiKeywordsTfCotfNDisDao;
  @Autowired
  private KpiKeywordsTfCotfNKwDao kpiKeywordsTfCotfNKwDao;
  @Autowired
  private ConstDisciplineNsfcService constDisciplineNsfcService;

  // 抽取关键词
  @Override
  public List<Map<String, Object>> extractKeyWordsFormInfo(String title, String summary) throws SysServiceException {
    List<String> keywordSplit = new ArrayList<String>();
    List<Map<String, Object>> keyWordsList = new ArrayList<Map<String, Object>>();
    try {
      Object obj = extractKeyWords(title, summary);
      if (obj != null) {
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) resultMap.get("result");
        if (result.get("keywordSplit") != "") {
          keywordSplit = (List<String>) result.get("keywordSplit");
          for (int i = 0; i < keywordSplit.size(); i++) {
            Map<String, Object> keyword = new HashMap<String, Object>();
            keyword.put("kw", keywordSplit.get(i));
            keyWordsList.add(keyword);
          }
          return keyWordsList.size() > 5 ? keyWordsList.subList(0, 5) : keyWordsList;
        }
      }
    } catch (Exception e) {
      logger.error("抽取关键词出错", e);
      throw new SysServiceException(e);
    }
    return null;
  }

  private Object extractKeyWords(String title, String summary) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("zhTitle", title);
      data.put("zhAbstract", summary);
      mapDate.put("openid", 99999999L);// 系统默认openId
      mapDate.put("token", "11111111kwsplit1");// 系统默认token
      mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    } catch (Exception e) {
      logger.error("抽取关键词出错", e);
      throw new SysServiceException(e);
    }
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  // 科研趋势
  @Override
  public Map<String, Object> researchTrendByKeyWords(String selectKeyword) throws SysServiceException {
    List<Object> pubSerisBar = new ArrayList<Object>();
    List<Object> patSerisBar = new ArrayList<Object>();
    List<Object> prjSerisBar = new ArrayList<Object>();
    List<String> category = new ArrayList<String>();
    try {
      Object obj = researchTrend(selectKeyword);
      if (obj != null) {
        Map<String, Object> resultMapFormJson = JacksonUtils.jsonToMap(obj.toString());
        Object resultObj = resultMapFormJson.get("result");
        if (resultObj != null) {
          List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultObj;
          if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
              if ("论文".equals(map.get("type"))) {
                pubSerisBar = (List<Object>) map.get("countData");
              } else if ("专利".equals(map.get("type"))) {
                patSerisBar = (List<Object>) map.get("countData");
              } else if ("项目".equals(map.get("type"))) {// 项目暂时不考虑
                prjSerisBar = (List<Object>) map.get("countData");
              }
            }
            category = (List<String>) resultList.get(0).get("yearList");
          }
        }
      }
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("pubSerisBar", pubSerisBar);
      result.put("patSerisBar", patSerisBar);
      result.put("prjSerisBar", prjSerisBar);
      result.put("category", category);
      return result;
    } catch (Exception e) {
      logger.error("根据输入的关键词计算科研趋势报错", e);
      throw new SysServiceException(e);
    }
  }

  private Object researchTrend(String selectKeyword) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      // 获取当前年份
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
      String endYear = sdf.format(new Date());
      data.put("keywordList", selectKeyword);
      data.put("startYear", "1990");
      data.put("endYear", endYear);
      mapDate.put("openid", 99999999L);// 系统默认openId
      mapDate.put("token", "11111111kwsios01");// 系统默认token
      mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    } catch (Exception e) {
      logger.error("根据输入的关键词计算科研趋势报错", e);
      throw new SysServiceException(e);
    }
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  // 相关学者
  @Override
  public List<Map<String, Object>> relatedResearchers(String selectKeyword) throws SysServiceException {
    try {
      Object obj = getResearchers(selectKeyword);
      if (obj != null) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMapFormJson = JacksonUtils.jsonToMap(obj.toString());
        Object resultObj = resultMapFormJson.get("result");
        if (resultObj != null) {
          List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultObj;
          if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
              Long psnId = NumberUtils.toLong(map.get("psnId").toString());
              if (map.get("psnShortUrl") == null) {
                map.put("psnShortUrl", "###");
              }
              PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
              if (psnStatistics != null) {
                map.put("pubCount", psnStatistics.getPubSum());
              } else {
                map.put("pubCount", "0");
              }
              dataList.add(map);
            }
          }
          Collections.sort(dataList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
              return org.apache.commons.lang.ObjectUtils.toString(o2.get("pubCount"))
                  .compareTo(org.apache.commons.lang.ObjectUtils.toString(o1.get("pubCount")));// 降序
            }
          });
          return dataList.size() > 20 ? dataList.subList(0, 20) : dataList;
        }
      }
    } catch (Exception e) {
      logger.error("根据输入的关键词查找相关学者报错", e);
      throw new SysServiceException(e);
    }
    return null;
  }

  private Object getResearchers(String selectKeyword) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("keywordList", selectKeyword);
      mapDate.put("openid", 99999999L);// 系统默认openId
      mapDate.put("token", "11111111seapbpkw");// 系统默认token
      mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    } catch (Exception e) {
      logger.error("根据输入的关键词查找相关学者报错", e);
      throw new SysServiceException(e);
    }
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  // 相关单位
  @Override
  public Map<String, Object> relatedIntitutions(String selectKeyword) throws SysServiceException {
    List<Object> insPubSerisBar = new ArrayList<Object>();
    List<Object> insPortal = new ArrayList<Object>();
    List<Object> ins = new ArrayList<Object>();
    try {
      Object obj = getIntitutions(selectKeyword);
      if (obj != null) {
        Map<String, Object> resultMapFormJson = JacksonUtils.jsonToMap(obj.toString());
        Object resultObj = resultMapFormJson.get("result");
        if (resultObj != null) {
          List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultObj;
          Collections.sort(resultList, new MapComparatorAsc());
          if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
              insPubSerisBar.add(map.get("pub_sun"));
              insPortal.add(map.get("domain"));
              String insName = map.get("ins_name").toString();
              if (insName.length() > 12) {
              }
              ins.add(insName);
            }
          }
          Map<String, Object> result = new HashMap<String, Object>();
          result.put("insPubSerisBar", insPubSerisBar);
          result.put("category", ins);
          result.put("insPortal", insPortal);
          return result;
        }
      }
    } catch (Exception e) {
      logger.error("根据输入的关键词查找相关单位报错", e);
      throw new SysServiceException(e);
    }
    return null;
  }

  private Object getIntitutions(String selectKeyword) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("keywordList", selectKeyword);
      mapDate.put("openid", 99999999L);// 系统默认openId
      mapDate.put("token", "11111111seaibpkw");// 系统默认token
      mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    } catch (Exception e) {
      logger.error("根据输入的关键词查找相关单位报错", e);
      throw new SysServiceException(e);
    }
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  @Override
  public Map<String, Object> relatedDisByKeyWords(String selectKeyword) throws SysServiceException {
    List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> linkList = new ArrayList<Map<String, Object>>();
    Map<String, Object> nodeMap1 = new HashMap<String, Object>();
    Map<String, Object> webkitDep = new HashMap<String, Object>();
    try {
      List<KpiKeywordsTfCotfNDis> nsfcList = kpiKeywordsTfCotfNDisDao.findNsfcKeywordsListBykw(selectKeyword);// 拿到学科
      nodeMap1.put("category", "0");
      nodeMap1.put("name", selectKeyword);
      nodeMap1.put("value", "");
      nodeMap1.put("id", 0);
      nodeList.add(nodeMap1);
      int id = 1;
      for (KpiKeywordsTfCotfNDis kpiKeywordsTfCotfNDis : nsfcList) {
        Map<String, Object> nodeMap2 = new HashMap<String, Object>();
        Map<String, Object> linkMap2 = new HashMap<String, Object>();
        String sub3DisCode = kpiKeywordsTfCotfNDis.getDisPk().getSub3DisCode();
        ConstDisciplineNsfc disNsfc = constDisciplineNsfcService.findConstBySub3Dis(sub3DisCode);
        // 展示的节点
        nodeMap2.put("category", "1");
        nodeMap2.put("name", disNsfc.getZhName());
        nodeMap2.put("value", kpiKeywordsTfCotfNDis.getCounts());
        nodeMap2.put("id", id);
        nodeList.add(nodeMap2);
        // 节点之间连接
        linkMap2.put("source", 0);
        linkMap2.put("target", id);
        linkMap2.put("value", kpiKeywordsTfCotfNDis.getCounts());
        linkList.add(linkMap2);
        id++;
      }
      int source = 1;
      for (KpiKeywordsTfCotfNDis kpiKeywordsTfCotfNDis : nsfcList) {
        String sub3DisCode = kpiKeywordsTfCotfNDis.getDisPk().getSub3DisCode();
        List<KpiKeywordsTfCotfNKw> anaKwLst = kpiKeywordsTfCotfNKwDao.findNsfcKeywordsListBykw(sub3DisCode);
        for (KpiKeywordsTfCotfNKw kpiKeywordsTfCotfNKw : anaKwLst) {
          Map<String, Object> nodeMap3 = new HashMap<String, Object>();
          Map<String, Object> linkMap3 = new HashMap<String, Object>();
          // 展示的节点
          nodeMap3.put("category", "2");
          nodeMap3.put("name", kpiKeywordsTfCotfNKw.getKwPk().getKwFirst());
          nodeMap3.put("value", kpiKeywordsTfCotfNKw.getCounts());
          nodeMap3.put("id", id);
          nodeList.add(nodeMap3);
          // 节点之间连接
          linkMap3.put("source", source);
          linkMap3.put("target", id);
          linkMap3.put("value", kpiKeywordsTfCotfNKw.getCounts());
          linkList.add(linkMap3);
          id++;
        }
        source++;
      }
      webkitDep.put("nodes", nodeList);
      webkitDep.put("links", linkList);
    } catch (Exception e) {
      logger.error("根据输入的关键词查找相关学科报错", e);
      throw new SysServiceException(e);
    }
    return webkitDep;
  }

  static class MapComparatorAsc implements Comparator<Map<String, Object>> {
    @Override
    public int compare(Map<String, Object> m1, Map<String, Object> m2) {
      Integer v1 = Integer.valueOf(m1.get("pub_sun").toString());
      Integer v2 = Integer.valueOf(m2.get("pub_sun").toString());
      if (v1 != null) {
        return v1.compareTo(v2);
      }
      return 0;
    }
  }
}
