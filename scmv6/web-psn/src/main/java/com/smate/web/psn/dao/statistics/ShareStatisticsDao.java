package com.smate.web.psn.dao.statistics;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.statistics.ShareStatistics;

/**
 * 共享统计模块Dao
 * 
 * @author zk
 * 
 */
@Repository(value = "shareStatisticsDao")
public class ShareStatisticsDao extends SnsHibernateDao<ShareStatistics, Long> {

  /**
   * 统计人员资源被分享数----由于成果改造，成果的分享记录放到v_pub_share表了
   * 
   * @param awardPsnId 要统计的人员的ID
   * @return 人员资源被分享总数
   */
  public Long countPsnShareSum(Long sharePsnId) {
    String hql =
        "select count(1) from ShareStatistics t where t.sharePsnId = :sharePsnId and t.actionType <> 1 and t.actionType <> 2 and t.actionType <> 4";
    return (Long) super.createQuery(hql).setParameter("sharePsnId", sharePsnId).uniqueResult();
  }
}
