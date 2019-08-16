package com.smate.center.open.dao.group;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupInvitePsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组与人员的邀请关系表Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnDao extends SnsHibernateDao<GroupInvitePsn, Long> {

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupMemberByGroupId(Long groupId, List<Long> excludePsnIds) throws Exception {
    String hql =
        "from GroupInvitePsn where groupId=:groupId and status=:status and isAccept=:isAccept and psnId not in(:pids) order by createDate desc";
    Query query = createQuery(hql).setParameter("groupId", groupId).setParameter("status", "01")
        .setParameter("isAccept", "1").setParameterList("pids", excludePsnIds);
    return query.list();
  }

  /**
   * 查询群组 管理员 -or isAccept is null )
   * 
   * @param groupId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupMemberByGroupId(Long groupId) {
    String hql =
        "from GroupInvitePsn where groupId=:groupId and status=:status and  isAccept=:isAccept  and  groupRole in ('1' ,'2') order by groupRole asc ,createDate asc";
    Query query = createQuery(hql).setParameter("groupId", groupId).setParameter("status", "01")
        .setParameter("isAccept", "1").setMaxResults(5);
    return query.list();
  }

  /**
   * 获取已加入的人员总数
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public Integer findGroupMemberCount(Long groupId) {
    String hql = "select count(invitePsnId) from GroupInvitePsn where groupId = '" + groupId
        + "' and isAccept = 1 and status = '01'";
    Long sum = super.findUnique(hql);
    return sum.intValue();
  }

  /**
   * 获取待批准的人员总数
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public Integer findGroupToMemberCount(Long groupId) {
    String hql = "select count(*) from GroupInvitePsn where groupId = ? and status = '01' and isAccept is null";
    Long sum = super.findUnique(hql, groupId);
    return sum.intValue();
  }

  /**
   * 获取群组统计数(修改了取群组的表为GroupFilter_MJG_SCM-6255).
   * 
   * @param psnId
   * @return
   */
  public Integer getGroupCountByPsnId(Long psnId) {
    String hql =
        "select count(gi.invitePsnId) from GroupInvitePsn gi,GroupFilter g where gi.psnId=:psnId and gi.status='01' and gi.isAccept='1' and gi.groupId = g.groupId and g.status='01' and g.openType in ('H','O') ";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return count.intValue();
  }

}
