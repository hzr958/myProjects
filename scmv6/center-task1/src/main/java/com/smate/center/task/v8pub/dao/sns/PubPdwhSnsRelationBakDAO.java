package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubPdwhSnsRelationBak;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubPdwhSnsRelationBakDAO extends SnsHibernateDao<PubPdwhSnsRelationBak, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getPubPdwhIdList(Long startId, Long endId, Integer size) {
    String hql =
        "select t.pdwhPubId from PubPdwhSnsRelationBak t where t.pdwhPubId <=:startId and t.pdwhPubId >=:endId and t.status=0";
    return this.createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size)
        .list();
  }

  public void updatePubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) {
    String hql = "update PubPdwhSnsRelationBak set status=:status,errMsg=:errMsg where pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("status", status).setParameter("errMsg", errMsg)
        .setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }
}
