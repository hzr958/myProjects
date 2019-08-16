package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.Friend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人好友Dao.
 * 
 * wsn
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {

  public Long getFriendCount(Long psnId) {
    String hql = "select count(*) from Friend where psnId=?";
    return findUnique(hql, psnId);
  }

}
