package com.smate.center.batch.dao.pdwh.pubimport;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubKeywords;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果关键词dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubKeywordsDao extends PdwhHibernateDao<PdwhPubKeywords, Long> {

  public Long getpubKeywords(Long pubId, int type) {
    String hql = "select distinct(t.pubId) from PdwhPubKeywords t where t.pubId = :pubId and t.language = :type";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).uniqueResult();
  }

  public void deletePubKeywords(Long pubId, int type) {
    String hql = "delete PdwhPubKeywords t where t.pubId = :pubId and t.language = :type";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).executeUpdate();

  }

}
