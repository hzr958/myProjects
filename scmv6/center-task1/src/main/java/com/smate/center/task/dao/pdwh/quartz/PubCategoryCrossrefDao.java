package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PubCategoryCrossref;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubCategoryCrossrefDao extends PdwhHibernateDao<PubCategoryCrossref, Long> {

  public PubCategoryCrossref getCategory(Long pubId, String category) {
    String hql = "from PubCategoryCrossref  t where t.pubId = :pubId and t.crossrefCategory = :category";
    return (PubCategoryCrossref) super.createQuery(hql).setParameter("pubId", pubId).setParameter("category", category)
        .uniqueResult();

  }

}
