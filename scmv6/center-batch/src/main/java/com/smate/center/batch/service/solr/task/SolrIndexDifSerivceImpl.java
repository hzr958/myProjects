package com.smate.center.batch.service.solr.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubIndexUrlDao;
import com.smate.center.batch.dao.pdwh.pub.PubTfDao;
import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IsiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.pubmed.PubMedPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.wanfang.WfPublicationDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhFullTextFileDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPublicationDao;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.InsUnitDao;
import com.smate.center.batch.dao.sns.pub.Patent20161012FulltextDao;
import com.smate.center.batch.dao.sns.pub.PubSnsDetailDAO;
import com.smate.center.batch.dao.solr.PubInfoPdwhDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.PubTf;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubExtend;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExtend;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubExtend;
import com.smate.center.batch.model.pdwh.pub.ei.EiPubExtend;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExtend;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubExtend;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubExtend;
import com.smate.center.batch.model.pdwh.pub.wanfang.WfPubExtend;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.center.batch.model.sns.pub.Patent20161012Fulltext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.solr.PubInfoPdwh;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

@Service("solrIndexDifService")
@Transactional(rollbackFor = Exception.class)
public class SolrIndexDifSerivceImpl implements SolrIndexDifService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  @Autowired
  private PersonDao personDao;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private PubInfoPdwhDao pubInfoPdwhDao;
  @Autowired
  private PubMedPublicationDao pubMedPublicationDao;
  @Autowired
  private SpsPublicationDao spsPublicationDao;
  @Autowired
  private CnkiPublicationDao cnkiPublicationDao;
  @Autowired
  private EiPublicationDao eiPublicationDao;
  @Autowired
  private WfPublicationDao wfPublicationDao;
  @Autowired
  private CniprPublicationDao cniprPublicationDao;
  @Autowired
  private CnkiPatPublicationDao cnkiPatPublicationDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private PubTfDao pubTfDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private Patent20161012FulltextDao patent20161012FulltextDao;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private FileService fileService;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;

  @Value("${domainscm}")
  private String domainScm;
  private Gson gs = new Gson();
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  public static String TF_CALCULATION = "tf_calculation";
  public static String NSFC_TF = "nsfc_tf";
  public static String NSFC_COTF = "nsfc_cotf";
  private Integer batchSize = 2400;
  String pdwhShortUrlDomain = domainScm + "/" + ShortUrlConst.S_TYPE + "/";

  /**
   * 批量重构索引已经迁移到task，取值为pdwh_publication，此处不再使用
   * 
   * @param server
   * @throws Exception
   */
  @Deprecated
  /*
   * 为成果，专利，人员配置建立索引 ；可以清除缓存内容，方便重建索引（）
   */
  @Override
  public void runIndex() {
    SolrServer server = initializeSolrServer();

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_removePubCache") == 1) {
      cacheService.remove(INDEX_TYPE_PUB, "last_pub1_id");
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_removePatCache") == 1) {
      cacheService.remove(INDEX_TYPE_PAT, "last_patent_id");
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_removeUserCache") == 1) {
      cacheService.remove(INDEX_TYPE_PSN, "last_psn_id");
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_publication") == 1) {
      try {
        indexPublication(server);
      } catch (Exception e) {
        logger.debug("Publication索引创建出错", e);
      }
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_patent") == 1) {
      try {
        indexPatent(server);
      } catch (Exception e) {
        logger.debug("Patent索引创建出错", e);
      }
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_user") == 1) {
      try {
        indexUser(server);
      } catch (Exception e) {
        logger.debug("User索引创建出错", e);
      }
    }
  }

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  private SolrServer initializeSolrServerForTfCotf() {
    serverUrl = "http://192.168.15.192:28080/solr/collection_tf_cotf";
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  /**
   * 批量重构索引已经迁移到task，取值为pdwh_publication，此处不再使用
   * 
   * @param server
   * @throws Exception
   */
  @Deprecated
  public void indexPublication(SolrServer server) throws Exception {
    Long lastId = (Long) cacheService.get(INDEX_TYPE_PUB, "last_pub1_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<PubInfoPdwh> pubList = getPublist(lastId);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========publicationList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubAllId();
    this.cacheService.put(INDEX_TYPE_PUB, 60 * 60 * 24, "last_pub1_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (PubInfoPdwh one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_PUB);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getPubAllId(), INDEX_TYPE_PUB));
      doc.setField("one", one.getPubId());
      doc.setField("pubDbId", one.getDbId());
      doc.setField("zhTitle", one.getZhTitle());
      doc.setField("enTitle", one.getEnTitle());
      doc.setField("authors", one.getAuthorNames());
      doc.setField("doi", one.getDoi());
      doc.setField("zhAbstract", one.getZhAbstract());
      doc.setField("enAbstract", one.getEnAbstract());
      doc.setField("zhKeywords", one.getZhKeywords());
      doc.setField("enKeywords", one.getEnKeywords());
      doc.setField("pubYear", one.getPubYear());
      doc.setField("pubTypeId", one.getPubType());
      doc.setField("enPubBrief", one.getEnBrief());
      doc.setField("zhPubBrief", one.getZhBrief());
      doc.setField("language", one.getLanguage());
      String pubShortUrl = pdwhPubIndexUrlDao.getStringUrlByPubId(one.getPubAllId());
      if (StringUtils.isNotBlank(pubShortUrl)) {
        doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
      }
      // 用于有全文的排序，1有；0无
      doc.setField("fullText", this.hasFullText(one.getPubAllId()));
      doc.setField("journalName", "");
      docList.add(doc);
      lastId = one.getPubAllId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger
            .info("Publication索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("Publication索引创建出错，end = " + new Date());
      }
    }
  }

  /**
   * 基准库成果改造创建成果索引(单个成果)
   * 
   * @param pdwhPub
   */
  @Override
  public void indexPublication(PdwhPublication pdwhPub) throws Exception {
    // PdwhPublication pdwhPub = this.getPubInfo(pdwhPubId);
    SolrServer server = initializeSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    // 必须字段设定schema.xml配置
    doc.setField("businessType", INDEX_TYPE_PUB);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", generateIdForIndex(pdwhPub.getPubId(), INDEX_TYPE_PUB));
    doc.setField("pubId", pdwhPub.getPubId());
    doc.setField("pubDbId", pdwhPub.getDbId());
    doc.setField("zhAbstract", pdwhPub.getZhAbstract());
    doc.setField("enAbstract", pdwhPub.getEnAbstract());
    doc.setField("zhTitle", pdwhPub.getZhTitle());
    doc.setField("enTitle", pdwhPub.getEnTitle());
    doc.setField("authors", pdwhPub.getAuthorName());
    doc.setField("cleanAuthors", XmlUtil.cleanXMLAuthorChars(pdwhPub.getAuthorName()));
    doc.setField("doi", pdwhPub.getDoi());
    doc.setField("zhKeywords", pdwhPub.getZhKeywords());
    doc.setField("enKeywords", pdwhPub.getEnKeywords());
    doc.setField("pubYear", pdwhPub.getPubYear());
    // 1奖励,2书/著作,10书籍章节，在此处都归类为7其他
    if (pdwhPub.getPubType() == 1 || pdwhPub.getPubType() == 2 || pdwhPub.getPubType() == 10) {
      doc.setField("pubTypeId", 7);
    } else {
      doc.setField("pubTypeId", pdwhPub.getPubType());
    }
    doc.setField("enPubBrief", pdwhPub.getEnBriefDesc());
    doc.setField("zhPubBrief", pdwhPub.getZhBriefDesc());
    String pubShortUrl = pdwhPubIndexUrlDao.getStringUrlByPubId(pdwhPub.getPubId());
    if (StringUtils.isNotBlank(pubShortUrl)) {
      doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
    }
    doc.setField("fundInfo", pdwhPub.getFundInfo());
    // 用于有全文的排序，1有;0无
    doc.setField("fullText", this.hasFullText(pdwhPub.getPubId()));
    doc.setField("journalName", "");
    if (doc != null) {
      try {
        server.add(doc);
        server.commit();
        logger.info("pdwhPub索引创建完成 ");
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("pdwhPub索引创建出错");
      }
    }

  }

  /**
   * 基准库成果改造，专利索引
   * 
   * @throws Exception
   */
  @Override
  public void indexPatent(PdwhPublication pdwhPub) throws Exception {
    SolrServer server = initializeSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    // 必须字段设定schema.xml配置
    doc.setField("businessType", INDEX_TYPE_PAT);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    // doc.setField("env", "test");
    doc.setField("id", generateIdForIndex(pdwhPub.getPubId(), INDEX_TYPE_PAT));
    doc.setField("patId", pdwhPub.getPubId());
    doc.setField("patDbId", pdwhPub.getDbId());
    doc.setField("zhPatTitle", pdwhPub.getZhTitle());
    doc.setField("enPatTitle", pdwhPub.getEnTitle());
    doc.setField("patAuthors", pdwhPub.getAuthorName());
    doc.setField("cleanPatAuthors", XmlUtil.cleanXMLAuthorChars(pdwhPub.getAuthorName()));
    doc.setField("patYear", pdwhPub.getPubYear());
    doc.setField("patTypeId", pdwhPub.getPatentCategory());
    doc.setField("organization", pdwhPub.getOrganization());
    doc.setField("patentNo", pdwhPub.getPatentNo());
    doc.setField("enPatBrief", pdwhPub.getEnBriefDesc());
    doc.setField("zhPatBrief", pdwhPub.getZhBriefDesc());
    String pubShortUrl = pdwhPubIndexUrlDao.getStringUrlByPubId(pdwhPub.getPubId());
    if (StringUtils.isNotBlank(pubShortUrl)) {
      doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
    }
    doc.setField("fullText", this.hasFullText(pdwhPub.getPubId()));
    if (doc != null) {
      try {
        server.add(doc);
        server.commit();
        logger.info("pdwh专利索引创建完成 ");
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("pdwh专利索引创建出错");
      }
    }
  }

  /**
   * 批量重构索引已经迁移到task，取值为pdwh_publication，此处不再使用
   * 
   * @param server
   * @throws Exception
   */
  @Deprecated
  /*
   * 为专利作索引，与一般publication区分开
   */
  public void indexPatent(SolrServer server) throws Exception {
    Long lastId = (Long) cacheService.get(INDEX_TYPE_PAT, "last_patent_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<PubInfoPdwh> pubList = getPatentlist(lastId);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========patentList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubAllId();
    this.cacheService.put(INDEX_TYPE_PAT, 60 * 60 * 24, "last_patent_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();

    for (PubInfoPdwh one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_PAT);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getPubAllId(), INDEX_TYPE_PAT));
      doc.setField("patId", one.getPubId());
      doc.setField("patDbId", one.getDbId());
      doc.setField("zhPatTitle", one.getZhTitle());
      doc.setField("enPatTitle", one.getEnTitle());
      doc.setField("patAuthors", one.getAuthorNames());
      doc.setField("patYear", one.getPubYear());
      doc.setField("patTypeId", one.getPatentCategory());
      doc.setField("organization", one.getOrganization());
      doc.setField("patentNo", one.getPatentNo());
      doc.setField("enPatBrief", one.getEnBrief());
      doc.setField("zhPatBrief", one.getZhBrief());
      doc.setField("patLanguage", one.getLanguage());
      String pubShortUrl = pdwhPubIndexUrlDao.getStringUrlByPubId(one.getPubAllId());
      if (StringUtils.isNotBlank(pubShortUrl)) {
        doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
      }
      doc.setField("fullText", this.hasFullText(one.getPubAllId()));
      docList.add(doc);
      lastId = one.getPubAllId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("Patent索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("Patent索引创建出错，end = " + new Date());
      }
    }
  }

  /**
   * 基准库成果改造获取成果信息
   * 
   * @param pdwhPub
   * @return
   */
  public PdwhPublication getPubInfo(Long pdwhPubId) {
    // 需要获取更新后的xml以及publication信息，所以需要重新查询 SCM-11599
    PdwhPublication pdwhPub = this.pdwhPublicationDao.get(pdwhPubId);
    PdwhPubXml pubXml = this.pdwhPubXmlDao.get(pdwhPubId);
    if (pubXml != null && StringUtils.isNotEmpty(pubXml.getXml())) {
      try {
        PubXmlDocument xmlDoc = new PubXmlDocument(pubXml.getXml());
        pdwhPub.setZhAbstract(
            StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cabstract")));
        pdwhPub.setEnAbstract(
            StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "eabstract")));
        pdwhPub.setEnKeywords(
            StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ekeywords")));
        pdwhPub.setZhKeywords(
            StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ckeywords")));
        pdwhPub.setDoi(StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi")));
      } catch (Exception e) {
        logger.info("获取xml相关属性错误, pubAllId = " + pdwhPub.getPubId());
      }
    }

    Assert.notNull(pdwhPub);
    return pdwhPub;

  }

  public List<PubInfoPdwh> getPublist(Long lastId) {
    List<Publication> list = new ArrayList<Publication>();

    List<PubInfoPdwh> pdwhList = pubInfoPdwhDao.findPubByBatchSize(lastId, 3500);

    for (PubInfoPdwh pub : pdwhList) {
      if (pub == null || pub.getPubAllId() == null) {
        continue;
      }
      // 1奖励,2书/著作,10书籍章节，在此处都归类为7其他
      if (pub.getPubType() == 1 || pub.getPubType() == 2 || pub.getPubType() == 10) {
        pub.setPubType(7);
      }

      String pubXml = getPubXmlString(pub.getDbId(), pub.getPubId());

      if (StringUtils.isNotEmpty(pubXml)) {
        try {
          PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
          pub.setZhAbstract(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cabstract")));
          pub.setEnAbstract(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "eabstract")));
          pub.setEnKeywords(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ekeywords")));
          pub.setZhKeywords(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ckeywords")));
          pub.setDoi(StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi")));
        } catch (Exception e) {
          logger.info("获取xml相关属性错误, pubAllId = " + pub.getPubAllId());
        }
      }
    }

    return pdwhList;
  }

  public List<PubInfoPdwh> getPatentlist(Long lastId) {
    List<PubInfoPdwh> list = new ArrayList<PubInfoPdwh>();
    list = pubInfoPdwhDao.findPatByBatchSize(lastId, batchSize);

    for (PubInfoPdwh pub : list) {
      if (pub == null || pub.getPubAllId() == null) {
        continue;
      }

      String pubXml = getPubXmlString(pub.getDbId(), pub.getPubId());

      if (StringUtils.isNotEmpty(pubXml)) {
        try {
          PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
          pub.setOrganization(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "organization")));
          pub.setPatentNo(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no")));
          String patCat =
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_category"));
          // 专利查询分类：51 发明专利 52 实用新型 53外观设计7其他；
          pub.setPatentCategory(StringUtils.isEmpty(patCat) ? 7 : Integer.valueOf(patCat));
        } catch (Exception e) {
          logger.info("获取xml相关属性错误, pubAllId = " + pub.getPubAllId());
        }
      }
    }
    return list;
  }

  @Deprecated
  public void indexUser(SolrServer server) throws Exception {
    Long lastId = (Long) cacheService.get(INDEX_TYPE_PSN, "last_psn_id");
    if (lastId == null) {
      lastId = 0L;
    }
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    List<Person> psnList = personDao.findUserByBatchSize(lastId, batchSize);
    if (CollectionUtils.isEmpty(psnList)) {
      return;
    }
    Integer lastIndex = psnList.size() - 1;
    Long lastPsnId = psnList.get(lastIndex).getPersonId();
    this.cacheService.put(INDEX_TYPE_PSN, 60 * 60 * 24, "last_psn_id", lastPsnId);

    for (Person psn : psnList) {
      if (psn == null) {
        continue;
      }
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
      doc.setField("businessType", INDEX_TYPE_PSN);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      doc.setField("id", generateIdForIndex(psn.getPersonId(), INDEX_TYPE_PSN));
      doc.setField("psnId", psn.getPersonId());
      doc.setField("psnName", psn.getName());
      doc.setField("enPsnName", psn.getEname());
      doc.setField("title", psn.getPosition());

      // 获取个人成果中的关键词
      String kwsStr = this.getPsnKwsByPubKws(psn.getPersonId());
      doc.setField("psnKeywords", kwsStr);

      // 人员是否在隐私列表中
      if (this.psnPrivateDao.existsPsnPrivate(psn.getPersonId())) {
        doc.setField("isPrivate", 1);
      } else {
        doc.setField("isPrivate", 0);
      }

      if (psn.getInsId() != null) {

        InsPortal ins = insPortalDao.get(psn.getInsId());
        if (ins != null) {
          doc.setField("zhInsName", ins.getZhTitle());
          doc.setField("enInsName", ins.getEnTitle());
        }
        try {
          RolPsnIns psnIns = rolPsnInsDao.findPsnIns(psn.getPersonId(), psn.getInsId());
          if (psnIns != null && psnIns.getUnitId() != null) {
            InsUnit unit = insUnitDao.get(psnIns.getUnitId());
            if (unit != null) {
              doc.setField("zhUnit", unit.getZhName());
              doc.setField("enUnit", unit.getEnName());
            }
          }
        } catch (DaoException e) {
          logger.info("获取psnIns相关属性错误, psnId= " + psn.getPersonId(), e);
        }
      }
      docList.add(doc);
      lastId = psn.getPersonId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("User索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("User索引创建出错，end = " + new Date());
      }
    }
  }

  /**
   * 获取个人所属的所有成果关键词
   */
  public String getPsnKwsByPubKws(Long psnId) {
    List<Long> pubIdList = psnPubDAO.getPubIdsByPsnId(psnId);
    StringBuilder keywords = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        PubSnsDetailDOM detail = pubSnsDetailDAO.findByPubId(pubId);
        if (detail != null) {
          keywords.append(detail.getKeywords());
        }
      }
    }
    return keywords.toString();
  }

  public Integer hasFullText(Long pubAllId) {
    Long count = this.pdwhFullTextFileDao.getCountByPubAllId(pubAllId);
    if (count == null)
      return 0;

    if (count <= 0) {
      return 0;
    } else {
      return 1;
    }
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

  public String getPubXmlString(Integer dbId, Long pubId) {
    Integer isi_int = 2;
    String xmlString = null;
    // isi文献
    if (PubXmlConstants.SOURCE_DBID_ISTP_INT.equals(dbId) || PubXmlConstants.SOURCE_DBID_SCI_INT.equals(dbId)
        || PubXmlConstants.SOURCE_DBID_SSCI_INT.equals(dbId) || isi_int.equals(dbId)) {
      IsiPubExtend pubXml = isiPublicationDao.getIsiPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // scopus文献
    } else if (PubXmlConstants.SOURCE_DBID_SCOPUS_INT.equals(dbId)) {
      SpsPubExtend pubXml = spsPublicationDao.getSpsPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // cnki文献
    } else if (PubXmlConstants.SOURCE_DBID_CNKI_INT.equals(dbId)) {
      CnkiPubExtend pubXml = cnkiPublicationDao.getCnkiPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // ei
    } else if (PubXmlConstants.SOURCE_DBID_EI_INT.equals(dbId)) {
      EiPubExtend pubXml = eiPublicationDao.getEiPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // wanfang
    } else if (PubXmlConstants.SOURCE_WANG_FANG_INT.equals(dbId)) {
      WfPubExtend pubXml = wfPublicationDao.getWfPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // CNIPRDb，已经停止了cnipr的导入，2016-02-29和lzh确认
    } else if (PubXmlConstants.SOURCE_CNIPR_INT.equals(dbId)) {
      CniprPubExtend pubXml = cniprPublicationDao.getCniprPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // cnkipat
    } else if (PubXmlConstants.SOURCE_CNKIPAT_INT.equals(dbId)) {
      CnkiPatPubExtend pubXml = cnkiPatPublicationDao.getCnkiPatPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
      // pubmed
    } else if (PubXmlConstants.SOURCE_PUBMED_INT.equals(dbId)) {
      PubMedPubExtend pubXml = pubMedPublicationDao.getPubMedPubExtend(pubId);
      if (pubXml != null) {
        xmlString = pubXml.getXmlData();
      }
    }
    return xmlString;
  }

  /*
   * 以下测试使用
   */
  public static void main(String[] args) {
    String searchString = "孙业润";
    Integer pubYear = 2010;
    Integer pubType = 5;
    StringBuilder sbQueryString = new StringBuilder();
    if (StringUtils.isNotEmpty(searchString)) {
      searchString = searchString.toLowerCase().trim();
      sbQueryString.append("(zhTitle:").append(searchString);
      sbQueryString.append(" OR enTitle:").append(searchString);
      sbQueryString.append(" OR authors:").append(searchString);
      sbQueryString.append(" OR doi:").append(searchString);
      sbQueryString.append(" OR zhAbstract:").append(searchString);
      sbQueryString.append(" OR enAbstract:").append(searchString);
      sbQueryString.append(" OR zhKeywords:").append(searchString);
      sbQueryString.append(" OR enKeywords:").append(searchString);
      sbQueryString.append(" OR journalName:").append(searchString);
      sbQueryString.append(")");
      if (pubType != null) {
        sbQueryString.append(" AND pubTypeId:").append(pubType);
      }
      if (pubYear != null) {
        sbQueryString.append(" AND pubYear:").append(pubYear);
      }
    } else {
      if (pubType != null) {
        sbQueryString.append("pubTypeId:").append(pubType);
        if (pubYear != null) {
          sbQueryString.append(" AND pubYear:").append(pubYear);
        }
      } else {
        sbQueryString.append("pubYear:").append(pubYear);
      }
    }

    String queryString = sbQueryString.toString();

    System.out.println(queryString);
  }

  @Override
  @Deprecated
  public void addPsnIndex(Long psnId) {
    SolrServer solr = this.initializeSolrServer();

    Person psn = personDao.get(psnId);

    if (psn == null) {
      logger.info("人员索引错误，信息为空，psnId = " + psnId);
      return;
    }
    SolrInputDocument doc = new SolrInputDocument();
    // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
    doc.setField("businessType", INDEX_TYPE_PSN);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", generateIdForIndex(psn.getPersonId(), INDEX_TYPE_PSN));
    doc.setField("psnId", psn.getPersonId());
    doc.setField("psnName", psn.getName());
    doc.setField("enPsnName", psn.getEname());
    doc.setField("title", psn.getPosition());

    // 获取个人成果中的关键词
    String kwsStr = this.getPsnKwsByPubKws(psn.getPersonId());
    doc.setField("psnKeywords", kwsStr);

    // 人员是否在隐私列表中
    if (this.psnPrivateDao.existsPsnPrivate(psnId)) {
      doc.setField("isPrivate", 1);
    } else {
      doc.setField("isPrivate", 0);
    }

    if (psn.getInsId() != null) {

      InsPortal ins = insPortalDao.get(psn.getInsId());
      if (ins != null) {
        doc.setField("zhInsName", ins.getZhTitle());
        doc.setField("enInsName", ins.getEnTitle());
      }
      try {
        RolPsnIns psnIns = rolPsnInsDao.findPsnIns(psn.getPersonId(), psn.getInsId());
        if (psnIns != null && psnIns.getUnitId() != null) {
          InsUnit unit = insUnitDao.get(psnIns.getUnitId());
          if (unit != null) {
            doc.setField("zhUnit", unit.getZhName());
            doc.setField("enUnit", unit.getEnName());
          }
        }
      } catch (DaoException e) {
        logger.info("获取psnIns相关属性错误, psnId= " + psn.getPersonId(), e);
      }
    }

    try {
      Date start = new Date();
      solr.add(doc);
      solr.commit();
      Date end = new Date();
      logger.info("Person索引创建完成，psnId = " + psnId + ", end Time = " + new Date() + " , 总共耗费时间(s)："
          + (end.getTime() - start.getTime()) / 1000);
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
      logger.info("Person索引创建出错，psnId=" + psnId + " end = " + new Date());
    }
  }

  @Override
  public void calculateTF() {
    if (taskMarkerService.getApplicationQuartzSettingValue("pubTFCalculateTask_removePubAllId") == 1) {
      cacheService.remove(TF_CALCULATION, "last_pub_all_id");
    }
    int size = 1000;
    Long lastId = (Long) cacheService.get(TF_CALCULATION, "last_pub_all_id");

    if (lastId == null) {
      lastId = pubInfoPdwhDao.getMinPubAllId();
    }

    List<Long> pubIdList = pubInfoPdwhDao.findPubAllIdBySize(lastId, size);
    if (CollectionUtils.isEmpty(pubIdList)) {
      return;
    }

    lastId = pubIdList.get(pubIdList.size() - 1);
    cacheService.put(TF_CALCULATION, 60 * 60 * 24, "last_pub_all_id", lastId);
    for (Long pubAllId : pubIdList) {
      PubTf pubTf = this.getPubTF(pubAllId);
      if (pubTf == null) {
        continue;
      }
      this.savePubTf(pubTf);
    }

  }

  public PubTf getPubTF(Long pubAllId) {
    SolrServer server = this.initializeSolrServer();
    String queryId = "100000" + pubAllId;
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowseTVCtest");
    query.setQuery("id:" + queryId);
    query.addFilterQuery("env:" + runEnv);
    try {
      QueryResponse qrs = server.query(query);
      NamedList<Object> list = (NamedList<Object>) qrs.getResponse().get("termVectors");
      TermsResponse termsResponse = qrs.getTermsResponse();
      /*
       * if(termsResponse == null){ return null; }
       */
      Map<String, List<Term>> rsMap = new HashMap<String, List<Term>>();
      Map<String, Long> pubTFMap = new HashMap<String, Long>();
      // 此处由于是按唯一的id查询的，所以rsMap中只会包含{pubAllId,
      // List<term>},不会包含多个pubAllId，但是此处还是按map遍历来写~
      List tfListEn = new ArrayList<String>();
      List tfListZh = new ArrayList<String>();
      NamedList<Object> obj = (NamedList<Object>) list.get(queryId);

      if (obj == null || obj.size() == 0) {
        return null;
      }

      NamedList<Object> enInfoCombine = (NamedList<Object>) obj.get("enInfoCombine");
      if (enInfoCombine != null && enInfoCombine.size() != 0) {
        Iterator<Entry<String, Object>> enEntry = enInfoCombine.iterator();
        while (enEntry.hasNext()) {
          Entry<String, Object> en = enEntry.next();
          String keyName = en.getKey();
          NamedList enVList = (NamedList) en.getValue();
          Integer tf = (Integer) enVList.get("tf");
          if (tf > 0 && StringUtils.isNotEmpty(keyName) && keyName.length() > 1) {
            tfListEn.add(keyName + ":" + tf);
          }
        }
      }

      NamedList<Object> zhInfoCombine = (NamedList<Object>) obj.get("zhInfoCombine");
      if (zhInfoCombine != null && zhInfoCombine.size() != 0) {
        Iterator<Entry<String, Object>> zhEntry = zhInfoCombine.iterator();
        while (zhEntry.hasNext()) {
          Entry<String, Object> zh = zhEntry.next();
          String keyName = zh.getKey();
          NamedList zhVList = (NamedList) zh.getValue();
          Integer tf = (Integer) zhVList.get("tf");
          if (tf > 0 && StringUtils.isNotEmpty(keyName) && keyName.length() > 1) {
            tfListZh.add(keyName + ":" + tf);
          }
        }
      }

      String resultStrEn = gs.toJson(tfListEn);
      String resultStrZh = gs.toJson(tfListZh);
      PubTf pubTf = new PubTf();
      pubTf.setPubId(pubAllId);
      pubTf.setTfDataEn(resultStrEn);
      pubTf.setTfDataZh(resultStrZh);

      /*
       * Iterator<Entry<String, Object>> itr = ((NamedList)
       * qrs.getResponse().get("termVectors")).iterator(); while(itr.hasNext()){ Entry<String, Object>
       * entry = itr.next(); for(Iterator<Entry<String, Object>> subItr =
       * ((NamedList)entry.getValue()).iterator(); subItr.hasNext(); ){ Entry<String, Object> entryItem =
       * subItr.next(); if("enInfoCombine".equalsIgnoreCase(entryItem.getKey())){
       * 
       * } } }
       */

      return pubTf;
    } catch (Exception e) {
      logger.info("Publication获取词频出错，pubAllId=" + pubAllId + " time = " + new Date(), e);
      return null;
    }

  }

  public void savePubTf(PubTf pubTf) {
    this.pubTfDao.save(pubTf);
  }

  @Override
  public void deletePrivateUser() {

    Long lastPsnId = (Long) this.cacheService.get(INDEX_TYPE_PUB, "delete_private_psnid");

    if (lastPsnId == null) {
      lastPsnId = 0L;
    }

    List<Long> privateUserList = this.psnPrivateDao.getPsnPrivateList(lastPsnId, batchSize);

    if (privateUserList == null || privateUserList.size() == 0) {
      logger.debug("从psn_private表获取的隐私名单为空！");
      return;
    }

    lastPsnId = privateUserList.get(privateUserList.size() - 1);
    this.cacheService.put(INDEX_TYPE_PSN, 60 * 60 * 24, "delete_private_psnid", lastPsnId);

    List<String> userIds = new ArrayList<String>();
    for (Long psnId : privateUserList) {
      Long psnIdOfSolr = this.generateIdForIndex(psnId, INDEX_TYPE_PSN);
      userIds.add(String.valueOf(psnIdOfSolr));
    }
    SolrServer server = this.initializeSolrServer();
    try {
      server.deleteById(userIds);
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
      logger.info("排除隐私用户索引出错，end = " + new Date());
    }
  }

  @Override
  public void getFulltextPath() {
    List<Patent20161012Fulltext> idList = this.patent20161012FulltextDao.getAll();
    for (Patent20161012Fulltext pub : idList) {
      Long fileId = pub.getFullTextFileId();
      try {
        ArchiveFile archiveFile = archiveFilesService.getArchiveFile(fileId);
        if (archiveFile != null) {
          String fileName = archiveFile.getFileName();
          String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
          String filePath = fileService.getFilePath(archiveFile.getFilePath());
          // 生产机文件root目录
          String realPath = "/mnt/scmv3file/scholarfile";
          String srcFilePath = realPath + "/" + ServiceConstants.DIR_UPFILE + filePath;

          pub.setFullTextUrl(srcFilePath);
          this.patent20161012FulltextDao.save(pub);
        }

      } catch (Exception e) {
        e.printStackTrace();
        logger.info("patent_tmp临时文件，获取全文错误，end = " + new Date(), e);
      }
    }
  }

  @Override
  public void saveKwStrForTf(SolrServer server, String kws, String disCode, String id) throws Exception {
    // PdwhPublication pdwhPub = this.getPubInfo(pdwhPubId);
    SolrInputDocument doc = new SolrInputDocument();
    Calendar calendar = Calendar.getInstance();
    String timestamp = String.valueOf(calendar.getTimeInMillis());
    // 必须字段设定schema.xml配置
    doc.setField("businessType", NSFC_TF);
    runEnv = "uat";
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", timestamp + NSFC_TF + id);
    doc.setField("pid", id);
    doc.setField("kws", kws); // 不需要准确查询，所以不需要计算hashvalue。比如包含数据的，查出数据挖掘，也可以。
    if (StringUtils.isNotEmpty(disCode)) {
      doc.setField("nsfcDisCode", disCode);
      Integer length = disCode.length();
      switch (length) {
        case 3:
          doc.setField("nsfcDisCode_1", disCode);
          break;
        case 5:
          doc.setField("nsfcDisCode_1", disCode.substring(0, 3));
          doc.setField("nsfcDisCode_2", disCode);
          break;
        case 7:
          doc.setField("nsfcDisCode_1", disCode.substring(0, 3));
          doc.setField("nsfcDisCode_2", disCode.substring(0, 5));
          doc.setField("nsfcDisCode_3", disCode);
          break;
      }
    }
    if (doc != null) {
      try {
        server.add(doc);
        server.commit();
        logger.info("pdwhPub索引创建完成 ");
      } catch (SolrServerException | IOException e) {
        logger.error("pdwhPub索引创建出错", e);
      }
    }
  }

  @Override
  public void saveKwStrForCoTf(SolrServer server, List<SolrInputDocument> docList) throws Exception {
    if (docList != null && docList.size() != 0) {
      try {
        server.add(docList);
        server.commit();
        logger.info("pdwhPub索引创建完成 ");
      } catch (SolrServerException | IOException e) {
        logger.error("pdwhPub索引创建出错", e);
      }
    }
  }

  @Override
  public void saveKwStrForCoTf(SolrServer server, String firstKw, String secondKw, String disCode, String id, int k)
      throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    Calendar calendar = Calendar.getInstance();
    String timestamp = String.valueOf(calendar.getTimeInMillis());
    // 必须字段设定schema.xml配置
    doc.setField("businessType", NSFC_COTF);
    runEnv = "uat";
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", k + NSFC_COTF + id);
    doc.setField("pid", id);
    doc.setField("firstKw", firstKw); // 不需要准确查询，所以不需要计算hashvalue。比如包含数据的，查出数据挖掘，也可以。
    doc.setField("firstKwHash", HashUtils.getStrHashCode(firstKw));
    doc.setField("secondKw", secondKw);
    doc.setField("secondKwHash", HashUtils.getStrHashCode(secondKw));
    if (StringUtils.isNotEmpty(disCode)) {
      doc.setField("nsfcDisCode", disCode);
      Integer length = disCode.length();
      switch (length) {
        case 3:
          doc.setField("nsfcDisCode_1", disCode);
          break;
        case 5:
          doc.setField("nsfcDisCode_1", disCode.substring(0, 3));
          doc.setField("nsfcDisCode_2", disCode);
          break;
        case 7:
          doc.setField("nsfcDisCode_1", disCode.substring(0, 3));
          doc.setField("nsfcDisCode_2", disCode.substring(0, 5));
          doc.setField("nsfcDisCode_3", disCode);
          break;
        default:
          break;
      }
    }
    if (doc != null) {
      try {
        server.add(doc);
        server.commit();
        logger.info("pdwhPub索引创建完成 ");
      } catch (SolrServerException | IOException e) {
        logger.error("pdwhPub索引创建出错", e);
      }
    }
  }

  @Override
  public SolrInputDocument getKwStrForCoTf(String firstKw, String secondKw, String disCode, String id)
      throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    Calendar calendar = Calendar.getInstance();
    String timestamp = String.valueOf(calendar.getTimeInMillis());
    // 必须字段设定schema.xml配置
    doc.setField("businessType", NSFC_COTF);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", timestamp + NSFC_COTF + id);
    doc.setField("pid", id);
    doc.setField("firstKw", firstKw); // 不需要准确查询，所以不需要计算hashvalue。比如包含数据的，查出数据挖掘，也可以。
    doc.setField("firstKwHash", HashUtils.getStrHashCode(firstKw));
    doc.setField("secondKw", secondKw);
    doc.setField("secondKwHash", HashUtils.getStrHashCode(secondKw));
    doc.setField("nsfcDisCode", disCode);
    return doc;
  }
}
