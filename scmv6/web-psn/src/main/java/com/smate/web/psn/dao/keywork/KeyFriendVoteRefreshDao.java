package com.smate.web.psn.dao.keywork;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.keyword.KeyFriendVoteRefresh;

/**
 * 刷新好友具有相同投票关键词统计.
 * 
 * @author WSN
 * 
 */
@Repository
public class KeyFriendVoteRefreshDao extends SnsHibernateDao<KeyFriendVoteRefresh, Long> {

  public void deleteRefreshRecord(Long psnId, Long friendPsnId) throws DaoException {
    List<Long> psnIds = new ArrayList<Long>();
    psnIds.add(psnId);
    psnIds.add(friendPsnId);
    String hql = "delete from KeyFriendVoteRefresh t where t.psnId in(:psnIds) and t.friendPsnId in(:friendIds)";
    super.createQuery(hql).setParameterList("psnIds", psnIds).setParameterList("friendIds", psnIds).executeUpdate();
  }

}
