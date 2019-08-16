package com.smate.center.task.single.service.pub;

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

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.core.base.utils.cache.CacheService;

@Service("simplePublicationIndexService")
@Transactional(rollbackFor = Exception.class)
public class SimplePublicationIndexSeriviceImpl implements SimplePublicationIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CacheService cacheService;
  @Value("${new.solr.server.url}")
  private String newServerUrl;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_SIMPLE_PDWH_PUB = "simple_pdwh_pub_index";
  public static String INDEX_TYPE_SIMPLE_SNS_PUB = "simple_sns_pub_index";

  /**
   * 初始化solr
   * 
   * @return
   */
  private SolrServer initSimpleSolrServer() {
    SolrServer server = new HttpSolrServer(newServerUrl);
    return server;
  }

  @Override
  public void simplePdwhPubIndex() {
    SolrServer server = initSimpleSolrServer();
    Long lastId = (Long) cacheService.get(INDEX_TYPE_SIMPLE_PDWH_PUB, "simple_last_pdwhpub_id");
    if (lastId == null) {
      lastId = 0L;
    }
    List<PdwhPublication> pubList = getPublist(lastId);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========publicationList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubId();
    this.cacheService.put(INDEX_TYPE_SIMPLE_PDWH_PUB, 60 * 60 * 24, "simple_last_pdwhpub_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (PdwhPublication one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_SIMPLE_PDWH_PUB);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      doc.setField("id", generateIdForIndex(one.getPubId(), INDEX_TYPE_SIMPLE_PDWH_PUB));
      doc.setField("pubId", one.getPubId());
      doc.setField("zhTitle", one.getZhTitle());
      doc.setField("enTitle", one.getEnTitle());
      docList.add(doc);
      lastId = one.getPubId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info(
            "simple pdwhpub索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("simple pdwhpub索引创建出错，end = " + new Date());
      }
    }

  }

  /**
   * sns成果索引
   */
  @Override
  public void simpleSnsPubIndex() {
    SolrServer server = initSimpleSolrServer();
    Long lastId = (Long) cacheService.get(INDEX_TYPE_SIMPLE_SNS_PUB, "simple_last_snspub_id");
    if (lastId == null) {
      lastId = 0L;
    }
    List<Publication> pubList = getSnsPublist(lastId);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========publicationList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getId();
    this.cacheService.put(INDEX_TYPE_SIMPLE_SNS_PUB, 60 * 60 * 24, "simple_last_snspub_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (Publication one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_SIMPLE_SNS_PUB);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      doc.setField("id", generateIdForIndex(one.getId(), INDEX_TYPE_SIMPLE_SNS_PUB));
      doc.setField("pubId", one.getId());
      doc.setField("pubOwnerOpenId", getOpenId(one.getPsnId()));
      doc.setField("zhTitle", one.getZhTitle());
      doc.setField("enTitle", one.getEnTitle());
      docList.add(doc);
      lastId = one.getId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info(
            "simple snspub索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("simple snspub索引创建出错，end = " + new Date());
      }
    }

  }

  /**
   * get openId by psnId
   * 
   * @param psnId
   * @return
   */
  public Long getOpenId(Long psnId) {
    Long openId = null;
    try {
      openId = openUserUnionDao.getOpenIdByPsnId(psnId);
    } catch (DaoException e) {
      logger.error("snsPub索引根据PsnId获取openId出错,psnId" + psnId);
    }
    return openId;
  }

  /**
   * 获取SNSpub
   * 
   * @param lastId
   * @return
   */
  private List<Publication> getSnsPublist(Long lastId) {
    return publicationDao.batchGetPublist(lastId, 3500);
  }

  /**
   * 获取基准库成果
   * 
   * @param lastId
   * @return
   */
  public List<PdwhPublication> getPublist(Long lastId) {

    List<PdwhPublication> pdwhList = pdwhPublicationDao.findPubByBatchSize(lastId, 3500);

    return pdwhList;

  }

  /*
   * 在index时区分唯一id
   */
  public Long generateIdForIndex(Long id, String type) {
    // pdwhpub前缀为310000
    if (INDEX_TYPE_SIMPLE_PDWH_PUB.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("310000" + idString);
    }
    // snspub前缀为410000
    else if (INDEX_TYPE_SIMPLE_SNS_PUB.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("410000" + idString);
    } else {
      return id;
    }
  }
}
