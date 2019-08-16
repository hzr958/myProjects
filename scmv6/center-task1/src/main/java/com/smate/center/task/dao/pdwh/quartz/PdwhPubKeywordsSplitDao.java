package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPubKeywordsSplit;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果关键词拆分dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubKeywordsSplitDao extends PdwhHibernateDao<PdwhPubKeywordsSplit, Long> {

  public Long getPubKeywordsSplit(Long pubId, int type) {
    String hql = "select distinct t.pubId from PdwhPubKeywordsSplit t where t.pubId = :pubId and t.language = :type";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).uniqueResult();
  }

  public void deletePubKeywordsSplit(Long pubId, int type) {
    String hql = "delete PdwhPubKeywordsSplit t where t.pubId = :pubId and t.language = :type";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).executeUpdate();
  }

}
