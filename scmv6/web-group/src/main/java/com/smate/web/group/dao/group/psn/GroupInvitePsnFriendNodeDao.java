package com.smate.web.group.dao.group.psn;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.GroupInvitePsnFriendNode;

/**
 * 群组与人员的邀请关系表(当前人所有节点上好友加入的群组)Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnFriendNodeDao extends SnsHibernateDao<GroupInvitePsnFriendNode, Long> {
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsnFriendNode> findGroupInvitePsnFriendNodeList(Long groupId) {
    String hql = "from GroupInvitePsnFriendNode where id.groupId=?";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

}
