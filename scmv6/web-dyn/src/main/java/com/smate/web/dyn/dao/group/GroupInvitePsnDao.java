package com.smate.web.dyn.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.group.GroupInvitePsn;

/**
 * 群组 成员邀请表 dao
 * 
 * @author tsz
 *
 */

@Repository
public class GroupInvitePsnDao extends SnsHibernateDao<GroupInvitePsn, Long> {


  /**
   * 根据groupId和psnId获取群组记录
   * 
   * @author lhd
   * @param groupId
   * @param psnId
   * @return
   */
  public GroupInvitePsn getGroupInvitePsn(Long groupId, Long psnId) {
    String hql = "from GroupInvitePsn where groupId = ? and psnId=? and status = '01'";
    return this.findUnique(hql, groupId, psnId);
  }



}
