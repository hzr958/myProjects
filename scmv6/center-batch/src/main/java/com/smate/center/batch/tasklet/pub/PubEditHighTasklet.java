package com.smate.center.batch.tasklet.pub;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.center.batch.service.pub.ScholarPublicationXmlManager;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.center.batch.util.pub.PubSimpleUtils;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 成果改造第二阶段-成果编辑保存任务实现
 * 
 * @author lxz
 *
 */
public class PubEditHighTasklet extends BaseTasklet {

  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  PubSimpleService pubSimpleService;
  @Autowired
  private BatchJobsContextFactory batchJobsContextFactory;
  @Autowired
  private BatchJobsService batchJobsService;

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

    String pubIdStr = String.valueOf(jobContentMap.get("msg_id"));
    Long id = Long.parseLong(pubIdStr);
    Long version = Long.parseLong(String.valueOf(jobContentMap.get("version")));
    String groupId = String.valueOf(jobContentMap.get("groupId"));
    PubSimple pubSimple = pubSimpleService.queryPubSimpleAndXml(id);
    if (pubSimple == null) {
      return;
    }
    if (StringUtils.isBlank(groupId) || "null".equalsIgnoreCase(groupId)) {
      groupId = "1";
    }

    // 版本不一致,或者被删除,取消处理
    if (pubSimple.getSimpleVersion().equals(version) && !PubSimpleUtils.checkStatus(pubSimple)) {
      if (PubSimpleUtils.checkSimpleTask(pubSimple)) {
        // 具有老数据
        scholarPublicationXmlManager.updateXml(pubSimple.getPubId(), pubSimple.getPubDataStore().getData(),
            pubSimple.getPubType(), PublicationArticleType.OUTPUT, pubSimple);
        // 2015-11-11 增加低优先级任务
        String context =
            BatchJobUtil.getActionContext(pubIdStr, pubSimple.getSimpleVersion() + "", "\"edit\"", groupId);
        try {
          BatchJobs job = batchJobsContextFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_LOW_SAVE, context,
              BatchWeightEnum.B.toString());
          batchJobsService.saveJob(job);
        } catch (CreateBatchJobException e) {
          throw new BatchTaskException(e);
        }
      } else {
        // 没有老数据
        scholarPublicationXmlManager.createXmlNew(pubSimple.getPubDataStore().getData(), pubSimple.getPubType(),
            PublicationArticleType.OUTPUT, pubSimple);
        // 2015-11-11 增加低优先级任务
        String context =
            BatchJobUtil.getActionContext(pubIdStr, pubSimple.getSimpleVersion() + "", "\"enter\"", groupId);
        try {
          BatchJobs job = batchJobsContextFactory.createBatchJob(BatchOpenCodeEnum.SIMPLE_PUB_LOW_SAVE, context,
              BatchWeightEnum.B.toString());
          batchJobsService.saveJob(job);
        } catch (CreateBatchJobException e) {
          throw new BatchTaskException(e);
        }
      }
    }

  }

}
