package com.smate.web.group.dao.group.psn;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.GroupInvitePsnNode;

/**
 * 群组与人员的邀请关系表(当前人所有节点上加入的群组)Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnNodeDao extends SnsHibernateDao<GroupInvitePsnNode, Long> {

  public List<GroupInvitePsnNode> findGroupInvitePsnNodeList(Long groupId) {
    String hql = "from GroupInvitePsnNode where id.groupId=?";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  public void deleteMember(Long groupId, Long memberId) {
    String hql = "update GroupInvitePsnNode t set t.isAccept='0' where t.id.groupId=:groupId and t.id.psnId=:memberId";
    super.createQuery(hql).setParameter("groupId", groupId).setParameter("memberId", memberId).executeUpdate();
  }

}
