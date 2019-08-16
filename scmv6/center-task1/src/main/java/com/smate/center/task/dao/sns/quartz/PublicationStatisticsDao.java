package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;
import com.smate.center.task.model.sns.quartz.PublicationStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果操作统计信息Dao
 * 
 * @author zk
 * 
 */
@Repository
public class PublicationStatisticsDao extends SnsHibernateDao<PublicationStatistics, Long> {
  /**
   * 获取成果的统计数据
   * 
   * @param pubId
   * @return
   */
  public PublicationStatistics getPubStatisticsById(Long pubId) {
    String hql = "from PublicationStatistics p where p.pubId = :pubId";
    return (PublicationStatistics) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public void updateCommentStatistics(Long pubId, Integer commentCount) {
    String hql = "update PublicationStatistics p set commentCount=:commentCount  where p.pubId = :pubId";
    super.createQuery(hql).setParameter("commentCount", commentCount).setParameter("pubId", pubId).executeUpdate();
  }

}
