package com.smate.web.group.dao.group;


import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GroupKeyDisc;



/**
 * 群组关键词学科表DAO类.
 * 
 * @author mjg
 * 
 */
@Repository
public class GroupKeyDiscDao extends SnsHibernateDao<GroupKeyDisc, Long> {

  /**
   * 保存群组学科
   * 
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
