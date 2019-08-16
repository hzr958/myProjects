package com.smate.web.group.dao.group.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.GroupPsnNodeSum;
import com.smate.web.group.model.group.psn.GroupPsnNodeSumPk;

/**
 * 群组统计Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupPsnNodeSumDao extends SnsHibernateDao<GroupPsnNodeSum, Long> {
  public GroupPsnNodeSum findGroupPsnSum(GroupPsnNodeSumPk groupPsnNodeSumPk) throws Exception {
    String hql = "from GroupPsnNodeSum where id.code=? and id.nodeId=?";
    return super.findUnique(hql, groupPsnNodeSumPk.getCode(), groupPsnNodeSumPk.getNodeId());
  }

}
