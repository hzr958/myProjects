package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPubIndexUrl;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubIndexUrlDao extends PdwhHibernateDao<PdwhPubIndexUrl, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getNeedInitPubId(Integer index, Integer batchSize) {
    String hql = "select t.pubId from PdwhPubIndexUrl t where t.pubIndexUrl is null";
    return this.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();

  }

  public String getStringUrlByPubId(Long pubId) {
    String hql = "select t.pubIndexUrl from PdwhPubIndexUrl t where t.pubId =:pubId";
    return (String) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<String> getAllUrl(Integer index, Integer batchSize) {
    String hql = "select t.pubIndexUrl from PdwhPubIndexUrl t where t.updateDate > sysdate-30";
    return this.createQuery(hql).setFirstResult(index).setMaxResults(batchSize).list();
  }
}
