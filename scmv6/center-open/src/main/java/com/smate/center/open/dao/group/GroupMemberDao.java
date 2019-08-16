package com.smate.center.open.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupMember;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组成员Dao.
 * 
 * @author lhd
 */
@Repository
public class GroupMemberDao extends SnsHibernateDao<GroupMember, Long> {

  /**
   * 根据psnId检索人员
   * 
   * @author lhd
   * @param psnId
   * @return
   * @throws Exception
   */
  public GroupMember getGroupMemberInfo(Long psnId) throws Exception {
    String hql = " from GroupMember where psnId=?";
    return this.findUnique(hql, psnId);
  }

}
