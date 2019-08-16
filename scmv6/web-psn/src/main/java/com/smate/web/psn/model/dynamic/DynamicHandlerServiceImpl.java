package com.smate.web.psn.model.dynamic;



import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.service.statistics.AttendStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * 动态处理service.
 * 
 * @author chenxiangrong
 * 
 */
@Service("dynamicHandlerService")
@Transactional(rollbackFor = Exception.class)
public class DynamicHandlerServiceImpl implements DynamicHandlerService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final int CLEAN_TIME = -90;
  protected final int HISTORY_NUM = 100;
  protected final int add_status = 0;
  protected final int cancel_status = 1;


  @Autowired
  private AttendStatisticsService attendStatisticsService;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  BatchJobsService batchJobsService;


  /**
   * 取消关注.
   */
  @Override
  public void minusAttentionVisible(Long psnId) throws ServiceException {

    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();

      BatchJobs job;
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.ATEENTION_DYNAMIC,
          BatchJobUtil.getAttDynamicContext(currentPsnId + "", currentPsnId, psnId, cancel_status),
          BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);// 删除好友操作方执行的动态处理,将被删除的好友的动态对他不可见
      BatchJobs job1;
      job1 = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.ATEENTION_DYNAMIC,
          BatchJobUtil.getAttDynamicContext(psnId + "", psnId, currentPsnId, cancel_status),
          BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job1);// 被删除方执行的动态处理，将删除好友操作方的动态对他不可见，
      // 取消关注为0,删除好友操作方记录取消关注被删除方
      attendStatisticsService.addAttRecord(currentPsnId, psnId, 0);
      // 被删除方取消关注删除方
      attendStatisticsService.addAttRecord(psnId, currentPsnId, 0);
    } catch (ServiceException e) {
      logger.error("取消关注{}， 出错啦！", psnId, e);
    } catch (CreateBatchJobException e) {
      logger.error("取消关注{}，添加job任务异常！", psnId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 取消关注.  不是删除好友， 单方面取消的
   */
  @Override
  public void cancleAttentionVisible(Long psnId) throws ServiceException {

    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();

      BatchJobs job;
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.ATEENTION_DYNAMIC,
          BatchJobUtil.getAttDynamicContext(currentPsnId + "", currentPsnId, psnId, cancel_status),
          BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
      attendStatisticsService.addAttRecord(currentPsnId, psnId, 0);
    } catch (ServiceException e) {
      logger.error("取消关注{}， 出错啦！", psnId, e);
    } catch (CreateBatchJobException e) {
      logger.error("取消关注{}，添加job任务异常！", psnId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 添加关注.
   */
  @Override
  public void addAttentionVisible(Long psnId) throws ServiceException {
    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();

      BatchJobs job;
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.ATEENTION_DYNAMIC,
          BatchJobUtil.getAttDynamicContext(currentPsnId + "", currentPsnId, psnId, add_status),
          BatchWeightEnum.A.toString());
      boolean saveJob = batchJobsService.saveJob(job);
      // 添加关注为1
      attendStatisticsService.addAttRecord(currentPsnId, psnId, 1);
    } catch (ServiceException e) {
      logger.error("添加关注{}，出错啦！", psnId, e);
    } catch (CreateBatchJobException e) {
      logger.error("添加关注{}，添加job任务异常！", psnId, e);
      throw new ServiceException(e);
    }

  }
  /**
   * 添加关注.
   */
  @Override
  public void addAttentionVisible(Long currentPsnId  , Long psnId) throws ServiceException {
    try {
      BatchJobs job;
      job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.ATEENTION_DYNAMIC,
          BatchJobUtil.getAttDynamicContext(currentPsnId + "", currentPsnId, psnId, add_status),
          BatchWeightEnum.A.toString());
      boolean saveJob = batchJobsService.saveJob(job);
      // 添加关注为1
      attendStatisticsService.addAttRecord(currentPsnId, psnId, 1);
    } catch (ServiceException e) {
      logger.error("添加关注{}，出错啦！", psnId, e);
    } catch (CreateBatchJobException e) {
      logger.error("添加关注{}，添加job任务异常！", psnId, e);
      throw new ServiceException(e);
    }

  }



}
