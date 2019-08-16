package com.smate.center.task.v8pub.dao.sns;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubDataTaskDAO extends SnsHibernateDao<PubDataTaskPO, Long> {
  @SuppressWarnings("unchecked")
  public List<PubDataTaskPO> findPubId(Long startId, Long endId, Integer size) {
    String hql = "from PubDataTaskPO t where t.pubId <=:startId and t.pubId >=:endId and t.status = 0";
    return this.createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<PubDataTaskPO> findPubId(Integer size) {
    String hql = "from PubDataTaskPO t where t.status = 0";
    return this.createQuery(hql).setMaxResults(size).list();
  }

}
