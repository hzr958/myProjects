package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.KpiRefreshPub;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class KpiRefreshPubDao extends RolHibernateDao<KpiRefreshPub, Long> {

  public void updatePubDel(Long pubId) {
    String hql = "update KpiRefreshPub t set t.isDel = 1 where t.pubId =:pubId";
    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
