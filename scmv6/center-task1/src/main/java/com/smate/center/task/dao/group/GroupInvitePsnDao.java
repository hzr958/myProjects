package com.smate.center.task.dao.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.group.GroupInvitePsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zjh 群组成员dao
 *
 */
@Repository
public class GroupInvitePsnDao extends SnsHibernateDao<GroupInvitePsn, Long> {

  /**
   * 根据groupId查找出群组成员
   * 
   * @param groupId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Long> findGroupAdminsPsnIdList(Long groupId) throws Exception {

    String hql = "select psnId from GroupInvitePsn where groupId =:groupId and isAccept='1' and status='01' ";
    return super.createQuery(hql).setParameter("groupId", groupId).list();
  }

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
  public List<Long> findGroupInvitePsnByPrj(List<Long> psnIds) {
    String hql =
        "select distinct t.psnId from GrpProposer t where exists(select 1 from GrpBaseinfo t2 where t2.grpCategory=11 and t2.status='01' and t.grpId=t2.grpId) and t.psnId in(:psnIds) order by t.psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  // 只查询从基金委isis导到sns的群组
  @SuppressWarnings("unchecked")
  public List<Long> findPrjGroupIdsByPsnId(Long psnId) {
    String hql =
        "select distinct t.grpId from GrpProposer t where exists(select 1 from GrpBaseinfo t2 where t2.projectNo is not null and t2.grpCategory=11 and t2.status='01'and t.grpId=t2.grpId) and t.isAccept=1 and  t.psnId = :psnId";
    return createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findGroupInvitePsnIdsByMap(List<Long> groupIds, Long psnId) {
    String hql =
        "select t.psnId,count(t.psnId) from GrpProposer t,PersonKnow t2 where t.psnId=t2.personId and t2.isAddFrd=0 and t2.isLogin=1 and t2.isPrivate=0 and t.isAccept=1 and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId2) and t.grpId in(:groupIds) group by t.psnId";
    Query query = createQuery(hql).setParameter("psnId2", psnId).setParameterList("groupIds", groupIds);
    List<Object[]> list = query.list();
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    for (Object[] objs : list) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", objs[0]);
      map.put("count", objs[1]);
      mapList.add(map);
    }
    return mapList;
  }
}
