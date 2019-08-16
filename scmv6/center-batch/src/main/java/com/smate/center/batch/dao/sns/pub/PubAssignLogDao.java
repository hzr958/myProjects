package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubAssignLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubAssignLogDao extends SnsHibernateDao<PubAssignLog, Long> {

  public PubAssignLog getPubAssign(Long pdwhPubId, Long psnId) {
    String hql =
        "from PubAssignLog t where t.psnId = :psnId and t.pdwhPubId = :pdwhPubId  and t.confirmResult=0 and t.status=0";
    return (PubAssignLog) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pdwhPubId", pdwhPubId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubConfirmNew(List<Long> dupPubIds, Long psnId) {
    String hql =
        "select distinct(t.pdwhPubId) from PubAssignLog t where t.psnId = :psnId and t.pdwhPubId in (:dupPubIds)  and t.status=0";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("dupPubIds", dupPubIds).list();
  }

}
