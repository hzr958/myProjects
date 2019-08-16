package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnFriend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 好友变化监听表，用于好友的群组Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnFriendDao extends SnsHibernateDao<GroupInvitePsnFriend, Long> {

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsnFriend> findGroupInvitePsnFriendList(Long psnId) throws DaoException {
    String hql = "from GroupInvitePsnFriend where id.psnId=?";
    return super.createQuery(hql, psnId).list();
  }

  public GroupInvitePsnFriend findGroupInvitePsnFriendList(Long psnId, Long friendPsnId) throws DaoException {
    String hql = "from GroupInvitePsnFriend where id.psnId=? and id.friendPsnId=?";
    return this.findUnique(hql, psnId, friendPsnId);
  }

  public void deleteGroupInvitePsnFriendByPsnId(Long psnId) throws DaoException {
    String hql = "delete from GroupInvitePsnFriend where id.friendPsnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsnFriend> findListByPsnId(Long delPsnId) throws DaoException {
    String hql = "from GroupInvitePsnFriend where id.psnId=? or id.friendPsnId=?";
    return super.createQuery(hql, delPsnId, delPsnId).list();
  }
  // ==============人员合并 end============
}
