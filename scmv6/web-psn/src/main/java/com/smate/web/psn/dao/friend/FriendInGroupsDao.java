package com.smate.web.psn.dao.friend;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.friend.FriendInGroups;

/**
 * 好友分组DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午4:02:26
 *
 */
@Repository
public class FriendInGroupsDao extends SnsHibernateDao<FriendInGroups, Long> {
  /**
   * 删除好友分组记录
   * 
   * @param friendId
   * @return
   */
  public int deleteByFriendId(Long friendId) {
    String hql = "delete from FriendInGroups where friendId=:friendId";
    return super.createQuery(hql).setParameter("friendId", friendId).executeUpdate();
  }
}
