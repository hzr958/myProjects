package com.smate.center.batch.service.pdwh.isipub;

import com.smate.center.batch.constant.DbSourceConst;
import com.smate.center.batch.constant.ImportPubXmlConstants;
import com.smate.center.batch.dao.pdwh.pub.PubAllIdsSyncDao;
import com.smate.center.batch.dao.pdwh.pub.isi.*;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.isi.*;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pdwh.pub.PublicationPdwhService;
import com.smate.center.batch.service.pdwh.pubmatch.PubExpandCommonService;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.mq.IsiPubCacheAssignProducer;
import com.smate.center.batch.service.pub.mq.PubSyncToPubFtSrvProducer;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * isi基准库成果服务.
 * 
 * @author liqinghua
 * 
 */
@Service("isiPublicationService")
@Transactional(rollbackFor = Exception.class)
public class IsiPublicationServiceImpl implements IsiPublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = 5661992852411986526L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IsiPublicationDao isiPublicationDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private IsiPubInsMatchService isiPubInsMatchService;
  @Autowired
  private IsiPubAddrDao isiPubAddrDao;
  @Autowired
  private IsiPubAssignDao isiPubAssignDao;
  @Autowired
  private IsiPubCacheAssignProducer isiPubCacheAssignProducer;
  @Autowired
  private IsiPubKeywordsDao isiPubKeywordsDao;
  @Autowired
  private IsiPubKeywordsSplitDao isiPubKeywordsSplitDao;
  @Autowired
  private PubAllIdsSyncDao pubAllIdsSyncDao;
  @Autowired
  private PubExpandCommonService pubExpandCommonService;
  @Autowired
  private IsiPublicationSyncDao isiPublicationSyncDao;

  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private PubSyncToPubFtSrvProducer pubSyncToPubFtSrvProducer;

  @Override
  public Long getPubPdwhIdBySourceId(String sourceId) throws ServiceException {
    try {
      return this.isiPublicationDao.queryPubPdwhIdBySourceId(sourceId);
    } catch (Exception e) {
      LogUtils.error(logger, e, "根据sourceId={}查找成果基准库id出现异常", sourceId);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getDupPub(Long sourceIdHash, Long titleHash, Long unitHash) throws ServiceException {

    try {
      IsiPubDup pubDup = isiPublicationDao.getIsiPubDup(sourceIdHash, titleHash, unitHash);
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

    IsiPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getSourceIdHash(), pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(new Date());
      // 增加到基准库
      this.addPub(dataXml, pub);
      // 匹配单位
      Integer match = isiPubInsMatchService.matchPubCache(insId, pub.getPubId(), pub.getPubAddrs());
      // 保存至BatchJobs
      if (match == 1) {
        publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.ISI);
      }
    } else {
      // 更新引用次数，收录情况,资助基金, 作者信息
      this.refreshDupPub(pub, dataXml, dupId);
      // 匹配单位
      Integer match = isiPubInsMatchService.matchPubCache(insId, dupId);
      // 保存至BatchJobs
      if (match == 1) {
        publicationPdwhService.saveToBatchJobs(dupId, insId, DbSourceConst.ISI);
      }
    }
  }

  @Override
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException {

    IsiPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getSourceIdHash(), pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(fetchTime);
      pub.setCreatePsn(psnId);
      pub.setCreateIns(insId);
      // 增加到基准库
      this.addPub(dataXml, pub);
    } else {
      // 更新引用次数，收录情况
      this.refreshDupPub(pub, dataXml, dupId);
    }
  }

  /**
   * 增加文献到基准库.
   * 
   * @param dataXml
   * @param pub
   * @throws ServiceException
   */
  private void addPub(String dataXml, IsiPublication pub) throws ServiceException {

    try {
      // 先保存
      this.isiPublicationDao.save(pub);
      // 保存查重数据
      this.isiPublicationDao.saveIsiPubDup(pub.getPubId(), pub.getSourceIdHash(), pub.getTitleHash(),
          pub.getUnitHash());
      // 保存收录情况
      saveIsiPubSourceDb(pub.getDbId(), pub.getPubId());
      // 保存关键词
      saveKeywords(pub);
      // 保存Xml
      this.isiPublicationDao.saveIsiPubExtend(pub.getPubId(), dataXml);
      // 保存到同步puball的任务表
      this.pubAllIdsSyncDao.savePubAlldsSync(pub.getPubId(), pub.getDbId());
      // 解析成果地址
      String orgsStr = pub.getOrganization();
      if (StringUtils.isNotBlank(orgsStr)) {
        List<IsiPubAddr> pubAddrs = new ArrayList<IsiPubAddr>();
        // 获取机构名称列表，构造IsiPubCacheOrgs对象
        Set<String> pubAddrSet = XmlUtil.parseIsiPubAddrs(orgsStr);
        for (String pubAddr : pubAddrSet) {
          IsiPubAddr cco = new IsiPubAddr(pub.getPubId(), StringUtils.substring(pubAddr, 0, 500),
              PubHashUtils.cleanPubAddrHash(pubAddr));
          // 成果拆分地址不可为空
          if (cco.getAddr() != null && cco.getAddrHash() != null) {
            this.isiPubAddrDao.save(cco);
            pubAddrs.add(cco);
          }
        }
        pub.setPubAddrs(pubAddrs);
      }
      // 增加保存成果拆分日志记录,为成果拆分数据任务提供数据来源_MJG_IsiPubExpandTask成果拆分任务_2014-01-23.
      IsiPubExpandLog pubLog = new IsiPubExpandLog();
      pubLog.setPubId(pub.getPubId());
      pubLog.setStatus(0);// 0-待处理.
      pubExpandCommonService.saveIsiPubExpandLog(pubLog);

      this.pubSyncToPubFtSrvProducer.sendUpdatePubMessage(this.getPubSyncPubFtSrvData(pub));
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
  private void saveKeywords(IsiPublication pub) throws ServiceException {
    try {
      String zhKeywords = pub.getZhKeywords();
      String enKeywords = pub.getEnKeywords();

      if (StringUtils.isNotBlank(zhKeywords)) {
        this.isiPubKeywordsDao.save(new IsiPubKeywords(pub.getPubId(), zhKeywords, 2));
        this.saveKeywordsSplit(pub.getPubId(), zhKeywords, 2);
      }
      if (StringUtils.isNotBlank(enKeywords)) {
        this.isiPubKeywordsDao.save(new IsiPubKeywords(pub.getPubId(), enKeywords, 1));
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
        this.isiPubKeywordsSplitDao.save(
            new IsiPubKeywordsSplit(pubId, keyword, keyword.toLowerCase(), PubHashUtils.getKeywordHash(keyword), type));
      }
    } catch (Exception e) {
      logger.error("saveKeywordsSplit保存关键词", e);
      throw new ServiceException("saveKeywordsSplit保存关键词", e);
    }
  }

  /**
   * 插入新基准库数据，如果查询到重复，更新引用次数，收录情况，资助基金信息.
   * 
   * @param pub
   * @throws ServiceException
   */
  private void refreshDupPub(IsiPublication pub, String newXmlString, Long pubId) throws ServiceException {

    try {
      IsiPublication dupPub = this.isiPublicationDao.get(pubId);
      IsiPubExtend extend = this.isiPublicationDao.getIsiPubExtend(pubId);
      if (extend == null) {
        return;
      }
      ImportPubXmlDocument document = new ImportPubXmlDocument(extend.getXmlData());
      // 更新引用次数
      if (pub.getCitedTimes() != null && pub.getCitedTimes() > 0) {
        Integer dupCitedTimes = dupPub.getCitedTimes();
        // 当数据库中重复成果引用次数不为空，且比新导入成果xml中引用次数小；或者为空时，更新引用次数
        if ((dupCitedTimes != null && pub.getCitedTimes() > dupCitedTimes) || dupCitedTimes == null) {

          dupPub.setCitedTimes(pub.getCitedTimes());
          document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "cite_times",
              pub.getCitedTimes().toString());
          dupPub.setCitedTimes(pub.getCitedTimes());
        }
      }

      // 更新基金资助信息
      String fundInfo = pub.getFundInfo();
      document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "fundinfo", fundInfo);

      String sc = document.getScienceCategory();
      if (StringUtils.isEmpty(sc)) {
        String scienceCategory = pub.getScienceCategory();
        if (StringUtils.isNotEmpty(scienceCategory)) {
          document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "sc", scienceCategory);
        }
      }

      // 更新所有作者信息
      if (StringUtils.isNotEmpty(newXmlString)) {
        ImportPubXmlDocument newDoc = new ImportPubXmlDocument(newXmlString);
        List authorList = newDoc.getPubAuthorList();
        if (authorList != null && authorList.size() > 0) {
          document.removeNode("/pub_authors");
          Element authorEles = document.createElement("/pub_authors");
          for (int i = 0; i < authorList.size(); i++) {
            Element newAuthor = authorEles.addElement("author");
            Element author = (Element) authorList.get(i);
            document.copyPubElement(newAuthor, author);
          }
        }
      }

      this.isiPublicationDao.saveIsiPubExtend(pubId, document.getXmlString());
      this.isiPublicationDao.save(dupPub);
      saveIsiPubSourceDb(pub.getDbId(), pubId);

      // 更新作者信息

    } catch (Exception e) {
      logger.error("插入新基准库数据，如果查询到重复，更新引用次数，收录情况", e);
      throw new ServiceException("插入新基准库数据，如果查询到重复，更新引用次数，收录情况", e);
    }
  }

  /**
   * 根据成果ID获取基准库ISI成果表中的成果记录.
   * 
   * @param pubId
   * @return
   */
  @Override
  public IsiPubExtend getIsiPubExtend(Long pubId) {
    return this.isiPublicationDao.getIsiPubExtend(pubId);
  }

  /**
   * 保存收录情况.
   * 
   * @param pub
   * @param pubId
   */
  private void saveIsiPubSourceDb(Integer dbId, Long pubId) {
    // 更新收录情况
    int sci = PubXmlConstants.SOURCE_DBID_SCI_INT.equals(dbId) ? 1 : 0;
    int ssci = PubXmlConstants.SOURCE_DBID_SSCI_INT.equals(dbId) ? 1 : 0;
    int istp = PubXmlConstants.SOURCE_DBID_ISTP_INT.equals(dbId) ? 1 : 0;
    this.isiPublicationDao.saveIsiPubSourceDb(pubId, sci, ssci, istp);
  }

  /**
   * 解析成果Xml到IsiPublication封装.
   * 
   * @param dataXml
   * @return
   * @throws ServiceException
   */
  private IsiPublication praseDataXmlToPub(String dataXml) throws ServiceException {

    Map<String, Object> map = praseDataXml(dataXml);
    IsiPublication pub = new IsiPublication();

    pub.setPubYear((Integer) map.get("pubYear"));
    pub.setDbId((Integer) map.get("dbId"));
    pub.setSourceId(StringUtils.substring((String) map.get("sourceId"), 0, 100));
    pub.setDoi(StringUtils.substring((String) map.get("doi"), 0, 100));
    pub.setTitle(StringUtils.substring((String) map.get("enTitle"), 0, 500));
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
    pub.setCitedTimes((Integer) map.get("citeTimes"));
    // 必须使用原始字符串计算hash，截断的不算
    Long sourceIdHash = PubHashUtils.cleanSourceIdHash((String) map.get("sourceId"));
    pub.setSourceIdHash(sourceIdHash);
    Long titleHash = PubHashUtils.cleanTitleHash((String) map.get("enTitle"));
    pub.setTitleHash(titleHash);
    Long unitHash = PubHashUtils.getEnPubUnitFingerPrint((Integer) map.get("pubYear"), (String) map.get("original"),
        (String) map.get("authorNames"));
    pub.setUnitHash(unitHash);
    pub.setZhKeywords(StringUtils.substring((String) map.get("zhKeywords"), 0, 2000));
    pub.setEnKeywords(StringUtils.substring((String) map.get("enKeywords"), 0, 2000));
    pub.setFundInfo(StringUtils.substring((String) map.get("fundInfo"), 0, 1000));
    pub.setScienceCategory(StringUtils.substring((String) map.get("sc"), 0, 200));
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
  public List<IsiPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException {
    try {

      List<IsiPubAssign> list = this.isiPubAssignDao.getNeedSendPub(startId, size);
      if (CollectionUtils.isEmpty(list)) {
        return list;
      }
      // 获取XML数据
      List<Long> pubIds = new ArrayList<Long>();
      for (IsiPubAssign assign : list) {
        // 状态为1的才需要xml数据
        if (assign.getResult() == 1) {
          pubIds.add(assign.getPubId());
        }
      }
      if (pubIds.size() > 0) {
        List<IsiPubExtend> xmlList = this.isiPublicationDao.getIsiPubExtends(pubIds);
        for (IsiPubExtend xml : xmlList) {
          for (IsiPubAssign assign : list) {
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
  public void sendInsPub(IsiPubAssign pubassign) throws ServiceException {
    try {
      if (pubassign.getInsId() != null) {
        isiPubCacheAssignProducer.sendAssignMsg(pubassign.getPubId(), pubassign.getXmlData(), pubassign.getInsId(),
            (pubassign.getResult() == 1 ? 1 : 2));
      }
      pubassign.setIsSend(1);
      this.isiPubAssignDao.save(pubassign);
    } catch (Exception e) {
      logger.error("发送单位成果", e);
      throw new ServiceException("发送单位成果", e);
    }
  }

  /**
   * 获取待解析的成果列表.
   * 
   * @param minPubId 获取成果ID的最小值.
   * @param size 一次获取成果记录数.
   * @return
   */
  @Override
  public List<IsiPubExtend> getIsiPubExtendList(List<Long> pubIdList) {
    return this.isiPublicationDao.getIsiPubExtendList(pubIdList);
  }

  /**
   * 获取待处理的成果列表.
   * 
   * @param minPubId
   * @param size
   * @return
   */
  @Override
  public List<Long> getIsiPubIdList(Long minPubId, int size) {
    return this.isiPublicationDao.getIsiPubIdList(minPubId, size);
  }

  /**
   * 获取遗漏未拆分的成果任务列表.
   * 
   * @param maxSize
   * @return
   */
  @Override
  public List<Long> getMissPubIdTask(Integer maxSize) {
    return this.isiPublicationDao.getMissPubIdTask(maxSize);
  }

  private Map<String, Object> getPubSyncPubFtSrvData(IsiPublication pub) throws ServiceException {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("pubId", pub.getPubId());
    map.put("pubType", pub.getPubType());
    map.put("title", pub.getTitle());
    map.put("titleHash", HashUtils.getStrHashCode(pub.getTitle()));
    map.put("issn", pub.getIssn());
    map.put("confName", pub.getConfName());
    map.put("sourceId", pub.getSourceId());
    return map;
  }

  @Override
  public List<Long> getNeedSyncIsiPub(int maxSize) throws ServiceException {

    try {
      return this.isiPublicationSyncDao.queryNeedSyncData(maxSize);
    } catch (Exception e) {
      LogUtils.error(logger, e, "查询需要同步到pubftsrv的isi成果");
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncIsiPub(List<Long> pubIdList) throws ServiceException {

    try {
      List<IsiPublication> list = this.isiPublicationDao.queryIsiPublicationList(pubIdList);
      if (CollectionUtils.isNotEmpty(list)) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (IsiPublication pub : list) {
          map = this.getPubSyncPubFtSrvData(pub);
          if (MapUtils.isNotEmpty(map)) {
            listMap.add(map);
          }
        }
        if (CollectionUtils.isNotEmpty(listMap)) {
          this.pubSyncToPubFtSrvProducer.sendUpdatePubMessage(listMap);
        }
      }
      this.isiPublicationSyncDao.deleteIsiPublicationSync(pubIdList);
    } catch (Exception e) {
      LogUtils.error(logger, e, "冗余ISI成果信息到pubftsrv出现异常");
      throw new ServiceException(e);
    }
  }

  @Override
  public IsiPubAssign getIsiPubAssign(Long pubId, Long insId) throws ServiceException {

    IsiPubAssign assignInfo = this.isiPubAssignDao.getIsiPubAssign(pubId, insId);

    if (assignInfo != null) {
      if (pubId != null) {
        IsiPubExtend xml = this.isiPublicationDao.getIsiPubExtend(pubId);
        assignInfo.setXmlData(xml.getXmlData());
      }
    }
    return assignInfo;
  }

  @Override
  public List<Long> getIsiPubId(Long insId) {
    List<Long> list = this.isiPubAssignDao.getIsiPubId(insId);
    return list;
  }

  @Override
  public void savePubAssignStatus(IsiPubAssign pubAssign, Integer status) {
    pubAssign.setStatus(status);
    this.isiPubAssignDao.save(pubAssign);
  }
}
