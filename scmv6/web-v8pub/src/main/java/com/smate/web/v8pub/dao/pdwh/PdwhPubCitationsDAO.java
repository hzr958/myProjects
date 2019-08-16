package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubCitationsPO;

@Repository
public class PdwhPubCitationsDAO extends PdwhHibernateDao<PdwhPubCitationsPO, Long> {

  public PdwhPubCitationsPO getByPdwhPubId(Long pdwhPubId, Long dbId) {
    String hql = "FROM PdwhPubCitationsPO t where t.pdwhPubId=:pdwhPubId and t.dbId =:dbId "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) "
        + " order by t.citations desc";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubCitationsPO) list.get(0);
    }
    return null;
  }

  public PdwhPubCitationsPO getByPubIdAndDbId(Long pubId, Integer dbId) {
    String hql = "FROM PdwhPubCitationsPO t where t.pdwhPubId=:pdwhPubId and t.dbId=:dbId "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pubId).setParameter("dbId", dbId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubCitationsPO) list.get(0);
    }
    return null;
  }
}
