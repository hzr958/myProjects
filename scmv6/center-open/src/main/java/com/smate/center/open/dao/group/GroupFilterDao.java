package com.smate.center.open.dao.group;


import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupFilter;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 群组过滤检索表DAO类.
 * 
 * @author mjg
 * 
 */
@Repository
public class GroupFilterDao extends SnsHibernateDao<GroupFilter, Long> {

  /**
   * 保存群组过滤检索信息.
   * 
   * @author lhd
   * @param groupFilter
   */
  public void saveGroupFilter(GroupFilter groupFilter) {
    if (groupFilter != null) {
      if (groupFilter.getId() != null) {
        super.getSession().update(groupFilter);
      } else {
        super.save(groupFilter);
      }
    }
  }

  /**
   * 获取群组检索过滤信息.
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public GroupFilter getGroupFilter(Long groupId) {
    String hql = "from GroupFilter t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupFilter) obj;
    }
    return null;
  }

}
