package com.smate.web.v8pub.service.solr;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dao.journal.BaseJnlCategoryRankDao;
import com.smate.web.v8pub.dao.journal.BaseJournalCategoryDao;
import com.smate.web.v8pub.dao.pdwh.PdwhPubAddrInsRecordDao;
import com.smate.web.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.web.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.web.v8pub.dao.pdwh.PubCategoryDao;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.CategoryScmDao;
import com.smate.web.v8pub.dao.sns.FileDownloadRecordDao;
import com.smate.web.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.po.journal.BaseJnlCategoryRankEnum;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PdwhPubSituationService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.utils.MessageDigestUtils;

@Service("solrIndexDifService")
@Transactional(rollbackFor = Exception.class)
public class SolrIndexDifSerivceImpl implements SolrIndexDifService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String runEnv = System.getenv("RUN_ENV");
  @Value("${solr.server.url.update}")
  private String serverUrl;
  @Value("${domainscm}")
  private String domainScm;

  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PdwhPubSituationService pdwhPubSituationService;
  @Autowired
  private PubCategoryDao pubCategoryDao;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private BaseJnlCategoryRankDao baseJnlCategoryRankDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private FileDownloadRecordDao fileDownloadRecordDao;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private CategoryScmDao categoryScmDao;

  public String INDEX_TYPE_PDWH_PAPER = "pdwh_paper_index";
  public String INDEX_TYPE_PDWH_PAT = "pdwh_pat_index";
  public String INDEX_TYPE_PSN = "person_index";
  public String INDEX_TYPE_FUND = "fund_index";
  public String INDEX_TYPE_KW = "keywords_index";
  public String INDEX_TYPE_SNS_PUB = "simple_sns_pub_index";

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  @Override
  public void indexPublication(PubPdwhDetailDOM pubPdwhDetailDOM, String pubIndexUrl, Integer publishYear,
      Integer publishMonth, Integer publishDay) throws Exception {
    if (pubPdwhDetailDOM == null) {
      return;
    }
    Long pubId = pubPdwhDetailDOM.getPubId();
    if (StringUtils.isBlank(pubIndexUrl)) {
      PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlService.get(pubId);
      pubIndexUrl = pdwhPubIndexUrl.getPubIndexUrl();
    }
    SolrServer server = initializeSolrServer();
    // 先删除原索引，再重新增加新索引
    server.deleteById(String.valueOf(generateIdForIndex(pubId, INDEX_TYPE_PDWH_PAPER)));

    SolrInputDocument doc = new SolrInputDocument();
    // 必须字段设定schema.xml配置
    doc.setField("businessType", INDEX_TYPE_PDWH_PAPER);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", generateIdForIndex(pubId, INDEX_TYPE_PDWH_PAPER));
    doc.setField("pubId", pubId);
    doc.setField("pubDbId", pubPdwhDetailDOM.getSrcDbId());
    buildPubDbIds(doc, pubId);
    doc.setField("summary", pubPdwhDetailDOM.getSummary());
    doc.setField("pubTitle", pubPdwhDetailDOM.getTitle());
    doc.setField("authors", pubPdwhDetailDOM.getAuthorNames());
    try {
      String cleanAuthors = XmlUtil.cleanXMLAuthorChars(pubPdwhDetailDOM.getAuthorNames());
      if (cleanAuthors != null) {
        String[] authors = cleanAuthors.split(";");
        StringBuffer md5Authors = new StringBuffer();
        for (int i = 0; i < authors.length - 1; i++) {
          md5Authors.append(MessageDigestUtils.messageDigest(authors[i].trim()));
          md5Authors.append("; ");
        }
        md5Authors.append(MessageDigestUtils.messageDigest(authors[authors.length - 1].trim()));
        doc.setField("cleanAuthors", cleanAuthors);
        doc.setField("md5Authors", md5Authors);
      }
    } catch (Exception e) {
      logger.error("生成cleanAuthors出错", e);
    }

    doc.setField("doi", pubPdwhDetailDOM.getDoi());
    doc.setField("keywords", pubPdwhDetailDOM.getKeywords());
    doc.setField("pubOrganization", pubPdwhDetailDOM.getOrganization());
    doc.setField("pubCitations", pubPdwhDetailDOM.getCitations());
    buildPdwhPubRegionIds(pubId, doc);// 构造成果地区
    buildPdwhPubCategory(pubId, doc);// 构造成果研究领域

    if (publishYear != null) {
      doc.setField("pubYear", publishYear);
    }
    if (publishMonth != null) {
      doc.setField("pubMonth", publishMonth);
    }
    if (publishDay != null) {
      doc.setField("pubDay", publishDay);
    }
    doc.setField("pubTypeId", pubPdwhDetailDOM.getPubType());
    doc.setField("pubBrief", pubPdwhDetailDOM.getBriefDesc());
    doc.setField("pubShortUrl", buildShortUrl(pubIndexUrl));
    doc.setField("fundInfo", pubPdwhDetailDOM.getFundInfo());
    buildPdwhPubFulltext(pubId, doc);// 构造成果全文信息
    PdwhPubStatisticsPO pdwhPubStatistics = pdwhPubStatisticsDAO.get(pubId);
    // SCM-19763 增加基准库与个人库关联的成果的统计数
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pubId);
    Long readCount = 0L;
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      readCount = pubStatisticsDAO.findPubReadCounts(snsPubIds);
      if (readCount == null) {
        readCount = 0L;
      }
    }
    // SCM-19763 增加基准库与个人库关联的成果的统计数
    readCount =
        (pdwhPubStatistics.getReadCount() == null) ? 0 + readCount : pdwhPubStatistics.getReadCount() + readCount;
    doc.setField("readCount", readCount.intValue());

    if (4 == pubPdwhDetailDOM.getPubType()) {
      JournalInfoBean infoBean = (JournalInfoBean) pubPdwhDetailDOM.getTypeInfo();
      if (infoBean != null) {
        doc.setField("journalGrade", getPubQualityByJnlRank(infoBean.getJid()));
        doc.setField("journalName", infoBean.getName());
      }
    }

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

  private void buildPdwhPubFulltext(Long pubId, SolrInputDocument doc) {
    PdwhPubFullTextPO fulltext = this.pdwhPubFullTextDAO.getByPubId(pubId);
    Long downLoadSum = 0L;
    if (fulltext != null) {
      if (fulltext.getPermission() == 0) {
        downLoadSum = fileDownloadRecordDao.getFileDonwloadSum(fulltext.getFileId());
      }
      if (StringUtils.isNotBlank(fulltext.getThumbnailPath())) {
        doc.setField("fullTextImgUrl", domainScm + fulltext.getThumbnailPath());
      }
    }
    doc.setField("downLoadCount", downLoadSum.intValue());
    // 用于有全文的排序，1有；0无
    doc.setField("fullText", fulltext == null ? 0 : 1);
  }

  private void buildPdwhPubCategory(Long pubId, SolrInputDocument doc) {
    Set<Long> superCatIds = new HashSet<Long>();
    List<Long> categoryList = this.pubCategoryDao.getScmCategoryByPubId(pubId);
    if (categoryList != null && categoryList.size() > 0) {
      doc.setField("pubCategory", categoryList.toArray());
      for (Long catId : categoryList) {
        Long superCatId = categoryScmDao.getSuperCatId(catId);
        superCatIds.add(0L == superCatId ? catId : superCatId);
      }
      doc.setField("pubSuperCategory", superCatIds.toArray());
    }
  }

  private void buildPdwhPubRegionIds(Long pubId, SolrInputDocument doc) {
    Set<Long> regionIdList = new HashSet<Long>();
    Optional.ofNullable(pdwhPubAddrInsRecordDao.getPubRegionIdByPubId(pubId)).ifPresent(regionIds -> {
      regionIdList.addAll(regionIds);
      regionIds.forEach(regionId -> {
        Optional.ofNullable(constRegionDao.getSuperRegionList(regionId, false)).ifPresent(superRegionIds -> {
          regionIdList.addAll(superRegionIds);
        });
      });
    });
    if (regionIdList != null && regionIdList.size() > 0) {
      doc.setField("regionCode", regionIdList.toArray());
    }
  }

  private Object getPubQualityByJnlRank(Long jid) {
    Integer highestRank = 9;
    List<Long> ids = baseJournalCategoryDao.getCategoryIdsByJnlId(jid);
    if (CollectionUtils.isNotEmpty(ids)) {
      List<String> rankStrings = baseJnlCategoryRankDao.getRanksByCategoryIdList(ids);
      if (CollectionUtils.isNotEmpty(rankStrings)) {
        String highestRankString = rankStrings.get(0);
        highestRank = BaseJnlCategoryRankEnum.getRankValue(highestRankString);
      }
    }
    return highestRank;
  }

  private void buildPubDbIds(SolrInputDocument doc, Long pdwhPubId) {
    List<String> dbIdList = pdwhPubSituationService.listByPdwhPubId(pdwhPubId);
    if (dbIdList.size() > 0) {
      doc.setField("pubDbIdList", dbIdList.toArray());
    }
  }

  @Override
  public void indexPatent(PubPdwhDetailDOM pubPdwhDetailDOM, String pubIndexUrl, Integer publishYear,
      Integer publishMonth, Integer publishDay) throws Exception {
    if (pubPdwhDetailDOM == null) {
      return;
    }
    SolrServer server = initializeSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    Long pubId = pubPdwhDetailDOM.getPubId();
    if (StringUtils.isBlank(pubIndexUrl)) {
      PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlService.get(pubId);
      pubIndexUrl = pdwhPubIndexUrl.getPubIndexUrl();
    }
    // 先删除原索引，再重新增加新索引
    server.deleteById(String.valueOf(generateIdForIndex(pubId, INDEX_TYPE_PDWH_PAT)));

    // 必须字段设定schema.xml配置
    doc.setField("businessType", INDEX_TYPE_PDWH_PAT);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", generateIdForIndex(pubId, INDEX_TYPE_PDWH_PAT));
    doc.setField("patId", pubId);
    doc.setField("patDbId", pubPdwhDetailDOM.getSrcDbId());
    doc.setField("patTitle", pubPdwhDetailDOM.getTitle());
    doc.setField("patAuthors", pubPdwhDetailDOM.getAuthorNames());
    doc.setField("patCitations", pubPdwhDetailDOM.getCitations());
    buildPdwhPubRegionIds(pubId, doc);// 构造成果地区
    buildPdwhPubCategory(pubId, doc);// 构造成果研究领域
    try {
      String cleanAuthors = XmlUtil.cleanXMLAuthorChars(pubPdwhDetailDOM.getAuthorNames());
      if (cleanAuthors != null) {
        String[] authors = cleanAuthors.split(";");
        StringBuffer md5Authors = new StringBuffer();
        for (int i = 0; i < authors.length - 1; i++) {
          md5Authors.append(MessageDigestUtils.messageDigest(authors[i]).trim());
          md5Authors.append("; ");
        }
        md5Authors.append(MessageDigestUtils.messageDigest(authors[authors.length - 1].trim()));
        doc.setField("cleanPatAuthors", cleanAuthors);
        doc.setField("md5PatAuthors", md5Authors);
      }
    } catch (Exception e) {
      logger.error("生成cleanAuthors出错", e);
    }
    if (publishYear != null) {
      doc.setField("patYear", publishYear);
    }
    if (publishMonth != null) {
      doc.setField("patMonth", publishMonth);
    }
    if (publishDay != null) {
      doc.setField("patDay", publishDay);
    }
    PatentInfoBean patentInfoBean = (PatentInfoBean) pubPdwhDetailDOM.getTypeInfo();
    if (patentInfoBean != null) {
      String patenType = patentInfoBean.getType();
      if (StringUtils.isNotBlank(patenType) && StringUtils.isNumeric(patenType)) {
        doc.setField("patTypeId", Integer.valueOf(patenType));
      } else {
        // 为空也设置为其他
        doc.setField("patTypeId", 7);
      }
      doc.setField("patentNo", patentInfoBean.getApplicationNo());
    }
    doc.setField("organization", pubPdwhDetailDOM.getOrganization());
    doc.setField("patBrief", pubPdwhDetailDOM.getBriefDesc());
    doc.setField("pubShortUrl", buildShortUrl(pubIndexUrl));
    buildPdwhPubFulltext(pubId, doc);// 构造成果全文信息
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

  private String buildShortUrl(String pubIndexUrl) {
    String pdwhShortUrlDomain = "";
    if (StringUtils.isNotBlank(pubIndexUrl)) {
      pdwhShortUrlDomain = domainScm + "/" + ShortUrlConst.S_TYPE + "/" + pubIndexUrl;
    }
    return pdwhShortUrlDomain;
  }

  /**
   * 在index时区分唯一id
   * 
   * @param id
   * @param type
   * @return
   */
  public Long generateIdForIndex(Long id, String type) {
    if (INDEX_TYPE_PDWH_PAPER.equalsIgnoreCase(type)) {// pdwhPaper前缀为100000
      String idString = String.valueOf(id);
      return Long.parseLong("100000" + idString);
    } else if (INDEX_TYPE_PDWH_PAT.equalsIgnoreCase(type)) { // pdwhPat前缀为700000
      String idString = String.valueOf(id);
      return Long.parseLong("700000" + idString);
    } else if (INDEX_TYPE_SNS_PUB.equalsIgnoreCase(type)) {// snsPub前缀为700000

      String idString = String.valueOf(id);
      return Long.parseLong("410000" + idString);
    } else if (INDEX_TYPE_PSN.equalsIgnoreCase(type)) {// psn前缀为50
      String idString = String.valueOf(id);
      return Long.parseLong("50" + idString);
    } else {
      return id;
    }
  }

  @Override
  public String updateSolr(Long pdwhPubId, String pubIndexUrl) {
    Map<String, String> result = new HashMap<>();
    try {
      if (NumberUtils.isNotNullOrZero(pdwhPubId)) {

        PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailService.getByPubId(pdwhPubId);
        PubPdwhPO pubPdwh = pubPdwhDAO.get(pdwhPubId);
        // 判断是否为专利
        if (PublicationTypeEnum.isPatent(pubPdwhDetailDOM.getPubType())) {
          indexPatent(pubPdwhDetailDOM, pubIndexUrl, pubPdwh.getPublishYear(), pubPdwh.getPublishMonth(),
              pubPdwh.getPublishDay());
        } else {
          indexPublication(pubPdwhDetailDOM, pubIndexUrl, pubPdwh.getPublishYear(), pubPdwh.getPublishMonth(),
              pubPdwh.getPublishDay());
        }
        result.put("result", "SUCCESS");
      } else {
        result.put("result", "pdwhPubId为空");
      }
    } catch (Exception e) {
      result.put("result", "ERROR");
      logger.error("基准库成果索引创建出错,pdwhPubId={}" + pdwhPubId, e);
    }
    return JacksonUtils.mapToJsonStr(result);
  }


}
