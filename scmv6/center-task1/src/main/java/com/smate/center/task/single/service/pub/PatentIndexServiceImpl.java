package com.smate.center.task.single.service.pub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.google.gdata.util.common.base.Joiner;
import com.smate.center.task.dao.innocity.InnoCityRequirementDao;
import com.smate.center.task.dao.innocity.JyPatentDao;
import com.smate.center.task.dao.pdwh.quartz.CniprPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.CnkiPatPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.CnkiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.EiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.IsiPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.PubMedPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.SpsPublicationDao;
import com.smate.center.task.dao.pdwh.quartz.WfPublicationDao;
import com.smate.center.task.dao.snsbak.bdsp.PubPdwhAddrStandardDao;
import com.smate.center.task.model.innocity.InnoCityRequirement;
import com.smate.center.task.model.innocity.JyPatent;
import com.smate.center.task.model.pdwh.pub.cnipr.CniprPubExtend;
import com.smate.center.task.model.pdwh.pub.cnkipat.CnkiPatPubExtend;
import com.smate.center.task.model.pdwh.pub.sps.SpsPubExtend;
import com.smate.center.task.model.pdwh.pub.wanfang.WfPubExtend;
import com.smate.center.task.model.pdwh.quartz.CnkiPubExtend;
import com.smate.center.task.model.pdwh.quartz.EiPubExtend;
import com.smate.center.task.model.pdwh.quartz.IsiPubExtend;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.pdwh.quartz.PubMedPubExtend;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.data.XmlUtil;

