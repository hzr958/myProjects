package com.smate.center.open.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupKeyDisc;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组关键词学科表DAO类.
 * 
 * @author mjg
 */
@Repository
public class GroupKeyDiscDao extends SnsHibernateDao<GroupKeyDisc, Long> {

  /**
   * 保存群组学科
   * 
   * @author lhd
   * @param groupKeyDisc
   */
  public void saveGroupKeyDisc(GroupKeyDisc groupKeyDisc) {
    if (groupKeyDisc != null) {
      if (groupKeyDisc.getId() != null) {
        super.getSession().update(groupKeyDisc);
      } else {
        super.save(groupKeyDisc);
      }
    }
  }

  /**
   * 获取群组关键词学科.
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public GroupKeyDisc getGroupKeyDisc(Long groupId) {
    String hql = "from GroupKeyDisc t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupKeyDisc) obj;
    }
    return null;
  }
}
