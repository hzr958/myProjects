package com.smate.center.open.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.pdwh.pub.PdwhPubIndexUrl;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubIndexUrlDao extends PdwhHibernateDao<PdwhPubIndexUrl, Long> {


  public String getStringUrlByPubId(Long pubId) {
    String hql = "select t.pubIndexUrl from PdwhPubIndexUrl t where t.pubId =:pubId";
    return (String) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }
}
