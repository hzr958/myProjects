package com.smate.center.batch.dao.rcmd.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rcmd.pub.DynRecommendPsnTask;
import com.smate.core.base.utils.data.RcmdHibernateDao;


/**
 * 推荐动态人员定时器任务数据库处理类.
 * 
 * @author mjg
 * 
 */
@Repository
public class DynRecommendPsnTaskDao extends RcmdHibernateDao<DynRecommendPsnTask, Long> {

  /**
   * 获取人员需要计算推荐结果的记录.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynRecommendPsnTask> getDynRecommendPsnTask(Long psnId) {
    String hql = "from DynRecommendPsnTask t where t.psnId=? ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 获取待刷新人员列表.
   * 
   * @param maxSize
   * @author xys
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynRecommendPsnTask> getPsnsNeedRefresh(int maxSize) {
    String hql = "from DynRecommendPsnTask t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 保存动态推荐人员任务记录.
   * 
   * @param psnTask
   */
  public void saveDynRecommendPsnTask(DynRecommendPsnTask psnTask) {
    if (psnTask != null) {
      if (psnTask.getId() != null) {
        String hql = "update DynRecommendPsnTask t set t.status=?,t.recomType=? where t.psnId=? ";
        super.createQuery(hql, psnTask.getStatus(), psnTask.getRecomType(), psnTask.getPsnId()).executeUpdate();
      } else {
        super.save(psnTask);
      }
    }
  }

  /**
   * 
   * @param psnId
   * @param recomType
   * @return
   */
  public DynRecommendPsnTask getDynRecommendPsnTask(Long psnId, int recomType) {
    String hql = "from DynRecommendPsnTask t where t.psnId=? and t.recomType=? and rownum=1 ";
    Object obj = super.createQuery(hql, psnId, recomType).uniqueResult();
    if (obj != null) {
      return (DynRecommendPsnTask) obj;
    }
    return null;
  }
}
