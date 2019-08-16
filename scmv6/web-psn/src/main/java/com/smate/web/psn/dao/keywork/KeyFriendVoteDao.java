package com.smate.web.psn.dao.keywork;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.keyword.KeyFriendVote;

/**
 * 好友具有相同投票关键词投票统计.
 * 
 * @author WSN
 * 
 */
@Repository
public class KeyFriendVoteDao extends SnsHibernateDao<KeyFriendVote, Long> {

  /**
   * 删除统计的好友具有相同关键词投票记录
   * 
   * @param psnId
   * @param friendPsnId
   * @throws DaoException
   */
  public void deleteKeyFriendVotesByPsn(Long psnId, Long friendPsnId) throws DaoException {
    List<Long> psnIds = new ArrayList<Long>();
    psnIds.add(psnId);
    psnIds.add(friendPsnId);
    String hql = "delete from KeyFriendVote t where t.psnId in(:psnIds) and t.friendPsnId in(:friendIds)";
    super.createQuery(hql).setParameterList("psnIds", psnIds).setParameterList("friendIds", psnIds).executeUpdate();
  }

}
