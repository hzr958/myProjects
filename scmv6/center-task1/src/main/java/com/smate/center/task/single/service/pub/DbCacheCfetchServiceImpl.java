package com.smate.center.task.single.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.task.dao.pdwh.quartz.DbCacheCfetchDao;
import com.smate.center.task.dao.pdwh.quartz.OriginalPdwhPubRelationDao;
import com.smate.center.task.model.pdwh.pub.DbCacheCfetch;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;

/**
 * 批量抓取crossref数据处理业务实现类.
 * 
 * @author zll
 * 
 */
@Service("DbCacheCfetchService")
@Transactional(rollbackFor = Exception.class)
public class DbCacheCfetchServiceImpl implements DbCacheCfetchService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCacheCfetchDao dbCacheCfetchDao;
  @Autowired
  private OriginalPdwhPubRelationDao originalPdwhPubRelationDao;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Resource
  private BatchJobsService batchJobsService;

  @Override
  public List<DbCacheCfetch> getTohandleList(int batchSize) {
    return dbCacheCfetchDao.getTohandleList(batchSize);
  }

  @Override
  public Long saveOriginalPdwhPubRelation(Map<String, Object> pubData, long psnId, Long crossrefId) {
    OriginalPdwhPubRelation originalPdwhPub = new OriginalPdwhPubRelation(crossrefId, psnId, 3, 2, new Date());
    originalPdwhPubRelationDao.save(originalPdwhPub);
    return originalPdwhPub.getId();
  }

  @Override
  public void saveError(Long crossrefId, String message) {
    dbCacheCfetchDao.saveError(crossrefId, message);

  }

  @Override
  public void saveSuccess(Long crossrefId) {
    dbCacheCfetchDao.modifyStatus(crossrefId);
  }

  @Override
  public void handleCrossrefData(Long originalId) {
    BatchJobs job;
    try {
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.SAVE_PUB_FROM_CROSSREF,
          BatchJobUtil.getSavePdwhPubDataFromCrossref(String.valueOf(originalId)), BatchWeightEnum.C.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      logger.error("---------------保存crossref数据到v_batch_job表出错");
    }

  }

}
