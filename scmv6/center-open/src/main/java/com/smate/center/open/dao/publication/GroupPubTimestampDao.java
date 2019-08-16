package com.smate.center.open.dao.publication;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.GroupPubTimestamp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * GroupPubTimestamp表实体Dao
 * 
 * @author ajb
 * 
 */
@Repository
public class GroupPubTimestampDao extends SnsHibernateDao<GroupPubTimestamp, Long> {
  /**
   * 通过groupId 查找对象
   * 
   * @author ajb
   */
  public GroupPubTimestamp findGroupPubTimestampByGroupId(Long groupId) {
    String hql = "  from GroupPubTimestamp t where t.groupId=:groupId";
    return (GroupPubTimestamp) super.createQuery(hql).setParameter("groupId", groupId).uniqueResult();
  }

}
