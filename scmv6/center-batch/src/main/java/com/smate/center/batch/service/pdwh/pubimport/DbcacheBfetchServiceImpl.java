package com.smate.center.batch.service.pdwh.pubimport;

import com.smate.center.batch.connector.dao.job.BatchJobsDao;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.constant.DbIdPriorityEnum;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubDupDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlToHandleDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXmlToHandle;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.process.pub.PdwhPubCitedTimesUpdateProcess;
import com.smate.center.batch.process.pub.PdwhPubImportSaveNewProcess;
import com.smate.center.batch.process.pub.PdwhPubImportUpdateProcess;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dbcacheBfetchService")
@Transactional(rollbackFor = Exception.class)
public class DbcacheBfetchServiceImpl implements DbcacheBfetchService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubDupDao pdwhPubDupDao;
  @Autowired
  private PdwhPubXmlToHandleDao pdwhPubXmlToHandleDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private BatchJobsDao batchJobsDao;

  private PdwhPubImportUpdateProcess pdwhPubImportUpdateProcess;

  private PdwhPubImportSaveNewProcess pdwhPubImportSaveNewProcess;

  private PdwhPubCitedTimesUpdateProcess pdwhPubCitedTimesUpdateProcess;

  @Override
  public PdwhPubXmlToHandle getXmlFileById(Long xmlId) {
    return this.pdwhPubXmlToHandleDao.get(xmlId);
  }

  @Override
  public void updatePubInfo(PdwhPublication pdwhPub, Map<String, Object> pdwhPubInfoMap, Long dupPubId)
      throws Exception {
    PdwhPubImportContext context = new PdwhPubImportContext();
    context.setInsId((Long) pdwhPubInfoMap.get("insId"));
    context.setDbId(pdwhPub.getDbId());
    context.setNow(new Date());
    context.setDupPubId(dupPubId);
    context.setPdwhPubInfoMap(pdwhPubInfoMap);
    context.setPriorityFlag(this.getPriorityFlag(pdwhPub.getDbId(), dupPubId));
    context.setOperation("update");// 当前操作为更新
    context.setReplaceFlag(0);// 设置为不会全部覆盖
    context.setCurrentPsnId((Long) pdwhPubInfoMap.get("operatePsnId"));// 2L为后台导入，其他则为从sns，rol检索同步至基准库

    this.pdwhPubImportUpdateProcess.start(pdwhPub, context);
  }

  /**
   * 计算此处更新是否需要覆盖相关成果内容(1：当前导入成果优先级大于等于当前，需要覆盖已保存xml文件；0：当前导入成果优先级小于当前， 不需要覆盖已保存xml文件)
   */
  private Integer getPriorityFlag(Integer newPubDbId, Long dupPubId) {
    PdwhPublication oldPdwhPub = this.pdwhPublicationDao.get(dupPubId);
    Assert.notNull(oldPdwhPub, "oldPdwhPub不能为空，pdwhPubId = " + dupPubId);
    Integer oldPubDbId = oldPdwhPub.getDbId();
    if (oldPubDbId == null) {// 如果原有dbId为空，则覆盖
      return 1;
    } else if (newPubDbId == null) {// 如果新dbId为空，则不覆盖
      return 0;
    } else {
      Integer oldPriority = DbIdPriorityEnum.getPriority(oldPubDbId);
      Integer newPriority = DbIdPriorityEnum.getPriority(newPubDbId);
      if (newPriority >= oldPriority) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  @Override
  public void saveNewPubInfo(PdwhPublication pdwhPub, Map<String, Object> pdwhPubInfoMap) throws Exception {
    PdwhPubImportContext context = new PdwhPubImportContext();
    context.setInsId((Long) pdwhPubInfoMap.get("insId"));
    context.setDbId(pdwhPub.getDbId());
    context.setNow(new Date());
    context.setPdwhPubInfoMap(pdwhPubInfoMap);
    context.setOperation("saveNew");// 当前操作为saveNew，保存新成果
    context.setCurrentPsnId((Long) pdwhPubInfoMap.get("operatePsnId"));// 2L为后台导入，其他则为从sns，rol检索同步至基准库
    this.pdwhPubImportSaveNewProcess.start(pdwhPub, context);

  }

  @Override
  public void saveNewPdwhPubInfo(PdwhPublication pdwhPub, Map<String, Object> pdwhPubInfoMap) throws Exception {
    PdwhPubImportContext context = new PdwhPubImportContext();
    context.setDbId(pdwhPub.getDbId());
    context.setNow(new Date());
    context.setPdwhPubInfoMap(pdwhPubInfoMap);
    context.setOperation("saveNew");// 当前操作为saveNew，保存新成果
    context.setCurrentPsnId((Long) pdwhPubInfoMap.get("operatePsnId"));// 2L为后台导入，其他则为从sns，rol检索同步至基准库
    this.pdwhPubCitedTimesUpdateProcess.start(pdwhPub, context);

  }

  @Override
  public void pdwhPubSaveSuccess(Long xmlId) {
    this.pdwhPubXmlToHandleDao.delete(xmlId);
  }

  @Override
  public void pdwhPubSaveError(PdwhPubXmlToHandle tmpPubXml) {
    tmpPubXml.setStatus(3);
    this.pdwhPubXmlToHandleDao.save(tmpPubXml);
  }

  @Override
  public void pdwhPubSaveInfoMissing(PdwhPubXmlToHandle tmpPubXml) {
    tmpPubXml.setStatus(4);
    this.pdwhPubXmlToHandleDao.save(tmpPubXml);
  }

  @Override
  public PdwhPublication wrapPdwhPubXmlInfo(String xmlString) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long getDupPub(PdwhPublication pdwhPub) {
    if (StringUtils.isNotBlank(pdwhPub.getDoi())) {
      List<Long> dupPubIds = this.getDupPubIdsByDoi(pdwhPub.getDoiHash());
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        return dupPubIds.get(0);
      }
    }

    if (StringUtils.isNotBlank(pdwhPub.getSourceId())) {
      List<Long> dupPubIds = this.getDupPubIdsBySourceId(pdwhPub.getSourceIdHash());
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        return dupPubIds.get(0);
      }
    }

    if (StringUtils.isNotBlank(pdwhPub.getPatentNo()) || StringUtils.isNotBlank(pdwhPub.getPatentOpenNo())) {
      List<Long> dupPubIds = this.getDupPubIdsByPatentInfo(pdwhPub.getPatentNoHash(), pdwhPub.getPatentOpenNoHash());
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        return dupPubIds.get(0);
      }
    }

    if (pdwhPub.getPubYear() == null || pdwhPub.getPubType() == null || pdwhPub.getPubYear() == 0) {
      return null;
    }

    List<Long> dupPubIds = this.getDupPubIdsByTitle(pdwhPub.getZhTitleHash(), pdwhPub.getEnTitleHash(),
        pdwhPub.getTitleHashValue(), pdwhPub.getPubYear(), pdwhPub.getPubType());
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }

    return null;
  }

  @Override
  public void constructBatchJobs(Long XmlId) throws ServiceException {
    try {
      // 构造存入v-batch_jobs表实体
      String jobContext = "{\"msg_id\":" + XmlId + "}\"";
      BatchJobs jobs = new BatchJobs();
      jobs.setJobContext(jobContext);
      jobs.setWeight("B");
      jobs.setStatus(0);
      jobs.setStrategy("pdwhpub3");
      jobs.setCreateTime(new Date());
      batchJobsDao.save(jobs);
    } catch (Exception e) {
      logger.error("构造存入v-batch_jobs表实体出错,xmlId=" + XmlId, e);
      throw new ServiceException("保存到v-batch_jobs表实体出错", e);
    }
  }

  private Map<String, Long> getHashKeyValue(PdwhPublication pdwhPub) {
    if (pdwhPub == null) {
      return null;
    }
    Map<String, Long> hashKeyValueMap = new HashMap<String, Long>();
    String zhTitle = PubHashUtils.cleanTitle(pdwhPub.getZhTitle());
    String enTitle = PubHashUtils.cleanTitle(pdwhPub.getEnTitle());
    String pubYear = String.valueOf(pdwhPub.getPubYear());
    String pubType = String.valueOf(pdwhPub.getPubType());

    String[] unionValues = new String[] {zhTitle, enTitle, pubYear, pubType};
    String[] titleValues = new String[] {zhTitle, enTitle};

    Long unionHashValue = PubHashUtils.fingerPrint(unionValues) == null ? 0L : PubHashUtils.fingerPrint(unionValues);
    Long titleHashValue = PubHashUtils.fingerPrint(titleValues) == null ? 0L : PubHashUtils.fingerPrint(titleValues);
    Long enTitleHash = HashUtils.getStrHashCode(enTitle);
    Long zhTitleHash = HashUtils.getStrHashCode(zhTitle);
    hashKeyValueMap.put("unionHashValue", unionHashValue); // 中英文，出版年份，成果类型的hash值
    hashKeyValueMap.put("titleHashValue", titleHashValue); // 中英文标题hash值
    hashKeyValueMap.put("enTitleHash", enTitleHash);
    hashKeyValueMap.put("zhTitleHash", zhTitleHash);

    return hashKeyValueMap;
  }

  private List<Long> getDupPubIdsByDoi(Long doiHash) {
    if (doiHash == null) {
      return null;
    }

    List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsByDoiHash(doiHash);
    return dupPubIds;
  }

  private List<Long> getDupPubIdsBySourceId(Long sourceIdHash) {
    if (sourceIdHash == null) {
      return null;
    }
    List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsBySourceIdHash(sourceIdHash);
    return dupPubIds;
  }

  private List<Long> getDupPubIdsByPatentInfo(Long patentNoHash, Long patentOpenNoHash) {
    if (patentNoHash == null && patentOpenNoHash == null) {
      return null;
    }
    List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsByPatentInfo(patentNoHash, patentOpenNoHash);
    return dupPubIds;
  }

  private List<Long> getDupPubIdsByTitle(Long zhTitleHash, Long enTitleHash, Long titleHashValue, Integer pubYear,
      Integer pubType) {
    if (zhTitleHash == null && enTitleHash == null && titleHashValue == null) {
      return null;
    }
    List<Long> dupPubIds =
        this.pdwhPubDupDao.getDupPubIdsByTitle(zhTitleHash, enTitleHash, titleHashValue, pubType, pubYear);

    return dupPubIds;
  }

  public PdwhPubImportUpdateProcess getPdwhPubImportUpdateProcess() {
    return pdwhPubImportUpdateProcess;
  }

  public void setPdwhPubImportUpdateProcess(PdwhPubImportUpdateProcess pdwhPubImportUpdateProcess) {
    this.pdwhPubImportUpdateProcess = pdwhPubImportUpdateProcess;
  }

  public PdwhPubImportSaveNewProcess getPdwhPubImportSaveNewProcess() {
    return pdwhPubImportSaveNewProcess;
  }

  public void setPdwhPubImportSaveNewProcess(PdwhPubImportSaveNewProcess pdwhPubImportSaveNewProcess) {
    this.pdwhPubImportSaveNewProcess = pdwhPubImportSaveNewProcess;
  }

  public PdwhPubCitedTimesUpdateProcess getPdwhPubCitedTimesUpdateProcess() {
    return pdwhPubCitedTimesUpdateProcess;
  }

  public void setPdwhPubCitedTimesUpdateProcess(PdwhPubCitedTimesUpdateProcess pdwhPubCitedTimesUpdateProcess) {
    this.pdwhPubCitedTimesUpdateProcess = pdwhPubCitedTimesUpdateProcess;
  }

}
