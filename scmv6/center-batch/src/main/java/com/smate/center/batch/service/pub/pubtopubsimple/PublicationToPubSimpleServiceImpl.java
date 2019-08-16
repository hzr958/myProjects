package com.smate.center.batch.service.pub.pubtopubsimple;

import com.smate.center.batch.dao.pdwh.pub.JournalDao;
import com.smate.center.batch.dao.pub.pubtopubsimple.PubToPubSimpleErrorLogDao;
import com.smate.center.batch.dao.pub.pubtopubsimple.PubToPubSimpleIntermediateDao;
import com.smate.center.batch.dao.pub.pubtopubsimple.TmpPubDao;
import com.smate.center.batch.dao.pub.pubtopubsimple.TmpPubIdDao;
import com.smate.center.batch.dao.sns.pub.*;
import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.pub.pubtopubsimple.PubToPubSimpleErrorLog;
import com.smate.center.batch.model.pub.pubtopubsimple.PubToPubSimpleIntermediate;
import com.smate.center.batch.model.pub.pubtopubsimple.TmpPub;
import com.smate.center.batch.model.pub.pubtopubsimple.TmpPubId;
import com.smate.center.batch.model.sns.pub.*;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.util.pub.PubXmlObjectUtil;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 成果的信息添加到pubsimple新增的相关表中
 * 
 * @author lxz
 *
 */
