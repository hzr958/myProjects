package com.smate.center.task.service.rcmd.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.rcmd.quartz.PubDupDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubDupFields;
import com.smate.center.task.single.util.pub.PubXmlDbUtils;
import com.smate.center.task.utils.PubXmlTypeUtils;

@Service("pubDupService")
@Transactional(rollbackFor = Exception.class)
public class PubDupServiceImpl implements PubDupService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubDupDao pubDupDao;

  @Override
  public Map<Integer, List<Long>> getDupPub(PubDupFields dupFields) throws ServiceException {
    try {

      return getDupPub(dupFields, dupFields.getOwnerId());
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new ServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }



  @Override
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupFields dupFields, Long ownerId, Long excludeId)
      throws ServiceException {
    return this.getDupPubField(dupFields, ownerId, excludeId, PubDupFields.NORMAL_STATUS);
  }

  @Override
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupFields dupFields, Long ownerId, Long excludeId,
      Integer status) throws ServiceException {
    try {
      Map<Integer, List<PubDupFields>> map = new HashMap<Integer, List<PubDupFields>>();
      // souce_id查重
      List<PubDupFields> dupList = this.getDupBySourceId(dupFields.getSourceDbId(), dupFields.getIsiHash(),
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
  private Map<Integer, List<PubDupFields>> getTitleDupPub(Integer pubType, Integer pubYear, Long zhTitleHash,
      Long enTitleHash, Long ownerId, Integer sourceDbId, Long excludePubId, Integer status) throws ServiceException {

    try {
      List<PubDupFields> dupIds =
          this.pubDupDao.findDupByTitle(zhTitleHash, enTitleHash, pubYear, pubType, ownerId, status);
      Map<Integer, List<PubDupFields>> map = new HashMap<Integer, List<PubDupFields>>();

      if (CollectionUtils.isNotEmpty(dupIds)) {
        // 排除pubid
        dupIds = this.removeExcPub(dupIds, excludePubId);
        for (int i = 0; i < dupIds.size(); i++) {
          PubDupFields obj = dupIds.get(i);
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
          List<PubDupFields> dupList = map.get(2) == null ? new ArrayList<PubDupFields>() : map.get(2);
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
   * 专利查重.
   * 
   * @param ownerId
   * @param pubType
   * @param patentHash
   * @return
   */
  private List<PubDupFields> getDupByPatent(Long patentHash, Long patentOpenHash, Long ownerId, Integer pubType,
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
  private List<PubDupFields> getDupByConference(Long zhTitleHash, Long enTitleHash, Integer sourceDbId, Long auNameHash,
      Long confnHash, Integer pubType, Integer pubYear, Long cpFingerPrint, Long caFingerPrint, Long cvaFingerPrint,
      Long cvpFingerPrint, Long ownerId, Long excludePubId, Integer status) throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    // 是否会议
    if (!PubXmlTypeUtils.isConference(pubType)) {
      return null;
    }
    try {
      List<PubDupFields> dupList = null;
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
  private List<PubDupFields> getDupByJournal(Long issueHash, Long jaFingerPrint, Long jpFingerPrint, Integer sourceDbId,
      Long zhTitleHash, Long enTitleHash, Long auNameHash, Long jFingerPrint, Integer pubType, Integer pubYear,
      Long ownerId, Long excludePubId, Integer status) throws ServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(pubType, "PubType不能为空");
    // 是否期刊
    if (!PubXmlTypeUtils.isJournalType(pubType)) {
      return null;
    }
    try {
      List<PubDupFields> dupList = null;
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
   * @param param
   * @param ownerId
   * @param excludePubId
   * @param doiHash
   * @return
   */
  private List<PubDupFields> getDupByDoiId(Integer pubYear, Long doiHash, Long ownerId, Long excludePubId,
      Integer status) throws ServiceException {
    try {
      List<PubDupFields> dupList = null;
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
   * 查重同库重复的成果.
   * 
   * @param ownerId
   * @param excludePubId
   * @param sourceDbId
   * @param isiIdHash
   * @param eiIdHash
   * @param spsIdHash
   */
  private List<PubDupFields> getDupBySourceId(Integer sourceDbId, Long isiIdHash, Long eiIdHash, Long spsIdHash,
      Long ownerId, Long excludePubId, Integer status) throws ServiceException {

    Assert.notNull(ownerId, "ownerid不能为空");

    try {
      List<PubDupFields> dupList = null;

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
   * 移除排除的数据.
   * 
   * @param dupList
   * @param excludePubId
   * @return
   */
  private List<PubDupFields> removeExcPub(List<PubDupFields> dupList, Long excludePubId) {
    if (CollectionUtils.isNotEmpty(dupList) && excludePubId != null && dupList.contains(excludePubId)) {
      for (int i = 0; i < dupList.size(); i++) {
        PubDupFields dup = dupList.get(i);
        if (excludePubId.equals(dup.getPubId())) {
          dupList.remove(i);
          break;
        }
      }
    }
    return dupList;
  }

  /**
   * 将PubDupFields的ID取出.
   * 
   * @param dupmap
   * @return
   */
  public Map<Integer, List<Long>> covertDupFieldToIdMap(Map<Integer, List<PubDupFields>> dupmap) {
    Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
    if (MapUtils.isEmpty(dupmap)) {
      return map;
    }
    for (int i = 1; i <= 2; i++) {
      if (CollectionUtils.isNotEmpty(dupmap.get(i))) {
        List<Long> dupIds = new ArrayList<Long>();
        for (PubDupFields dupFields : dupmap.get(i)) {
          dupIds.add(dupFields.getPubId());
        }
        map.put(i, dupIds);
      }
    }
    return map;
  }

  @Override
  public Map<Integer, List<Long>> getDupPub(PubDupFields dupFields, Long ownerId) throws ServiceException {
    try {
      return this.covertDupFieldToIdMap(this.getDupPubField(dupFields, ownerId, null));
    } catch (Exception e) {
      logger.error("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
      throw new ServiceException("getDupPub查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.", e);
    }
  }

}
