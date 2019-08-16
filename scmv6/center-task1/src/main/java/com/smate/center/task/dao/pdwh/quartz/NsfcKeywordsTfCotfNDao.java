package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.NsfcKeywordsTfCotfN;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcKeywordsTfCotfNDao extends PdwhHibernateDao<NsfcKeywordsTfCotfN, Long> {
  @SuppressWarnings("unchecked")
  public List<NsfcKeywordsTfCotfN> getNsfcKeywordsList(Long lastId, Integer size) {

    String hql = "from NsfcKeywordsTfCotfN t where t.id>:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(size).list();
  }
}
