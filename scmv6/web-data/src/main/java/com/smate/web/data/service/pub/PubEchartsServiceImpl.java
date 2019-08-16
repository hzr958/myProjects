package com.smate.web.data.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.data.consts.HbaseConsts;
import com.smate.web.data.form.pub.PubEchartsForm;
import com.smate.web.data.model.pub.HKeywordsItem;
import com.smate.web.data.utils.HbaseUtils;
import com.smate.web.data.utils.MessageDigestUtils;

/**
 * 成果图表展示服务实现类
 * 
 * @author lhd
 *
 */
@Service("pubEchartsService")
@Transactional(rollbackFor = Exception.class)
public class PubEchartsServiceImpl implements PubEchartsService {
  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_HADOOP_PUB = "hadoop_pub_index";

  @Override
  public void buildChartData(PubEchartsForm form) throws Exception {
    List<String> groupList = new ArrayList<String>();
    List<Cell> cells = HbaseUtils.queryList(HbaseConsts.TB_PK_STRENGTH_STR, form.getPubId() + "");
    Map<String, Object> echartMap = new HashMap<String, Object>();
    if (CollectionUtils.isNotEmpty(cells)) {
      for (Cell cell : cells) {
        String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell)); // 取到修饰名(列名)
        String cidStr = qualifier.substring(qualifier.length() - 1);
        groupList.add(cidStr);
        String jsonStr = Bytes.toString(CellUtil.cloneValue(cell)); // 取到值
        echartMap.put(qualifier, JacksonUtils.jsonObject(jsonStr));
        if (jsonStr.contains("en_US") && jsonStr.contains("zh_CN")) {
          form.setZhAndEn(true);
        }
      }
      form.setShowMap(echartMap);
      form.setGroupList(groupList);
    } else {
      form.setShowMap(echartMap);
    }
  }

  public static void main(String[] args) {
    String key = "cold spray";
    key = key.replaceAll("\\s+", " ").trim().toLowerCase();
    System.out.println(MessageDigestUtils.messageDigest(key));
  }

  @Override
  public void buildRcmdChartData(PubEchartsForm form) throws Exception {
    List<String> repeatList = new ArrayList<String>();
    List<HKeywordsItem> list = new ArrayList<HKeywordsItem>();
    String[] split = StringUtils.split(form.getKeywords(), ";");
    if (split != null) {
      for (String key : split) {
        if (StringUtils.isNotBlank(key)) {
          key = key.replaceAll("\\s+", " ").trim().toLowerCase();
          if (!repeatList.contains(key)) {
            repeatList.add(key);
            queryList(MessageDigestUtils.messageDigest(key), list);
          }
        }
      }
    }
    form.setShowList(list);
  }

  private void queryList(String key, List<HKeywordsItem> list) throws Exception {
    Assert.notNull(key, "关键词key不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl + "collection2");
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/keywordsRcmd");
    query.setQuery(key);// 查询条件,即 key1Sha1
    query.setSort("key12Jaccard", ORDER.desc);// 根据公式值 key12Jaccard 排序
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_HADOOP_PUB);

    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(0);
    query.setRows(10);
    QueryResponse response = server.query(query);
    SolrDocumentList solrDocumentList = response.getResults();
    if (CollectionUtils.isNotEmpty(solrDocumentList)) {
      for (SolrDocument doc : solrDocumentList) {
        HKeywordsItem item = new HKeywordsItem();
        item.setKey1(doc.getFieldValue("key1") + "");
        item.setKey1Val(NumberUtils.toDouble(doc.getFieldValue("key1Val") + ""));
        item.setKey1Sha1(doc.getFieldValue("key1Sha1") + "");
        item.setKey2(doc.getFieldValue("key2") + "");
        item.setKey2Val(NumberUtils.toDouble(doc.getFieldValue("key2Val") + ""));
        item.setKey2Sha1(doc.getFieldValue("key2Sha1") + "");
        item.setKey12Sha1(doc.getFieldValue("key12Sha1") + "");
        item.setKey12Val(NumberUtils.toDouble(doc.getFieldValue("key12Val") + ""));
        item.setKey12Jaccard(NumberUtils.toDouble(doc.getFieldValue("key12Jaccard") + ""));
        item.setcId(doc.getFieldValue("cId") + "");
        list.add(item);
      }
    }
  }

}
