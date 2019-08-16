package com.smate.center.batch.service.solr.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PdwhPublicationAllDao;
import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IsiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.pubmed.PubMedPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.wanfang.WfPublicationDao;
import com.smate.center.batch.dao.sns.pub.PubFulltextDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.PublicationPdwhDao;
import com.smate.center.batch.dao.solr.PdwhSnsPubFullTextDao;
import com.smate.center.batch.dao.solr.PubAuthorPdwhDao;
import com.smate.center.batch.dao.solr.PubInfoPdwhDao;
import com.smate.center.batch.dao.solr.SolrIndexErrorLogDao;
import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubExtend;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExtend;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubExtend;
import com.smate.center.batch.model.pdwh.pub.ei.EiPubExtend;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExtend;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubExtend;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubExtend;
import com.smate.center.batch.model.pdwh.pub.wanfang.WfPubExtend;
import com.smate.center.batch.model.sns.pub.PubFulltext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.model.solr.PdwhSnsPubFullText;
import com.smate.center.batch.model.solr.PubAuthorPdwh;
import com.smate.center.batch.model.solr.PubInfoPdwh;
import com.smate.center.batch.model.solr.QueryFields;
import com.smate.center.batch.model.solr.SolrIndexErrorLog;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

@Service("solrIndexPreprocessService")
@Transactional(rollbackFor = Exception.class)
public class SolrIndexPreprocessServiceImpl implements SolrIndexPreprocessService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexErrorLogDao solrIndexErrorLogDao;
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
  private PubAuthorPdwhDao pubAuthorPdwhDao;
  @Autowired
  private PublicationPdwhDao publicationPdwhDao;
  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private PdwhPublicationAllDao pdwhPublicationAllDao;
  @Autowired
  private PdwhSnsPubFullTextDao pdwhSnsPubFullTextDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private CacheService cacheService;

  static String PDWH_PRE_BUILD_AUTHOR = "buildAuthors";
  static String PDWH_PRE_DUP_CLEAN = "dupClean";
  static String PDWH_PRE_PUB_FULLTEXT = "fullText";

  @Override
  public void buildAuthors() {
    Long lastId = (Long) cacheService.get(PDWH_PRE_BUILD_AUTHOR, "last_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<PubInfoPdwh> pubList = pubInfoPdwhDao.findAllPubByBatchSize(lastId, 1500);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========pubList为空，buildAuthors终止===========, time = " + new Date());
      return;
    }
    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubAllId();
    this.cacheService.put(PDWH_PRE_BUILD_AUTHOR, 60 * 60 * 24, "last_id", lastPubId);

    for (PubInfoPdwh pub : pubList) {
      Integer dbId = pub.getDbId();
      Long pubId = pub.getPubId();
      Long pubAllId = pub.getPubAllId();
      lastId = pubAllId;
      try {
        String xml = this.getPubXmlString(dbId, pubId);
        if (StringUtils.isEmpty(xml)) {
          continue;
        }
        PubXmlDocument doc = new PubXmlDocument(xml);
        this.processAuthor(doc, pub);
        if (StringUtils.isNotEmpty(pub.getZhTitle())) {
          Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
          Matcher m = p.matcher(pub.getZhTitle());
          if (m.find()) {
            pub.setLanguage(QueryFields.LANGUAGE_ZH);
          } else {
            pub.setLanguage(QueryFields.LANGUAGE_EN);
          }
        } else {
          pub.setLanguage(QueryFields.LANGUAGE_EN);
        }

        pubInfoPdwhDao.save(pub);
      } catch (Exception e) {
        logger.error("基准库重构作者错误,pubId = " + pubId, e);
        SolrIndexErrorLog error = new SolrIndexErrorLog();
        String errorMsg = e == null ? "" : e.toString();
        error.setErrorMsg("基准库重构作者错误，pubId = " + pubId + errorMsg);
        error.setRecordTime(new Date());
        solrIndexErrorLogDao.save(error);
      }
    }
  }

  @Override
  public void cleanDupPubAll() {

    Integer[] ascDbIds = new Integer[] {15, 16, 17, 2, 8, 5, 14, 4, 10, 11, 21};

    Long lastId = (Long) cacheService.get(PDWH_PRE_DUP_CLEAN, "last_pub_id");
    if (lastId == null) {
      lastId = 0L;
    }
    List<PubInfoPdwh> pubList = pubInfoPdwhDao.findAllPubByBatchSize(lastId, 1500);

    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========pubList为空，buildAuthors终止===========, time = " + new Date());
      return;
    }
    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubId();
    // 一天过期
    this.cacheService.put(PDWH_PRE_DUP_CLEAN, 60 * 60 * 24, "last_pub_id", lastPubId);

    for (PubInfoPdwh pub : pubList) {
      Long pubAllId = pub.getPubAllId();
      lastId = pubAllId;
      try {
        List<PubInfoPdwh> dupPubList = pubInfoPdwhDao.getPubByHash(pub.getZhTitleHash(), pub.getEnTitleHash());
        if (dupPubList.size() <= 1) {
          continue;
        }

        TreeMap map = new TreeMap();

        for (PubInfoPdwh dupPub : dupPubList) {
          Integer dupIdx = ArrayUtils.indexOf(ascDbIds, dupPub.getDbId());
          map.put(dupIdx, dupPub.getDbId());
        }

        for (PubInfoPdwh dupPub : dupPubList) {
          if (!map.get(map.firstKey()).equals(dupPub.getDbId())) {
            this.pubInfoPdwhDao.delete(dupPub.getPubAllId());
          }
        }

      } catch (Exception e) {
        logger.error("PubAll查重错误,pubId = " + pubAllId, e);
        SolrIndexErrorLog error = new SolrIndexErrorLog();
        String errorMsg = e == null ? "" : e.toString();
        error.setErrorMsg("PubAll查重错误，pubId = " + pubAllId + errorMsg);
        error.setRecordTime(new Date());
        solrIndexErrorLogDao.save(error);
      }
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

  public void processAuthor(PubXmlDocument xmlDoc, PubInfoPdwh pub) {
    try {
      ArrayList<PubAuthorPdwh> rsList = null;
      ArrayList<String[]> namesListPubXPath = this.getAuthorsFromPubXPath(xmlDoc);
      if (CollectionUtils.isNotEmpty(namesListPubXPath)) {
        int seqNo = 1;
        StringBuilder fullNameStr = new StringBuilder();
        StringBuilder abbrNameStr = new StringBuilder();

        for (String[] names : namesListPubXPath) {
          rsList = new ArrayList<PubAuthorPdwh>();
          PubAuthorPdwh author = new PubAuthorPdwh();
          String fullName = names[0];
          String abbrName = names[1];
          author.setSeqNo(seqNo);
          author.setName(fullName);
          author.setAbbrName(abbrName);
          author.setDbId(pub.getDbId());
          author.setPubAllId(pub.getPubAllId());
          author.setPubId(pub.getPubId());
          // rsList.add(author);

          if (StringUtils.isNotEmpty(fullName)) {
            // 控制在400char以内
            if (fullNameStr.length() + fullName.length() < 800) {
              fullNameStr.append(fullName).append("; ");
            }
          }

          if (StringUtils.isNotEmpty(abbrName)) {
            if (abbrNameStr.length() + abbrName.length() < 800) {
              abbrNameStr.append(abbrName).append("; ");
            }
          }
          this.pubAuthorPdwhDao.save(author);
          seqNo++;
        }

        String nameStr = fullNameStr.toString();
        if (StringUtils.isEmpty(nameStr)) {
          nameStr = abbrNameStr.toString();
        }
        if (StringUtils.isNotEmpty(nameStr)) {
          // 去掉多余的“；”
          nameStr = nameStr.substring(0, nameStr.length() - 2);
          pub.setAuthorNames(nameStr);
        }

      }
    } catch (Exception e) {
      logger.error("pdwh作者重构错误,pubAllId = " + pub.getPubAllId(), e);
      SolrIndexErrorLog error = new SolrIndexErrorLog();
      String errorMsg = e == null ? "" : e.toString();
      error.setErrorMsg("pdwh作者重构错误,pubAllId = " + pub.getPubAllId() + errorMsg);
      error.setRecordTime(new Date());
      solrIndexErrorLogDao.save(error);
    }
  }

  public ArrayList<String[]> getAuthorsFromPubXPath(PubXmlDocument xmlDoc) {
    String authorNamesStr = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    String authorNamesAbbrStr = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names_abbr");
    String[] fullNames = null;
    String[] abbrNames = null;
    if (StringUtils.isNotBlank(authorNamesStr)) {
      // authorNamesStr = authorNamesStr.replaceAll("<.*?>", ";");
      authorNamesStr = XmlUtil.changeSBCChar(authorNamesStr);
      fullNames = authorNamesStr.split(";");
    }
    if (StringUtils.isNotBlank(authorNamesAbbrStr)) {

      // authorNamesAbbrStr = authorNamesAbbrStr.replaceAll("<.*?>", ";");
      authorNamesStr = XmlUtil.changeSBCChar(authorNamesStr);
      abbrNames = authorNamesAbbrStr.split(";");
    }
    ArrayList<String[]> namesList = null;
    ArrayList<String> abbrNamesList = null;
    ArrayList<String> fullNamesList = null;

    if (fullNames != null) {
      fullNamesList = new ArrayList<String>();
      for (String authorName : fullNames) {
        if (StringUtils.isNotBlank(authorName)) {
          // 大于50不存储
          if (authorName.length() > 50) {
            continue;
          }
          // 去除数字
          authorName = authorName.replaceAll("\\d+", "");
          authorName = StringUtils.trimToEmpty(cleanNameStr(authorName));
          if (StringUtils.isEmpty(authorName)) {
            continue;
          }
          fullNamesList.add(authorName);
        }
      }

    }

    if (abbrNames != null) {
      abbrNamesList = new ArrayList<String>();
      for (String authorName : abbrNames) {
        if (StringUtils.isNotBlank(authorName)) {
          // 大于50不存储
          if (authorName.length() > 50) {
            continue;
          }
          // 去除数字
          authorName = authorName.replaceAll("\\d+", "");
          authorName = StringUtils.trimToEmpty(cleanNameStr(authorName));
          if (StringUtils.isEmpty(authorName)) {
            continue;
          }
          abbrNamesList.add(authorName.trim());
        }
      }
    }

    if (CollectionUtils.isNotEmpty(fullNamesList)) {
      namesList = new ArrayList<String[]>();
      // fullname与abbrname都不为空
      if (CollectionUtils.isNotEmpty(abbrNamesList)) {
        for (int i = 0; i < fullNamesList.size(); i++) {
          String fullName = fullNamesList.get(i);
          String abbrName = "";
          if (i < abbrNamesList.size()) {
            abbrName = abbrNamesList.get(i);
          }
          String[] names = {fullName, abbrName};
          namesList.add(names);
        }
      } else {
        // abbranme为空
        for (String name : fullNamesList) {
          String fullName = name;
          String abbrName = "";
          String[] names = {fullName, abbrName};
          namesList.add(names);
        }

      }
      // fullname为空，
    } else {
      namesList = new ArrayList<String[]>();
      for (String name : abbrNamesList) {
        String fullName = name;
        String abbrName = name;
        String[] names = {fullName, abbrName};
        namesList.add(names);
      }
    }

    return namesList;
  }

  @Override
  public void checkFullTextForPdwh() {
    Long lastId = (Long) cacheService.get(PDWH_PRE_PUB_FULLTEXT, "last_pub_ft_id");
    if (lastId == null) {
      lastId = 0L;
    }

    List<PubFulltext> pubList = this.pubFulltextDao.queryFulltextList(lastId, 1500);
    if (CollectionUtils.isEmpty(pubList)) {
      logger.info("==========pubList为空，checkFullTextForPdwh终止===========, time = " + new Date());
      return;
    }
    Integer lastIndex = pubList.size() - 1;
    Long lastPubId = pubList.get(lastIndex).getPubId();
    this.cacheService.put(PDWH_PRE_PUB_FULLTEXT, 60 * 60 * 24, "last_pub_ft_id", lastPubId);

    for (PubFulltext ft : pubList) {
      Long pubId = ft.getPubId();
      lastId = pubId;
      try {
        ArchiveFile file = this.archiveFileDao.findArchiveFileById(ft.getFulltextFileId());
        if (file == null) {
          continue;
        }

        // 与pdwh进行匹配
        PublicationPdwh pub = this.publicationPdwhDao.get(pubId);
        if (pub != null) {
          this.matchPdwhPub(pub, ft);
        }
      } catch (Exception e) {
        logger.error("pdwh全文匹配错误,pubId = " + pubId, e);
        SolrIndexErrorLog error = new SolrIndexErrorLog();
        String errorMsg = e == null ? "" : e.toString();
        error.setErrorMsg("pdwh全文匹配错误，pubId = " + pubId + errorMsg);
        error.setRecordTime(new Date());
        solrIndexErrorLogDao.save(error);
      }
    }

  }

  private void matchPdwhPub(PublicationPdwh pub, PubFulltext ft) {
    // ISI
    if (pub.getIsiId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getIsiPub(pub.getIsiId());
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // CNKI
    if (pub.getCnkiId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getCnkiId(), 4);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // CNKIPAT
    if (pub.getCnkiPatId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getCnkiPatId(), 21);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // CNIPR
    if (pub.getCniprId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getCniprId(), 11);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // EI
    if (pub.getEiId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getEiId(), 14);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // WANGFANG
    if (pub.getWfId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getWfId(), 10);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // SCOUPUS
    if (pub.getSpsId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getSpsId(), 8);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // PUBMED
    if (pub.getPubmedId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getPubmedId(), 19);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }
    // SCIENCEDIRECT
    if (pub.getScdId() != null) {
      PdwhPublicationAll pubAll = pdwhPublicationAllDao.getPubAll(pub.getScdId(), 5);
      if (pubAll != null) {
        saveFtToPdwh(pubAll, ft);
      }
    }

  }

  public void saveFtToPdwh(PdwhPublicationAll pubAll, PubFulltext ft) {
    PdwhSnsPubFullText pfPdwh = new PdwhSnsPubFullText();
    pfPdwh.setDbId(pubAll.getDbid());
    pfPdwh.setPdwhPubAllId(pubAll.getId());
    pfPdwh.setPdwhPubId(pubAll.getPubId());
    pfPdwh.setSnsPubId(ft.getPubId());

    Publication pub = publicationDao.get(ft.getPubId());
    if (pub != null) {
      pfPdwh.setSnsCreatPsnId(pub.getPsnId());
    }
    this.pdwhSnsPubFullTextDao.save(pfPdwh);
  }

  public String cleanNameStr(String str) {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    String regEx = "[`~!#$%^&*+=/?！@#￥%……&*|【】‘；”“’。，、？]";
    return str.replaceAll(regEx, "");
  }
}
