package com.smate.sie.center.task.pdwh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.sie.center.task.dao.SiePatDupFieldsDao;
import com.smate.sie.center.task.model.PatDupParam;
import com.smate.sie.center.task.model.SiePatDupFields;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.pub.dto.PatentInfoDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 专利查重服务.
 * 
 * @author jszhou
 *
 */
@Service("patDupFieldsService")
@Transactional(rollbackFor = Exception.class)
public class SiePatDupFieldsServiceImpl implements SiePatDupFieldsService {

  /**
   * 
   */
  private static final long serialVersionUID = 14475245675367915L;
  private final Logger logger = LoggerFactory.getLogger(SiePatDupFieldsServiceImpl.class);
  @Autowired
  private SiePatDupFieldsDao patDupDao;

  @Override
  public Map<Integer, List<Long>> getDupPatByImportPat(PubJsonDTO pubJson) throws SysServiceException {

    try {
      PatDupParam param = buildImportPatDupParam(pubJson);
      return this.getDupPat(param, pubJson.insId);
    } catch (Exception e) {
      logger.error("getDupPatByImportPat专利导入查重", e);
      throw new SysServiceException("getDupPatByImportPat专利导入查重", e);
    }
  }


  @Override
  public Map<Integer, List<Long>> getDupPat(PatDupParam param, Long ownerId) throws SysServiceException {
    try {
      return this.covertDupFieldToIdMap(this.getDupPatField(param, ownerId));
    } catch (Exception e) {
      logger.error("getDupPat保查询重复专利,key=1严格，key=2宽松，查询到严格的，直接返回", e);
      throw new SysServiceException("getDupPat保查询重复专利,key=1严格，key=2宽松，查询到严格的，直接返回", e);
    }
  }

  @Override
  public Map<Integer, List<SiePatDupFields>> getDupPatField(PatDupParam param, Long ownerId)
      throws SysServiceException {
    try {
      return this.getDupPatField(param, ownerId, SiePatDupFields.NORMAL_STATUS);
    } catch (Exception e) {
      logger.error("getDupPat保查询重复专利,key=1严格，key=2宽松", e);
      throw new SysServiceException("getDupPat保查询重复专利,key=1严格，key=2宽松", e);
    }
  }

