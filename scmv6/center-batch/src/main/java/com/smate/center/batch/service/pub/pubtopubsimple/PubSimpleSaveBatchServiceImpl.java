package com.smate.center.batch.service.pub.pubtopubsimple;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * center-batch任务保存服务类
 * 
 * @author lxz
 */
@Service("pubSimpleSaveBatchService")
public class PubSimpleSaveBatchServiceImpl implements PubSimpleSaveBatchService {

  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  BatchJobsService batchJobsService;

  /**
   * 保存编辑任务
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @param version
   * @throws BatchTaskException
   */
  @Override
  public void savePubEditBatch(Long pubId, Long version) throws BatchTaskException {
    BatchJobs job;
    try {
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_EDIT_SAVE,
          BatchJobUtil.getVersionContext(pubId + "", version + ""), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new BatchTaskException(e);
    }

  }

  /**
   * 保存新增任务
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @param version
   * @throws BatchTaskException
   */
  @Override
  public void savePubAddBatch(Long pubId, Long version) throws BatchTaskException {
    BatchJobs job;
    try {
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_ADD_SAVE,
          BatchJobUtil.getVersionContext(pubId + "", version + ""), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new BatchTaskException(e);
    }

  }

  /**
   * 保存导入任务
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @param version
   * @throws BatchTaskException
   */
  @Override
  public void savePubImportBatch(Long pubId, Long version) throws BatchTaskException {
    BatchJobs job;
    try {
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_IMPORT,
          BatchJobUtil.getVersionContext(pubId + "", version + ""), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new BatchTaskException(e);
    }
  }


  /**
   * 保存导入任务+groupId
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @param version
   */
  @Override
  public void savePubImportBatch(Long pubId, Long version, Long groupId) throws BatchTaskException {
    BatchJobs job;
    try {
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_IMPORT,
          BatchJobUtil.getVersionContext(pubId + "", version + "", groupId + ""), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new BatchTaskException(e);
    }
  }

}
