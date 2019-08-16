package com.smate.center.batch.connector.dao.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.model.job.BatchJobsForTaskPool;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * BatchJobs总表dao
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
@Repository
public class BatchJobsDao extends HibernateDao<BatchJobs, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 根据jobid更新出错记录
   * 
   * @param Long status 处理状态
   * @return
   * @version 6.0.1
   */
  public void updateErrorStatus(Long msgId) {
    String hql = "update BatchJobs t set t.status = 3 where t.jobId = ?";
    super.createQuery(hql, msgId).executeUpdate();
  }

  /**
   * 查询正在处理的记录，返回list中的第一个
   * 
   * @param
   * @return BatchJobs
   * @version 6.0.1
   */
  @SuppressWarnings("unchecked")
  public BatchJobs getOneMsg() {
    Integer fixedSize = 200;
    List<BatchJobs> result = new ArrayList<BatchJobs>();
    String hql = "from BatchJobs t where t.status = 1";
    result = (List<BatchJobs>) super.createQuery(hql).setMaxResults(fixedSize).list();

    if (CollectionUtils.isNotEmpty(result)) {
      return result.get(0);
    } else {
      return null;
    }
  }

  /**
   * 查询正在处理的记录，返回list中的第一个
   * 
   * @param Integer status 处理状态
   * @return BatchJobs
   * @version 6.0.1
   */
  public Long getCountByStatus(Integer status) {
    String hql = "select count(1) from BatchJobs t where t.status = ?";
    Long count = super.findUnique(hql, status);
    return count;
  }

  /**
   * 通过权重weight查询指定数量的未处理的记录
   * 
   * @param String weight 任务权重
   * @param Integer num 需要查询的数量
   * @return List<BatchJobs>
   * @version 6.0.1
   * 
   */
  @SuppressWarnings("unchecked")
  public List<BatchJobs> getTaskMsgIdsByWeightAndNum(String weight, Integer num) {
    String hql = "from BatchJobs t where t.weight = ? and t.status=0 order by t.jobId asc";
    return super.createQuery(hql, weight).setMaxResults(num).list();
  }

  /**
   * 获取status为0的所有任务
   * 
   * @param
   * @return List<BatchJobs>
   * @version 6.0.1
   * 
   */
  @SuppressWarnings("unchecked")
  public List<BatchJobs> getAllTaskWithStatus0() {
    String hql = "from BatchJobs t where t.status=0";
    return super.createQuery(hql).list();
  }

  /**
   * 获取status为1的所有任务
   * 
   * @param
   * @return List<BatchJobs>
   * @version 6.0.1
   * 
   */
  @SuppressWarnings("unchecked")
  public List<BatchJobs> getAllTaskWithStatus1() {
    String hql = "from BatchJobs t where t.status=1";
    return super.createQuery(hql).list();
  }

  /**
   * 获取status为0的不同权重任务数
   * 
   * @param String weight 任务权重
   * @return Long
   * @version 6.0.1
   * 
   */
  public Long getTaskMsgIdCountByWeight(String weight) {
    String hql = "select count(1) from BatchJobs t where t.status = 0 and t.weight = ?";
    Long count = super.findUnique(hql, weight);
    return count;
  }

  /**
   * 获取status为0的不同权重任务List
   * 
   * @param String
   * @return List<BatchJobs>
   * @version 6.0.1
   * 
   */
  public List<BatchJobs> getTaskByWeight(String weight) {
    String hql = "from BatchJobs t where t.status = 0 and t.weight = ?";
    return super.findUnique(hql, weight);

  }

  /**
   * 通过id获取任务
   * 
   * @param Long id V_BATCH_JOBS表主键
   * @return BatchJobs
   * @version 6.0.1
   * 
   */
  public BatchJobs getTaskById(Long id) {
    String hql = "from BatchJobs t where t.jobId = ?";
    return super.findUnique(hql, id);

  }

  /**
   * 通过id获取状态为1的任务
   * 
   * @param Long id V_BATCH_JOBS表主键
   * @return BatchJobs
   * @version 6.0.1
   */
  public BatchJobs getTaskWithStatus1ById(Long id) {
    String hql = "from BatchJobs t where t.jobId = ? and t.status=1";
    return super.findUnique(hql, id);
  }

  /**
   * 获取status为1的任务，并存入Map中，供taskPool调度多线程任务
   * 
   * @param Long id V_BATCH_JOBS表主键
   * @return Map<Long, BatchJobsForTaskPool>
   * @version 6.0.1
   */
  @SuppressWarnings("unchecked")
  public Map<Long, BatchJobsForTaskPool> getTaskMapStatus1() {
    String hql = "from BatchJobs t where t.status=1";
    List<BatchJobs> list = super.createQuery(hql).list();
    Map<Long, BatchJobsForTaskPool> map = new HashMap<Long, BatchJobsForTaskPool>();

    if (CollectionUtils.isNotEmpty(list)) {
      for (BatchJobs single : list) {
        BatchJobsForTaskPool task = new BatchJobsForTaskPool();
        task.setBatchJobs(single);
        task.setTaskPoolStatus(0);
        map.put(single.getJobId(), task);;
      }
    }

    return map;
  }

}
