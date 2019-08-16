package com.smate.sie.center.task.pdwh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.single.util.pub.PubXmlDbUtils;
import com.smate.center.task.utils.PubXmlTypeUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.sie.center.task.dao.SiePubDupFieldsDao;
import com.smate.sie.center.task.model.PubDupParam;
import com.smate.sie.center.task.model.SiePubDupFields;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.pub.dto.BookInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.ConferencePaperDTO;
import com.smate.sie.core.base.utils.pub.dto.JournalInfoDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果查重服务.
 * 
 * @author jszhou
 */
@Service("siePubDupFieldsService")
@Transactional(rollbackFor = Exception.class)
public class SiePubDupFieldsServiceImpl implements SiePubDupFieldsService {

  /**
   * 
   */
  private static final long serialVersionUID = 14475245675367915L;
  private final Logger logger = LoggerFactory.getLogger(SiePubDupFieldsServiceImpl.class);
  @Autowired
  private SiePubDupFieldsDao pubDupDao;

  /**
   * 查重同库重复的成果.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws SysServiceException
   */
  private List<SiePubDupFields> getDupBySourceId(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws SysServiceException {

    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    try {
      Long sourceDbId = param.getSourceDbId();
      // source id
      Long isiIdHash = PubHashUtils.cleanSourceIdHash(param.getIsiId());
      Long eiIdHash = PubHashUtils.cleanSourceIdHash(param.getEiId());
      Long spsIdHash = PubHashUtils.cleanSourceIdHash(param.getSpsId());

      return getDupBySourceId(sourceDbId, isiIdHash, eiIdHash, spsIdHash, ownerId, excludePubId, status);
    } catch (Exception e) {
      logger.error("getDupBySourceId查询重复成果", e);
      throw new SysServiceException("getDupBySourceId查询重复成果", e);
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
  private List<SiePubDupFields> getDupBySourceId(Long sourceDbId, Long isiIdHash, Long eiIdHash, Long spsIdHash,
      Long ownerId, Long excludePubId, Integer status) throws SysServiceException {

    Assert.notNull(ownerId, "ownerid不能为空");

    try {
      List<SiePubDupFields> dupList = null;

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
      throw new SysServiceException("getDupBySourceId查询重复成果", e);
    }
  }

  /**
   * 查重doi重复的成果.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws SysServiceException
   */
  private List<SiePubDupFields> getDupByDoiId(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws SysServiceException {
    try {
      // doi
      Long doiHash = PubHashUtils.cleanDoiHash(param.getDoi());
      return getDupByDoiId(param.getPubYear(), doiHash, ownerId, excludePubId, status);
    } catch (Exception e) {
      logger.error("getDupByDoiId查询重复成果", e);
      throw new SysServiceException("getDupByDoiId查询重复成果", e);
    }
  }

  /**
   * @param param
   * @param ownerId
   * @param excludePubId
   * @param doiHash
   * @return
   */
  private List<SiePubDupFields> getDupByDoiId(Integer pubYear, Long doiHash, Long ownerId, Long excludePubId,
      Integer status) throws SysServiceException {
    try {
      List<SiePubDupFields> dupList = null;
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
      throw new SysServiceException("getDupByDoiId查询重复成果", e);
    }
  }

  /**
   * 期刊查重.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws SysServiceException
   */
  private List<SiePubDupFields> getDupByJournal(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws SysServiceException {
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

      Long sourceDbId = param.getSourceDbId();
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
      throw new SysServiceException("getDupByJournal查询重复成果", e);
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
  private List<SiePubDupFields> getDupByJournal(Long issueHash, Long jaFingerPrint, Long jpFingerPrint, Long sourceDbId,
      Long zhTitleHash, Long enTitleHash, Long auNameHash, Long jFingerPrint, Integer pubType, Integer pubYear,
      Long ownerId, Long excludePubId, Integer status) throws SysServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(pubType, "PubType不能为空");
    // 是否期刊
    if (!PubXmlTypeUtils.isJournalType(pubType)) {
      return null;
    }
    try {
      List<SiePubDupFields> dupList = null;
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
      throw new SysServiceException("getDupByJournal查询重复成果", e);
    }
  }

  /**
   * 会议查重.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws SysServiceException
   */
  private List<SiePubDupFields> getDupByConference(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws SysServiceException {
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
      Long sourceDbId = param.getSourceDbId();
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
      throw new SysServiceException("getDupByConference查询重复成果", e);
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
  private List<SiePubDupFields> getDupByConference(Long zhTitleHash, Long enTitleHash, Long sourceDbId, Long auNameHash,
      Long confnHash, Integer pubType, Integer pubYear, Long cpFingerPrint, Long caFingerPrint, Long cvaFingerPrint,
      Long cvpFingerPrint, Long ownerId, Long excludePubId, Integer status) throws SysServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    // 是否会议
    if (!PubXmlTypeUtils.isConference(pubType)) {
      return null;
    }
    try {
      List<SiePubDupFields> dupList = null;
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
      throw new SysServiceException("getDupByConference查询重复成果", e);
    }
  }

  /**
   * 著作查重.
   * 
   * @param param
   * @param ownerId
   * @param excludePubId
   * @return
   * @throws SysServiceException
   */
  private List<SiePubDupFields> getDupByBook(PubDupParam param, Long ownerId, Long excludePubId, Integer status)
      throws SysServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    Assert.notNull(param.getPubType(), "PubType不能为空");
    // 是否是著作
    if (param.getPubType() != 2) {
      return null;
    }
    try {
      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());
      Integer pubType = param.getPubType();
      Long bzFingerPrint = PubHashUtils.getBzFingerPrint(param.getIsbn(), param.getZhTitle());
      Long beFingerPrint = PubHashUtils.getBeFingerPrint(param.getIsbn(), param.getEnTitle());

      return pubDupDao.findDupByBook(zhTitleHash, enTitleHash, pubType, param.getPubYear(), bzFingerPrint,
          beFingerPrint, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupByBook查询重复成果", e);
      throw new SysServiceException("getDupByBook查询重复成果", e);
    }
  }

  // /** 专利查重.
  // *
  // * @param param
  // * @param ownerId
  // * @param excludePubId
  // * @return
  // * @throws SysServiceException
  // */
  // private List<SiePubDupFields> getDupByPatent(PubDupParam param, Long
  // ownerId, Long excludePubId, Integer status)
  // throws SysServiceException {
  // Assert.notNull(ownerId, "ownerid不能为空");
  // Assert.notNull(param, "param不能为空");
  // Assert.notNull(param.getPubType(), "PubType不能为空");
  // try {
  // // 是否会议
  // if (!PubXmlTypeUtils.isPatent(param.getPubType())) {
  // return null;
  // }
  // Integer pubType = param.getPubType();
  // Long patentHash = PubHashUtils.cleanPatentNoHash(param.getPatentNo());
  // Long patentOpenHash =
  // PubHashUtils.cleanPatentNoHash(this.cleanPatentOpenNo(param.getPatentOpenNo()));
  // return getDupByPatent(patentHash, patentOpenHash, ownerId, pubType,
  // status);
  // } catch (Exception e) {
  // logger.error("getDupByPatent查询重复成果", e);
  // throw new SysServiceException("getDupByPatent查询重复成果", e);
  // }
  // }

  // 暂时用于国内比较，部分国外专利号构成不同；专利文献号构成详见SCM-9260
  @SuppressWarnings("unused")
  private String cleanPatentOpenNo(String patentOpenNo) {
    String str = XmlUtil.getTrimBlankLower(patentOpenNo);
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    // 获取形如cn102102675的hash值
    Pattern pattern = Pattern.compile("^(cn)(\\d+)");
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      return matcher.group();
    } else {
      return null;
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
  @SuppressWarnings("unused")
  private List<SiePubDupFields> getDupByPatent(Long patentHash, Long patentOpenHash, Long ownerId, Integer pubType,
      Integer status) throws SysServiceException {
    // 是否会议
    if (!PubXmlTypeUtils.isPatent(pubType)) {
      return null;
    }
    try {
      return pubDupDao.findDupByPatent(patentHash, patentOpenHash, pubType, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupByPatent查询重复成果", e);
      throw new SysServiceException("getDupByPatent查询重复成果", e);
    }
  }

  /**
   * 标题查询重复成果.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  private Map<Integer, List<SiePubDupFields>> getTitleDupPub(PubDupParam param, Long ownerId, Integer status)
      throws SysServiceException {

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
      throw new SysServiceException("宽松查询重复成果", e);
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
   * @throws SysServiceException
   */
  private Map<Integer, List<SiePubDupFields>> getTitleDupPub(Integer pubType, Integer pubYear, Long zhTitleHash,
      Long enTitleHash, Long ownerId, Long sourceDbId, Long excludePubId, Integer status) throws SysServiceException {
    try {
      List<SiePubDupFields> dupIds =
          this.pubDupDao.findDupByTitle(zhTitleHash, enTitleHash, pubYear, pubType, ownerId, status);
      Map<Integer, List<SiePubDupFields>> map = new HashMap<Integer, List<SiePubDupFields>>();
      if (CollectionUtils.isNotEmpty(dupIds)) {
        // dupIds = this.removeExcPub(dupIds, excludePubId);
        for (int i = 0; i < dupIds.size(); i++) {
          SiePubDupFields obj = dupIds.get(i);
          List<SiePubDupFields> dupList = map.get(2) == null ? new ArrayList<SiePubDupFields>() : map.get(2);
          dupList.add(obj);
          map.put(2, dupList);
        }
      }
      return map;
    } catch (Exception e) {
      logger.error("getTitleDupPub标题查询重复成果", e);
      throw new SysServiceException("getTitleDupPub标题查询重复成果", e);
    }
  }

  /**
   * 移除排除的数据.
   * 
   * @param dupList
   * @param excludePubId
   * @return
   */
  @SuppressWarnings("unlikely-arg-type")
  private List<SiePubDupFields> removeExcPub(List<SiePubDupFields> dupList, Long excludePubId) {
    if (CollectionUtils.isNotEmpty(dupList) && excludePubId != null && dupList.contains(excludePubId)) {
      for (int i = 0; i < dupList.size(); i++) {
        SiePubDupFields dup = dupList.get(i);
        if (excludePubId.equals(dup.getPubId())) {
          dupList.remove(i);
          break;
        }
      }
    }
    return dupList;
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(Long pubId) throws SysServiceException {

    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(pubId));
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new SysServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(Long pubId) throws SysServiceException {
    return this.getDupPubField(pubId, SiePubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(Long pubId, Integer status) throws SysServiceException {
    try {
      SiePubDupFields dupFields = this.pubDupDao.getPubDupFields(pubId);
      if (dupFields == null) {
        return null;
      }
      return this.getDupPubField(dupFields, dupFields.getOwnerId(), pubId, status);
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new SysServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  /**
   * 将PubDupFields的ID取出.
   * 
   * @param dupmap
   * @return
   */
  public Map<Integer, List<Long>> covertDupFieldToIdMap(Map<Integer, List<SiePubDupFields>> dupmap) {
    Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
    if (MapUtils.isEmpty(dupmap)) {
      return map;
    }
    for (int i = 1; i <= 2; i++) {
      if (CollectionUtils.isNotEmpty(dupmap.get(i))) {
        List<Long> dupIds = new ArrayList<Long>();
        for (SiePubDupFields dupFields : dupmap.get(i)) {
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
  public List<Long> covertDupFieldToId(Map<Integer, List<SiePubDupFields>> dupmap) {
    List<Long> dupIds = new ArrayList<Long>();
    if (MapUtils.isEmpty(dupmap)) {
      return dupIds;
    }
    for (int i = 1; i <= 2; i++) {
      if (CollectionUtils.isNotEmpty(dupmap.get(i))) {
        for (SiePubDupFields dupFields : dupmap.get(i)) {
          dupIds.add(dupFields.getPubId());
        }
      }
    }
    return dupIds;
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(SiePubDupFields dupFields) throws SysServiceException {
    try {

      return getDupPub(dupFields, dupFields.getOwnerId());
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new SysServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(SiePubDupFields dupFields, Long owner) throws SysServiceException {
    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(dupFields, owner, null));
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new SysServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

  @Override
  public List<Long> getDupPubIds(Long pubId) throws SysServiceException {
    try {

      return this.covertDupFieldToId(getDupPubField(pubId));
    } catch (Exception e) {
      logger.error("getDupPubAll查询重复成果Id.", e);
      throw new SysServiceException("getDupPubAll查询重复成果Id.", e);
    }
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(PubDupParam param, Long ownerId) throws SysServiceException {
    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(param, ownerId));
    } catch (Exception e) {
      logger.error("getDupPub保查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回", e);
      throw new SysServiceException("getDupPub保查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回", e);
    }
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(PubDupParam param, Long ownerId)
      throws SysServiceException {
    try {
      return this.getDupPubField(param, ownerId, null, SiePubDupFields.NORMAL_STATUS);
    } catch (Exception e) {
      logger.error("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
      throw new SysServiceException("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId)
      throws SysServiceException {
    return this.getDupPubField(param, ownerId, excludeId, SiePubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId,
      Integer status) throws SysServiceException {
    try {
      Map<Integer, List<SiePubDupFields>> map = new HashMap<Integer, List<SiePubDupFields>>();
      // souce_id查重
      List<SiePubDupFields> dupList = this.getDupBySourceId(param, ownerId, excludeId, status);
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
      // 著作查重
      dupList = this.getDupByBook(param, ownerId, excludeId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 标题查重
      return this.getTitleDupPub(param, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
      throw new SysServiceException("getDupPub保查询重复成果,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(SiePubDupFields dupFields, Long ownerId, Long excludeId)
      throws SysServiceException {
    return this.getDupPubField(dupFields, ownerId, excludeId, SiePubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<SiePubDupFields>> getDupPubField(SiePubDupFields dupFields, Long ownerId, Long excludeId,
      Integer status) throws SysServiceException {
    try {
      Map<Integer, List<SiePubDupFields>> map = new HashMap<Integer, List<SiePubDupFields>>();
      // souce_id查重
      List<SiePubDupFields> dupList = this.getDupBySourceId(dupFields.getSourceDbId(), dupFields.getIsiHash(),
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
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 标题查重
      return this.getTitleDupPub(dupFields.getPubType(), dupFields.getPubYear(), dupFields.getZhTitleHash(),
          dupFields.getEnTitleHash(), ownerId, dupFields.getSourceDbId(), excludeId, status);
    } catch (Exception e) {
      logger.error("getDupPubField保查询重复成果,key=1严格，key=2宽松", e);
      throw new SysServiceException("getDupPubField保查询重复成果,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public SiePubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId, boolean canDup)
      throws SysServiceException {
    Integer status = SiePubDupFields.DELETE_STATUS;
    if (canDup) {
      status = SiePubDupFields.NORMAL_STATUS;
    }
    return this.savePubDupFields(param, pubType, pubId, ownerId, status);
  }

  @Override
  public SiePubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId, Integer status)
      throws SysServiceException {
    try {
      SiePubDupFields dupFields = this.pubDupDao.getPubDupFields(pubId);
      if (dupFields == null) {
        dupFields = new SiePubDupFields();
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

      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());
      // 著作指纹
      Long bzFingerPrint = PubHashUtils.getBzFingerPrint(param.getIsbn(), param.getZhTitle());
      Long beFingerPrint = PubHashUtils.getBeFingerPrint(param.getIsbn(), param.getEnTitle());
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
      dupFields.setZhTitleHash(zhTitleHash);
      dupFields.setArticleType(1);
      dupFields.setConfnHash(confnHash);
      dupFields.setIssn(StringUtils.substring(param.getIssn(), 0, 40));
      dupFields.setJfingerPrint(jfingerPrint);
      dupFields.setStatus(status);
      dupFields.setBzFingerPrint(bzFingerPrint);
      dupFields.setBeFingerPrint(beFingerPrint);
      this.pubDupDao.save(dupFields);
      return dupFields;
    } catch (Exception e) {
      logger.error("保存成果查重字段pubId=" + pubId, e);
      throw new SysServiceException("保存成果查重字段pubId=" + pubId, e);
    }
  }

  @Override
  public Map<Integer, List<Long>> getDupPubByImportPub(PubJsonDTO pubJson) throws SysServiceException {

    try {
      PubDupParam param = buildImportPubDupParam(pubJson);
      return this.getDupPub(param, pubJson.insId);
    } catch (Exception e) {
      logger.error("getDupPubByImportPub成果导入查重", e);
      throw new SysServiceException("getDupPubByImportPub成果导入查重", e);
    }
  }


  /**
   * 构造导入成果查询条件.
   * 
   * @param pubEle
   * @return
   * @throws SysServiceException
   * @throws PubException
   */
  private PubDupParam buildImportPubDupParam(PubJsonDTO pubJson) throws SysServiceException, PubException {
    PubDupParam param = new PubDupParam();
    String articleNo = "";
    Long jid = null;
    Integer pubType = pubJson.pubTypeCode;
    if (pubType == 4) {// 期刊论文
      if (pubJson.pubTypeInfo != null) {
        JournalInfoDTO a = JacksonUtils.jsonObject(pubJson.pubTypeInfo.toJSONString(), JournalInfoDTO.class);
        String volume = StringUtils.trimToEmpty(a.getVolumeNo());
        param.setVolume(volume);
        String issue = StringUtils.trimToEmpty(a.getIssue());
        param.setIssue(issue);
        String issn = StringUtils.trimToEmpty(a.getISSN());
        param.setIssn(XmlUtil.buildStandardIssn(issn));
        String jname = StringUtils.trimToEmpty(a.getName());
        param.setJname(jname);
        Long jfingerPrint = PubHashUtils.getJFingerPrint(jname, issn);
        param.setJfingerPrint(jfingerPrint);
        articleNo = a.getArticleNo();
      }
    }
    if (pubType == 2) {// 著作/书籍章节
      if (pubJson.pubTypeInfo != null) {
        BookInfoDTO a = JacksonUtils.jsonObject(pubJson.pubTypeInfo.toJSONString(), BookInfoDTO.class);
        String isbn = StringUtils.trimToEmpty(a.getISBN());
        param.setIsbn(isbn);
        a.getArticleNo();
      }
    }
    String auNames = StringUtils.trimToEmpty(pubJson.authorNames);
    String zhTitle = pubJson.title;
    String doi = StringUtils.trimToEmpty(pubJson.doi);
    if (pubType == 3) {// 会议论文
      if (pubJson.pubTypeInfo != null) {
        ConferencePaperDTO a = JacksonUtils.jsonObject(pubJson.pubTypeInfo.toJSONString(), ConferencePaperDTO.class);
        String confName = StringUtils.trimToEmpty(a.getName());
        param.setConfName(confName);
      }
    }
    String isiId = null;
    String eiId = null;
    String spsId = null;
    String[] dates = DateUtils.splitToYearMothDayByStr(pubJson.publishDate);
    Integer pubYear = null;
    if (dates != null && NumberUtils.isDigits(dates[0])) {
      pubYear = Integer.valueOf(dates[0]);
    }
    Integer sourceDbId = pubJson.srcDbId;
    param.setSourceDbId(NumberUtils.toLong(sourceDbId != null ? sourceDbId.toString() : ""));
    param.setArticleNo(articleNo);
    param.setDoi(doi);
    param.setEnTitle(zhTitle);
    param.setIsiId(isiId);
    param.setJid(jid);
    param.setPubType(pubType);
    param.setPubYear(pubYear);
    param.setZhTitle(zhTitle);
    param.setEiId(eiId);
    param.setSpsId(spsId);
    param.setAuthorNames(auNames);
    return param;
  }

  @Override
  public SiePubDupFields getPubDupFields(Long pubId) throws SysServiceException {

    try {
      return this.pubDupDao.getPubDupFields(pubId);
    } catch (Exception e) {
      logger.error("获取成果查重信息", e);
      throw new SysServiceException("获取成果查重信息", e);
    }
  }

  @Override
  public void setDupDisabled(List<Long> pubIds) throws SysServiceException {
    try {
      this.pubDupDao.setDupDisabled(pubIds);
    } catch (Exception e) {
      logger.error("set un dup status", e);
      throw new SysServiceException("set un dup status", e);
    }
  }

  @Override
  public SiePubDupFields get(Long id) throws SysServiceException {
    return pubDupDao.get(id);
  }

  @Override
  public void updateDupOwner(Long pubId, Long ownerId) throws SysServiceException {
    pubDupDao.updatePubDupOwner(pubId, ownerId);
  }

  @Override
  public boolean getDupPubStatus(PubJsonDTO pubJson) {
    boolean flag = true;
    try {
      Map<Integer, List<Long>> dupMap = getDupPubByImportPub(pubJson);
      if (dupMap.size() > 0) {
        flag = false;
      }
    } catch (SysServiceException e) {
      e.printStackTrace();
    }
    return flag;
  }

}
