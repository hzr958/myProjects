package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.constant.DbSourceConst;
import com.smate.center.batch.dao.pdwh.pub.PubAllIdsSyncDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.sps.*;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.mq.SpsPubCacheAssignProducer;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * scopus基准库成果服务.
 * 
 * @author liqinghua
 * 
 */
@Service("spsPublicationService")
@Transactional(rollbackFor = Exception.class)
public class SpsPublicationServiceImpl implements SpsPublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = 5661992852411986526L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SpsPublicationDao spsPublicationDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private SpsPubInsMatchService spsPubInsMatchService;
  @Autowired
  private SpsPubAddrDao spsPubAddrDao;
  @Autowired
  private SpsPubAssignDao spsPubAssignDao;
  @Autowired
  private SpsPubCacheAssignProducer spsPubCacheAssignProducer;
  @Autowired
  private PubAllIdsSyncDao pubAllIdsSyncDao;
  @Autowired
  private PublicationPdwhService publicationPdwhService;

  @Override
  public Long getDupPub(Long sourceIdHash, Long titleHash, Long unitHash) throws ServiceException {

    try {
      SpsPubDup pubDup = spsPublicationDao.getSpsPubDup(sourceIdHash, titleHash, unitHash);
      if (pubDup != null) {
        return pubDup.getPubId();
      }
      return null;
    } catch (Exception e) {
      logger.error("查询重复基准库成果", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void addDbCachePub(Long insId, String dataXml) throws ServiceException {

    SpsPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getSourceIdHash(), pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(new Date());
      // 增加到基准库
      this.addPub(dataXml, pub);
      // 匹配单位
      spsPubInsMatchService.matchPubCache(insId, pub.getPubId(), pub.getPubAddrs());
      // 保存至BatchJobs
      publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.SCOPUS);
    } else {
      // 匹配单位
      spsPubInsMatchService.matchPubCache(insId, dupId);
      // 保存至BatchJobs
      publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.SCOPUS);
    }
  }

  @Override
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException {

    SpsPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getSourceIdHash(), pub.getTitleHash(), pub.getUnitHash());
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
  private void addPub(String dataXml, SpsPublication pub) throws ServiceException {

    try {
      // 先保存
      this.spsPublicationDao.save(pub);
      // 保存查重数据
      this.spsPublicationDao.saveSpsPubDup(pub.getPubId(), pub.getSourceIdHash(), pub.getTitleHash(),
          pub.getUnitHash());
      // 保存Xml
      this.spsPublicationDao.saveSpsPubExtend(pub.getPubId(), dataXml);
      // 保存到同步puball的任务表
      this.pubAllIdsSyncDao.savePubAlldsSync(pub.getPubId(), pub.getDbId());
      // 解析成果地址
      String orgsStr = pub.getOrganization();
      if (StringUtils.isNotBlank(orgsStr)) {
        List<SpsPubAddr> pubAddrs = new ArrayList<SpsPubAddr>();
        // 获取机构名称列表，构造IsiPubCacheOrgs对象
        Set<String> pubAddrSet = XmlUtil.parseScopusPubAddrs(orgsStr);
        for (String pubAddr : pubAddrSet) {
          SpsPubAddr cco = new SpsPubAddr(pub.getPubId(), StringUtils.substring(pubAddr, 0, 500),
              PubHashUtils.cleanPubAddrHash(pubAddr));
          // scopus成果地址拆分的地址不可为空
          if (cco.getAddrHash() != null) {
            this.spsPubAddrDao.save(cco);
            pubAddrs.add(cco);
          }
        }
        pub.setPubAddrs(pubAddrs);
      }
    } catch (Exception e) {
      logger.error("增加文献到基准库", e);
      throw new ServiceException("增加文献到基准库", e);
    }
  }

  /**
   * 解析成果Xml到IsiPublication封装.
   * 
   * @param dataXml
   * @return
   * @throws ServiceException
   */
  private SpsPublication praseDataXmlToPub(String dataXml) throws ServiceException {

    Map<String, Object> map = praseDataXml(dataXml);
    SpsPublication pub = new SpsPublication();

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
    // 必须使用原始字符串计算hash，截断的不算
    Long sourceIdHash = PubHashUtils.cleanSourceIdHash((String) map.get("sourceId"));
    pub.setSourceIdHash(sourceIdHash);
    Long titleHash = PubHashUtils.cleanTitleHash((String) map.get("enTitle"));
    pub.setTitleHash(titleHash);
    Long unitHash = PubHashUtils.getEnPubUnitFingerPrint((Integer) map.get("pubYear"), (String) map.get("original"),
        (String) map.get("authorNames"));
    pub.setUnitHash(unitHash);
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
  public List<SpsPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException {
    try {

      List<SpsPubAssign> list = this.spsPubAssignDao.getNeedSendPub(startId, size);
      if (CollectionUtils.isEmpty(list)) {
        return list;
      }
      // 获取XML数据
      List<Long> pubIds = new ArrayList<Long>();
      for (SpsPubAssign assign : list) {
        // 状态为1的才需要xml数据
        if (assign.getResult() == 1) {
          pubIds.add(assign.getPubId());
        }
      }
      if (pubIds.size() > 0) {
        List<SpsPubExtend> xmlList = this.spsPublicationDao.getSpsPubExtends(pubIds);
        for (SpsPubExtend xml : xmlList) {
          for (SpsPubAssign assign : list) {
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
  public void sendInsPub(SpsPubAssign pubassign) throws ServiceException {
    try {
      if (pubassign.getInsId() != null) {
        spsPubCacheAssignProducer.sendAssignMsg(pubassign.getPubId(), pubassign.getXmlData(), pubassign.getInsId(),
            (pubassign.getResult() == 1 ? 1 : 2));
      }
      pubassign.setIsSend(1);
      this.spsPubAssignDao.save(pubassign);
    } catch (Exception e) {
      logger.error("发送单位成果", e);
      throw new ServiceException("发送单位成果", e);
    }
  }

  @Override
  public SpsPubAssign getSpsPubAssign(Long pubId, Long insId) throws ServiceException {

    SpsPubAssign assignInfo = this.spsPubAssignDao.getSpsPubAssign(pubId, insId);

    if (assignInfo != null) {

      if (pubId != null) {
        SpsPubExtend xml = this.spsPubAssignDao.getSpsPubExtend(pubId);
        assignInfo.setXmlData(xml.getXmlData());
      }
    }
    return assignInfo;
  }
}
