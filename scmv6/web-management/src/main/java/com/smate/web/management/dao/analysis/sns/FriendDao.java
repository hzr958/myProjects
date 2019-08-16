package com.smate.web.management.dao.analysis.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.sns.Friend;


/**
 * 个人好友Dao.
 * 
 * cwli
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {


  public Long isFriend(Long psnId, Long friendPsnId) {
    String hql = "select count(id) from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

}
