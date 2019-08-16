package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GroupInvitePsnFriendNode;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组与人员的邀请关系Dao.
 * 
 * zzx
 */
@Repository
public class GroupInvitePsnFriendNodeDao extends SnsHibernateDao<GroupInvitePsnFriendNode, Long> {
  /**
   * 根据groupId查找群组与人员的邀请关系表List
   * 
   * @param groupId
   * @return
   */
  public List<GroupInvitePsnFriendNode> findGroupInvitePsnFriendNodeList(Long groupId) {
    String hql = "from GroupInvitePsnFriendNode where id.groupId=?";
    return createQuery(hql, groupId).list();
  }
}
