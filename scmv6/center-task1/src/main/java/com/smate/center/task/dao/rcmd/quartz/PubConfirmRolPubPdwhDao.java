package com.smate.center.task.dao.rcmd.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPubPdwh;
import com.smate.core.base.utils.data.RcmdHibernateDao;

@Repository
public class PubConfirmRolPubPdwhDao extends RcmdHibernateDao<PubConfirmRolPubPdwh, Long> {
  public void deletePdwh(Long rolPubId) {
    String hql = "delete from PubConfirmRolPubPdwh t where t.rolPubId=:rolPubId";
    this.createQuery(hql).setParameter("rolPubId", rolPubId).executeUpdate();
  }

}
