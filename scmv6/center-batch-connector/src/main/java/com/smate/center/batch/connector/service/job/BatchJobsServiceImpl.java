package com.smate.center.batch.connector.service.job;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.dao.job.BatchJobsDao;
import com.smate.center.batch.connector.dao.job.BatchJobsHistoryDao;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.model.job.BatchJobsForTaskPool;
import com.smate.center.batch.connector.model.job.BatchJobsHistory;
import com.smate.center.batch.connector.util.BatchCodeWeightRelationUtil;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * BatchJobs操作Service实现
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("batchJobsService")
@Transactional(rollbackFor = Exception.class)
public class BatchJobsServiceImpl implements BatchJobsService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BatchJobsDao batchJobsDao;

  @Autowired
  BatchJobsHistoryDao BatchJobsHistoryDao;

  // 服务码&权重
  private Map<String, String> relationMap;

  @Override
  public boolean checkJobParams(BatchJobs job) {
    // 检查context是否符合json格式
    String context = job.getJobContext();

    if (!JacksonUtils.isJsonString(context)) {
      return false;
    }

    // 检查服务码与权重是否正确
    relationMap = BatchCodeWeightRelationUtil.CODE_WEIGHT_RELATION_MAP;

    if (job != null && job.getStrategy() != null) {
      String weight = relationMap.get(job.getStrategy());
      if (weight != null) {
        boolean same = weight.equals(job.getWeight());

        if (same) {
          // 初始化状态码
          job.setStatus(0);
          return true;
        }

      }
    }
    return false;
  }

  @Override
  public boolean saveJob(BatchJobs job) {
    if (checkJobParams(job)) {
      batchJobsDao.save(job);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public BatchJobs getTaskMessageList() throws BatchTaskException {
    BatchJobs result = batchJobsDao.getOneMsg();

    if (result != null) {
      BatchJobs update = result;
      // 状态置为3，已经处理
      update.setStatus(3);
      batchJobsDao.save(update);
    }

    return result;
  }

  @Override
  public void updateStatus(Long msgId) throws BatchTaskException {
    batchJobsDao.updateErrorStatus(msgId);
  }

  @Override
  public List<BatchJobs> getAllMsgStatus1() throws BatchTaskException {
    List<BatchJobs> list = new ArrayList<BatchJobs>();
    list = batchJobsDao.getAllTaskWithStatus1();
    return list;
  }

  @Override
  public Map<Long, BatchJobsForTaskPool> getAllTaskMapStatus1() throws BatchTaskException {
    Map<Long, BatchJobsForTaskPool> listMap = new HashMap<Long, BatchJobsForTaskPool>();
    listMap = batchJobsDao.getTaskMapStatus1();
    return listMap;
  }

  /*
   * 通过jobid获取得状态为1的job
   */
  @Override
  public BatchJobs getJobByJobId(Long id) throws BatchTaskException {
    BatchJobs job = new BatchJobs();
    job = batchJobsDao.getTaskById(id);
    return job;
  }

  /*
   * 更新job的startThreadId以及startTime
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public void updateStartThreadOfJob(Long jobId, Long threadId, Long jobInstanceId, Date startTime)
      throws BatchTaskException {
    BatchJobs job = batchJobsDao.getTaskById(jobId);

    if (job != null) {
      job.setJobStartTime(new Date());
      job.setStartThread(threadId);
      job.setJobInstanceId(jobInstanceId);
      batchJobsDao.save(job);
    } else {
      logger.error("更新任务状态出错！V_Batch_Jobs表找不到Job_Id={}的相关记录！", jobId);
    }
  }

  /*
   * 发送消息成功后，更新job的endThreadId，status以及endTime,从batch_jobs表中删除，移入V_BATCH_JOBS_HISTORY
   */
  @Override
  public void updateEndThreadOfJobSuccess(Long jobId, Long threadId) throws BatchTaskException {
    BatchJobs job = new BatchJobs();
    job = batchJobsDao.getTaskById(jobId);
    if (job != null) {
      BatchJobsHistory jobHis = new BatchJobsHistory();
      jobHis.setJobId(job.getJobId());
      jobHis.setJobContext(job.getJobContext());
      jobHis.setWeight(job.getWeight());
      jobHis.setStatus(2);
      jobHis.setStartThread(job.getStartThread());
      jobHis.setJobStartTime(job.getJobStartTime());
      jobHis.setJobInstanceId(job.getJobInstanceId());
      jobHis.setEndThread(threadId);
      jobHis.setStrategy(job.getStrategy());
      jobHis.setJobEndTime(new Date());
      BatchJobsHistoryDao.save(jobHis);
      batchJobsDao.delete(job);
    } else {
      logger.error("更新任务状态出错！V_Batch_Jobs表找不到Job_Id={}的相关记录！", jobId);
    }
  }

  @Override
  public void updateEndThreadOfJobError(Long jobId, Long threadId, String errorMsg) throws BatchTaskException {
    BatchJobs job = new BatchJobs();
    job = batchJobsDao.getTaskById(jobId);

    if (StringUtils.isBlank(errorMsg)) {
      errorMsg = "Unknown Error!";
    }

    if (errorMsg.length() > 200) {
      errorMsg = errorMsg.substring(errorMsg.length() - 200, errorMsg.length());
    }
    if (job != null) {
      job.setStatus(3);
      job.setEndThread(threadId);
      job.setJobEndTime(new Date());
      job.setErrorMsg(errorMsg);
      batchJobsDao.save(job);
    } else {
      logger.error("更新任务状态出错！V_Batch_Jobs表找不到Job_Id={}的相关记录！", jobId);
    }
  }

  /*
   * 通过id获取v_batch_job中status为1的任务
   * 
   */
  @Override
  public BatchJobs getJobWithStatus1ByJobId(Long id) throws BatchTaskException {
    BatchJobs job = batchJobsDao.getTaskWithStatus1ById(id);
    return job;
  }

  /**
   * 创建后台缩略图生成任务
   *
   * @author houchuanjie
   * @date 2018年1月11日 上午11:37:12
   * @param psnFile
   * @param archiveFile
   * @throws CreateBatchJobException
   */
  @SuppressWarnings("unchecked")
  public void createAndSaveThumbnailBatchJob(final ArchiveFile archiveFile, FileTypeEnum fileType,
      SimpleEntry<?, ?>... keyValuePairs) throws CreateBatchJobException {
    // 任务执行需要的上下文参数
    HashMap<String, Object> context = new HashMap<>();
    // msg_id代表archiveFileId
    context.put("msg_id", archiveFile.getFileId());
    // file_type标识文件类型：个人文件
    context.put("file_type", fileType.getValue());
    // 其他参数
    for (SimpleEntry<?, ?> entry : keyValuePairs) {
      context.put(entry.getKey().toString(), entry.getValue());
    }
    BatchJobs job =
        BatchJobsFactory.createBatchJob(BatchOpenCodeEnum.PRODUCE_THUMBNAIL_IMAGE_STRATEGY, BatchWeightEnum.B, context);
    checkJobParams(job);
    saveJob(job);
  }
}
