package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.dao.rol.pub.RolPubDupDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.RolPubDupFields;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.model.sns.pub.PubDupParam;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.center.batch.util.pub.PubXmlTypeUtils;
import com.smate.center.batch.util.pub.XmlFragmentCleanerHelper;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成果查重服务.
 * 
 * @author liqinghua
 * 
 */
@Service("rolPubDupService")
@Transactional(rollbackFor = Exception.class)
public class RolPubDupServiceImpl implements RolPubDupService {

  /**
   * 
   */
  private static final long serialVersionUID = 14475245675367915L;
  private final Logger logger = LoggerFactory.getLogger(RolPubDupServiceImpl.class);
  @Autowired
  private RolPubDupDao pubDupDao;
  @Autowired
  private ConstRefDbService constRefDbService;

  /**
   * 查重同库重复的成果.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws ServiceException
   */
  private List<RolPubDupFields> getDupBySourceId(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws ServiceException {

    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    try {
      Integer sourceDbId = param.getSourceDbId();
      // source id
      Long isiIdHash = PubHashUtils.cleanSourceIdHash(param.getIsiId());
      Long eiIdHash = PubHashUtils.cleanSourceIdHash(param.getEiId());
      Long spsIdHash = PubHashUtils.cleanSourceIdHash(param.getSpsId());

      return getDupBySourceId(sourceDbId, isiIdHash, eiIdHash, spsIdHash, ownerId, excludePubId, status);
    } catch (Exception e) {
      logger.error("getDupBySourceId查询重复成果", e);
      throw new ServiceException("getDupBySourceId查询重复成果", e);
    }
  }

  /**
   * 查重同库重复的成果.
   * 
   * @param ownerId
   * @param excludePubId
   * @param sourceDbId
   * @param isiIdHash
   * @param eiIdHash
   * @param spsIdHash
   */
  private List<RolPubDupFields> getDupBySourceId(Integer sourceDbId, Long isiIdHash, Long eiIdHash, Long spsIdHash,
      Long ownerId, Long excludePubId, Integer status) throws ServiceException {

    Assert.notNull(ownerId, "ownerid不能为空");

    try {
      List<RolPubDupFields> dupList = null;

      // isi的先跟本库查找
      if (PubXmlDbUtils.isIsiDb(sourceDbId) && isiIdHash != null) {
        dupList = pubDupDao.findDupByIsiId(isiIdHash, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      }
      // scopus同库查重
      if (PubXmlDbUtils.isScopusDb(sourceDbId) && spsIdHash != null) {
        dupList = pubDupDao.findDupBySpsId(spsIdHash, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      }
      // ei同库查重
      if (PubXmlDbUtils.isEiDb(sourceDbId) && eiIdHash != null) {
        dupList = pubDupDao.findDupByEiId(eiIdHash, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      }
      return null;
    } catch (Exception e) {
      logger.error("getDupBySourceId查询重复成果", e);
      throw new ServiceException("getDupBySourceId查询重复成果", e);
    }
  }

  /**
   * 查重doi重复的成果.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws ServiceException
   */
  private List<RolPubDupFields> getDupByDoiId(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws ServiceException {
    try {
      // doi
      Long doiHash = PubHashUtils.cleanDoiHash(param.getDoi());
      return getDupByDoiId(param.getPubYear(), doiHash, ownerId, excludePubId, status);
    } catch (Exception e) {
      logger.error("getDupByDoiId查询重复成果", e);
      throw new ServiceException("getDupByDoiId查询重复成果", e);
    }
  }

  /**
   * @param param
   * @param ownerId
   * @param excludePubId
   * @param doiHash
   * @return
   */
  private List<RolPubDupFields> getDupByDoiId(Integer pubYear, Long doiHash, Long ownerId, Long excludePubId,
      Integer status) throws ServiceException {
    try {
      List<RolPubDupFields> dupList = null;
      // doi查重
      if (doiHash != null) {
        dupList = pubDupDao.findDupByDoi(doiHash, pubYear, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      }
      return dupList;
    } catch (Exception e) {
      logger.error("getDupByDoiId查询重复成果", e);
      throw new ServiceException("getDupByDoiId查询重复成果", e);
    }
  }

  /**
   * 期刊查重.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws ServiceException
   */
  private List<RolPubDupFields> getDupByJournal(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    Assert.notNull(param.getPubType(), "PubType不能为空");
    // 是否期刊
    if (!PubXmlTypeUtils.isJournalType(param.getPubType())) {
      return null;
    }
    try {
      // issue hash
      Long issueHash = PubHashUtils.cleanIssueHash(param.getIssue());
      Long jaFingerPrint = PubHashUtils.getJaFingerPrint(param.getJname(), param.getIssn(), param.getVolume(),
          param.getIssue(), param.getArticleNo());
      Long jpFingerPrint = PubHashUtils.getJpFingerPrint(param.getJname(), param.getIssn(), param.getVolume(),
          param.getIssue(), param.getStartPage());
      Long jFingerPrint = PubHashUtils.getJFingerPrint(param.getJname(), param.getIssn());

      Integer sourceDbId = param.getSourceDbId();
      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());
      // 作者hash
      Long auNameHash = PubHashUtils.getAuNameHash(param.getAuthorNames());
      Integer pubType = param.getPubType();
      Integer pubYear = param.getPubYear();

      return getDupByJournal(issueHash, jaFingerPrint, jpFingerPrint, sourceDbId, zhTitleHash, enTitleHash, auNameHash,
          jFingerPrint, pubType, pubYear, ownerId, excludePubId, status);
    } catch (Exception e) {
      logger.error("getDupByJournal查询重复成果", e);
      throw new ServiceException("getDupByJournal查询重复成果", e);
    }
  }

  /**
   * 期刊查重.
   * 
   * @param ownerId
   * @param excludePubId
   * @param issueHash
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param sourceDbId
   * @param zhTitleHash
   * @param enTitleHash
   * @param auNameHash
   * @param jid
   * @param pubType
   * @param pubYear
   * @return
   */
  private List<RolPubDupFields> getDupByJournal(Long issueHash, Long jaFingerPrint, Long jpFingerPrint,
      Integer sourceDbId, Long zhTitleHash, Long enTitleHash, Long auNameHash, Long jFingerPrint, Integer pubType,
      Integer pubYear, Long ownerId, Long excludePubId, Integer status) throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(pubType, "PubType不能为空");
    // 是否期刊
    if (!PubXmlTypeUtils.isJournalType(pubType)) {
      return null;
    }
    try {
      List<RolPubDupFields> dupList = null;
      // 中文库,title + {issue|au_names} + jid + pub_year(可能为空)
      if (PubXmlDbUtils.isCnkiDb(sourceDbId) || PubXmlDbUtils.isWanFangDb(sourceDbId)) {

        dupList = pubDupDao.findDupByIssueAu(zhTitleHash, enTitleHash, issueHash, auNameHash, jFingerPrint, pubType,
            pubYear, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      } else {
        // 外文库.
        // jid + volume + issue + {article_no | page_start} + year
        // 各项非空且相同
        dupList = pubDupDao.findDupByJFingerPrint(jaFingerPrint, jpFingerPrint, pubType, pubYear, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      }
      return dupList;
    } catch (Exception e) {
      logger.error("getDupByJournal查询重复成果", e);
      throw new ServiceException("getDupByJournal查询重复成果", e);
    }
  }

  /**
   * 会议查重.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws ServiceException
   */
  private List<RolPubDupFields> getDupByConference(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    Assert.notNull(param.getPubType(), "PubType不能为空");
    // 是否会议
    if (!PubXmlTypeUtils.isConference(param.getPubType())) {
      return null;
    }
    try {
      param.setIssn(XmlUtil.buildStandardIssn(param.getIssn()));
      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());
      Integer sourceDbId = param.getSourceDbId();
      // 作者hash
      Long auNameHash = PubHashUtils.getAuNameHash(param.getAuthorNames());
      Long confnHash = PubHashUtils.cleanConfNameHash(param.getConfName());
      Integer pubType = param.getPubType();
      // 会议指纹
      Long cpFingerPrint = PubHashUtils.getCpFingerPrint(param.getIsbn(), param.getStartPage());
      Long caFingerPrint = PubHashUtils.getCaFingerPrint(param.getIsbn(), param.getArticleNo());
      Long cvaFingerPrint =
          PubHashUtils.getCvaFingerPrint(param.getIssn(), param.getVolume(), param.getIssue(), param.getArticleNo());
      Long cvpFingerPrint =
          PubHashUtils.getCvpFingerPrint(param.getIssn(), param.getVolume(), param.getIssue(), param.getStartPage());

      return getDupByConference(zhTitleHash, enTitleHash, sourceDbId, auNameHash, confnHash, pubType,
          param.getPubYear(), cpFingerPrint, caFingerPrint, cvaFingerPrint, cvpFingerPrint, ownerId, excludePubId,
          status);
    } catch (Exception e) {
      logger.error("getDupByConference查询重复成果", e);
      throw new ServiceException("getDupByConference查询重复成果", e);
    }
  }

  /**
   * 会议查重.
   * 
   * @param ownerId
   * @param excludePubId
   * @param zhTitleHash
   * @param enTitleHash
   * @param sourceDbId
   * @param auNameHash
   * @param confnHash
   * @param pubType
   * @param cFingerPrint
   * @return
   */
  private List<RolPubDupFields> getDupByConference(Long zhTitleHash, Long enTitleHash, Integer sourceDbId,
      Long auNameHash, Long confnHash, Integer pubType, Integer pubYear, Long cpFingerPrint, Long caFingerPrint,
      Long cvaFingerPrint, Long cvpFingerPrint, Long ownerId, Long excludePubId, Integer status)
      throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    // 是否会议
    if (!PubXmlTypeUtils.isConference(pubType)) {
      return null;
    }
    try {
      List<RolPubDupFields> dupList = null;
      // 中文库,title + au_names + conf_name
      if (PubXmlDbUtils.isCnkiDb(sourceDbId) || PubXmlDbUtils.isWanFangDb(sourceDbId)) {

        dupList = pubDupDao.findDupByConf(confnHash, zhTitleHash, enTitleHash, pubType, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      } else {
        // 外文库.
        // title + isbn +  {article_no | page_start} + year各项非空且相同
        dupList = pubDupDao.findDupByCFingerPrint(cpFingerPrint, caFingerPrint, zhTitleHash, enTitleHash, pubType,
            pubYear, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
        // issn +  volume   +[issue]+ {article_no | page_start} + year
        dupList = pubDupDao.findDupByCFingerPrint(cvaFingerPrint, cvpFingerPrint, pubType, pubYear, ownerId, status);
        dupList = this.removeExcPub(dupList, excludePubId);
        if (CollectionUtils.isNotEmpty(dupList)) {
          return dupList;
        }
      }
      return dupList;
    } catch (Exception e) {
      logger.error("getDupByConference查询重复成果", e);
      throw new ServiceException("getDupByConference查询重复成果", e);
    }
  }

  /**
   * 专利查重.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws ServiceException
   */
  private List<RolPubDupFields> getDupByPatent(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    Assert.notNull(param.getPubType(), "PubType不能为空");
    try {
      // 是否会议
      if (!PubXmlTypeUtils.isPatent(param.getPubType())) {
        return null;
      }
      Integer pubType = param.getPubType();
      Long patentHash = PubHashUtils.cleanPatentNoHash(param.getPatentNo());
      Long patentOpenHash = PubHashUtils.cleanPatentNoHash(param.getPatentOpenNo());
      return getDupByPatent(patentHash, patentOpenHash, ownerId, pubType, status);
    } catch (Exception e) {
      logger.error("getDupByPatent查询重复成果", e);
      throw new ServiceException("getDupByPatent查询重复成果", e);
    }
  }

  /**
   * 专利查重.
   * 
   * @param ownerId
   * @param pubType
   * @param patentHash
   * @return
   */
  private List<RolPubDupFields> getDupByPatent(Long patentHash, Long patentOpenHash, Long ownerId, Integer pubType,
      Integer status) throws ServiceException {
    // 是否会议
    if (!PubXmlTypeUtils.isPatent(pubType)) {
      return null;
    }
    try {
      return pubDupDao.findDupByPatent(patentHash, patentOpenHash, pubType, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupByPatent查询重复成果", e);
      throw new ServiceException("getDupByPatent查询重复成果", e);
    }
  }

  /**
   * 标题查询重复成果.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  private Map<Integer, List<RolPubDupFields>> getTitleDupPub(PubDupParam param, Long ownerId, Integer status)
      throws ServiceException {

    try {
      Assert.notNull(ownerId, "ownerid不能为空");
      Assert.notNull(param, "param不能为空");
      Assert.notNull(param.getPubType(), "PubType不能为空");
      Integer pubType = param.getPubType();
      Integer pubYear = param.getPubYear();

      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());

      return this.getTitleDupPub(pubType, pubYear, zhTitleHash, enTitleHash, ownerId, param.getSourceDbId(), null,
          status);
    } catch (Exception e) {
      logger.error("宽松查询重复成果", e);
      throw new ServiceException("宽松查询重复成果", e);
    }
  }

  /**
   * 标题查询重复成果.
   * 
   * @param pubType
   * @param pubYear
   * @param zhTitleHash
   * @param enTitleHash
   * @param ownerId
   * @param sourceDbId
   * 
   * @return
   * @throws ServiceException
   */
  private Map<Integer, List<RolPubDupFields>> getTitleDupPub(Integer pubType, Integer pubYear, Long zhTitleHash,
      Long enTitleHash, Long ownerId, Integer sourceDbId, Long excludePubId, Integer status) throws ServiceException {

    try {
      List<RolPubDupFields> dupIds =
          this.pubDupDao.findDupByTitle(zhTitleHash, enTitleHash, pubYear, pubType, ownerId, status);
      Map<Integer, List<RolPubDupFields>> map = new HashMap<Integer, List<RolPubDupFields>>();

      if (CollectionUtils.isNotEmpty(dupIds)) {
        // 排除pubid
        dupIds = this.removeExcPub(dupIds, excludePubId);
        for (int i = 0; i < dupIds.size(); i++) {
          RolPubDupFields obj = dupIds.get(i);
          // 取消，很多极品成果，没办法
          // Integer dbId = obj.getSourceDbId();
          // // 相同库，标题、年份等相同，做严格查重处理
          // if (sourceDbId != null && sourceDbId.equals(dbId)) {
          // List<PubDupFields> dupList = map.get(1) == null ? new
          // ArrayList<PubDupFields>() : map.get(1);
          // dupList.add(obj);
          // map.put(1, dupList);
          // } else {
          // // 其他做宽松查重
          // List<PubDupFields> dupList = map.get(2) == null ? new
          // ArrayList<PubDupFields>() : map.get(2);
          // dupList.add(obj);
          // map.put(2, dupList);
          // }
          List<RolPubDupFields> dupList = map.get(2) == null ? new ArrayList<RolPubDupFields>() : map.get(2);
          dupList.add(obj);
          map.put(2, dupList);
        }
      }
      return map;
    } catch (Exception e) {
      logger.error("getTitleDupPub标题查询重复成果", e);
      throw new ServiceException("getTitleDupPub标题查询重复成果", e);
    }
  }

  /**
   * 移除排除的数据.
   * 
   * @param dupList
   * @param excludePubId
   * @return
   */
  private List<RolPubDupFields> removeExcPub(List<RolPubDupFields> dupList, Long excludePubId) {
    if (CollectionUtils.isNotEmpty(dupList) && excludePubId != null && dupList.contains(excludePubId)) {
      for (int i = 0; i < dupList.size(); i++) {
        RolPubDupFields dup = dupList.get(i);
        if (excludePubId.equals(dup.getPubId())) {
          dupList.remove(i);
          break;
        }
      }
    }
    return dupList;
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(Long pubId) throws ServiceException {

    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(pubId));
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new ServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(Long pubId) throws ServiceException {
    return this.getDupPubField(pubId, RolPubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(Long pubId, Integer status) throws ServiceException {
    try {
      RolPubDupFields dupFields = this.pubDupDao.getPubDupFields(pubId);
      if (dupFields == null) {
        return null;
      }
      return this.getDupPubField(dupFields, dupFields.getOwnerId(), pubId, status);
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new ServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  /**
   * 将PubDupFields的ID取出.
   * 
   * @param dupmap
   * @return
   */
  public Map<Integer, List<Long>> covertDupFieldToIdMap(Map<Integer, List<RolPubDupFields>> dupmap) {
    Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
    if (MapUtils.isEmpty(dupmap)) {
      return map;
    }
    for (int i = 1; i <= 2; i++) {
      if (CollectionUtils.isNotEmpty(dupmap.get(i))) {
        List<Long> dupIds = new ArrayList<Long>();
        for (RolPubDupFields dupFields : dupmap.get(i)) {
          dupIds.add(dupFields.getPubId());
        }
        map.put(i, dupIds);
      }
    }
    return map;
  }

  /**
   * 将PubDupFields的ID取出.
   * 
   * @param dupmap
   * @return
   */
  public List<Long> covertDupFieldToId(Map<Integer, List<RolPubDupFields>> dupmap) {
    List<Long> dupIds = new ArrayList<Long>();
    if (MapUtils.isEmpty(dupmap)) {
      return dupIds;
    }
    for (int i = 1; i <= 2; i++) {
      if (CollectionUtils.isNotEmpty(dupmap.get(i))) {
        for (RolPubDupFields dupFields : dupmap.get(i)) {
          dupIds.add(dupFields.getPubId());
        }
      }
    }
    return dupIds;
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(RolPubDupFields dupFields) throws ServiceException {
    try {

      return getDupPub(dupFields, dupFields.getOwnerId());
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new ServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(RolPubDupFields dupFields, Long owner) throws ServiceException {
    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(dupFields, owner, null));
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new ServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  @Override
  public List<Long> getDupPubIds(Long pubId) throws ServiceException {
    try {

      return this.covertDupFieldToId(getDupPubField(pubId));
    } catch (Exception e) {
      logger.error("getDupPubAll查询重复成果Id.", e);
      throw new ServiceException("getDupPubAll查询重复成果Id.", e);
    }
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(PubDupParam param, Long ownerId) throws ServiceException {
    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(param, ownerId));
    } catch (Exception e) {
      logger.error("getDupPub保查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回", e);
      throw new ServiceException("getDupPub保查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回", e);
    }
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(PubDupParam param, Long ownerId) throws ServiceException {
    try {
      return this.getDupPubField(param, ownerId, null, RolPubDupFields.NORMAL_STATUS);
    } catch (Exception e) {
      logger.error("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
      throw new ServiceException("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId)
      throws ServiceException {
    return this.getDupPubField(param, ownerId, excludeId, RolPubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId,
      Integer status) throws ServiceException {
    try {
      Map<Integer, List<RolPubDupFields>> map = new HashMap<Integer, List<RolPubDupFields>>();
      // souce_id查重
      List<RolPubDupFields> dupList = this.getDupBySourceId(param, ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // doi查重
      dupList = this.getDupByDoiId(param, ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 期刊查重
      dupList = this.getDupByJournal(param, ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 会议查重
      dupList = this.getDupByConference(param, ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 专利查重
      dupList = this.getDupByPatent(param, ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 标题查重
      return this.getTitleDupPub(param, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
      throw new ServiceException("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(RolPubDupFields dupFields, Long ownerId, Long excludeId)
      throws ServiceException {
    return this.getDupPubField(dupFields, ownerId, excludeId, RolPubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<RolPubDupFields>> getDupPubField(RolPubDupFields dupFields, Long ownerId, Long excludeId,
      Integer status) throws ServiceException {
    try {
      Map<Integer, List<RolPubDupFields>> map = new HashMap<Integer, List<RolPubDupFields>>();
      // souce_id查重
      List<RolPubDupFields> dupList = this.getDupBySourceId(dupFields.getSourceDbId(), dupFields.getIsiHash(),
          dupFields.getEiHash(), dupFields.getSpsHash(), ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // doi查重
      dupList = this.getDupByDoiId(dupFields.getPubYear(), dupFields.getDoiHash(), ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 期刊查重
      dupList = this.getDupByJournal(dupFields.getIssueHash(), dupFields.getJaFingerPrint(),
          dupFields.getJpFingerPrint(), dupFields.getSourceDbId(), dupFields.getZhTitleHash(),
          dupFields.getEnTitleHash(), dupFields.getAuNameHash(), dupFields.getJfingerPrint(), dupFields.getPubType(),
          dupFields.getPubYear(), ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 会议查重
      dupList = this.getDupByConference(dupFields.getZhTitleHash(), dupFields.getEnTitleHash(),
          dupFields.getSourceDbId(), dupFields.getAuNameHash(), dupFields.getConfnHash(), dupFields.getPubType(),
          dupFields.getPubYear(), dupFields.getCpFingerPrint(), dupFields.getCaFingerPrint(),
          dupFields.getCvaFingerPrint(), dupFields.getCvpFingerPrint(), ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 专利查重
      dupList = this.getDupByPatent(dupFields.getPatentHash(), dupFields.getPatentOpenNoHash(), ownerId,
          dupFields.getPubType(), status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 标题查重
      return this.getTitleDupPub(dupFields.getPubType(), dupFields.getPubYear(), dupFields.getZhTitleHash(),
          dupFields.getEnTitleHash(), ownerId, dupFields.getSourceDbId(), excludeId, status);
    } catch (Exception e) {
      logger.error("getDupPubField保查询重复成果,key=1严格，key=2宽松", e);
      throw new ServiceException("getDupPubField保查询重复成果,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public RolPubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId,
      Integer articleType, boolean canDup) throws ServiceException {
    Integer status = RolPubDupFields.DELETE_STATUS;
    if (canDup) {
      status = RolPubDupFields.NORMAL_STATUS;
    }
    return this.savePubDupFields(param, pubType, pubId, ownerId, articleType, status);
  }

  @Override
  public RolPubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId,
      Integer articleType, Integer status) throws ServiceException {
    try {
      RolPubDupFields dupFields = this.pubDupDao.getPubDupFields(pubId);
      if (dupFields == null) {
        dupFields = new RolPubDupFields();
      }
      param.setIssn(XmlUtil.buildStandardIssn(param.getIssn()));

      // isi source id
      String isiId = XmlUtil.getTrimBlankLower(param.getIsiId());
      Long isiIdHash = PubHashUtils.cleanSourceIdHash(param.getIsiId());
      String eiId = XmlUtil.getTrimBlankLower(param.getEiId());
      Long eiIdHash = PubHashUtils.cleanSourceIdHash(param.getEiId());
      String issue = XmlUtil.getTrimBlankLower(param.getIssue());
      Long issueHash = PubHashUtils.cleanIssueHash(issue);
      String spsId = XmlUtil.getTrimBlankLower(param.getSpsId());
      Long spsIdHash = PubHashUtils.cleanSourceIdHash(param.getSpsId());
      // doi
      String doi = XmlUtil.getTrimBlankLower(param.getDoi());
      Long doiHash = PubHashUtils.cleanDoiHash(doi);
      // conf_name
      Long confnHash = PubHashUtils.cleanConfNameHash(param.getConfName());
      // 期刊指纹
      Long jaFingerPrint = PubHashUtils.getJaFingerPrint(param.getJname(), param.getIssn(), param.getVolume(),
          param.getIssue(), param.getArticleNo());
      Long jpFingerPrint = PubHashUtils.getJpFingerPrint(param.getJname(), param.getIssn(), param.getVolume(),
          param.getIssue(), param.getStartPage());
      // 会议指纹
      Long cpFingerPrint = PubHashUtils.getCpFingerPrint(param.getIsbn(), param.getStartPage());
      Long caFingerPrint = PubHashUtils.getCaFingerPrint(param.getIsbn(), param.getArticleNo());
      Long cvaFingerPrint =
          PubHashUtils.getCvaFingerPrint(param.getIssn(), param.getVolume(), param.getIssue(), param.getArticleNo());
      Long cvpFingerPrint =
          PubHashUtils.getCvpFingerPrint(param.getIssn(), param.getVolume(), param.getIssue(), param.getStartPage());

      Long jfingerPrint = PubHashUtils.getJFingerPrint(param.getJname(), param.getIssn());

      // 作者hash
      Long auNameHash = PubHashUtils.getAuNameHash(param.getAuthorNames());
      // patent_no
      String patentNo = XmlUtil.getTrimBlankLower(param.getPatentNo());
      Long patentHash = PubHashUtils.cleanPatentNoHash(patentNo);
      // patent_open_no
      String patentOpenNo = XmlUtil.getTrimBlankLower(param.getPatentOpenNo());
      Long patentOpenNoHash = PubHashUtils.cleanPatentNoHash(patentOpenNo);

      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());

      dupFields.setArticleNo(param.getArticleNo());
      dupFields.setCpFingerPrint(cpFingerPrint);
      dupFields.setCaFingerPrint(caFingerPrint);
      dupFields.setCvaFingerPrint(cvaFingerPrint);
      dupFields.setCvpFingerPrint(cvpFingerPrint);
      dupFields.setDoi(doi);
      dupFields.setDoiHash(doiHash);
      dupFields.setEnTitleHash(enTitleHash);
      dupFields.setIsbn(StringUtils.trimToNull(param.getIsbn()));
      dupFields.setIsiHash(isiIdHash);
      dupFields.setIsiId(isiId);
      dupFields.setEiId(eiId);
      dupFields.setEiHash(eiIdHash);
      dupFields.setSpsHash(spsIdHash);
      dupFields.setSpsId(spsId);
      dupFields.setIssue(issue);
      dupFields.setIssueHash(issueHash);
      dupFields.setJaFingerPrint(jaFingerPrint);
      dupFields.setJid(param.getJid());
      dupFields.setJpFingerPrint(jpFingerPrint);
      dupFields.setOwnerId(ownerId);
      dupFields.setPubId(pubId);
      dupFields.setPubType(pubType);
      dupFields.setPubYear(param.getPubYear());
      dupFields.setSourceDbId(param.getSourceDbId());
      dupFields.setStartPage(StringUtils.trimToNull(param.getStartPage()));
      dupFields.setVolume(StringUtils.trimToNull(param.getVolume()));
      dupFields.setAuNameHash(auNameHash);
      dupFields.setPatentHash(patentHash);
      dupFields.setPatentNo(StringUtils.substring(patentNo, 0, 100));
      dupFields.setPatentOpenNo(StringUtils.substring(patentOpenNo, 0, 100));
      dupFields.setPatentOpenNoHash(patentOpenNoHash);
      dupFields.setZhTitleHash(zhTitleHash);
      dupFields.setArticleType(articleType);
      dupFields.setConfnHash(confnHash);
      dupFields.setIssn(StringUtils.substring(param.getIssn(), 0, 40));
      dupFields.setJfingerPrint(jfingerPrint);
      dupFields.setStatus(status);
      this.pubDupDao.save(dupFields);
      return dupFields;
    } catch (Exception e) {
      logger.error("保存成果查重字段pubId=" + pubId, e);
      throw new ServiceException("保存成果查重字段pubId=" + pubId, e);
    }
  }

  @Override
  public Map<Integer, List<Long>> getDupPubByImportPub(Element pubEle, Long ownerId) throws ServiceException {

    try {
      PubDupParam param = buildImportPubDupParam(pubEle);
      return this.getDupPub(param, ownerId);
    } catch (Exception e) {
      logger.error("getDupPubByImportPub成果导入查重", e);
      throw new ServiceException("getDupPubByImportPub成果导入查重", e);
    }
  }

  /**
   * 构造导入成果查询条件.
   * 
   * @param pubEle
   * @return
   * @throws ServiceException
   * @throws PubException
   */
  private PubDupParam buildImportPubDupParam(Element pubEle) throws ServiceException, PubException {
    String articleNo = StringUtils.trimToEmpty(pubEle.attributeValue("article_number"));
    Long jid = IrisNumberUtils.createLong(pubEle.attributeValue("jid"));
    String volume = StringUtils.trimToEmpty(pubEle.attributeValue("volume"));
    String issue = StringUtils.trimToEmpty(pubEle.attributeValue("issue"));
    String startPage = StringUtils.trimToEmpty(pubEle.attributeValue("start_page"));
    String isbn = StringUtils.trimToEmpty(pubEle.attributeValue("isbn"));
    String auNames = StringUtils.trimToEmpty(pubEle.attributeValue("author_names"));
    String patentNo = StringUtils.trimToEmpty(pubEle.attributeValue("patent_no"));
    String patentOpenNo = StringUtils.trimToEmpty(pubEle.attributeValue("patent_open_no"));
    String zhTitle = pubEle.attributeValue("ctitle");
    String enTitle = pubEle.attributeValue("etitle");
    String doi = StringUtils.trimToEmpty(pubEle.attributeValue("doi"));
    String issn = StringUtils.trimToEmpty(pubEle.attributeValue("issn"));
    issn = XmlUtil.buildStandardIssn(issn);
    // 期刊名称
    String jname = StringUtils.trimToEmpty(pubEle.attributeValue("original"));
    String confName = StringUtils.trimToEmpty(pubEle.attributeValue("proceeding_title"));
    String isiId = null;
    String eiId = null;
    String spsId = null;
    String[] dates = XmlFragmentCleanerHelper.splitDateYearMonth(pubEle, "pubyear", PubXmlConstants.CHS_DATE_PATTERN);

    Integer pubYear = null;
    if (NumberUtils.isDigits(dates[0])) {
      pubYear = Integer.valueOf(dates[0]);
    }
    Integer sourceDbId = null;
    // 如source_db_code="ChinaJournal"
    String sourceDbCode = StringUtils.trimToEmpty(pubEle.attributeValue("source_db_code"));
    if (StringUtils.isNotBlank(sourceDbCode)) {
      ConstRefDb sourceDb = constRefDbService.getConstRefDbByCode(sourceDbCode);
      if (sourceDb != null) {
        sourceDbId = sourceDb.getId().intValue();
      }
    }
    // pubtype
    String pubTypeId = StringUtils.trimToEmpty(pubEle.attributeValue("pub_type"));
    Integer pubType = null;
    if (NumberUtils.isDigits(pubTypeId)) {
      pubType = Integer.valueOf(pubTypeId);
    }
    if (PubXmlDbUtils.isIsiDb(sourceDbId)) {
      isiId = StringUtils.trimToEmpty(pubEle.attributeValue("source_id"));
    }
    if (PubXmlDbUtils.isScopusDb(sourceDbId)) {
      spsId = StringUtils.trimToEmpty(pubEle.attributeValue("source_id"));
    }
    if (PubXmlDbUtils.isEiDb(sourceDbId)) {
      eiId = StringUtils.trimToEmpty(pubEle.attributeValue("source_id"));
    }
    Long jfingerPrint = PubHashUtils.getJFingerPrint(jname, issn);

    PubDupParam param = new PubDupParam();
    param.setArticleNo(articleNo);
    param.setDoi(doi);
    param.setEnTitle(enTitle);
    param.setIsbn(isbn);
    param.setIsiId(isiId);
    param.setIssue(issue);
    param.setJid(jid);
    param.setPubType(pubType);
    param.setPubYear(pubYear);
    param.setSourceDbId(sourceDbId);
    param.setStartPage(startPage);
    param.setVolume(volume);
    param.setZhTitle(zhTitle);
    param.setEiId(eiId);
    param.setSpsId(spsId);
    param.setAuthorNames(auNames);
    param.setPatentNo(patentNo);
    param.setPatentOpenNo(patentOpenNo);
    param.setJname(jname);
    param.setIssn(issn);
    param.setConfName(confName);
    param.setJfingerPrint(jfingerPrint);
    return param;
  }

  @Override
  public List<Long> getDupPubByImportPubAll(Element pubEle, Long ownerId) throws ServiceException {

    try {
      PubDupParam param = buildImportPubDupParam(pubEle);
      return this.covertDupFieldToId(this.getDupPubField(param, ownerId));
    } catch (Exception e) {
      logger.error("getDupPubByImportPubAll成果导入查重", e);
      throw new ServiceException("getDupPubByImportPubAll成果导入查重", e);
    }
  }

  @Override
  public List<Long> getStrictDupPubByImportPub(Element pubEle, Long ownerId) throws ServiceException {
    return this.getStrictDupPubByImportPub(pubEle, ownerId, RolPubDupFields.NORMAL_STATUS);
  }

  @Override
  public List<Long> getStrictDupPubByImportPub(Element pubEle, Long ownerId, Integer status) throws ServiceException {
    try {
      PubDupParam param = buildImportPubDupParam(pubEle);
      Map<Integer, List<RolPubDupFields>> dupmap = this.getDupPubField(param, ownerId, null, status);
      Map<Integer, List<Long>> dupIdsMap = this.covertDupFieldToIdMap(dupmap);
      if (MapUtils.isNotEmpty(dupIdsMap) && CollectionUtils.isNotEmpty(dupIdsMap.get(1))) {
        return dupIdsMap.get(1);
      }
      return null;
    } catch (Exception e) {
      logger.error("getDupPubByImportPubAll成果导入查重", e);
      throw new ServiceException("getDupPubByImportPubAll成果导入查重", e);
    }
  }

  @Override
  public void removeById(Long pubId) throws ServiceException {

    try {
      this.pubDupDao.deleteById(pubId);
    } catch (Exception e) {
      logger.error("删除查重数据，删除成果时，将此数据删除", e);
      throw new ServiceException("删除查重数据，删除成果时，将此数据删除", e);
    }
  }

  @Override
  public void setPubCanDup(Long pubId, boolean canDup) throws ServiceException {
    try {
      RolPubDupFields dupFields = this.pubDupDao.getPubDupFields(pubId);
      if (dupFields != null) {
        if (canDup) {
          dupFields.setStatus(1);
        } else {
          dupFields.setStatus(0);
        }
        this.pubDupDao.save(dupFields);
      }
    } catch (Exception e) {
      logger.error("设置成果能够参与查重", e);
      throw new ServiceException("设置成果能够参与查重", e);
    }
  }

  @Override
  public void setPubCanDupAndArticleType(Long pubId, boolean canDup, Integer articleType) throws ServiceException {
    try {
      RolPubDupFields dupFields = this.pubDupDao.getPubDupFields(pubId);
      if (dupFields != null) {
        if (canDup) {
          dupFields.setStatus(1);
        } else {
          dupFields.setStatus(0);
        }
        dupFields.setArticleType(articleType);
        this.pubDupDao.save(dupFields);
      }
    } catch (Exception e) {
      logger.error("设置成果能够参与查重", e);
      throw new ServiceException("设置成果能够参与查重", e);
    }
  }

  @Override
  public RolPubDupFields getPubDupFields(Long pubId) throws ServiceException {

    try {
      return this.pubDupDao.getPubDupFields(pubId);
    } catch (Exception e) {
      logger.error("获取成果查重信息", e);
      throw new ServiceException("获取成果查重信息", e);
    }
  }

  @Override
  public void setDupDisabled(List<Long> pubIds) throws ServiceException {
    try {
      this.pubDupDao.setDupDisabled(pubIds);
    } catch (Exception e) {
      logger.error("set un dup status", e);
      throw new ServiceException("set un dup status", e);
    }
  }

  @Override
  public RolPubDupFields get(Long id) throws ServiceException {
    return pubDupDao.get(id);
  }

  @Override
  public void updateDupOwner(Long pubId, Long ownerId) throws ServiceException {
    pubDupDao.updatePubDupOwner(pubId, ownerId);
  }

}
