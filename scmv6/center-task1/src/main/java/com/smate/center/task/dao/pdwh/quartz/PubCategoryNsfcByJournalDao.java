package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PubCategoryNsfcByJournal;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubCategoryNsfcByJournalDao extends PdwhHibernateDao<PubCategoryNsfcByJournal, Long> {
  public void deleteByPubId(Long pubId) {
    String hql = "delete PubCategoryNsfcByJournal t where t.pubId =:pubId";
    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  public Long getCountsByPubId(Long pubId) {
    String hql = "select count(1) from PubCategoryNsfcByJournal t where t.pubId =:pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public List<String> findCategoryByPubId(Long pubId) {
    String hql = "select distinct(t.nsfcCategoryId) from PubCategoryNsfcByJournal t where t.pubId=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
