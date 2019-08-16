package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.constant.DbSourceConst;
import com.smate.center.batch.constant.ImportPubXmlConstants;
import com.smate.center.batch.dao.pdwh.pub.PubAllIdsSyncDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.ei.*;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.mq.EiPubCacheAssignProducer;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * ei基准库成果服务.
 * 
 * @author liqinghua
 * 
 */
@Service("eiPublicationService")
@Transactional(rollbackFor = Exception.class)
public class EiPublicationServiceImpl implements EiPublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = 5661992852411986526L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private EiPublicationDao eiPublicationDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private EiPubInsMatchService eiPubInsMatchService;
  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private EiPubAddrDao eiPubAddrDao;
  @Autowired
  private PubAllIdsSyncDao pubAllIdsSyncDao;
  @Autowired
  private EiPubAssignDao eiPubAssignDao;
  @Resource(name = "eiPubCacheAssignProducer")
  private EiPubCacheAssignProducer eiPubCacheAssignProducer;

  @Override
  public Long getDupPub(Long sourceIdHash, Long titleHash, Long unitHash) throws ServiceException {

    try {
      EiPubDup pubDup = eiPublicationDao.getEiPubDup(sourceIdHash, titleHash, unitHash);
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

    EiPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getSourceIdHash(), pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(new Date());
      // 增加到基准库
      this.addPub(dataXml, pub);
      // 匹配单位
      Integer match = eiPubInsMatchService.matchPubCache(insId, pub.getPubId(), pub.getPubAddrs());
      // 保存至BatchJobs
      if (match == 1) {
        publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.EI);
      }
    } else {
      // 更新资助基金信息
      this.refreshDupPub(pub, dupId);
      // 匹配单位
      Integer match = eiPubInsMatchService.matchPubCache(insId, dupId);
      // 保存至BatchJobs
      if (match == 1) {
        publicationPdwhService.saveToBatchJobs(dupId, insId, DbSourceConst.EI);
      }
    }
  }

  @Override
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException {

    EiPublication pub = praseDataXmlToPub(dataXml);
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
  private void addPub(String dataXml, EiPublication pub) throws ServiceException {

    try {
      // 先保存
      this.eiPublicationDao.save(pub);
      // 保存查重数据
      this.eiPublicationDao.saveEiPubDup(pub.getPubId(), pub.getSourceIdHash(), pub.getTitleHash(), pub.getUnitHash());
      // 保存Xml
      this.eiPublicationDao.saveEiPubExtend(pub.getPubId(), dataXml);
      // 保存到同步puball的任务表
      this.pubAllIdsSyncDao.savePubAlldsSync(pub.getPubId(), pub.getDbId());
      // 解析成果地址
      String orgsStr = pub.getOrganization();
      if (StringUtils.isNotBlank(orgsStr)) {
        List<EiPubAddr> pubAddrs = new ArrayList<EiPubAddr>();
        // 获取机构名称列表，构造IsiPubCacheOrgs对象
        Set<String> pubAddrSet = XmlUtil.parseEiPubAddrs(orgsStr);
        for (String pubAddr : pubAddrSet) {
          EiPubAddr cco = new EiPubAddr(pub.getPubId(), StringUtils.substring(pubAddr, 0, 500),
              PubHashUtils.cleanPubAddrHash(pubAddr));
          // 成果拆分地址不可为空
          if (cco.getAddr() != null && cco.getAddrHash() != null) {
            this.eiPubAddrDao.save(cco);
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
   * 解析成果Xml到EiPublication封装.
   * 
   * @param dataXml
   * @return
   * @throws ServiceException
   */
  private EiPublication praseDataXmlToPub(String dataXml) throws ServiceException {

    Map<String, Object> map = praseDataXml(dataXml);
    EiPublication pub = new EiPublication();

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
    pub.setFundInfo(StringUtils.substring((String) map.get("fundInfo"), 0, 1000));
    pub.setCategoryNo((String) map.get("categoryNo"));
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
  public EiPubAssign getEiPubAssign(Long pubId, Long insId) throws ServiceException {

    EiPubAssign assignInfo = this.eiPubAssignDao.getEiPubAssign(pubId, insId);

    if (assignInfo != null) {
      if (pubId != null) {
        EiPubExtend xml = this.eiPublicationDao.getEiPubExtend(pubId);
        assignInfo.setXmlData(xml.getXmlData());
      }
    }
    return assignInfo;
  }

  @Override
  public void sendInsPub(EiPubAssign pubassign) throws ServiceException {
    try {
      if (pubassign.getInsId() != null) {
        eiPubCacheAssignProducer.sendAssignMsg(pubassign.getPubId(), pubassign.getXmlData(), pubassign.getInsId(),
            (pubassign.getResult() == 1 ? 1 : 2));
      }
      pubassign.setIsSend(1);
      this.eiPubAssignDao.save(pubassign);
    } catch (Exception e) {
      logger.error("发送单位成果", e);
      throw new ServiceException("发送单位成果", e);
    }
  }

  /**
   * 插入新基准库数据，如果查询到重复，更新资助基金信息.
   * 
   * @param pub
   * @throws ServiceException
   */
  private void refreshDupPub(EiPublication pub, Long pubId) throws ServiceException {

    try {
      EiPublication dupPub = this.eiPublicationDao.get(pubId);
      EiPubExtend extend = this.eiPublicationDao.getEiPubExtend(pubId);

      if (extend == null) {
        return;
      }

      ImportPubXmlDocument document = new ImportPubXmlDocument(extend.getXmlData());

      // 更新基金资助信息
      String fundInfo = pub.getFundInfo();
      document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "fundinfo", fundInfo);

      // 更新journal_category_no分类号
      String jcn = pub.getCategoryNo();

      if (StringUtils.isNotEmpty(jcn)) {
        document.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "journal_category_no", jcn);
      }

      this.eiPublicationDao.saveEiPubExtend(pubId, document.getXmlString());
      this.eiPublicationDao.save(dupPub);
      // saveIsiPubSourceDb(pub.getDbId(), pubId);

    } catch (Exception e) {
      logger.error("插入新基准库数据，如果查询到重复，更新引用次数，收录情况", e);
      throw new ServiceException("插入新基准库数据，如果查询到重复，更新引用次数，收录情况", e);
    }
  }
}
