package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.constant.DbSourceConst;
import com.smate.center.batch.dao.pdwh.pub.PubAllIdsSyncDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnkipat.*;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.mq.CnkiPatPubCacheAssignProducer;
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
 * CnkiPat专利基准库成果服务.
 * 
 * @author liqinghua
 * 
 */
@Service("cnkiPatPublicationService")
@Transactional(rollbackFor = Exception.class)
public class CnkiPatPublicationServiceImpl implements CnkiPatPublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = -5287631946611520971L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CnkiPatPublicationDao cnkiPatPublicationDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private CnkiPatPubAddrDao cnkiPatPubAddrDao;
  @Autowired
  private PubAllIdsSyncDao pubAllIdsSyncDao;
  @Autowired
  private CnkiPatPubInsMatchService cnkiPatPubInsMatchService;
  @Autowired
  private CnkiPatPubAssignDao cnkiPatPubAssignDao;
  @Autowired
  private CnkiPatPubCacheAssignProducer cnkiPatPubCacheAssignProducer;
  @Autowired
  private PublicationPdwhService publicationPdwhService;

  @Override
  public Long getDupPub(Long titleHash, Long patentHash) throws ServiceException {
    try {
      CnkiPatPubDup pubDup = cnkiPatPublicationDao.getCnkiPatPubDup(titleHash, patentHash, null);
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
  public Long getDupPub(Long titleHash, Long patentHash, Long patentOpenHash) throws ServiceException {

    try {
      CnkiPatPubDup pubDup = cnkiPatPublicationDao.getCnkiPatPubDup(titleHash, patentHash, patentOpenHash);
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

    CnkiPatPublication pub = praseDataXmlToPub(dataXml);
    if (StringUtils.isBlank(pub.getPatentNo()) && StringUtils.isBlank(pub.getPatentOpenNo())) {
      throw new ServiceException("专利号与公开号不能同时为空");
    }
    Long dupId = this.getDupPub(pub.getTitleHash(), pub.getPatentHash(), pub.getPatentOpenHash());
    if (dupId == null) {
      pub.setFetchDate(new Date());
      // 增加到基准库
      this.addPub(dataXml, pub);
      // 匹配单位
      cnkiPatPubInsMatchService.matchPubCache(insId, pub.getPubId(), pub.getPubAddrs());
      // 保存至BatchJobs
      publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.Cnkipat);

    } else {
      if (StringUtils.isNotBlank(pub.getPatentNo())) {
        fillPatentNo(pub, dupId);
      }
      // 匹配单位
      cnkiPatPubInsMatchService.matchPubCache(insId, dupId);
      // 保存至BatchJobs
      publicationPdwhService.saveToBatchJobs(pub.getPubId(), insId, DbSourceConst.Cnkipat);
    }
  }

  @Override
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException {

    CnkiPatPublication pub = praseDataXmlToPub(dataXml);
    if (StringUtils.isBlank(pub.getPatentNo()) && StringUtils.isBlank(pub.getPatentOpenNo())) {
      throw new ServiceException("专利号与公开号不能同时为空");
    }
    Long dupId = this.getDupPub(pub.getTitleHash(), pub.getPatentHash(), pub.getPatentOpenHash());
    if (dupId == null) {
      pub.setFetchDate(fetchTime);
      pub.setCreatePsn(psnId);
      pub.setCreateIns(insId);
      // 增加到基准库
      this.addPub(dataXml, pub);
    } else if (StringUtils.isNotBlank(pub.getPatentNo())) {
      fillPatentNo(pub, dupId);
    }
  }

  /**
   * 补充专利号.
   * 
   * @param pub
   * @param dupId
   */
  private void fillPatentNo(CnkiPatPublication pub, Long dupId) {
    CnkiPatPublication dupPub = this.cnkiPatPublicationDao.get(dupId);
    if (StringUtils.isBlank(dupPub.getPatentNo())) {
      dupPub.setPatentNo(pub.getPatentNo());
      this.cnkiPatPublicationDao.save(dupPub);

      CnkiPatPubDup dupF = this.cnkiPatPublicationDao.getCnkiPatPubDup(dupId);
      dupF.setPatentHash(pub.getPatentHash());
      this.cnkiPatPublicationDao.saveCnkiPatPubDup(dupF);
    }
  }

  /**
   * 增加文献到基准库.
   * 
   * @param dataXml
   * @param pub
   * @throws ServiceException
   */
  private void addPub(String dataXml, CnkiPatPublication pub) throws ServiceException {

    try {
      // 先保存
      this.cnkiPatPublicationDao.save(pub);
      // 保存查重数据
      this.cnkiPatPublicationDao.saveCnkiPatPubDup(pub.getPubId(), pub.getTitleHash(), pub.getPatentHash(),
          pub.getPatentOpenHash());
      // 保存Xml
      this.cnkiPatPublicationDao.saveCnkiPatPubExtend(pub.getPubId(), dataXml);
      // 保存到同步puball的任务表
      this.pubAllIdsSyncDao.savePubAlldsSync(pub.getPubId(), pub.getDbId());

      // 解析成果地址
      String orgsStr = pub.getOrganization();
      if (StringUtils.isNotBlank(orgsStr)) {
        List<CnkiPatPubAddr> pubAddrs = new ArrayList<CnkiPatPubAddr>();
        // 获取机构名称列表，构造IsiPubCacheOrgs对象
        Set<String> pubAddrSet = XmlUtil.parseCnkiPubAddrs(orgsStr);
        for (String pubAddr : pubAddrSet) {
          CnkiPatPubAddr cco = new CnkiPatPubAddr(pub.getPubId(), StringUtils.substring(pubAddr, 0, 500));
          // 成果地址不可为空
          if (cco.getAddr() != null) {
            this.cnkiPatPubAddrDao.save(cco);
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
   * 解析成果Xml到CnkiPatPublication封装.
   * 
   * @param dataXml
   * @return
   * @throws ServiceException
   */
  private CnkiPatPublication praseDataXmlToPub(String dataXml) throws ServiceException {

    Map<String, Object> map = praseDataXml(dataXml);
    CnkiPatPublication pub = new CnkiPatPublication();

    pub.setPubYear((Integer) map.get("pubYear"));
    pub.setDbId((Integer) map.get("dbId"));
    pub.setDoi(StringUtils.substring((String) map.get("doi"), 0, 100));
    pub.setZhTitle(StringUtils.substring((String) map.get("zhTitle"), 0, 500));
    pub.setEnTitle(StringUtils.substring((String) map.get("enTitle"), 0, 500));
    pub.setPubType((Integer) map.get("pubType"));
    pub.setAuthorNames(StringUtils.substring((String) map.get("authorNames"), 0, 500));
    pub.setPatentNo(StringUtils.substring((String) map.get("patentNo"), 0, 100));
    pub.setPatentOpenNo(StringUtils.substring((String) map.get("patentOpenNo"), 0, 100));
    pub.setOrganization((String) map.get("organization"));

    String patentNo = (String) map.get("patentNo");
    String patentOpenNo = (String) map.get("patentOpenNo");

    // 必须使用原始字符串计算hash，截断的不算
    Long patentHash = PubHashUtils.cleanPatentNoHash(patentNo);
    Long patentOpenHash = PubHashUtils.cleanPatentNoHash(patentOpenNo);
    pub.setPatentHash(patentHash);
    pub.setPatentOpenHash(patentOpenHash);

    Long titleHash = PubHashUtils.cleanTitleHash((String) map.get("zhTitle"), (String) map.get("enTitle"));
    pub.setTitleHash(titleHash);
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
  public List<CnkiPatPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException {
    try {

      List<CnkiPatPubAssign> list = this.cnkiPatPubAssignDao.getNeedSendPub(startId, size);
      if (CollectionUtils.isEmpty(list)) {
        return list;
      }
      // 获取XML数据
      List<Long> pubIds = new ArrayList<Long>();
      for (CnkiPatPubAssign assign : list) {
        pubIds.add(assign.getPubId());
      }
      if (pubIds.size() > 0) {
        List<CnkiPatPubExtend> xmlList = this.cnkiPatPublicationDao.getCnkiPatPubExtends(pubIds);
        for (CnkiPatPubExtend xml : xmlList) {
          for (CnkiPatPubAssign assign : list) {
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
  public void sendInsPub(CnkiPatPubAssign pubassign) throws ServiceException {
    try {
      if (pubassign.getInsId() != null) {
        cnkiPatPubCacheAssignProducer.sendAssignMsg(pubassign.getPubId(), pubassign.getXmlData(), pubassign.getInsId());
      }
      pubassign.setIsSend(1);
      this.cnkiPatPubAssignDao.save(pubassign);
    } catch (Exception e) {
      logger.error("发送单位成果", e);
      throw new ServiceException("发送单位成果", e);
    }
  }

  @Override
  public CnkiPatPubAssign getCnkiPatPubAssign(Long pubId, Long insId) throws ServiceException {

    CnkiPatPubAssign assignInfo = this.cnkiPatPubAssignDao.getCnkiPatPubAssign(pubId, insId);

    if (assignInfo != null) {
      if (pubId != null) {
        CnkiPatPubExtend xml = this.cnkiPatPubAssignDao.getCnkiPatPubExtend(pubId);
        assignInfo.setXmlData(xml.getXmlData());
      }
    }
    return assignInfo;
  }
}
