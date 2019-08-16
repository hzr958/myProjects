package com.smate.center.open.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.profile.AwardStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 赞操作统计模块
 * 
 * @author wsn
 * 
 */
@Repository(value = "awardStatisticsDao")
public class AwardStatisticsDao extends SnsHibernateDao<AwardStatistics, Long> {

  public Long countAward(Long psnId, Integer actionType) {
    String hql =
        "select count(t.id) from AwardStatistics t where t.awardPsnId = :psnId and t.action = 1 and t.actionType=:actionType";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("actionType", actionType)
        .uniqueResult();
  }

}
