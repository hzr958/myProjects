package com.smate.center.batch.dao.sns.pub;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GroupFilter;
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

  /**
   * 更新群组所属人.
   * 
   * @param ownerPsnId
   * @param groupId
   */
  public void updateGroupOwner(Long ownerPsnId, Long groupId) {
    String hql = "update GroupFilter t set t.ownerPsnId=? where t.groupId=? ";
    super.createQuery(hql, ownerPsnId, groupId).executeUpdate();
  }

  /**
   * 获取群组所属人ID.
   * 
   * @param groupId
   * @return
   */
  public Long getGroupOwner(Long groupId) {
    String hql = "select ownerPsnId from GroupFilter t where t.groupId=? ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return Long.valueOf(ObjectUtils.toString(obj));
    }
    return null;
  }

}
