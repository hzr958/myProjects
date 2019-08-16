package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubHtml;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 成果html
 * 
 * @author zk
 * 
 */
@Repository
public class PubHtmlDao extends RolHibernateDao<PubHtml, Long> {

  public PubHtml findByPsnIdAndTempCode(Long psnId, Integer tempCode) {
    String hql = "from PubHtml p where p.pubId=? and p.tempCode=?";
    return (PubHtml) super.createQuery(hql, psnId, tempCode).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubHtml> findPubHtmlList(List<Long> pubIds) {

    String hql = "from PubHtml p where p.pubId in (:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  public PubHtml findPubHtml(Long pubId, Integer tempCode) {

    String hql = "from PubHtml p where p.pubId = ? and p.tempCode=?";
    return (PubHtml) super.createQuery(hql, pubId, tempCode).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubHtml> findPubHtmlList(List<Long> pubIds, Integer tempCode) {
    String hql = "from PubHtml p where p.pubId in (:pubIds) and p.tempCode=:tempCode";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("tempCode", tempCode).list();
  }
}
