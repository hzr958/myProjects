package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PsnStatisticsPubPrj;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 获取个人公开成果和项目数跑任务的表
 * 
 * @author ltl
 * 
 */
@Repository
public class PsnStatisticsPubPrjDao extends SnsHibernateDao<PsnStatisticsPubPrj, Long> {
  /**
   * 获取size条未处理的psnid
   */
  @SuppressWarnings("unchecked")
  public List<PsnStatisticsPubPrj> getPsnStatisticsList(Integer size) {
    String hql = "from PsnStatisticsPubPrj t where t.status=:status";
    return super.createQuery(hql).setParameter("status", 0).setMaxResults(size).list();
  }
}
