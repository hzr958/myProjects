package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果xml dao
 * 
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

  @SuppressWarnings("unchecked")
  public List<PdwhPubXml> getPdwhPubXml() {
    String hql = " from PdwhPubXml t where t.pubId <= 1000L";
    return super.createQuery(hql).list();
  }

}
