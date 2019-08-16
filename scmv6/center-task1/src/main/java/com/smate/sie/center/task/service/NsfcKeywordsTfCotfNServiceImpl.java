package com.smate.sie.center.task.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.pdwh.quartz.NsfcKeywordsTfCotfNDao;
import com.smate.center.task.model.pdwh.quartz.NsfcKeywordsTfCotfN;
import com.smate.core.base.utils.cache.CacheService;

@Service("nsfcKeywordsTfCotfNService")
@Transactional(rollbackFor = Exception.class)
public class NsfcKeywordsTfCotfNServiceImpl implements NsfcKeywordsTfCotfNService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_NSFC_KEYWORDS = "nsfc_keywords_index";
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  @Autowired
  private CacheService cacheService;
  @Autowired
  private NsfcKeywordsTfCotfNDao nsfcKeywordsTfCotfNDao;

  @Override
  public void nsfcKeywordsIndex(Integer size) {
    SolrServer server = initializeSolrServer();
    Long lastId = (Long) cacheService.get(INDEX_TYPE_NSFC_KEYWORDS, "last_nsfc_keywords_id");
    if (lastId == null) {
      lastId = 0L;
    }
    List<NsfcKeywordsTfCotfN> nsfcKeywordsList = nsfcKeywordsTfCotfNDao.getNsfcKeywordsList(lastId, size);
    Integer lastIndex = nsfcKeywordsList.size() - 1;
    Long lastNsfcKeywordsId = nsfcKeywordsList.get(lastIndex).getId();
    this.cacheService.put(INDEX_TYPE_NSFC_KEYWORDS, 60 * 60 * 24, "last_nsfc_keywords_id", lastNsfcKeywordsId);
    if (CollectionUtils.isEmpty(nsfcKeywordsList)) {
      logger.info("==========nsfcKeywordsList为空，index终止===========, time = " + new Date());
      return;
    }
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (NsfcKeywordsTfCotfN nsfcKeywords : nsfcKeywordsList) {
      if (nsfcKeywords == null || nsfcKeywords.getId() == null) {
        continue;
      }
      try {
        SolrInputDocument doc = new SolrInputDocument();
        doc = this.fillnsfcKeywordsInfo(doc, nsfcKeywords);
        docList.add(doc);
      } catch (Exception e) {
        logger.error("获取关键词与学科关系数据出错，nsfcKeywordsId=" + nsfcKeywords.getId() + "! ", e);
      }
      lastId = nsfcKeywords.getId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info(
            "nsfcKeywords索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("index索引创建出错，end = " + new Date());
      }
    }
  }

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl + "collection_tf_cotf_n");
    return server;
  }

  public SolrInputDocument fillnsfcKeywordsInfo(SolrInputDocument doc, NsfcKeywordsTfCotfN nsfcKeywords) {
    // 必须字段设定schema.xml配置
    Long nsfcId = nsfcKeywords.getId();
    String disCode = nsfcKeywords.getDisCode();

    doc.setField("businessType", INDEX_TYPE_NSFC_KEYWORDS);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", generateIdForIndex(nsfcId, INDEX_TYPE_NSFC_KEYWORDS));
    doc.setField("nsfcId", nsfcId);
    doc.setField("disCode", disCode);
    doc.setField("sub3DisCode", disCode.substring(0, 3));
    doc.setField("kwFirst", nsfcKeywords.getKwFirst());
    doc.setField("kwSecond", nsfcKeywords.getKwSecond());
    doc.setField("counts", nsfcKeywords.getCounts());
    return doc;
  }

  /*
   * 在index时区分唯一id
   */
  public Long generateIdForIndex(Long id, String type) {
    // pub前缀为100000
    if (INDEX_TYPE_PUB.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("100000" + idString);
    } else if (INDEX_TYPE_PSN.equalsIgnoreCase(type)) {
      // psn前缀为50
      String idString = String.valueOf(id);
      return Long.parseLong("50" + idString);
    } else if (INDEX_TYPE_PAT.equalsIgnoreCase(type)) {
      // pat前缀为700000
      String idString = String.valueOf(id);
      return Long.parseLong("700000" + idString);
    } else if (INDEX_TYPE_FUND.equalsIgnoreCase(type)) {
      // fund前缀为900000
      String idString = String.valueOf(id);
      return Long.parseLong("900000" + idString);
    } else if (INDEX_TYPE_KW.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("210000" + idString);
    } else {
      return id;
    }
  }
}
