package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhPubCitedTimes;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zjh 成果引用次数dao
 *
 */
@Repository
public class PdwhPubCitedTimesDao extends PdwhHibernateDao<PdwhPubCitedTimes, Long> {

  public PdwhPubCitedTimes getcitesByPubDBId(Long currentPubId, Integer dbId) {
    String hql = "from PdwhPubCitedTimes t  where t.pdwhPubId=:currentPubId and t.dbId =:dbId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return (PdwhPubCitedTimes) super.createQuery(hql).setParameter("currentPubId", currentPubId)
        .setParameter("dbId", dbId).uniqueResult();
  }
}
