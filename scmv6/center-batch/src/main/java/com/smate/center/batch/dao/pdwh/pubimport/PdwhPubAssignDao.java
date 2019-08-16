package com.smate.center.batch.dao.pdwh.pubimport;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAssign;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubAssignDao extends PdwhHibernateDao<PdwhPubAssign, Long> {
  /**
   * 获取PdwhPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public PdwhPubAssign getPdwhPubAssign(Long pubId, Long insId) {
    String hql = "from PdwhPubAssign t where t.pubId= :pubId and t.insId = :insId ";
    return (PdwhPubAssign) super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId)
        .uniqueResult();
  }

}
