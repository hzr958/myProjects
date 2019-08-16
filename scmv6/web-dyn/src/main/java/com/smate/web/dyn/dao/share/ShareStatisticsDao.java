package com.smate.web.dyn.dao.share;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.share.ShareStatistics;

/**
 * 共享统计模块Dao
 * 
 * @author zk
 * 
 */
@Repository(value = "shareStatisticsDao")
public class ShareStatisticsDao extends SnsHibernateDao<ShareStatistics, Long> {

  /**
   * 统计人员资源被分享数
   * 
   * @param awardPsnId 要统计的人员的ID
   * @return 人员资源被分享总数
   */
  public Long countPsnShareSum(Long sharePsnId) {
    String hql = "select count(1) from ShareStatistics t where t.sharePsnId = :sharePsnId";
    return (Long) super.createQuery(hql).setParameter("sharePsnId", sharePsnId).uniqueResult();
  }
}
