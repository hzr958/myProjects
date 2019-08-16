package com.smate.web.dyn.dao.dynamic;


import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.dynamic.DynStatistics;

/**
 * 初始化动态统计Dao
 * 
 * @author lhd
 *
 */
@Repository
public class DynStatisticsDao extends SnsHibernateDao<DynStatistics, Long> {

  /**
   * 根据dynId获得赞，分享，评论数
   * 
   * @param dynId
   * @return
   */
  public DynStatistics getDynamicStatistics(Long dynId) {
    String hql = "from DynStatistics where dynId=:dynId";
    return (DynStatistics) this.createQuery(hql).setParameter("dynId", dynId).uniqueResult();
  }

  public void updateDynamicShareStatistics(Long dynId, Integer shareCount) {
    String hql = "update DynStatistics set shareCount=:shareCount where dynId=:dynId";
    super.createQuery(hql).setParameter("shareCount", shareCount).setParameter("dynId", dynId).executeUpdate();
  }

  public void updateDynamicCommentStatistics(Long dynId, Integer commentCount) {
    String hql = "update DynStatistics set commentCount=:commentCount where dynId=:dynId";
    super.createQuery(hql).setParameter("commentCount", commentCount).setParameter("dynId", dynId).executeUpdate();
  }

}
