package com.smate.center.task.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubCitationsPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubCitationsDAO extends PdwhHibernateDao<PdwhPubCitationsPO, Long> {

  public PdwhPubCitationsPO getcitesByPubDBId(Long currentPubId, Integer dbId) {
    String hql = "from PdwhPubCitationsPO t where t.pdwhPubId = :currentPubId and t.dbId = :dbId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return (PdwhPubCitationsPO) createQuery(hql).setParameter("currentPubId", currentPubId).setParameter("dbId", dbId)
        .uniqueResult();
  }

  public void deleteByPubId(Long pdwhPubId) {
    String hql = "delete from PdwhPubCitationsPO t where t.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

}
