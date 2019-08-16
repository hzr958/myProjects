package com.smate.center.task.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PubPdwhScmRol;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 处理rol，scm对应至pdwh临时表dao.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PubPdwhScmRolDao extends SnsHibernateDao<PubPdwhScmRol, Long> {

  @SuppressWarnings("unchecked")
  public List<PubPdwhScmRol> getToHandleList(Integer size) {
    String hql = "from PubPdwhScmRol t where t.status = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhScmRol> getToHandleList(Integer size, Long startPubId, Long endPubId) {
    String hql = "from PubPdwhScmRol t where t.status = 0 and t.pubId > :startPubId and t.pubId <= :endPubId";
    return super.createQuery(hql).setMaxResults(size).setParameter("startPubId", startPubId)
        .setParameter("endPubId", endPubId).list();
  }
}
