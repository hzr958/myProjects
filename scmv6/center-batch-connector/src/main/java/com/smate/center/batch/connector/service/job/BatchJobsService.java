package com.smate.center.batch.connector.service.job;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.model.job.BatchJobsForTaskPool;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * BatchJobs操作Service
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param job
 */
public interface BatchJobsService {

  /**
   * 检查BatchJobs的参数
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @return true 正常 false 参数异常
   */
  public boolean checkJobParams(BatchJobs job);

  /**
   * 保存BatchJobs对象
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param job
   * @return true 正常 false 参数异常
   */
  public boolean saveJob(BatchJobs job);

  public BatchJobs getTaskMessageList() throws BatchTaskException;

  public void updateStatus(Long msgId) throws BatchTaskException;

  public List<BatchJobs> getAllMsgStatus1() throws BatchTaskException;

  public BatchJobs getJobByJobId(Long id) throws BatchTaskException;

  public BatchJobs getJobWithStatus1ByJobId(Long id) throws BatchTaskException;

  public void updateStartThreadOfJob(Long jobId, Long threadId, Long jobInstanceId, Date startTime)
      throws BatchTaskException;

  public void updateEndThreadOfJobSuccess(Long jobId, Long threadId) throws BatchTaskException;

  public void updateEndThreadOfJobError(Long jobId, Long threadId, String errorMsg) throws BatchTaskException;

  public Map<Long, BatchJobsForTaskPool> getAllTaskMapStatus1() throws BatchTaskException;

  /**
   * 创建后台缩略图生成任务
   *
   * @author houchuanjie
   * @date 2018年1月11日 上午11:37:12
   * @param archiveFile 文件记录对象
   * @param fileType 文件类型
   * @param keyValuePairs 其他参数键值对
   * @throws CreateBatchJobException
   */
  public void createAndSaveThumbnailBatchJob(final ArchiveFile archiveFile, FileTypeEnum fileType,
      SimpleEntry<?, ?>... keyValuePairs) throws CreateBatchJobException;


}
