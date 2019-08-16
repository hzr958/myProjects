package com.smate.center.task.dao.sns.grp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpMember;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组成员关系Dao类
 * 
 * @author zzx
 *
 */
@Repository
public class GrpMemberDao extends SnsHibernateDao<GrpMember, Long> {

  /**
   * 查找群组成员ids
   * 
   * @param grpId
   * @return
   */
  public List<Long> getGrpMemberIdByGrpId(Long grpId) {
    String hql = "select   t.psnId  from GrpMember t  where  t.grpId =:grpId and t.status = '01' ";
    return this.createQuery(hql).setParameter("grpId", grpId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getHandlePsnId(Long lastPsnId, int batchSize) {
    String hql =
        "select distinct t.psnId from GrpMember t where exists(select 1 from GrpBaseinfo t2 where t2.projectNo is not null and t2.grpCategory=11 and t2.status='01' and t.grpId=t2.grpId) and t.status='01' and t.psnId > :lastPsnId order by t.psnId asc";
    return createQuery(hql).setParameter("lastPsnId", lastPsnId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPrjCoBygrpId(Long prjGroupId, Long psnId) {
    String hql =
        "select distinct t.psnId from GrpMember t where t.grpId = :grpId and t.status='01'  and t.psnId not in (:psnId)";
    return createQuery(hql).setParameter("grpId", prjGroupId).setParameter("psnId", psnId).list();
  }

  public List<Long> findPrjGroupIdsByPsnId(Long psnId) {
    String hql =
        "select distinct t.grpId from GrpMember t where exists(select 1 from GrpBaseinfo t2 where t2.projectNo is not null and t2.grpCategory=11 and t2.status='01'and t.grpId=t2.grpId) and t.status='01' and  t.psnId = :psnId";
    return createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getInstGrpPsnId() {
    String hql =
        "select distinct t.psnId from GrpMember t where exists(select 1 from GrpBaseinfo t2 where t2.grpCategory=12 and t2.status='01' and t.grpId=t2.grpId) and t.status='01' ";
    return createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnInstGrpIds(Long psnId) {
    String hql =
        "select distinct t.grpId from GrpMember t where exists(select 1 from GrpBaseinfo t2 where t2.grpCategory=12 and t2.status='01' and t.grpId=t2.grpId) and t.status='01' and  t.psnId = :psnId";
    return createQuery(hql).setParameter("psnId", psnId).list();
  }


}
