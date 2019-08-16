package com.smate.center.open.dao.group;

import org.springframework.stereotype.Repository;
import com.smate.center.open.model.group.prj.GroupPrjExtend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组项目扩展表dao
 * 
 * @author lhd
 *
 */
@Repository
public class GroupPrjExtendDao extends SnsHibernateDao<GroupPrjExtend, Long> {

  /**
   * 获取群组项目扩展信息
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public GroupPrjExtend getGroupPrjExtend(Long groupId) {
    String hql = "from GroupPrjExtend t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupPrjExtend) obj;
    }
    return null;
  }

  /**
   * 保存群组项目扩展表信息
   * 
   * @author lhd
   * @param groupPrjExtend
   */
  public void saveGroupPrjExtend(GroupPrjExtend groupPrjExtend) {
    if (groupPrjExtend != null) {
      // if (groupPrjExtend.getId() != null) {
      // super.getSession().update(groupPrjExtend);
      // } else {
      super.save(groupPrjExtend);
      // }
    }

  }

}
