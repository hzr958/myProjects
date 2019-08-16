package com.smate.center.task.sys.quartz.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.task.sys.quartz.model.QuartzCronExpression;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 定时器任务配置Dao
 * 
 * @author zk
 *
 */
@Repository
public class QuartzCronExpressionDao extends SnsHibernateDao<QuartzCronExpression, Long> {

  /**
   * 获取QuartzCronExpression.
   * 
   * @param beanName
   * @return
   */
  public QuartzCronExpression findQuartzCronExpression(String beanName) {

    String hql = "from QuartzCronExpression t where t.beanName = :beanName ";
    return (QuartzCronExpression) super.createQuery(hql).setParameter("beanName", beanName).uniqueResult();
  }

  public void stopTask(String beanName) {
    String hql = "update QuartzCronExpression set status=:status where beanName=:beanName ";
    super.createQuery(hql).setParameter("beanName", beanName).setParameter("status", 0).executeUpdate();
  }
}
