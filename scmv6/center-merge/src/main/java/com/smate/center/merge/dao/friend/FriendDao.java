package com.smate.center.merge.dao.friend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.friend.Friend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人好友Dao.
 * 
 * cwli
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {

  @SuppressWarnings("unchecked")
  public List<Friend> findByFriendPsnId(Long friendPsnId) throws Exception {
    String hql = "from Friend f where f.friendPsnId=:friendPsnId or f.psnId=:friendPsnId";
    return super.createQuery(hql).setParameter("friendPsnId", friendPsnId).list();
  }
}
