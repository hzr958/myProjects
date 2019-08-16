package com.smate.center.task.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpRcmd;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GrpRcmdDao extends SnsHibernateDao<GrpRcmd, Long> {

  public GrpRcmd getGrpRcmd(Long psnId, Long grpId) {
    String hql = "from GrpRcmd  where psnId = :psnId and grpId = :grpId";
    return (GrpRcmd) super.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
  }

  public void updateGrpRcmd(Long grpId) {
    String hql = "update GrpRcmd t set t.status=99  where  t.grpId = :grpId and t.status =0";
    super.createQuery(hql).setParameter("grpId", grpId).executeUpdate();
  }

  public void deleteGrpRcmd(Long grpId) {
    String hql = "delete GrpRcmd t where  t.grpId = :grpId and t.status =0";
    super.createQuery(hql).setParameter("grpId", grpId).executeUpdate();
  }

}
