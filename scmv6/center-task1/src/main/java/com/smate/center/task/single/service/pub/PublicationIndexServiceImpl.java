package com.smate.center.task.single.service.pub;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.gdata.util.common.base.Joiner;
import com.smate.center.task.dao.pdwh.quartz.BaseJnlCategoryRankDao;
import com.smate.center.task.dao.pdwh.quartz.BaseJournalCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.CniprPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.CnkiPatPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.CnkiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.EiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.IsiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubSourceDbDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.PubMedPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.SpsPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.WfPublicationDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.dao.sns.quartz.KeywordsDicDao;
import com.smate.center.task.dao.sns.quartz.PubIndexUrlDao;
import com.smate.center.task.dao.snsbak.bdsp.PubPdwhAddrStandardDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubSourceDb;
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
import com.smate.center.task.model.sns.pub.KeywordsDic;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;

@Service("publicationIndexService")
@Transactional(rollbackFor = Exception.class)
public class PublicationIndexServiceImpl implements PublicationIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  @Value("${domainscm}")
  private String domainScm;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  public static String INDEX_TYPE_SUGGEST = "pdwh_suggest_search_index";
  @Autowired
  private CacheService cacheService;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;
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
  private PubMedPublicationDao pubMedPublicationDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private BaseJnlCategoryRankDao baseJnlCategoryRankDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private KeywordsDicDao keywordsDicDao;
  @Autowired
  private PubCategoryDao pubCategoryDao;
  @Autowired
  private PubPdwhAddrStandardDao pubPdwhAddrStandardDao;
  @Autowired
  private PdwhPubSourceDbDao pdwhPubSourceDbDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  @Override
  public void publicationIndex() {
    SolrServer server = initializeSolrServer();

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
      buildPubDbIds(doc, one.getPubId());
      doc.setField("zhTitle", one.getZhTitle());
      doc.setField("enTitle", one.getEnTitle());
      doc.setField("authors", one.getAuthorName());
      try {
        doc.setField("cleanAuthors", XmlUtil.cleanXMLAuthorChars(one.getAuthorName()));
      } catch (Exception e) {
        logger.error("生成cleanAuthors出错", e);
      }
      doc.setField("doi", one.getDoi());
      doc.setField("zhAbstract", one.getZhAbstract());
      doc.setField("enAbstract", one.getEnAbstract());
      doc.setField("zhKeywords", one.getZhKeywords());
      doc.setField("enKeywords", one.getEnKeywords());
      doc.setField("pubYear", one.getPubYear());
      doc.setField("pubTypeId", one.getPubType());
      doc.setField("enPubBrief", one.getEnBriefDesc());
      doc.setField("zhPubBrief", one.getZhBriefDesc());
      doc.setField("pubOrganization", one.getOrganization());
      String pubShortUrl = this.pdwhPubIndexUrlDao.getStringUrlByPubId(one.getPubId());
      if (StringUtils.isNotBlank(pubShortUrl)) {
        doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
      }
      /*
       * Long[] cats = { 1L, 2L, 201L, 204L, 108L }; doc.setField("pubCategory", cats);
       */
      List<Long> categoryList = this.pubCategoryDao.getScmCategoryByPubId(one.getPubId());
      if (categoryList != null && categoryList.size() > 0) {
        doc.setField("pubCategory", categoryList.toArray());
      }
      doc.setField("fundInfo", one.getFundInfo());
      // doc.setField("language", one.getLanguage());
      // 用于有全文的排序，1有；0无
      doc.setField("fullText", this.hasFullText(one.getPubId()));
      doc.setField("journalGrade", this.getPubQualityByJnlRank(one.getJnlId()));
      doc.setField("journalName", "");
      List<Long> provinceIds = pubPdwhAddrStandardDao.getPubPdwhAddrStandard(one.getPubId());
      if (provinceIds != null && provinceIds.size() > 0) {
        doc.setField("provinceId", Joiner.on(",").join(provinceIds));
      }
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

  private void buildPubDbIds(SolrInputDocument doc, Long pubId) {
    PdwhPubSourceDb db = pdwhPubSourceDbDao.get(pubId);
    if (db != null) {
      Set<Integer> dbIdList = new HashSet<Integer>();
      if (db.getEi() != null && db.getEi() == 1) {
        dbIdList.add(14);
      }
      if (db.getCnki() != null && db.getCnki() == 1) {
        dbIdList.add(4);
      }
      if (db.getCnkiPat() != null && db.getCnkiPat() == 1) {
        dbIdList.add(21);
      }
      if (db.getIstp() != null && db.getIstp() == 1) {
        dbIdList.add(15);
      }
      if (db.getRainPat() != null && db.getRainPat() == 1) {
        dbIdList.add(31);
      }
      if (db.getSci() != null && db.getSci() == 1) {
        dbIdList.add(16);
      }
      if (db.getSsci() != null && db.getSsci() == 1) {
        dbIdList.add(17);
      }
      if (dbIdList.size() > 0) {
        doc.setField("pubDbIdList", dbIdList.toArray());
      }
    }
  }

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  public List<PdwhPublication> getPublist(Long lastId) {

    List<PdwhPublication> pdwhList = pdwhPublicationDao.findPubByBatchSize(lastId, 3500);

    for (PdwhPublication pub : pdwhList) {
      if (pub == null || pub.getPubId() == null) {
        continue;
      }
      // 1奖励,2书/著作,10书籍章节，在此处都归类为7其他
      /*
       * if (pub.getPubType() == 1 || pub.getPubType() == 2 || pub.getPubType() == 10) {
       * pub.setPubType(7); }
       */
      String pubXml = pdwhPubXmlDao.getXmlStringByPubId(pub.getPubId());
      // String pubXml = getPubXmlString(pub.getDbId(), pub.getPubId());

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
          pub.setOrganization(
              StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "organization")));
        } catch (Exception e) {
          logger.info("获取xml相关属性错误, pubAllId = " + pub.getPubId());
        }
      }
    }

    return pdwhList;

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

  private Integer getPubQualityByJnlRank(Long jnlId) {
    Integer highestRank = 9;
    List<Long> ids = baseJournalCategoryDao.getCategoryIdsByJnlId(jnlId);
    if (CollectionUtils.isNotEmpty(ids)) {
      List<String> rankStrings = baseJnlCategoryRankDao.getRanksByCategoryIdList(ids);
      if (CollectionUtils.isNotEmpty(rankStrings)) {
        String highestRankString = rankStrings.get(0);
        highestRank = BaseJnlCategoryRankEnum.getRankValue(highestRankString);
      }
    }

    return highestRank;
  }

  @Override
  public void indexKeywords() {
    SolrServer server = initializeSolrServer();

    Long lastId = (Long) cacheService.get(INDEX_TYPE_KW, "last_kw_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<KeywordsDic> kwList = getKwDicList(lastId);
    if (kwList == null || kwList.size() == 0) {
      return;
    }

    Integer lastIndex = kwList.size() - 1;
    Long lastPubId = kwList.get(lastIndex).getId();
    this.cacheService.put(INDEX_TYPE_KW, 60 * 60 * 24, "last_kw_id", lastPubId);
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (KeywordsDic kd : kwList) {
      String keywordTxt = kd.getKeyword();
      if (StringUtils.isEmpty(keywordTxt)) {
        continue;
      }

      Long counts = 0L;
      try {
        counts = this.queryPubCounts(keywordTxt, server);
      } catch (SolrServerException e) {
        logger.error("获取keyword数量出错！ keywords=" + keywordTxt + "----", e);
      }
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_KW);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      doc.setField("id", generateIdForIndex(kd.getId(), INDEX_TYPE_KW));
      doc.setField("kwId", kd.getId());
      doc.setField("kwTotalCount", counts);// 通过检索得到的论文数，作为排序的简单依据
      doc.setField("kwTxt", keywordTxt);
      docList.add(doc);
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        server.add(docList);
        server.commit();
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("keywords索引创建出错，end = " + new Date());
      }
    }
  }

  private List<KeywordsDic> getKwDicList(Long lastid) {
    List<KeywordsDic> list = keywordsDicDao.findKeywordText(lastid, 4000);
    return list;
  }

  private Long queryPubCounts(String keyword, SolrServer server) throws SolrServerException {
    Assert.notNull(server, "Solr中serverUrl为空，请在正确配置！");

    SolrQuery query = new SolrQuery();

    query.setRequestHandler("/pubbrowse");
    query.setQuery(keyword);
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);

    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setParam("fl", "score");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public List<BigDecimal> getSuggestStrToHandleList(Integer type) {
    return this.keywordsDicDao.getSuggestStrToHandleList(type);
  }

  @Override
  public void updateSuggestStrStatus(Long id, Integer type, Integer status) {
    this.keywordsDicDao.updateSuggestStrStatus(id, type, status);
  }

  @Override
  public void indexSuggestInfo(Long id, Integer type) {
    if (type == 1) {
      this.indexSuggestUserInfo(id);
    } else if (type == 2) {
      this.indexSuggestInsInfo(id);
    }
  }

  private void indexSuggestUserInfo(Long psnId) {
    Person psn = this.personDao.getPersonName(psnId);
    if (psn == null) {
      return;
    }
    // 隐私名单
    if (this.psnPrivateDao.existsPsnPrivate(psnId)) {
      return;
    }
    SolrServer server = initializeSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
    doc.setField("businessType", INDEX_TYPE_SUGGEST);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", "990000" + psnId);
    doc.setField("suggestPsnId", psn.getPersonId());
    String nameStr = StringUtils.trimToEmpty(StringUtils.isNotBlank(psn.getName()) ? psn.getName() : psn.getEnName());
    if (StringUtils.isNotEmpty(nameStr)) {
      doc.setField("suggestPsnName", nameStr);
    }
    // 人员suggest的排列顺序人员10+；机构20+，留出排序的等级
    doc.setField("suggestStrLevel", 10);
    // 1为成果检索提供提示，其他比如专利，人员，待开发
    doc.setField("suggestType", 1);
    String insStr = "";
    if (psn.getInsId() != null) {
      Institution ins = institutionDao.get(psn.getInsId());
      if (ins != null) {
        insStr = StringUtils.trimToEmpty(StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName());
        doc.setField("suggestInsName", insStr);
        doc.setField("suggestInsId", ins.getId());
        // 有院校标准信息的优先显示
        doc.setField("suggestStrLevel", 11);
      }
    }
    String suggestStr = StringUtils.trimToEmpty(insStr + " " + nameStr);
    // 计算得分排序
    if (StringUtils.isEmpty(suggestStr)) {
      return;
    } else {
      doc.setField("suggestStr", suggestStr);
    }
    // 计算得分
    Integer suggestStrScore = 0;
    PsnStatistics psnStatistics = psnStatisticsDao.get(psn.getPersonId());
    if (psnStatistics != null) {
      suggestStrScore = psnStatistics.getPubSum() + psnStatistics.getHindex() * 10;
    }
    doc.setField("suggestStrScore", suggestStrScore);
    try {
      server.add(doc);
      server.commit();
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
    }
  }

  private void indexSuggestInsInfo(Long insId) {
    Institution ins = this.institutionDao.get(insId);
    if (ins == null) {
      return;
    }
    Long insType = ins.getNature();
    SolrServer server = initializeSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
    doc.setField("businessType", INDEX_TYPE_SUGGEST);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", "980000" + insId);
    doc.setField("suggestPsnId", 0);
    doc.setField("suggestPsnName", 0);
    String insStr =
        StringUtils.trimToEmpty(StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName());
    doc.setField("suggestInsName", insStr);
    doc.setField("suggestInsId", insId);
    // 机构suggest的排列顺序人员10+；机构20+，留出排序的等级,比如单位信息全的优先显示
    doc.setField("suggestStrLevel", 20);
    // 机构计算得分 TODO
    Integer suggestStrScore = 0;
    if (insType == 1 || insType == 2) {// 大学与科研机构
      suggestStrScore = 22;
    } else if (insType == 7) {// 医院
      suggestStrScore = 21;
    } else {
      suggestStrScore = 20;
    }
    doc.setField("suggestStrScore", suggestStrScore);
    // 1为成果检索提供提示，其他比如专利，人员，待开发
    doc.setField("suggestType", 1);
    String suggestStr = StringUtils.trimToEmpty(insStr);
    // 计算得分排序
    if (StringUtils.isEmpty(suggestStr)) {
      return;
    } else {
      doc.setField("suggestStr", suggestStr);
    }
    try {
      server.add(doc);
      server.commit();
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
    }
  }
}
