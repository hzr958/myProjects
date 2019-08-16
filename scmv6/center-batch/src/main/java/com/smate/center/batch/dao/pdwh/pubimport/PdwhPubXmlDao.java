package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * @author zjh
 *
 */
@Repository
public class PdwhPubXmlDao extends PdwhHibernateDao<PdwhPubXml, Long> {

  /**
   * 
   * @param pubId
   * @return
   */
  public String getXmlStringByPubId(Long pubId) {

    String hql = "select t.xml from PdwhPubXml t where t.pubId=:pubId";

    return (String) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();

  }

  public PdwhPubXml getpdwhPubXmlPubId(Long pubId) {
    String hql = " from PdwhPubXml t where t.pubId=:pubId";
    return (PdwhPubXml) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public List<PdwhPubXml> findListByIdBet(Integer size, Long startPubId, Long endPubId) {
    String hql = "from PdwhPubXml t where exists ( "
        + "select 1 from PdwhPublication t2 where t2.pubType=5 and t.pubId=t2.pubId "
        + ") and t.pubId>=:startPubId and t.pubId<:endPubId order by t.pubId ";
    return this.createQuery(hql).setParameter("startPubId", startPubId).setParameter("endPubId", endPubId)
        .setMaxResults(size).list();
  }
}
