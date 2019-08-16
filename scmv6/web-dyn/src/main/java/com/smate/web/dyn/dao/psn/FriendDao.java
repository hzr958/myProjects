package com.smate.web.dyn.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.psn.Friend;

/**
 * 个人好友Dao.
 * 
 * cwli
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {

  public Long isFriend(Long psnId, Long friendPsnId) {
    String hql = "select count(*) from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }


}

