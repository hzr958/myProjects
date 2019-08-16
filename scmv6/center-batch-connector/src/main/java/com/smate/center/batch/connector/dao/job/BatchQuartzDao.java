package com.smate.center.batch.connector.dao.job;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class BatchQuartzDao extends HibernateDao<BatchQuartz, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public Integer getTaskStatus(Long taskId) {
    String hql = "select t.status from BatchQuartz t where t.quartzId = ?";
    return super.findUnique(hql, taskId);
  }

  public BatchQuartz getTaskById(Long taskId) {
    String hql = "from BatchQuartz t where t.quartzId = ?";
    return super.findUnique(hql, taskId);
  }

  @SuppressWarnings("unchecked")
  public List<BatchQuartz> getTasksByStatus(Integer status) {
    String hql = "from BatchQuartz t where t.taskStatus = ? and t.executeCount>0";
    return super.createQuery(hql, status).list();
  }

  /**
   * 获取任务简介
   * 
   * @param strategy
   * @return
   */
  public String getMarkByStrategy(String strategy) {
    String hql = "select t.remark from BatchQuartz t where t.strategy = ?";
    return super.findUnique(hql, strategy);
  }

}
