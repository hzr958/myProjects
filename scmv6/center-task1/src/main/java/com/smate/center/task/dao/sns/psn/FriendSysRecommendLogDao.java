package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.FriendSysRecommendLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author zll
 * 
 */
@Repository
public class FriendSysRecommendLogDao extends SnsHibernateDao<FriendSysRecommendLog, Long> {

  public void delAllFriendSysRecommendLog() {
    super.update("truncate table PSN_FRIEND_RECOMMEND_LOG");
  }

  @SuppressWarnings("unchecked")
  public List<FriendSysRecommendLog> getFriendSysRecommendLog(Long psnId) throws DaoException {
    return super.createQuery(" from FriendSysRecommendLog where startPsnId=? or lastPsnId=?", psnId, psnId).list();
  }
}
