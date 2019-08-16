package com.smate.center.open.service.data.hadoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.constant.KeywordConstants;

/**
 * 成果关键词推荐服务
 * 
 * @author lhd
 *
 */
public class HPubKeywordsRcmdServiceImpl extends ThirdDataTypeBase {

  private final static int MAX = 50;
  @Value("${solr.server.url}")
  private String serverUrl;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();

    // 校验服务参数 serviceType
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object pubDataObj = serviceData.get(KeywordConstants.KEYWORDS);
    if (pubDataObj == null) {
      logger.error("服务参数格式不正确 ,keywordList 为空或不是json格式");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_723, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return temp;
    }
    paramet.putAll(serviceData);
    paramet.put(KeywordConstants.KEYWORDS, pubDataObj);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    String keywords = paramet.get(KeywordConstants.KEYWORDS).toString();
    List<String> list = new ArrayList<String>();
    String[] split = keywords.split(";");
    for (String key : split) {
      list.add(key);
    }
    List<String> resultList = new ArrayList<String>();
    if (CollectionUtils.isNotEmpty(list)) {
      // for (String key : list) {
      // queryList(list.size(), MessageDigestUtils.messageDigest(key),
      // resultList);
      // }
      resultList.add("微波萃取");// 1
      resultList.add("原子荧光光谱");// 2
      resultList.add("甲基汞");// 3
      resultList.add("生物粪");// 4
      resultList.add("西沙群岛");// 5
      resultList.add("电纺丝");// 6
      resultList.add("邻苯二酚");// 7
      resultList.add("对苯二酚");// 8
      resultList.add("DNA光解酶");// 9
      resultList.add("环丁烷型嘧啶二聚体");// 10
      resultList.add("模型研究");// 11
      resultList.add("热分解");// 12
      resultList.add("成矿流体");// 13
      resultList.add("斑岩铜矿");// 14
      resultList.add("包古图");// 15
      resultList.add("新疆");// 16
      resultList.add("非水毛细管电泳");// 17
      resultList.add("富集");// 18
      resultList.add("评述");// 19
      resultList.add("中生代");// 20
      resultList.add("新生代");// 21
      resultList.add("橄榄石");// 22
      resultList.add("组成填图");// 23
      resultList.add("岩石圈地幔");// 24
      resultList.add("华北东部");// 25
      resultList.add("稳定同位素");// 26
      resultList.add("红山矽卡岩铜矿");// 27
      resultList.add("中甸");// 28
      resultList.add("纳米晶");// 29
      resultList.add("非晶");// 30
      resultList.add("软磁薄膜");// 31
      resultList.add("介孔");// 32
      resultList.add("环戊烯");// 33
      resultList.add("戊二醛");// 34
      resultList.add("催化氧化");// 35
      resultList.add("鼻咽肿瘤");// 36
      resultList.add("热层风");// 37
      resultList.add("电离层");// 38
      resultList.add("数值模拟");// 39
      resultList.add("基因表达");// 40
      resultList.add("BRD7基因");// 41
      resultList.add("飞秒激光");// 42
      resultList.add("主动同步");// 43
      resultList.add("锁相环");// 44
      resultList.add("时间抖动");// 45
      resultList.add("附子");// 46
      resultList.add("炮制机理");// 47
      resultList.add("电喷雾串联质谱");// 48
      resultList.add("二萜类生物碱");// 49
      resultList.add("高离化态");// 50
    }
    resultMap = super.successMap(OpenMsgCodeConsts.SCM_000, resultList);
    return resultMap;
  }

  private void queryList(int count, String key, List<String> resultList) {
    try {
      SolrServer server = new HttpSolrServer(serverUrl);
      SolrQuery solrQuery = new SolrQuery();
      solrQuery.setRequestHandler("qt");// ????????????未设置
      solrQuery.setQuery(key);// 查询条件
      if (MAX % count == 0) {
        solrQuery.setRows(MAX / count);// 范围
      } else {
        solrQuery.setRows(MAX / count + 1);// 范围
      }
      solrQuery.setSort("id", ORDER.desc);// 排序
      QueryResponse response = server.query(solrQuery);
      SolrDocumentList solrDocumentList = response.getResults();
      if (CollectionUtils.isNotEmpty(solrDocumentList)) {
        for (SolrDocument solrDocument : solrDocumentList) {
          resultList.add(solrDocument.getFieldValue("key2") + "");// ====
          // list.add(solrDocument.get("key2") + "");// ===
        }
      }
      if (resultList.size() > MAX) {
        resultList = resultList.subList(0, MAX);
      }
    } catch (SolrServerException e) {
      logger.error("关键词推荐-queryList方法 出错", e);
    }
  }

}
