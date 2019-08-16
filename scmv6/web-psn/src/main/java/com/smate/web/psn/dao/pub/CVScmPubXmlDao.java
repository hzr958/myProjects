package com.smate.web.psn.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.pub.CVScmPubXml;

/**
 * 成果XMLDAO
 * 
 * @author tj
 *
 */
@Repository
public class CVScmPubXmlDao extends SnsHibernateDao<CVScmPubXml, Long> {

  /**
   * 查找成果xml
   * 
   * @param pubId
   * @return
   */
  public String findPubXmlById(Long pubId) {
    String hql = "select t.pubXml from CVScmPubXml t where t.pubId = :pubId";
    return (String) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }
}
