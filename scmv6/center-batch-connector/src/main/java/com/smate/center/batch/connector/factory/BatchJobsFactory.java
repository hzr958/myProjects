package com.smate.center.batch.connector.factory;

import java.util.Date;
import java.util.Map;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 生成BatchJobs对象工厂 请使用子类创建BatchJobs对象
 * 
 * @author LXZ
 * @since 6.0.1
 * @version 6.0.1
 *
 */
public abstract class BatchJobsFactory {

  /**
   * 
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param em 任务标识符
   * @param objId (BatchOpenCodeEnum) Context内容,String类型(BatchWeightEnum)
   * @param Weight 权重
   * @return
   */
  public BatchJobs createBatchJob(BatchOpenCodeEnum em, Long objId, String Weight) throws CreateBatchJobException {
    checkEnum(em);
    checkObjId(objId);
    checkWeight(Weight);
    return getBatchJob(em, objId, Weight);
  }

  /**
   * 
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param em 任务标识符
   * @param objId (BatchOpenCodeEnum) Context内容,String类型(BatchWeightEnum)
   * @param Weight 权重
   * @return
   */
  public BatchJobs createBatchJob(BatchOpenCodeEnum em, String objId, String Weight) throws CreateBatchJobException {
    checkEnum(em);
    checkObjId(objId);
    checkWeight(Weight);
    return getBatchJob(em, objId, Weight);
  }

  /**
   * 创建一个BatchJob任务
   *
   * @author houchuanjie
   * @date 2018年1月11日 下午2:03:29
   * @param strategy
   * @param weight
   * @param context
   * @return
   * @throws CreateBatchJobException
   */
  public static BatchJobs createBatchJob(BatchOpenCodeEnum strategy, BatchWeightEnum weight,
      Map<String, Object> context) throws CreateBatchJobException {
    checkEnum(strategy);
    checkWeight(weight.toString());
    BatchJobs job = new BatchJobs(JacksonUtils.mapToJsonStr(context), weight.toString(), strategy.toString());
    job.setStatus(0);
    job.setCreateTime(new Date());
    return job;
  }

  /**
   * 
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param em
   * @param objId
   * @param Weight
   * @return
   */
  public BatchJobs getBatchJob(BatchOpenCodeEnum em, Long objId, String Weight) {

    String context = BatchJobUtil.getContext(objId);
    String strategy = em.toString();
    BatchJobs job = new BatchJobs(context, Weight, strategy);
    job.setCreateTime(new Date());
    return job;
  }

  public BatchJobs getBatchJob1(String context, String Weight, BatchOpenCodeEnum em) {
    String strategy = em.toString();
    BatchJobs job = new BatchJobs(context, Weight, strategy);
    job.setCreateTime(new Date());
    return job;
  }

  protected abstract BatchJobs getBatchJob(BatchOpenCodeEnum em, String objId, String Weight);

  private static void checkEnum(BatchOpenCodeEnum em) throws CreateBatchJobException {
    if (em == null) {
      throw new CreateBatchJobException("BatchJob服务码不能为空");
    }
  }

  private void checkObjId(Object objId) throws CreateBatchJobException {
    if (objId == null) {
      throw new CreateBatchJobException("BatchJob Context不能为空");
    }
  }

  private static void checkWeight(String weight) throws CreateBatchJobException {
    if (weight == null) {
      throw new CreateBatchJobException("BatchJob权重不能为空");
    }
  }
}
