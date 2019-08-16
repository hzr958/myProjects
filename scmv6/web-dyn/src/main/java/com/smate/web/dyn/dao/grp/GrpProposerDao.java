package com.smate.web.dyn.dao.grp;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.grp.GrpProposer;

/**
 * 申请中成员关系Dao类
 * 
 * @author zzx
 *
 */
@Repository
public class GrpProposerDao extends SnsHibernateDao<GrpProposer, Long> {
  public Long getGrpReqCount(Long psnId) {
    String hql = "select count(t.grpId) from GrpProposer t where t.psnId=:psnId and t.type=2 and t.isAccept=2 "
        + " and exists (select 1 from GrpBaseinfo t2 where t2.grpId=t.grpId and t2.status<>'99' )"
        + " and exists (select 1 from Person  p where p.personId = t.inviterId) ";
    Object result = this.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (result == null) {
      return 0L;
    } else {
      return (Long) result;
    }
  }

  public Long getGrpInvCount(Long psnId) {
    String hql = "select count(1) from GrpProposer t where t.type=1 and t.isAccept=2 and t.grpId in "
        + " (select t2.grpId from GrpMember t2 where (t2.grpRole=1 or t2.grpRole=2) and t2.psnId =:psnId) "
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status<>'99' )"
        + " and exists (select 1 from Person  p where p.personId = t.psnId) ";
    Object result = this.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (result == null) {
      return 0L;
    } else {
      return (Long) result;
    }
  }
}
