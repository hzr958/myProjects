package com.smate.center.task.single.dao.solr;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.single.model.solr.PdwhSnsPubFullText;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhSnsPubFullTextDao extends PdwhHibernateDao<PdwhSnsPubFullText, Long> {

  public List<PdwhSnsPubFullText> getListByPdwhAllId() {
    return null;
  }

  public Long getCountByPubAllId(Long pubAllId) {
    String hql = "select count(1) from PdwhSnsPubFullText t where t.pdwhPubAllId =:pubAllId";
    return (Long) super.createQuery(hql).setParameter("pubAllId", pubAllId).uniqueResult();
  }
}
