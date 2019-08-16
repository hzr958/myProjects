package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.GroupFundInfoMembers;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 群组待处理成果Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupFundInfoMembersDao extends SnsHibernateDao<GroupFundInfoMembers, Long> {
  @SuppressWarnings("unchecked")
  public List<GroupFundInfoMembers> getToHandleList(Integer size, Long startGroupId, Long endGroupId) {
    // 根据groupID限定范围，暂无需求
    /*
     * String hql =
     * "from GroupFundInfoMembers t where t.groupId >:startGroupId and t.groupId <=:endGroupId and t.status = 0 order by t.groupId asc"
     * ; return super.createQuery(hql).setParameter("startGroupId",
     * startGroupId).setParameter("endGroupId", endGroupId) .setMaxResults(size).list();
     */
    String hql = "from GroupFundInfoMembers t where t.status = 0 order by t.groupId asc";
    return super.createQuery(hql).setMaxResults(size).list();
  }
}
