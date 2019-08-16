package com.smate.center.batch.dao.tmp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.tmp.TmpPubSimpleId;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class TmpPubSimpleIdDao extends SnsHibernateDao<TmpPubSimpleId, Long> {
  @SuppressWarnings("unchecked")
  public List<Long> getTmpPubSimpleIds(Integer size, Long startPubId, Long endPubId) {
    String hql =
        "select t.pubId from TmpPubSimpleId t where t.pubId >:startPubId and t.pubId <=:endPubId order by t.pubId asc";
    return super.createQuery(hql).setParameter("startPubId", startPubId).setParameter("endPubId", endPubId)
        .setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getTmpPubSimpleIdsNew(Integer size, Long startPubId, Long endPubId) {
    String hql =
        "select t.pubId from TmpPubSimpleId t where t.pubId >:startPubId and t.pubId <=:endPubId and not exists(select 1 from TmpEncodedPubSimpleId t1 where t1.pubId = t.pubId) order by t.pubId asc";
    return super.createQuery(hql).setParameter("startPubId", startPubId).setParameter("endPubId", endPubId)
        .setMaxResults(size).list();
  }
}
