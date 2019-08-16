package com.smate.web.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubSourceDbPO;

/**
 * 成果dbid dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubSourceDbDao extends PdwhHibernateDao<PdwhPubSourceDbPO, Long> {

  /**
   * 根据pubId获取PdwhPubSourceDb
   * 
   * @param pubId
   * @return
   */
  public PdwhPubSourceDbPO getPubSourceDb(Long pubId) {
    String hql = "from PdwhPubSourceDbPO t where t.pubId =:pubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (PdwhPubSourceDbPO) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();

  }

}
