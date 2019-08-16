package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSum;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSumPk;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组统计表.Dao
 * 
 * @author zzx
 *
 */
@Repository
public class GroupPsnNodeSumDao extends SnsHibernateDao<GroupPsnNodeSum, Long> {
  /**
   * 查询群组统计
   * 
   * @param groupPsnNodeSumPk
   * @return
   * @throws DaoException
   */
  public GroupPsnNodeSum findGroupPsnSum(GroupPsnNodeSumPk groupPsnNodeSumPk) throws DaoException {
    String hql = "from GroupPsnNodeSum where id.code=? and id.nodeId=?";
    return super.findUnique(hql, groupPsnNodeSumPk.getCode(), groupPsnNodeSumPk.getNodeId());
  }
}
