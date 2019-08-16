package com.smate.center.batch.dao.pdwh.pubimport;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhFullTextFile;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库全文文件dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhFullTextFileDao extends PdwhHibernateDao<PdwhFullTextFile, Long> {

  public Long getCountByPubAllId(Long pubAllId) {
    String hql = "select count(1) from PdwhFullTextFile t where t.pubId =:pubAllId";
    return (Long) super.createQuery(hql).setParameter("pubAllId", pubAllId).uniqueResult();
  }


}
