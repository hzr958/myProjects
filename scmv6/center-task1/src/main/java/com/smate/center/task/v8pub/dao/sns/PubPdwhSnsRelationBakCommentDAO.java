package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubPdwhSnsRelationBakC;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubPdwhSnsRelationBakCommentDAO extends SnsHibernateDao<PubPdwhSnsRelationBakC, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getPubPdwhIdList(Long startId, Long endId, Integer size) {
    String hql =
        "select t.pdwhPubId from PubPdwhSnsRelationBakC t where t.pdwhPubId <=:startId and t.pdwhPubId >=:endId and t.status=0 order by t.pdwhPubId asc";
    return this.createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size)
        .list();
  }

  public void updatePubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) {
    String hql = "update PubPdwhSnsRelationBakC set status=:status,errMsg=:errMsg where pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("status", status).setParameter("errMsg", errMsg)
        .setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }
}
