package com.smate.web.group.dao.group.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.GroupPsnNode;

/**
 * 所有节点上的群组Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupPsnNodeDao extends SnsHibernateDao<GroupPsnNode, Long> {

  /**
   * 查找对应节点上的群组
   * 
   * @param groupId
   * @return
   * @throws DaoException
   */
  public GroupPsnNode findGroupPnsNode(Long groupId) throws Exception {
    String hql = "from GroupPsnNode where groupId=?";
    return super.findUnique(hql, groupId);
  }

  /**
   * 更新群组成员数量
   * 
   * @param groupId
   * @return
   * @throws DaoException
   */
  public void updateSumMembers(int sumMembers, Long groupId) {
    String hql = "update GroupPsnNode set  sumMembers=:sumMembers  where groupId=:groupId";
    this.createQuery(hql).setParameter("sumMembers", sumMembers).setParameter("groupId", groupId).executeUpdate();
  }

}
