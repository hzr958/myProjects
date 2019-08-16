package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubPdwhSnsRelation;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 基准库成果ID和SNS成果ID关系对应dao
 * 
 * @author LJ
 *
 */
@Repository
public class PubPdwhSnsRelationDao extends SnsHibernateDao<PubPdwhSnsRelation, Long> {

  public Long getSnsPubIdByPdwhPubId(Long pdwhPubId) {

    String hql = "select t.snsPubId from PubPdwhSnsRelation t where t.pdwhPubId=:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (Long) createQuery(hql).setParameter("PdwhPubid", pdwhPubId).uniqueResult();

  }

}
