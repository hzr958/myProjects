package com.smate.center.task.dao.pdwh.quartz;

import com.smate.center.task.model.pdwh.pub.PdwhCitedRelation;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class PdwhCitedRelationDao extends PdwhHibernateDao<PdwhCitedRelation, Long> {

  public PdwhCitedRelation getPdwhCitedRelation(Long pubId, Long citedPubId) {
    String hql = "from PdwhCitedRelation where pdwdPubId = :pubId and pdwhCitedPubId = :citedPubId";
    return (PdwhCitedRelation) super.createQuery(hql).setParameter("pubId", pubId)
        .setParameter("citedPubId", citedPubId).uniqueResult();

  }

}
