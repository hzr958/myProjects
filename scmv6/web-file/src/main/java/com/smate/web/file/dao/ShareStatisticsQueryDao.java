package com.smate.web.file.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.ShareStatisticsQuery;

/**
 * 共享统计模块Dao
 * 
 * @author zk
 * 
 */
@Repository
public class ShareStatisticsQueryDao extends SnsHibernateDao<ShareStatisticsQuery, Long> {

  /**
   * 判断当前用户是否被成果所有者分享指定的成果
   *
   * @author houchuanjie
   * @date 2017年12月22日 下午8:14:38
   * @param currPsnId 当前用户
   * @param pubOwnerId 成果所有者
   * @param pubId 成果id
   * @return
   */
  public boolean isPubBeingShared(Long currPsnId, Long pubOwnerId, Long pubId) {
    String sql =
        "select DISTINCT 1 from share_statistics t where t.action_type in(1,2) and t.action_key = :pubId and t.share_psn_id = :sharePsnId and t.psn_id = :pubOwnerId";
    Object o = getSession().createSQLQuery(sql).setParameter("pubOwnerId", pubOwnerId).setParameter("pubId", pubId)
        .setParameter("sharePsnId", currPsnId).uniqueResult();
    if (o != null) {
      return true;
    } else {
      return false;
    }
  }
}
