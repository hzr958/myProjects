package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author LJ
 *
 *         2017年9月8日
 */
@Repository
public class TmpTaskInfoRecordDao extends PdwhHibernateDao<TmpTaskInfoRecord, Long> {
  /**
   * 根据任务类型获取需要处理的Id
   * 
   * @param batchSize
   * @param jobType
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getbatchhandleIdList(Integer batchSize, Integer jobType) throws Exception {
    String hql = "select t.handleId from TmpTaskInfoRecord t where t.jobType=:jobType and t.status=0";
    return super.createQuery(hql).setMaxResults(batchSize).setParameter("jobType", jobType).list();

  }

  /**
   * 根据任务类型获取需要处理的记录
   * 
   * @param batchSize
   * @param jobType
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<TmpTaskInfoRecord> getTaskInfoRecordList(Integer batchSize, List<Integer> type) throws Exception {
    String hql = "from TmpTaskInfoRecord t where t.jobType in (:type) and t.status=0";
    return super.createQuery(hql).setMaxResults(batchSize).setParameterList("type", type).list();

  }

  /**
   * 根据任务类型获取需要处理的记录
   * 
   * @param batchSize
   * @param jobType
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<TmpTaskInfoRecord> getTaskInfoRecordList(Integer batchSize, Integer jobType) throws Exception {
    String hql = "from TmpTaskInfoRecord t where t.jobType=:jobType and t.status=0";
    return super.createQuery(hql).setMaxResults(batchSize).setParameter("jobType", jobType).list();

  }

  public TmpTaskInfoRecord getJobByhandleId(Long handleId, Integer jobType) {
    String hql = "from TmpTaskInfoRecord t where t.handleId=:handleId and t.jobType=:jobType";
    return (TmpTaskInfoRecord) super.createQuery(hql).setParameter("handleId", handleId)
        .setParameter("jobType", jobType).uniqueResult();

  }

  /**
   * 通过任务id更新任务状态
   * 
   * @param jobId
   * @param status
   * @param errMsg
   * @throws Exception
   */
  public void updateTaskStatusById(Long jobId, int status, String errMsg) throws Exception {
    errMsg = errMsg.toString().length() > 450 ? errMsg.toString().substring(0, 500) : errMsg.toString();
    String hql =
        "update TmpTaskInfoRecord t set t.status =:status,t.errMsg=:errMsg,t.handletime=sysdate where t.jobId=:jobId ";
    super.createQuery(hql).setParameter("status", status).setParameter("errMsg", errMsg).setParameter("jobId", jobId)
        .executeUpdate();

  }

  /**
   * 更新任务状态
   * 
   * @param handleId
   * @param status
   * @param errMsg
   * @param jobType
   * @throws Exception
   */
  public void updateTaskStatus(Long handleId, int status, String errMsg, int jobType) throws Exception {
    errMsg =
        errMsg != null && errMsg.toString().length() > 450 ? errMsg.toString().substring(0, 500) : errMsg.toString();
    String hql =
        "update TmpTaskInfoRecord t set t.status =:status,t.errMsg=:errMsg,t.handletime=sysdate where t.handleId=:handleId and t.jobType=:jobType";
    super.createQuery(hql).setParameter("status", status).setParameter("errMsg", errMsg)
        .setParameter("jobType", jobType).setParameter("handleId", handleId).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<Long> getTmpBatchhandleIdList(Long startHandleId, Long endHandleId, Integer jobType) {
    String hql =
        "select t.handleId from TmpTaskInfoRecord t where t.jobType=:jobType and t.handleId >= :startHandleId  and t.handleId <= :endHandleId  and t.status=0";
    return super.createQuery(hql).setMaxResults(500).setParameter("jobType", jobType)
        .setParameter("startHandleId", startHandleId).setParameter("endHandleId", endHandleId).list();
  }

}
