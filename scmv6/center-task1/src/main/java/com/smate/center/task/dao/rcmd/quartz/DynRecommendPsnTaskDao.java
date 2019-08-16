package com.smate.center.task.dao.rcmd.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rcmd.quartz.DynRecommendPsnTask;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 推荐动态人员定时器任务数据库处理类.
 * 
 * @author Administrator
 *
 */
@Repository
public class DynRecommendPsnTaskDao extends RcmdHibernateDao<DynRecommendPsnTask, Long> {
  /**
   * 
   * @param psnId
   * @param recomType
   * @return
   */
  public DynRecommendPsnTask getDynRecommendPsnTask(Long psnId, int recomType) {
    String hql = "from DynRecommendPsnTask t where t.psnId=:psnId and t.recomType=:recomType and rownum=1 ";
    Object obj =
        super.createQuery(hql).setParameter("psnId", psnId).setParameter("recomType", recomType).uniqueResult();
    if (obj != null) {
      return (DynRecommendPsnTask) obj;
    }
    return null;
  }

}
