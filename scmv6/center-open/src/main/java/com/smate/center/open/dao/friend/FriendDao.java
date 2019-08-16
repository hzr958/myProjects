package com.smate.center.open.dao.friend;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.friend.Friend;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 个人好友Dao.
 * 
 * cwli
 */
@Repository
public class FriendDao extends HibernateDao<Friend, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public Long isFriend(Long psnId, Long friendPsnId) {
    String hql = "select count(*) from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

  public Long getFriendCount(Long psnId) {
    String hql = "select count(*) from Friend where psnId=?";
    return findUnique(hql, psnId);
  }
}
