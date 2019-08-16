package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GroupControl;
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

  /**
   * 更新群组主页可见权限
   * 
   * @param groupId
   * @param pageOpenStatus
   */
  public void updateGroupPageOpen(Long groupId, int pageOpenStatus) {
    String hql = "update GroupControl t set t.isPageOpen=? where t.groupId=? ";
    super.createQuery(hql, String.valueOf(pageOpenStatus), groupId).executeUpdate();
  }
}
