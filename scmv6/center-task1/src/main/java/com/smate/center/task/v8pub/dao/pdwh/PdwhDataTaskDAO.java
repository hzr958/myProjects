package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhDataTaskDAO extends PdwhHibernateDao<PdwhDataTaskPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhDataTaskPO> findPdwhPubId(Long startId, Long endId, Integer size) {
    String hql = "from PdwhDataTaskPO t where t.pubId <=:startId and t.pubId >=:endId and t.status = 0";
    return this.createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhDataTaskPO> findSorlPdwhPubId(Long startId, Long endId, Integer size) {
    String hql = "from PdwhDataTaskPO t where t.pubId <=:startId and t.pubId >=:endId and t.status = 2";
    return this.createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhDataTaskPO> findPdwhPubId(Integer size) {
    String hql = "from PdwhDataTaskPO t where t.status = 0";
    return this.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhDataTaskPO> findPsnId(Integer size) {
    String hql = "from PdwhDataTaskPO t where t.status = 3";
    return this.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhDataTaskPO> findPdwhIntoPubId(Integer sIZE) {
    String hql = "from PdwhDataTaskPO t where t.status = 4";
    return this.createQuery(hql).setMaxResults(sIZE).list();
  }

}
