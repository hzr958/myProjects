package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubCategoryCrossref;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubCategoryCrossrefDao extends PdwhHibernateDao<PubCategoryCrossref, Long> {

  public PubCategoryCrossref getCategory(Long pubId, String category) {
    String hql = "from PubCategoryCrossref  t where t.pubId = :pubId and t.crossrefCategory = :category";
    return (PubCategoryCrossref) super.createQuery(hql).setParameter("pubId", pubId).setParameter("category", category)
        .uniqueResult();

  }

}