@Service("patentIndexService")
@Transactional(rollbackFor = Exception.class)
public class PatentIndexServiceImpl implements PatentIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  @Value("${domainscm}")
  private String domainScm;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_PAT_RCMD = "patent_rcmd_index";
  public static String INDEX_TYPE_PAT_OWNER_RCMD = "patent_rcmd_owner_index";
  public static String INDEX_TYPE_PAT_REQUEST_RCMD = "patent_rcmd_request_index";

  @Autowired
  private CacheService cacheService;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
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
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private JyPatentDao jyPatentDao;
  @Autowired
  private InnoCityRequirementDao innoCityRequirementDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PubPdwhAddrStandardDao pubPdwhAddrStandardDao;
  private Integer batchSize = 2400;

  @Override
  public void indexPatent() {
    SolrServer server = initializeSolrServer();

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
      doc.setField("businessType", INDEX_TYPE_PAT);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getPubId(), INDEX_TYPE_PAT));
      doc.setField("patId", one.getPubId());
      doc.setField("patDbId", one.getDbId());
      doc.setField("zhPatTitle", one.getZhTitle());
      doc.setField("enPatTitle", one.getEnTitle());
      doc.setField("patAuthors", one.getAuthorName());
      try {
        doc.setField("cleanPatAuthors", XmlUtil.cleanXMLAuthorChars(one.getAuthorName()));
      } catch (Exception e) {
        logger.error("生成cleanPatAuthors出错", e);
      }
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
      // doc.setField("patLanguage", one.getLanguage());
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
        logger.info("Patent索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("Patent索引创建出错，end = " + new Date());
      }
    }
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

  public List<JyPatent> getPatentFromInnocity(Long lastId) {
    List<JyPatent> patList = new ArrayList<JyPatent>();
    patList = jyPatentDao.findPatByBatchSize(lastId, batchSize);
    return patList;
  }

  public List<InnoCityRequirement> getReqFromInnocity(Long lastId) {
    List<InnoCityRequirement> reqList = new ArrayList<InnoCityRequirement>();
    reqList = innoCityRequirementDao.findRequirementByBatchSize(lastId, batchSize);
    return reqList;
  }

  public Integer hasFullText(Long pubAllId) {
    Long count = this.pdwhFullTextFileDao.getCountByPubAllId(pubAllId);
    if (count == null) {
      return 0;
    }

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
    } else if (INDEX_TYPE_PAT_RCMD.equalsIgnoreCase(type)) {
      // patRcmd前缀为770000
      String idString = String.valueOf(id);
      return Long.parseLong("770000" + idString);
    } else if (INDEX_TYPE_PAT_OWNER_RCMD.equalsIgnoreCase(type)) {
      // patOwner前缀为770000
      String idString = String.valueOf(id);
      return Long.parseLong("780000" + idString);
    } else if (INDEX_TYPE_PAT_REQUEST_RCMD.equalsIgnoreCase(type)) {
      // patRequestRcmd前缀为790000
      String idString = String.valueOf(id);
      return Long.parseLong("790000" + idString);
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

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  @Override
  public void indexPatentRcmd() {
    SolrServer server = initializeSolrServer();

    Long lastId = (Long) cacheService.get(INDEX_TYPE_PAT_RCMD, "last_patent_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<JyPatent> pubList = getPatentFromInnocity(lastId);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========patentRcmdList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getId();
    this.cacheService.put(INDEX_TYPE_PAT_RCMD, 60 * 60 * 24, "last_patent_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();

    for (JyPatent one : pubList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_PAT_RCMD);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getId(), INDEX_TYPE_PAT_RCMD));
      doc.setField("patIdRcmd", one.getId());
      // doc.setField("patDbIdRcmd", one.getDbId());
      doc.setField("zhPatTitleRcmd", one.getZhTitle());
      // doc.setField("enPatTitleRcmd", one.getEnTitle());
      doc.setField("patAuthorsRcmd", one.getOwnerName());
      // doc.setField("patYearRcmd", one.getPubYear());
      // doc.setField("patTypeIdRcmd", one.getPatentCategory());
      // doc.setField("organizationRcmd", one.getOrganization());
      doc.setField("patentNoRcmd", one.getPatentNo());
      // doc.setField("enPatBriefRcmd", one.getEnBriefDesc());
      doc.setField("zhPatBriefRcmd", one.getRemark());
      // doc.setField("enPatAbstractRcmd", one.getZhBriefDesc());
      doc.setField("zhPatAbstractRcmd", one.getAbStract());
      doc.setField("patStatusRcmd", one.getStatus());
      // doc.setField("patLanguage", one.getLanguage());
      // doc.setField("patCategoryRcmd", "");
      docList.add(doc);
      lastId = one.getId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger
            .info("PatentRcmd索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("PatentRcmd索引创建出错，end = " + new Date());
      }
    }
  }

  @Override
  public void indexPatentRequestRcmd() {
    SolrServer server = initializeSolrServer();

    Long lastId = (Long) cacheService.get(INDEX_TYPE_PAT_REQUEST_RCMD, "last_req_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<InnoCityRequirement> reqList = getReqFromInnocity(lastId);

    if (CollectionUtils.isEmpty(reqList)) {
      logger.info("==========patentOwnerRcmdList为空，index终止===========, time = " + new Date());
      return;
    }

    Integer lastIndex = reqList.size() - 1;
    Long lastPubId = reqList.get(lastIndex).getId();
    this.cacheService.put(INDEX_TYPE_PAT_REQUEST_RCMD, 60 * 60 * 24, "last_req_id", lastPubId);

    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();

    for (InnoCityRequirement one : reqList) {
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置
      doc.setField("businessType", INDEX_TYPE_PAT_REQUEST_RCMD);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      // doc.setField("env", "test");
      doc.setField("id", generateIdForIndex(one.getId(), INDEX_TYPE_PAT_REQUEST_RCMD));
      doc.setField("reqId", one.getId());
      doc.setField("reqTitle", one.getTitle());
      // doc.setField("reqOwner", one.getAuthorName());
      // doc.setField("patYearRcmd", one.getPubYear());
      // doc.setField("reqCategory", one.getPatentCategory());
      // doc.setField("reqKeywords", one.getPatentNo());
      doc.setField("reqBrief", one.getDescription());
      doc.setField("reqStatus", one.getStatus());
      // doc.setField("patLanguage", one.getLanguage());
      // doc.setField("patCategoryRcmd", "");
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
            "PatentRequest索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("PatentRcmd索引创建出错，end = " + new Date());
      }
    }
  }
}
