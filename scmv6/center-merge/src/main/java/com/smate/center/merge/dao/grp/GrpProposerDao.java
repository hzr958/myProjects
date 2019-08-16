package com.smate.center.merge.dao.grp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.grp.GrpProposer;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GrpProposerDao extends SnsHibernateDao<GrpProposer, Long> {

  public List<GrpProposer> queryGrpReq(Long psnId) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=1  and t.psnId=:psnId"
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " and exists (select 1 from Person  p where p.personId = t.psnId) "
        + " order by t.createDate desc,t.id desc ";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