  @Override
  public Map<Integer, List<SiePatDupFields>> getDupPatField(PatDupParam param, Long ownerId, Integer status)
      throws SysServiceException {
    try {
      Map<Integer, List<SiePatDupFields>> map = new HashMap<Integer, List<SiePatDupFields>>();
      // 专利申请号查重
      List<SiePatDupFields> dupList = this.getDupByPatent(param, ownerId, status);
      if (CollectionUtils.isNotEmpty(dupList)) {
        map.put(1, dupList);
        return map;
      }
      // 标题查重
      return this.getTitleDupPat(param, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupPub保查询重复专利,key=1严格，key=2宽松", e);
      throw new SysServiceException("getDupPub保查询重复专利,key=1严格，key=2宽松", e);
    }
  }

  /**
   * 专利申请号查重.
   *
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  private List<SiePatDupFields> getDupByPatent(PatDupParam param, Long ownerId, Integer status)
      throws SysServiceException {
    Assert.notNull(ownerId, "ownerid不能为空");
    Assert.notNull(param, "param不能为空");
    try {
      Long patentNoHash = PubHashUtils.cleanPatentNoHash(param.getPatentNo());
      return getDupByPatent(patentNoHash, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupByPatent查询重复专利", e);
      throw new SysServiceException("getDupByPatent查询重复专利", e);
    }
  }

  /**
   * 专利查重.
   * 
   * @param ownerId
   * @param patentNoHash
   * @return
   */
  private List<SiePatDupFields> getDupByPatent(Long patentNoHash, Long ownerId, Integer status)
      throws SysServiceException {
    try {
      return patDupDao.findDupByPatent(patentNoHash, ownerId, status);
    } catch (Exception e) {
      logger.error("getDupByPatent查询重复专利", e);
      throw new SysServiceException("getDupByPatent查询重复专利", e);
    }
  }

  /**
   * 标题查询重复专利.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  private Map<Integer, List<SiePatDupFields>> getTitleDupPat(PatDupParam param, Long ownerId, Integer status)
      throws SysServiceException {
    try {
      Assert.notNull(ownerId, "ownerid不能为空");
      Assert.notNull(param, "param不能为空");
      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());
      return this.getTitleDupPat(zhTitleHash, enTitleHash, ownerId, param.getSourceDbId(), status, param.getPubYear());
    } catch (Exception e) {
      logger.error("宽松查询重复专利", e);
      throw new SysServiceException("宽松查询重复专利", e);
    }
  }

  /**
   * 标题查询重复专利.
   * 
   * @param zhTitleHash
   * @param enTitleHash
   * @param ownerId
   * @param sourceDbId
   * 
   * @return
   * @throws ServiceException
   */
  private Map<Integer, List<SiePatDupFields>> getTitleDupPat(Long zhTitleHash, Long enTitleHash, Long ownerId,
      Integer sourceDbId, Integer status, Integer pubYear) throws SysServiceException {
    try {
      List<SiePatDupFields> dupIds = this.patDupDao.findDupByTitle(zhTitleHash, enTitleHash, ownerId, status, pubYear);
      Map<Integer, List<SiePatDupFields>> map = new HashMap<Integer, List<SiePatDupFields>>();
      if (CollectionUtils.isNotEmpty(dupIds)) {
        for (int i = 0; i < dupIds.size(); i++) {
          SiePatDupFields obj = dupIds.get(i);
          List<SiePatDupFields> dupList = map.get(2) == null ? new ArrayList<SiePatDupFields>() : map.get(2);
          dupList.add(obj);
          map.put(2, dupList);
        }
      }
      return map;
    } catch (Exception e) {
      logger.error("getTitleDupPat标题查询重复专利", e);
      throw new SysServiceException("getTitleDupPat标题查询重复专利", e);
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
  private PatDupParam buildImportPatDupParam(PubJsonDTO pubJson) throws SysServiceException, PubException {
    PatentInfoDTO a = JacksonUtils.jsonObject(pubJson.pubTypeInfo.toJSONString(), PatentInfoDTO.class);
    String patentNo = StringUtils.trimToEmpty(a.getApplicationNo());
    String zhTitle = pubJson.title;
    String[] dates = DateUtils.splitToYearMothDayByStr(a.getApplicationDate());
    Integer pubYear = null;
    if (dates != null && NumberUtils.isDigits(dates[0])) {
      pubYear = Integer.valueOf(dates[0]);
    }
    Integer sourceDbId = pubJson.srcDbId;
    PatDupParam param = new PatDupParam();
    param.setSourceDbId(sourceDbId);
    param.setZhTitle(zhTitle);
    param.setEnTitle(zhTitle);
    param.setPubYear(pubYear);
    param.setPatentNo(patentNo);
    return param;
  }

  /**
   * 将PatDupFields的ID取出.
   * 
   * @param dupmap
   * @return
   */
  public Map<Integer, List<Long>> covertDupFieldToIdMap(Map<Integer, List<SiePatDupFields>> dupmap) {
    Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
    if (MapUtils.isEmpty(dupmap)) {
      return map;
    }
    for (int i = 1; i <= 2; i++) {
      if (CollectionUtils.isNotEmpty(dupmap.get(i))) {
        List<Long> dupIds = new ArrayList<Long>();
        for (SiePatDupFields dupFields : dupmap.get(i)) {
          dupIds.add(dupFields.getPatId());
        }
        map.put(i, dupIds);
      }
    }
    return map;
  }

  @Override
  public SiePatDupFields savePatDupFields(PatDupParam param, Long patId, Long ownerId, boolean canDup)
      throws SysServiceException {
    Integer status = SiePatDupFields.DELETE_STATUS;
    if (canDup) {
      status = SiePatDupFields.NORMAL_STATUS;
    }
    return this.savePatDupFields(param, patId, ownerId, status);
  }

  @Override
  public SiePatDupFields savePatDupFields(PatDupParam param, Long patId, Long ownerId, Integer status)
      throws SysServiceException {
    try {
      // patent_no
      String patentNo = XmlUtil.getTrimBlankLower(param.getPatentNo());
      Long patentNoHash = PubHashUtils.cleanPatentNoHash(patentNo);

      // 标题
      Long zhTitleHash = PubHashUtils.cleanTitleHash(param.getZhTitle());
      Long enTitleHash = PubHashUtils.cleanTitleHash(param.getEnTitle());
      SiePatDupFields dupFields = this.patDupDao.getPatDupFields(patId);
      if (dupFields == null) {
        dupFields = new SiePatDupFields();
      }
      dupFields.setPatId(patId);
      dupFields.setSourceDbId(param.getSourceDbId());
      dupFields.setOwnerId(ownerId);
      dupFields.setEnTitleHash(enTitleHash);
      dupFields.setZhTitleHash(zhTitleHash);
      dupFields.setStatus(status);
      dupFields.setPubYear(param.getPubYear());
      dupFields.setPatentNoHash(patentNoHash);
      dupFields.setPatentNo(patentNo);
      this.patDupDao.save(dupFields);
      return dupFields;
    } catch (Exception e) {
      logger.error("保存成果查重字段pubId=" + patId, e);
      throw new SysServiceException("保存专利查重字段patId=" + patId, e);
    }
  }

  @Override
  public boolean getDupPatStatus(PubJsonDTO pubJson) {
    boolean flag = true;
    try {
      Map<Integer, List<Long>> dupMap = getDupPatByImportPat(pubJson);
      if (dupMap.size() > 0) {
        flag = false;
      }
    } catch (SysServiceException e) {
      e.printStackTrace();
    }
    return flag;
  }
}
