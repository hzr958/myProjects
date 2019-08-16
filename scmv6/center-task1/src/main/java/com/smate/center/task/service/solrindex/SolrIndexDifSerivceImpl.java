package com.smate.center.task.service.solrindex;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
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
import com.smate.center.task.dao.pdwh.quartz.BaseJnlCategoryRankDao;
import com.smate.center.task.dao.pdwh.quartz.BaseJournalCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.CniprPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.CnkiPatPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.CnkiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.EiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.IsiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.PubMedPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.PubTfDao;
import com.smate.center.task.dao.pdwh.quartz.SpsPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.WfPublicationDao;
import com.smate.center.task.dao.sns.quartz.Patent20161012FulltextDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.pub.cnipr.CniprPubExtend;
import com.smate.center.task.model.pdwh.pub.cnkipat.CnkiPatPubExtend;
import com.smate.center.task.model.pdwh.pub.sps.SpsPubExtend;
import com.smate.center.task.model.pdwh.pub.wanfang.WfPubExtend;
import com.smate.center.task.model.pdwh.quartz.BaseJnlCategoryRankEnum;
import com.smate.center.task.model.pdwh.quartz.CnkiPubExtend;
import com.smate.center.task.model.pdwh.quartz.EiPubExtend;
import com.smate.center.task.model.pdwh.quartz.IsiPubExtend;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.pdwh.quartz.PubMedPubExtend;
import com.smate.center.task.model.pdwh.quartz.PubTf;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.model.sns.quartz.Patent20161012Fulltext;
import com.smate.center.task.service.sns.quartz.ArchiveFilesService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.dao.rol.psn.RolPsnInsDao;
import com.smate.center.task.single.dao.rol.pub.InsUnitDao;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.model.rol.psn.RolPsnIns;
import com.smate.center.task.single.model.rol.pub.InsUnit;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.utils.cache.CacheService;
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
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;

  @Autowired
  private CnkiPatPublicationDao cnkiPatPublicationDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
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
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private FileService fileService;
  @Autowired
  private BaseJnlCategoryRankDao baseJnlCategoryRankDao;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Value("${domainscm}")
  private String domainScm;
  private Gson gs = new Gson();
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  public static String TF_CALCULATION = "tf_calculation";
  public static String RESULT_FACET = "facet"; // 查询分类统计
  public static String RESULT_NUMFOUND = "numFound"; // 查询结果总数
  public static String RESULT_ITEMS = "items"; // 查询结果
  public static String RESULT_HIGHLIGHT = "highlight"; // 高亮结果显示
  private Integer batchSize = 2400;

  /*
   * 为成果，专利，人员配置建立索引 ；可以清除缓存内容，方便重建索引
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

  public void indexPublication(SolrServer server) throws Exception {
    Long lastId = (Long) cacheService.get(INDEX_TYPE_PUB, "last_pub1_id");
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
    this.cacheService.put(INDEX_TYPE_PUB, 60 * 60 * 24, "last_pub1_id", lastPubId);
    String pdwhShortUrlDomain = domainScm + "/" + ShortUrlConst.S_TYPE + "/";
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (PdwhPublication one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_PUB);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getPubId(), INDEX_TYPE_PUB));
      doc.setField("pubId", one.getPubId());
      doc.setField("pubDbId", one.getDbId());
      doc.setField("zhTitle", one.getZhTitle());
      doc.setField("enTitle", one.getEnTitle());
      doc.setField("authors", one.getAuthorName());
      doc.setField("cleanAuthors", XmlUtil.cleanXMLAuthorChars(one.getAuthorName()));
      doc.setField("doi", one.getDoi());
      doc.setField("zhAbstract", one.getZhAbstract());
      doc.setField("enAbstract", one.getEnAbstract());
      doc.setField("zhKeywords", one.getZhKeywords());
      doc.setField("enKeywords", one.getEnKeywords());
      doc.setField("pubYear", one.getPubYear());
      doc.setField("pubTypeId", one.getPubType());
      doc.setField("enPubBrief", one.getEnBriefDesc());
      doc.setField("zhPubBrief", one.getZhBriefDesc());
      String pubShortUrl = this.pdwhPubIndexUrlDao.getStringUrlByPubId(one.getPubId());
      if (StringUtils.isNotBlank(pubShortUrl)) {
        doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
      }
      doc.setField("fundInfo", one.getFundInfo());
      // 用于有全文的排序，1有；0无
      doc.setField("fullText", this.hasFullText(one.getPubId()));
      doc.setField("journalGrade", this.getPubQualityByJnlRank(one.getJnlId()));
      doc.setField("journalName", "");
      docList.add(doc);
      lastId = one.getPubId();
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

  /*
   * 为专利作索引，与一般publication区分开
   */
  public void indexPatent(SolrServer server) throws Exception {
    Long lastId = (Long) cacheService.get(INDEX_TYPE_PAT, "last_patent_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<PdwhPublication> pubList = getPatentlist(lastId);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========patentList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubId();
    this.cacheService.put(INDEX_TYPE_PAT, 60 * 60 * 24, "last_patent_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    String pdwhShortUrlDomain = domainScm + "/" + ShortUrlConst.S_TYPE + "/";
    for (PdwhPublication one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_PUB);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getPubId(), INDEX_TYPE_PAT));
      doc.setField("patId", one.getPubId());
      doc.setField("patDbId", one.getDbId());
      doc.setField("zhPatTitle", one.getZhTitle());
      doc.setField("enPatTitle", one.getEnTitle());
      doc.setField("patAuthors", one.getAuthorName());
      doc.setField("cleanPatAuthors", XmlUtil.cleanXMLAuthorChars(one.getAuthorName()));
      doc.setField("patYear", one.getPubYear());
      doc.setField("patTypeId", one.getPatentCategory());
      doc.setField("organization", one.getOrganization());
      doc.setField("patentNo", one.getPatentNo());
      doc.setField("enPatBrief", one.getEnBriefDesc());
      doc.setField("zhPatBrief", one.getZhBriefDesc());
      String pubShortUrl = this.pdwhPubIndexUrlDao.getStringUrlByPubId(one.getPubId());
      if (StringUtils.isNotBlank(pubShortUrl)) {
        doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
      }
      doc.setField("fullText", this.hasFullText(one.getPubId()));
      docList.add(doc);
      lastId = one.getPubId();
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

  public List<PdwhPublication> getPublist(Long lastId) {

    List<PdwhPublication> pdwhList = pdwhPublicationDao.findPubByBatchSize(lastId, 3500);

    for (PdwhPublication pub : pdwhList) {
      if (pub == null || pub.getPubId() == null) {
        continue;
      }
      // 1奖励,2书/著作,10书籍章节，在此处都归类为7其他
      if (pub.getPubType() == 1 || pub.getPubType() == 2 || pub.getPubType() == 10) {
        pub.setPubType(7);
      }

      String pubXml = pdwhPubXmlDao.getXmlStringByPubId(pub.getPubId());

      if (StringUtils.isNotEmpty(pubXml)) {
        try {
          PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
          pub.setZhAbstract(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cabstract")));
          pub.setEnAbstract(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "eabstract")));
          pub.setDoi(StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi")));
          pub.setFundInfo(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo")));
        } catch (Exception e) {
          logger.info("获取xml相关属性错误, pubId = " + pub.getPubId());
        }
      }
    }

    return pdwhList;
  }

  public List<PdwhPublication> getPatentlist(Long lastId) {
    List<PdwhPublication> list = new ArrayList<PdwhPublication>();
    list = pdwhPublicationDao.findPatByBatchSize(lastId, batchSize);

    for (PdwhPublication pub : list) {
      if (pub == null || pub.getPubId() == null) {
        continue;
      }
      String pubXml = pdwhPubXmlDao.getXmlStringByPubId(pub.getPubId());
      // String pubXml = getPubXmlString(pub.getDbId(), pub.getPubId());

      if (StringUtils.isNotEmpty(pubXml)) {
        try {
          PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
          pub.setOrganization(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "organization")));
          String patCat =
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_category"));
          // 专利查询分类：51 发明专利 52 实用新型 53外观设计7其他；
          pub.setPatentCategory(StringUtils.isEmpty(patCat) ? 7 : Integer.valueOf(patCat));
        } catch (Exception e) {
          logger.info("获取xml相关属性错误, pubAllId = " + pub.getPubId());
        }
      }
    }
    return list;
  }

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

  @Override
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
      lastId = pdwhPublicationDao.getMinPubAllId();
    }

    List<Long> pubIdList = pdwhPublicationDao.findPubAllIdBySize(lastId, size);
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
        ArchiveFile archiveFile = archiveFilesService.getArchiveFiles(fileId);
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
  public Map<String, Object> getRcmdPatents(Integer pageCount, Integer size, String queryKwString, String categoryStr)
      throws SolrServerException {

    Assert.notNull(queryKwString, "queryKwString不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/patentRcmd");
    query.setQuery(queryKwString);
    List<String> fqList = new ArrayList<String>();

    if (StringUtils.isNotEmpty(categoryStr)) {
      fqList.add("patCategoryRcmd:" + categoryStr);
    }
    fqList.add("env:" + runEnv);

    String[] str = fqList.toArray(new String[fqList.size()]);

    query.addFilterQuery(str);
    query.setRows(size);
    query.setStart(start);
    // query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    query.setSort("patYearRcmd", ORDER.desc);

    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();

    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;

  }

  @Override
  public Map<String, Object> getRequestRcmdFromPatent(Integer pageCount, Integer size, String queryKwString,
      String categoryStr) throws SolrServerException {

    Assert.notNull(queryKwString, "queryKwString不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/patentRequestRcmd");
    query.setQuery(queryKwString);
    List<String> fqList = new ArrayList<String>();

    if (StringUtils.isNotEmpty(categoryStr)) {
      fqList.add("patCategoryRcmd:" + categoryStr);
    }
    fqList.add("env:" + runEnv);

    String[] str = fqList.toArray(new String[fqList.size()]);

    query.addFilterQuery(str);
    query.setRows(size);
    query.setStart(start);
    // query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);

    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();

    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;

  }

  /*
   * 基金推荐solr
   */
  @Override
  public Map<String, Object> getFundRecommend(Integer pageCount, Integer size, String regionEn, String regionZh,
      String categoryEn, String categoryZh, Integer qualification, Integer timeGap, Integer fundInsTypeEnterprise,
      Integer fundInsTypeResearchIns, Date updateDate) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/fundRecommend");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    if (StringUtils.isNotEmpty(categoryEn)) {
      fqList.add("fundCategoryStrEn:" + this.constructFqStringForFund(categoryEn)
          + " OR (*:* NOT fundCategoryStrEn:[* TO *])");
    }
    if (StringUtils.isNotEmpty(categoryZh)) {
      fqList.add("fundCategoryStrZh:" + this.constructFqStringForFund(categoryZh)
          + " OR (*:* NOT fundCategoryStrZh:[* TO *])");
    }
    if (StringUtils.isNotEmpty(regionEn)) {// 国家级基金信息地区要求为空
      fqList
          .add("fundRegionStrEn:" + this.constructFqStringForFund(regionEn) + " OR (*:* NOT fundRegionStrEn:[* TO *])");
    }
    if (StringUtils.isNotEmpty(regionZh)) {
      fqList
          .add("fundRegionStrZh:" + this.constructFqStringForFund(regionZh) + " OR (*:* NOT fundRegionStrZh:[* TO *])");
    }
    if (qualification != null && qualification != -1) { // const_position中对应grades；
      fqList.add("fundDegreeAndTitleRequire1:[" + qualification + " TO *] OR (fundDegreeAndTitleRequire1:\"0\")");
    }
    if (fundInsTypeEnterprise != null) {
      fqList.add("fundInsTypeEnterprise:" + fundInsTypeEnterprise);
    }
    if (fundInsTypeResearchIns != null) {
      fqList.add("fundInsTypeResearchIns:" + fundInsTypeResearchIns);
    }
    if (updateDate != null) {
      // 如果和当前时间相同，则使用NOW
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
      if (sdf.format(new Date()).equals(sdf.format(updateDate))) {
        fqList.add("fundUpdateDate:[NOW/DAY-1DAY TO NOW/DAY+1DAY]");// 前一天到现在的
      } else {
        // 如果不同，则处理为solr要求时间，作为查询条件
        String dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(updateDate);
        fqList.add("fundUpdateDate:[" + dateStr + "/DAY TO " + dateStr + "/DAY+1DAY]");

      }
    }

    if (timeGap != null) {
      switch (timeGap) {
        case 1: // 一周以内
          fqList.add("fundEndDate:[NOW/DAY TO NOW/DAY+7DAY]");
          break;
        case 2: // 1个月
          fqList.add("fundEndDate:[NOW/DAY TO NOW+1MONTH]");
          break;
        case 3: // 3个月
          fqList.add("fundEndDate:[NOW/DAY TO NOW+3MONTH]");
          break;
        default:
          break;
      }
    } else {
      fqList.add("fundEndDate:[NOW/DAY-1DAY TO *]");
    }
    String[] fq = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(fq);
    query.setQuery("*");
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setStart(start);
    query.setRows(size);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  private Integer getPubQualityByJnlRank(Long jnlId) {
    Integer highestRank = 9;
    List<Long> ids = baseJournalCategoryDao.getCategoryIdsByJnlId(jnlId);
    if (CollectionUtils.isNotEmpty(ids)) {
      List<String> rankStrings = baseJnlCategoryRankDao.getRanksByCategoryIdList(ids);
      String highestRankString = rankStrings.get(0);
      highestRank = BaseJnlCategoryRankEnum.getRankValue(highestRankString);
    }

    return highestRank;
  }

  // 为基金拼接fq中字段，str用逗号隔开
  private String constructFqStringForFund(String queryString) {
    String[] strs = StringUtils.split(queryString, ",");
    if (strs == null) {
      return null;
    }

    Integer length = strs.length;
    if (length == 1) {
      return "(\"" + strs[0] + "\")";
    } else if (length > 1) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      for (Integer i = 0; i < length; i++) {
        sb.append("\"" + StringUtils.trim(strs[i]) + "\"");
        if (i < length - 1) {
          sb.append(" OR ");
        }
      }
      sb.append(")");
      return sb.toString();
    }
    return null;
  }

  public static void main(String[] args) {
    SolrIndexDifSerivceImpl test = new SolrIndexDifSerivceImpl();
    try {
      test.getFundRecommend(0, 10, "", "", "", "材料科学, 矿山, 机械工程    , 动力与电气工程", null, null, null, null, null);
      String str = "材料科学, 矿山, 机械工程    , 动力与电气工程";
      String str_1 = "材料科学, 矿山";
      String str_2 =
          "Material Science, Mining and Metallurgy, Mechanical Engineering, Power and Electrical Engineering";
      String str_3 = "Material Science, Mining and Metallurgy";
      String str_4 = "材料科学";
      String str_5 = "";
      String str1 = test.constructFqStringForFund(str_5);
      // Map<String, Object> map = test.getSearchSuggestion("数据");
      System.out.println(str1);

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
