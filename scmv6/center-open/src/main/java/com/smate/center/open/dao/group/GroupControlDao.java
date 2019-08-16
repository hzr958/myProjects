package com.smate.center.open.dao.group;


import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupControl;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 群组控制表DAO类.
 * 
 * @author mjg
 * 
 */
@Repository
public class GroupControlDao extends SnsHibernateDao<GroupControl, Long> {

  /**
   * 保存群组控制表记录.
   * 
   * @author lhd
   * @param groupControl
   */
  public void saveGroupControl(GroupControl groupControl) {
    if (groupControl != null) {
      if (groupControl.getId() != null) {
        super.getSession().update(groupControl);
      } else {
        super.save(groupControl);
      }
    }
  }

  /**
   * 获取群组控制表记录.
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public GroupControl getGroupControl(Long groupId) {
    String hql = "from GroupControl t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupControl) obj;
    }
    return null;
  }

}