@Service("publicationToPubSimpleService")
@Transactional(rollbackFor = Exception.class)
public class PublicationToPubSimpleServiceImpl implements PublicationToPubSimpleService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubSimpleHashDao pubSimpleHashDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private PubToPubSimpleErrorLogDao pubToPubSimpleErrorLogdao;
  @Autowired
  private PubSimpleFromPubDao pubSimpleFromPubDao;
  @Autowired
  private PubSimpleSaveBatchService pubSimpleSaveBatchService;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubToPubSimpleIntermediateDao pubToPubSimpleIntermediateDao;
  @Autowired
  private TmpPubIdDao tmpPubIdDao;
  @Autowired
  private TmpPubDao tmpPubDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private JournalDao journalDao;

  public Long savePubSimpleData(Publication pub, PubXmlDocument doc) throws Exception {
    // 1,查找pubsimple
    PubSimple pubSimple = new PubSimple();
    PubSimpleHash pubSimpleHash = new PubSimpleHash();
    pubSimple = convertToPubSimple(pub, pubSimple);
    pubSimple.setOwnerPsnId(doc.getExpandPsnId());
    pubSimpleHash = convertToPubSimpleHash(pub, pubSimpleHash);
    PubDataStore pubDataStore = new PubDataStore();
    // 保存v_pub_simple
    pubSimpleDao.save(pubSimple);
    // 保存v_pub_simple_hash
    pubSimpleHashDao.save(pubSimpleHash);
    pubDataStore.setPubId(pubSimple.getPubId());
    // 为导入的pubxml加入meta pubid；老表中这个属性可能为空，会导致后续的可能任务报错
    doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", String.valueOf(pubSimple.getPubId()));
    PubXmlObjectUtil.getPubDataStoreFromXml(doc.getXmlString(), pubDataStore);

    // 保存v_pub_data_store
    pubDataStoreDao.save(pubDataStore);
    // 保存导入任务
    pubSimpleSaveBatchService.savePubImportBatch(pubSimple.getPubId(), pubSimple.getSimpleVersion());
    return pubSimple.getPubId();
  }

  @Override
  public List<Long> getPubsByPsnId(Long pubId) {

    return pubSimpleDao.getPubIdsByPsnId(pubId);
  }

  @Override
  public void copyPubSimpleData(Publication pub, ScmPubXml scmPubXml) throws Exception {
    // 1,查找pubsimple
    Long pubId = pub.getId();
    try {

      String xml = scmPubXml.getPubXml();
      PubXmlDocument doc = new PubXmlDocument(xml);
      PubSimpleFromPub pubSimple = new PubSimpleFromPub();
      PubSimpleHash pubSimpleHash = new PubSimpleHash();
      if (pub.getPsnId() == null) {
        throw new Exception("成果ownPsnId为空！");
      }
      pubSimple = this.constructPubSimpleFromPulication(pub, pubSimple);
      pubSimpleHash = convertToPubSimpleHash(pub, pubSimpleHash);
      PubDataStore pubDataStore = new PubDataStore();

      // 保存v_pub_simple,不使用pubsimpledao,主键已经有id，使用序列会导致无法插入表中
      pubSimpleFromPubDao.save(pubSimple);
      // 保存v_pub_simple_hash
      pubSimpleHashDao.save(pubSimpleHash);
      pubDataStore.setPubId(pubSimple.getPubId());
      // 为导入的pubxml加入meta pubid；老表中这个属性可能为空，会导致后续的可能任务报错
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", String.valueOf(pubSimple.getPubId()));
      PubXmlObjectUtil.getPubDataStoreFromXml(doc.getXmlString(), pubDataStore);

      // 保存v_pub_data_store
      pubDataStoreDao.save(pubDataStore);
      // 保存导入任务
      // pubSimpleSaveBatchService.savePubImportBatch(pubSimple.getPubId(),
      // pubSimple.getSimpleVersion());
      // return pubSimple.getPubId();
    } catch (Exception e) {

      PubToPubSimpleErrorLog error = new PubToPubSimpleErrorLog();
      error.setPubId(pubId);
      String errorMsg = e.toString();
      error.setErrorMsg(errorMsg);
      this.saveError(error);
      logger.debug("BatchPubToPubSimpleTask出错==============", e);

    }
  }

  @Override
  public void copyPubXmlToDataStore(ScmPubXml scmPubXml) {
    if (scmPubXml == null || StringUtils.isEmpty(scmPubXml.getPubXml())) {
      return;
    }
    Long pubId = scmPubXml.getPubId();
    try {
      String xmlString = scmPubXml.getPubXml();
      PubXmlDocument doc = new PubXmlDocument(xmlString);
      Element ele = (Element) doc.getNode(PubXmlConstants.PUB_META_XPATH);
      if (ele == null) {
        doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", String.valueOf(pubId));
      }
      PubDataStore pubDataStore = pubDataStoreDao.get(pubId);
      if (pubDataStore == null) {
        pubDataStore = new PubDataStore();
        pubDataStore.setPubId(pubId);
      }
      PubXmlObjectUtil.getPubDataStoreFromXml(doc.getXmlString(), pubDataStore);
      pubDataStoreDao.save(pubDataStore);
    } catch (Exception e) {
      PubToPubSimpleErrorLog error = new PubToPubSimpleErrorLog();
      error.setPubId(pubId);
      String errorMsg = e.toString();
      error.setErrorMsg(errorMsg);
      this.saveError(error);
      logger.debug("copyPubXmlToDataStore出错==========", e);
    }

  }

  /**
   * 构造PubSimple
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pub
   * @param pubSimple
   * @return
   * @throws Exception
   */
  private PubSimple convertToPubSimple(Publication pub, PubSimple pubSimple) throws Exception {
    // 转换数据
    PropertyUtils.copyProperties(pubSimple, pub);
    // 初始化数据
    pubSimple.setPubId(null);
    pubSimple.setSimpleStatus(0L);
    pubSimple.setSimpleTask(0);
    pubSimple.setSimpleVersion(new Date().getTime());
    pubSimple.setCreateDate(new Date());
    // 返回
    return pubSimple;
  }

  /**
   * 构造PubSimpleHash
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pub
   * @param pubSimpleHash
   * @return
   */
  private PubSimpleHash convertToPubSimpleHash(Publication pub, PubSimpleHash pubSimpleHash) {
    // 转换数据
    // String zhHashCode =
    // PubHashUtils.getTitleInfoHash(pub.getZhTitle(),pub.getTypeId(),pub.getPublishYear());
    // String enHashCode =
    // PubHashUtils.getTitleInfoHash(pub.getEnTitle(),pub.getTypeId(),pub.getPublishYear());
    // pubSimpleHash.setPubId(pub.getId());
    // pubSimpleHash.setZhHashCode(zhHashCode);
    // pubSimpleHash.setEnHashCode(enHashCode);
    // 查重规则有更改
    String zhTitle = pub.getZhTitle();
    String enTitle = pub.getEnTitle();
    String pubType = pub.getTypeId() == null ? "" : pub.getTypeId() + "";
    String pubYear = pub.getPublishYear() == null ? "" : pub.getPublishYear() + "";
    String tpHashZh = PubHashUtils.getTpHash(zhTitle, pubType);
    String tpHashEn = PubHashUtils.getTpHash(enTitle, pubType);
    String zhHashCode = PubHashUtils.getTitleInfoHash(zhTitle, pubType, pubYear);
    String enHashCode = PubHashUtils.getTitleInfoHash(enTitle, pubType, pubYear);
    pubSimpleHash.setPubId(pub.getId());
    pubSimpleHash.setTpHashZh(tpHashZh);
    pubSimpleHash.setTpHashEn(tpHashEn);
    pubSimpleHash.setZhHashCode(zhHashCode);
    pubSimpleHash.setEnHashCode(enHashCode);
    // 返回
    return pubSimpleHash;
  }

  @Override
  public List<Long> getSnsPublication(Integer size, Long lastPubId, Long startPubId, Long endPubId) {

    List<Long> pubList = this.pubSimpleDao.getPublicationFromSns(size, startPubId, endPubId);

    return pubList;
  }

  @Override
  public List<Long> getSnsPubSimpleIds(Integer size, Long startPubId, Long endPubId) {

    List<Long> pubList = this.pubSimpleDao.getPubSimpleIds(size, startPubId, endPubId);

    return pubList;
  }

  @Override
  public void saveError(PubToPubSimpleErrorLog pubToPubSimpleErrorLog) {

    if (pubToPubSimpleErrorLog == null) {
      return;
    }

    String errorMsg = pubToPubSimpleErrorLog.getErrorMsg();

    if (StringUtils.isNotBlank(errorMsg)) {
      errorMsg = errorMsg.length() > 200 ? errorMsg.substring(0, 200) : errorMsg;
    } else {
      errorMsg = "Unknown Error!";
    }

    pubToPubSimpleErrorLog.setErrorMsg(errorMsg);

    this.pubToPubSimpleErrorLogdao.save(pubToPubSimpleErrorLog);
  }

  public PubSimpleFromPub constructPubSimpleFromPulication(Publication pub, PubSimpleFromPub pubSimple) {

    pubSimple.setPubId(pub.getId());
    pubSimple.setOwnerPsnId(pub.getPsnId());
    pubSimple.setArticleType(pub.getArticleType());
    pubSimple.setSourceDbId(pub.getSourceDbId());
    pubSimple.setPubType(pub.getTypeId());
    pubSimple.setCreateDate(pub.getCreateDate());
    pubSimple.setUpdateDate(pub.getUpdateDate());
    pubSimple.setPublishYear(pub.getPublishYear());
    pubSimple.setPublishMonth(pub.getPublishMonth());
    pubSimple.setPublishDay(pub.getPublishDay());
    pubSimple.setImpactFactors(pub.getImpactFactors());
    if (pub.getCitedTimes() != null) {
      pubSimple.setCitedTimes(Long.parseLong(Integer.toString(pub.getCitedTimes())));
    }
    pubSimple.setZhTitle(pub.getZhTitle());
    pubSimple.setEnTitle(pub.getEnTitle());
    pubSimple.setAuthorNames(pub.getAuthorNames());
    pubSimple.setBriefDesc(pub.getBriefDesc());
    pubSimple.setFullTextField(pub.getFulltextFileid());
    pubSimple.setStatus(pub.getStatus());
    pubSimple.setFullTextNodeId(pub.getFulltextNodeId());
    pubSimple.setFullTextFileExt(pub.getFulltextExt());
    pubSimple.setImpactFactorSort(pub.getImpactFactorsSort());
    pubSimple.setBriefDescEn(pub.getBriefDescEn());
    pubSimple.setSimpleStatus(1L);
    pubSimple.setSimpleTask(1);
    pubSimple.setSimpleVersion(new Date().getTime());

    return pubSimple;
  }

  @Override
  public Publication getSnsPublicationById(Long pubId) {

    return this.publicationDao.get(pubId);
  }

  @Override
  public List<Long> getPubIdList(Integer size) {

    List<Long> pubIdList = this.pubToPubSimpleIntermediateDao.getPubIdList(size);

    return pubIdList;
  }

  @Override
  public void savePubToPubSimpleIntermediate(Long pubId, Integer status) {

    PubToPubSimpleIntermediate single = this.pubToPubSimpleIntermediateDao.get(pubId);
    if (single != null) {
      single.setStatus(status);
    }
    this.pubToPubSimpleIntermediateDao.save(single);
  }

  @Override
  public void constructTmpPub() {

    List<TmpPubId> list = tmpPubIdDao.getAll();

    if (CollectionUtils.isEmpty(list))
      return;

    for (TmpPubId one : list) {
      Long pubId = one.getPubId();
      try {

        Publication publication = publicationDao.get(pubId);
        if (publication == null) {
          return;
        }

        ConstPubType constPubType = constPubTypeDao.get(publication.getTypeId());
        String pubTypeName = "";
        if (constPubType != null) {
          pubTypeName =
              StringUtils.isNotBlank(constPubType.getEnName()) ? constPubType.getEnName() : constPubType.getZhName();;
        }

        String issn = "";
        String enjournalName = "";
        String zhjournalName = "";

        if (publication.getJid() != null) {
          Journal journal = journalDao.findJournal(publication.getJid());
          if (journal != null) {
            issn = journal.getIssn();
            enjournalName = journal.getEnName();
            zhjournalName = journal.getZhName();
          }
        }
        TmpPub pub = new TmpPub();
        pub.setPubId(pubId);
        String enAbstract = "";
        String zhAbstract = "";
        String keywords = "";

        ScmPubXml xml = scmPubXmlDao.get(pubId);
        if (xml != null) {
          PubXmlDocument oldDoc = new PubXmlDocument(xml.getPubXml());
          // 全文ID
          enAbstract = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
          zhAbstract = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
          String enkeywords = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
          String zhkeywords = oldDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
          keywords = StringUtils.isNotBlank(enkeywords) ? enkeywords : zhkeywords;
        }

        pub.setAuthorNames(publication.getAuthorNames());
        pub.setCitedTimes(publication.getCitedTimes());
        pub.setEnAbstract(enAbstract);
        pub.setZhAbstract(zhAbstract);
        pub.setEnName(enjournalName);
        pub.setZhName(zhjournalName);
        pub.setIssn(issn);
        pub.setKeyWords(keywords);
        pub.setOwnerPsnId(publication.getPsnId());
        pub.setPublishYear(publication.getPublishYear());
        pub.setPubType(publication.getTypeId());
        pub.setPubTypeName(pubTypeName);
        pub.setTitle(
            StringUtils.isNotBlank(publication.getEnTitle()) ? publication.getEnTitle() : publication.getZhTitle());
        tmpPubDao.save(pub);
        one.setStatus(3);
        tmpPubIdDao.save(one);
      } catch (Exception e) {
        one.setStatus(9);
        tmpPubIdDao.save(one);
        logger.debug("获取成果xml出错==============", e);
      }
    }

    logger.debug("成果获取完成！！！！====");
  }
}
