package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.GroupInvitePsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组与人员的邀请关系表Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnDao extends SnsHibernateDao<GroupInvitePsn, Long> {

  /**
   * 获取群组统计数(修改了取群组的表为GroupFilter_MJG_SCM-6255).
   * 
   * @param psnId
   * @return
   */
  public Integer getGroupCountByPsnId(Long psnId) {

    String hql =
        "select count(gi.invitePsnId) from GroupInvitePsn gi,GroupFilter g where gi.psnId=? and gi.status='01' and gi.isAccept='1' and gi.groupId = g.groupId and g.status='01' and g.openType in ('H','O') ";
    Long count = (Long) super.createQuery(hql, psnId).uniqueResult();
    return count.intValue();
  }
}
