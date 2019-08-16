package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.dao.pdwh.pub.PubAllIdsSyncDao;
import com.smate.center.batch.dao.pdwh.pub.wanfang.WfPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.wanfang.WfPublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.wanfang.WfPubAddr;
import com.smate.center.batch.model.pdwh.pub.wanfang.WfPubDup;
import com.smate.center.batch.model.pdwh.pub.wanfang.WfPublication;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * wanfang基准库成果服务.
 * 
 * @author liqinghua
 * 
 */
@Service("wfPublicationService")
@Transactional(rollbackFor = Exception.class)
public class WfPublicationServiceImpl implements WfPublicationService {

  /**
   * 
   */
  private static final long serialVersionUID = 5661992852411986526L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WfPublicationDao wfPublicationDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private WfPubAddrDao wfPubAddrDao;
  @Autowired
  private PubAllIdsSyncDao pubAllIdsSyncDao;

  @Override
  public Long getDupPub(Long titleHash, Long unitHash) throws ServiceException {

    try {
      WfPubDup pubDup = wfPublicationDao.getWfPubDup(titleHash, unitHash);
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

    WfPublication pub = praseDataXmlToPub(dataXml);
    Long dupId = this.getDupPub(pub.getTitleHash(), pub.getUnitHash());
    if (dupId == null) {
      pub.setFetchDate(new Date());
      // 增加到基准库
      this.addPub(dataXml, pub);
      // 匹配单位
    } else {
      // 匹配单位
    }
  }

  @Override
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException {

    WfPublication pub = praseDataXmlToPub(dataXml);
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
  private void addPub(String dataXml, WfPublication pub) throws ServiceException {

    try {
      // 先保存
      this.wfPublicationDao.save(pub);
      // 保存查重数据
      this.wfPublicationDao.saveWfPubDup(pub.getPubId(), pub.getTitleHash(), pub.getUnitHash());
      // 保存Xml
      this.wfPublicationDao.saveWfPubExtend(pub.getPubId(), dataXml);
      // 保存到同步puball的任务表
      this.pubAllIdsSyncDao.savePubAlldsSync(pub.getPubId(), pub.getDbId());
      // 解析成果地址
      String orgsStr = pub.getOrganization();
      if (StringUtils.isNotBlank(orgsStr)) {
        List<WfPubAddr> pubAddrs = new ArrayList<WfPubAddr>();
        // 获取机构名称列表，构造IsiPubCacheOrgs对象
        Set<String> pubAddrSet = XmlUtil.parseWfPubAddrs(orgsStr);
        for (String pubAddr : pubAddrSet) {
          WfPubAddr cco = new WfPubAddr(pub.getPubId(), StringUtils.substring(pubAddr, 0, 500));
          // 成果地址不可为空
          if (cco.getAddr() != null) {
            this.wfPubAddrDao.save(cco);
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
   * 解析成果Xml到wfPublication封装.
   * 
   * @param dataXml
   * @return
   * @throws ServiceException
   */
  private WfPublication praseDataXmlToPub(String dataXml) throws ServiceException {

    Map<String, Object> map = praseDataXml(dataXml);
    WfPublication pub = new WfPublication();

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
    // 必须使用原始字符串计算hash，截断的不算
    Long titleHash = PubHashUtils.cleanTitleHash((String) map.get("zhTitle"), (String) map.get("enTitle"));
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
}
