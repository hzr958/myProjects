package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.constant.DbSourceConst;
import com.smate.center.batch.constant.ImportPubXmlConstants;
import com.smate.center.batch.dao.pdwh.pub.PubAllIdsSyncDao;
import com.smate.center.batch.dao.pdwh.pub.PubFundInfoPdwhDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.*;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PubFundInfoPdwh;
import com.smate.center.batch.model.pdwh.pub.cnki.*;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pubmatch.PubExpandCommonService;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.mq.CnkiPubCacheAssignProducer;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * cnki基准库成果服务.
 * 
 * @author liqinghua
 * 
 */
@Service("cnkiPublicationService")
@Transactional(rollbackFor = Exception.class)
public class CnkiPublicationServiceImpl implements CnkiPublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = -1156114415530631378L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CnkiPublicationDao cnkiPublicationDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private CnkiPubInsMatchService cnkiPubInsMatchService;
  @Autowired
  private CnkiPubAddrDao cnkiPubAddrDao;
  @Autowired
  private CnkiPubAssignDao cnkiPubAssignDao;
  @Autowired
  private CnkiPubCacheAssignProducer cnkiPubCacheAssignProducer;
  @Autowired
  private CnkiPubKeywordsDao cnkiPubKeywordsDao;
  @Autowired
  private CnkiPubKeywordsSplitDao cnkiPubKeywordsSplitDao;
  @Autowired
  private PubAllIdsSyncDao pubAllIdsSyncDao;
  @Autowired
  private PubExpandCommonService pubExpandCommonService;
  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private PubFundInfoPdwhDao pubFundInfoDao;

  @Override
  public Long getDupPub(Long titleHash, Long unitHash) throws ServiceException {

    try {
      CnkiPubDup pubDup = cnkiPublicationDao.getCnkiPubDup(titleHash, unitHash);
      if (pubDup != null) {
        return pubDup.getPubId();
      }
      return null;
    } catch (Exception e) {
      logger.error("查询重复记者库成果", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void addDbCachePub(Long insId, String dataXml) throws ServiceException {

    CnkiPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(new Date());
      // 增加到基准库
      this.addPub(dataXml, pub);
      // 匹配单位
      Integer match = cnkiPubInsMatchService.matchPubCache(insId, pub.getPubId(), pub.getPubAddrs());
      // 保存至BatchJobs
      if (match == 1) {
        publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.CNKI);
      }
    } else {
      // 更新资助基金信息
      refreshDupPub(pub, dupId);
      // 匹配单位
      Integer match = cnkiPubInsMatchService.matchPubCache(insId, dupId);
      // 保存至BatchJobs
      if (match == 1) {
        publicationPdwhService.saveToBatchJobs(dupId, insId, DbSourceConst.CNKI);
      }
    }
  }

  @Override
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException {

    CnkiPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(fetchTime);
      pub.setCreatePsn(psnId);
      pub.setCreateIns(insId);
      // 增加到基准库
      this.addPub(dataXml, pub);
    }
  }

  /**
   * 增加文献到基准库.
   * 
   * @param dataXml
   * @param pub
   * @throws ServiceException
   */
  private void addPub(String dataXml, CnkiPublication pub) throws ServiceException {

    try {
      // 先保存
      this.cnkiPublicationDao.save(pub);
      // 保存查重数据
      this.cnkiPublicationDao.saveCnkiPubDup(pub.getPubId(), pub.getTitleHash(), pub.getUnitHash());
      // 保存关键词
      saveKeywords(pub);
      // 保存Xml
      this.cnkiPublicationDao.saveCnkiPubExtend(pub.getPubId(), dataXml);
      // 保存到同步puball的任务表
      this.pubAllIdsSyncDao.savePubAlldsSync(pub.getPubId(), pub.getDbId());
      // 解析成果地址
      String orgsStr = pub.getOrganization();
      if (StringUtils.isNotBlank(orgsStr)) {
        List<CnkiPubAddr> pubAddrs = new ArrayList<CnkiPubAddr>();
        // 获取机构名称列表，构造cnkiPubCacheOrgs对象
        Set<String> pubAddrSet = XmlUtil.parseCnkiPubAddrs(orgsStr);
        for (String pubAddr : pubAddrSet) {
          CnkiPubAddr cco = new CnkiPubAddr(pub.getPubId(), StringUtils.substring(pubAddr, 0, 500));
          // 成果地址不可为空
          if (cco.getAddr() != null) {
            this.cnkiPubAddrDao.save(cco);
            pubAddrs.add(cco);
          }
        }
        pub.setPubAddrs(pubAddrs);
      }
      // 增加保存成果拆分日志记录,为成果拆分数据任务提供数据来源_MJG_CnkiPubExpandTask成果拆分任务_2014-01-23.
      CnkiPubExpandLog pubLog = new CnkiPubExpandLog();
      pubLog.setPubId(pub.getPubId());
      pubLog.setStatus(0);// 0-待处理.
      pubExpandCommonService.saveCnkiNeedExpandLog(pubLog);
    } catch (Exception e) {
      logger.error("增加文献到基准库", e);
      throw new ServiceException("增加文献到基准库", e);
    }
  }

  /**
   * 保存关键词.
   * 
   * @param pub
   * @throws ServiceException
   */
  private void saveKeywords(CnkiPublication pub) throws ServiceException {
    try {
      String zhKeywords = pub.getZhKeywords();
      String enKeywords = pub.getEnKeywords();

      if (StringUtils.isNotBlank(zhKeywords)) {
        this.cnkiPubKeywordsDao.save(new CnkiPubKeywords(pub.getPubId(), zhKeywords, 2));
        this.saveKeywordsSplit(pub.getPubId(), zhKeywords, 2);
      }
      if (StringUtils.isNotBlank(enKeywords)) {
        this.cnkiPubKeywordsDao.save(new CnkiPubKeywords(pub.getPubId(), enKeywords, 1));
        this.saveKeywordsSplit(pub.getPubId(), enKeywords, 1);
      }
    } catch (Exception e) {
      logger.error("saveKeywords保存关键词", e);
      throw new ServiceException("saveKeywords保存关键词", e);
    }
  }

  /**
   * 保存关键词拆分.
   * 
   * @param pubId
   * @param keywords
   * @param type
   * @throws ServiceException
   */
  @Override
  public void saveKeywordsSplit(Long pubId, String keywords, Integer type) throws ServiceException {
    try {

      if (StringUtils.isBlank(keywords)) {
        return;
      }
      Set<String> kws = XmlUtil.splitKeywords(keywords);
      if (CollectionUtils.isEmpty(kws)) {
        return;
      }

      for (String keyword : kws) {
        keyword = StringUtils.substring(keyword, 0, 200);
        this.cnkiPubKeywordsSplitDao.save(new CnkiPubKeywordsSplit(pubId, keyword, keyword.toLowerCase(),
            PubHashUtils.getKeywordHash(keyword), type));
      }
    } catch (Exception e) {
      logger.error("saveKeywordsSplit保存关键词", e);
      throw new ServiceException("saveKeywordsSplit保存关键词", e);
    }
  }

  /**
   * 解析成果Xml到CnkiPublication封装.
   * 
   * @param dataXml
   * @return
   * @throws ServiceException
   */
  private CnkiPublication praseDataXmlToPub(String dataXml) throws ServiceException {

    Map<String, Object> map = praseDataXml(dataXml);
    CnkiPublication pub = new CnkiPublication();

    pub.setPubYear((Integer) map.get("pubYear"));
    pub.setDbId((Integer) map.get("dbId"));
    pub.setSourceId(StringUtils.substring((String) map.get("sourceId"), 0, 100));
    pub.setDoi(StringUtils.substring((String) map.get("doi"), 0, 100));
    pub.setZhTitle(StringUtils.substring((String) map.get("zhTitle"), 0, 500));
    pub.setEnTitle(StringUtils.substring((String) map.get("enTitle"), 0, 500));
    pub.setPubType((Integer) map.get("pubType"));
    pub.setOriginal(StringUtils.substring((String) map.get("original"), 0, 200));
    pub.setIssn(StringUtils.substring((String) map.get("issn"), 0, 40));
    pub.setIsbn(StringUtils.substring((String) map.get("isbn"), 0, 40));
    pub.setIssue(StringUtils.substring((String) map.get("issue"), 0, 20));
    pub.setVolume(StringUtils.substring((String) map.get("volume"), 0, 20));
    pub.setStartPage(StringUtils.substring((String) map.get("startPage"), 0, 50));
    pub.setEndPage(StringUtils.substring((String) map.get("endPage"), 0, 50));
    pub.setArticleNo(StringUtils.substring((String) map.get("articleNo"), 0, 100));
    pub.setAuthorNames(StringUtils.substring((String) map.get("authorNames"), 0, 500));
    pub.setPatentNo(StringUtils.substring((String) map.get("patentNo"), 0, 100));
    pub.setConfName(StringUtils.substring((String) map.get("confName"), 0, 200));
    pub.setOrganization((String) map.get("organization"));
    // 查重hash
    Long titleHash = PubHashUtils.cleanTitleHash((String) map.get("zhTitle"), (String) map.get("enTitle"));
    pub.setTitleHash(titleHash);
    Long unitHash = PubHashUtils.getEnPubUnitFingerPrint((Integer) map.get("pubYear"), (String) map.get("original"),
        (String) map.get("authorNames"));
    pub.setUnitHash(unitHash);
    pub.setZhKeywords(StringUtils.substring((String) map.get("zhKeywords"), 0, 2000));
    pub.setEnKeywords(StringUtils.substring((String) map.get("enKeywords"), 0, 2000));
    pub.setFundInfo(StringUtils.substring((String) map.get("fundInfo"), 0, 1000));
    pub.setCategoryNo(StringUtils.substring((String) map.get("categoryNo"), 0, 100));
    return pub;
  }

  /**
   * 解析成果XML.
   * 
   * @param dataXml
   * @return
   */
  private Map<String, Object> praseDataXml(String dataXml) throws ServiceException {

    try {
      ImportPubXmlDocument document = new ImportPubXmlDocument(dataXml);
      Map<String, Object> map = document.prasePubWhData();
      String sourceDbCode = (String) map.get("sourceDbCode");
      if (StringUtils.isNotBlank(sourceDbCode)) {
        ConstRefDb sourceDb = constRefDbService.getConstRefDbByCode(sourceDbCode);
        if (sourceDb != null) {
          Integer dbId = sourceDb.getId().intValue();
          map.put("dbId", dbId);
        }
      }
      return map;
    } catch (Exception e) {
      logger.error("解析成果XML", e);
      throw new ServiceException("解析成果XML", e);
    }

  }

  @Override
  public List<CnkiPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException {
    try {

      List<CnkiPubAssign> list = this.cnkiPubAssignDao.getNeedSendPub(startId, size);
      if (CollectionUtils.isEmpty(list)) {
        return list;
      }
      // 获取XML数据
      List<Long> pubIds = new ArrayList<Long>();
      for (CnkiPubAssign assign : list) {
        pubIds.add(assign.getPubId());
      }
      if (pubIds.size() > 0) {
        List<CnkiPubExtend> xmlList = this.cnkiPublicationDao.getCnkiPubExtends(pubIds);
        for (CnkiPubExtend xml : xmlList) {
          for (CnkiPubAssign assign : list) {
            if (xml.getPubId().equals(assign.getPubId())) {
              assign.setXmlData(xml.getXmlData());
            }
          }
        }
      }
      return list;
    } catch (Exception e) {
      logger.error("获取需要发送到机构的指派信息", e);
      throw new ServiceException("获取需要发送到机构的指派信息", e);
    }
  }

  @Override
  public void sendInsPub(CnkiPubAssign pubassign) throws ServiceException {
    try {
      if (pubassign.getInsId() != null) {
        cnkiPubCacheAssignProducer.sendAssignMsg(pubassign.getPubId(), pubassign.getXmlData(), pubassign.getInsId());
      }
      pubassign.setIsSend(1);
      this.cnkiPubAssignDao.save(pubassign);
    } catch (Exception e) {
      logger.error("发送单位成果", e);
      throw new ServiceException("发送单位成果", e);
    }
  }

  /**
   * 获取待解析的成果列表.
   * 
   * @param pubIdList 成果ID列表.
   * @return
   */
  @Override
  public List<CnkiPubExtend> getCnkiPubExtendList(List<Long> pubIdList) {
    return this.cnkiPublicationDao.getCnkiPubExtendList(pubIdList);
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @Override
  public CnkiPubExtend getCnkiPubExtend(Long pubId) {
    return this.cnkiPublicationDao.getCnkiPubExtend(pubId);
  }

  /**
   * 获取待解析的成果ID列表.
   * 
   * @param minPubId 获取成果ID的最小值.
   * @param size 一次获取成果记录数.
   * @return
   */
  @Override
  public List<Long> getCnkiPubIdList(Long minPubId, int size) {
    return this.cnkiPublicationDao.getCnkiPubIdList(minPubId, size);
  }

  @Override
  public List<Long> getMissPubIdTask(Integer maxSize) {
    return cnkiPublicationDao.getMissPubIdTask(maxSize);
  }

  @Override
  public CnkiPubAssign getCnkiPubAssign(Long pubId, Long insId) throws ServiceException {

    CnkiPubAssign assignInfo = this.cnkiPubAssignDao.getCnkiPubAssign(pubId, insId);

    if (assignInfo != null) {

      if (pubId != null) {
        CnkiPubExtend xml = this.cnkiPubAssignDao.getCnkiPubExtend(pubId);
        assignInfo.setXmlData(xml.getXmlData());
      }
    }
    return assignInfo;
  }

  /**
   * 插入新基准库数据，如果查询到重复，更新资助基金信息.
   * 
   * @param pub
   * @throws ServiceException
   */
  private void refreshDupPub(CnkiPublication pub, Long pubId) throws ServiceException {

    try {
      CnkiPublication dupPub = this.cnkiPublicationDao.get(pubId);
      CnkiPubExtend extend = this.cnkiPublicationDao.getCnkiPubExtend(pubId);

      if (extend == null) {
        return;
      }

      ImportPubXmlDocument document = new ImportPubXmlDocument(extend.getXmlData());

      // 更新基金资助信息
      String fundInfo = pub.getFundInfo();
      document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "fundinfo", fundInfo);

      // 为空的话，更新cnki分类号
      String preCategoryNo =
          document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "journal_category_no");

      if (StringUtils.isBlank(preCategoryNo)) {
        String categoryNo = pub.getCategoryNo();
        document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "journal_category_no", categoryNo);
      }

      this.cnkiPublicationDao.saveCnkiPubExtend(pubId, document.getXmlString());
      this.cnkiPublicationDao.save(dupPub);
      // saveIsiPubSourceDb(pub.getDbId(), pubId);

    } catch (Exception e) {
      logger.error("插入新基准库数据，如果查询到重复，更新引用次数，收录情况", e);
      throw new ServiceException("插入新基准库数据，如果查询到重复，更新引用次数，收录情况", e);
    }
  }

  @Override
  public List<Long> getCnkiPubId(Long insId) {
    List<Long> list = this.cnkiPubAssignDao.getCnkiPubId(insId);
    return list;
  }

  @Override
  public void savePubAssignStatus(CnkiPubAssign pubAssign, Integer status) {
    pubAssign.setStatus(status);
    this.cnkiPubAssignDao.save(pubAssign);
  }

  @Override
  public void dealCnkiXml(String xml, Long pubid, Long insId) throws ServiceException {
    try {
      PubXmlDocument xmlDocument = new PubXmlDocument(xml);
      Element pubEle = (Element) xmlDocument.getNode(PubXmlConstants.PUBLICATION_XPATH);

      String zhAbstract =
          StringUtils.isNotBlank(pubEle.attributeValue("cabstract")) ? pubEle.attributeValue("cabstract").trim() : "";
      String enAbstract =
          StringUtils.isNotBlank(pubEle.attributeValue("eabstract")) ? pubEle.attributeValue("eabstract").trim() : "";
      String zhKw =
          StringUtils.isNotBlank(pubEle.attributeValue("ckeywords")) ? pubEle.attributeValue("ckeywords").trim() : "";
      String enKw =
          StringUtils.isNotBlank(pubEle.attributeValue("ekeywords")) ? pubEle.attributeValue("ekeywords").trim() : "";
      String journalName =
          StringUtils.isNotBlank(pubEle.attributeValue("original")) ? pubEle.attributeValue("original").trim() : "";
      String type =
          StringUtils.isNotBlank(pubEle.attributeValue("pub_type")) ? pubEle.attributeValue("pub_type").trim() : "";
      String year =
          StringUtils.isNotBlank(pubEle.attributeValue("pubyear")) ? pubEle.attributeValue("pubyear").trim() : "";
      String language = "";
      String authorName =
          StringUtils.isNotBlank(pubEle.attributeValue("author_names")) ? pubEle.attributeValue("author_names").trim()
              : "";
      String issn = StringUtils.isNotBlank(pubEle.attributeValue("issn")) ? pubEle.attributeValue("issn").trim() : "";
      String vol =
          StringUtils.isNotBlank(pubEle.attributeValue("volume")) ? pubEle.attributeValue("volume").trim() : "";
      String sPage =
          StringUtils.isNotBlank(pubEle.attributeValue("start_page")) ? pubEle.attributeValue("start_page").trim() : "";
      String ePage =
          StringUtils.isNotBlank(pubEle.attributeValue("end_page")) ? pubEle.attributeValue("end_page").trim() : "";
      String doi = StringUtils.isNotBlank(pubEle.attributeValue("doi")) ? pubEle.attributeValue("doi").trim() : "";
      String fundInfo =
          StringUtils.isNotBlank(pubEle.attributeValue("fundinfo")) ? pubEle.attributeValue("fundinfo").trim() : "";
      String zTitle =
          StringUtils.isNotBlank(pubEle.attributeValue("ctitle")) ? pubEle.attributeValue("ctitle").trim() : "";
      String eTitle =
          StringUtils.isNotBlank(pubEle.attributeValue("etitle")) ? pubEle.attributeValue("etitle").trim() : "";
      String issue =
          StringUtils.isNotBlank(pubEle.attributeValue("issue")) ? pubEle.attributeValue("issue").trim() : "";

      zhAbstract = zhAbstract.length() > 3000 ? zhAbstract.substring(0, 3000) : zhAbstract;
      enAbstract = enAbstract.length() > 3000 ? enAbstract.substring(0, 3000) : enAbstract;
      zhKw = zhKw.length() > 1000 ? zhKw.substring(0, 1000) : zhKw;
      enKw = enKw.length() > 1000 ? enKw.substring(0, 1000) : enKw;
      authorName = authorName.length() > 100 ? authorName.substring(0, 100) : authorName;

      if ("4".equals(type)) {
        type = "8";
      } else if ("5".equals(type)) {
        type = "1";
      } else {
        type = "9";
      }

      if (StringUtils.isNotEmpty(zTitle)) {
        language = "Zh";
      } else {
        language = "En";
      }

      PubFundInfoPdwh pub = new PubFundInfoPdwh();

      pub.setAuthorNames(authorName);
      pub.setDoi(doi);
      pub.setEnAbstract(enAbstract);
      pub.setEndPage(ePage);
      pub.setEnKeywords(enKw);
      pub.setEnTitle(eTitle);
      pub.setFundInfo(fundInfo);
      pub.setInsId(insId);
      pub.setIssn(issn);
      pub.setIssue(issue);
      pub.setJournalName(journalName);
      pub.setPubId(pubid);
      pub.setPubLanguage(language);
      pub.setPublishYear(year);
      pub.setResultType(type);
      pub.setStartPage(sPage);
      pub.setVolume(vol);
      pub.setZhAbstract(zhAbstract);
      pub.setZhKeywords(zhKw);
      pub.setZhTitle(zTitle);
      this.pubFundInfoDao.save(pub);

    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

}
