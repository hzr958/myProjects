package com.smate.center.task.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpProposer;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GrpProposerDao extends SnsHibernateDao<GrpProposer, Long> {

  public Long getpsnGroupReqCount(Long personId) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=1 "
        + " and exists (select 1 from GrpMember t2 where t.grpId=t2.grpId and t2.psnId=:psnId and (t2.grpRole=1 or t2.grpRole=2) and t2.status='01') "
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " order by t.createDate desc,t.id desc ";
    return (Long) this.createQuery("select count(1) " + hql).setParameter("psnId", personId).uniqueResult();
  }

  public Long getpsnInviteReqCount(Long personId) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=2 and t.psnId=:psnId"
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " order by t.createDate desc,t.id desc ";
    return (Long) this.createQuery("select count(1) " + hql).setParameter("psnId", personId).uniqueResult();
  }

}
