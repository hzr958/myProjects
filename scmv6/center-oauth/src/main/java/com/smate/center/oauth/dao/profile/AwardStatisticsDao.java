package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.AwardStatistics;
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
    return super.findUnique(
        "select count(t.id) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 and t.actionType=?", psnId,
        actionType);
  }

}
