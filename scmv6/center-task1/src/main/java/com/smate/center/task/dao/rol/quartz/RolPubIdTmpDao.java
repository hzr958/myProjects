package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.RolPubIdTmp;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class RolPubIdTmpDao extends RolHibernateDao<RolPubIdTmp, Long> {

  @SuppressWarnings("unchecked")
  public List<RolPubIdTmp> getRolPubId(Integer size) {
    String hql = "from RolPubIdTmp  t where t.status=0 order by t.pubId";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void updateStatus(RolPubIdTmp rolPubIdTmp) {
    String hql = "update RolPubIdTmp  t set t.status = :status where t.pubId = :pubId";
    super.createQuery(hql).setParameter("status", rolPubIdTmp.getStatus()).setParameter("pubId", rolPubIdTmp.getPubId())
        .executeUpdate();
  }

}
