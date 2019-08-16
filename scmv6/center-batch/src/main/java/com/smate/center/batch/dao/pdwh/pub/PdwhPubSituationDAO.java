package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhPubSituationPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubSituationDAO extends PdwhHibernateDao<PdwhPubSituationPO, Long> {
  @SuppressWarnings("unchecked")
  public Long getPubIdBySrcId(String sourceId) {
    String hql = "select p.pdwhPubId from PdwhPubSituationPO p where p.srcId = :sourceId and p.sitStatus =1 "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    List<Long> list = super.createQuery(hql).setParameter("sourceId", sourceId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
