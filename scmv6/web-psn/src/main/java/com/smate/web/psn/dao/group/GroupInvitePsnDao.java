package com.smate.web.psn.dao.group;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.friend.GroupInvitePsn;

/**
 * 群组邀请人员DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午5:13:17
 *
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

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupInvitePsnByPsnId(Long psnId) throws DaoException {

    String hql = "from GroupInvitePsn where psnId=?";
    Query query = createQuery(hql, psnId);
    return query.list();
  }
}
