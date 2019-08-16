package com.smate.center.batch.tasklet.pub;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsContextFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.service.pub.PubSimpleService;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.center.batch.util.pub.PubSimpleUtils;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 成果改造第二阶段-成果删除任务实现
 * 
 * @author lxz
 *
 */
public class PubDeleteHighTasklet extends BaseTasklet {

  @Autowired
  private PublicationService publicationService;
  @Autowired
  PubSimpleService pubSimpleService;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private BatchJobsContextFactory batchJobsContextFactory;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long id = Long.parseLong(withData);
    PubSimple pubSimple = pubSimpleService.queryPubSimple(id);
    if (pubSimple == null) {
      return DataVerificationStatus.FALSE;
    }
    return DataVerificationStatus.TRUE;
  }


  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    /*
     * String pubIdStr = String.valueOf(jobContentMap.get("msg_id")); Long id =
     * Long.parseLong(pubIdStr); logger.debug("jobId============= "+id+"=====");
     */

    String id = String.valueOf(jobContentMap.get("msg_id"));
    Long pubId = Long.parseLong(id);
    // 分为两种,跑完的和没有跑完的
    PubSimple pubSimple = pubSimpleService.queryPubSimple(pubId);
    pubSimpleService.delPubSimpleData(pubSimple);
    // 有老数据执行删除老数据逻辑，老数据指已经导入，并通过任务链处理完成，存入系统的数据。
    if (PubSimpleUtils.checkSimpleTask(pubSimple)) {
      publicationService.deletePublication(id);
      // 2015-11-11 增加低优先级任务
      String context = BatchJobUtil.getVersionContext(id, pubSimple.getSimpleVersion() + "");
      try {
        BatchJobs job = batchJobsContextFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_LOW_DELETE, context,
            BatchWeightEnum.B.toString());
        batchJobsService.saveJob(job);
      } catch (CreateBatchJobException e) {
        throw new BatchTaskException(e);
      }
    }
  }

}
