package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PubReference;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubReferenceDao extends PdwhHibernateDao<PubReference, Long> {

  public PubReference getReference(Long pubId, String key) {
    String hql = "from PubReference t where t.citedPdwhPubId = :pubId and t.key= :key";
    return (PubReference) super.createQuery(hql).setParameter("pubId", pubId).setParameter("key", key).uniqueResult();
  }

}
