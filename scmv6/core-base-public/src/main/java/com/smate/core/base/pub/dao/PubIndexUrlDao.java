package com.smate.core.base.pub.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubIndexUrlDao extends SnsHibernateDao<PubIndexUrl, Long> {

  /**
   * 根据成果id获取成果的短地址
   * 
   * @param pubId
   * @return
   */
  public String getPubIndexUrl(Long pubId) {
    String hql = "select t.pubIndexUrl from PubIndexUrl t where t.pubId =:pubId";
    return (String) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 查找成果短地址
   * 
   * @param pubIds
   * @return
   */
  public List<PubIndexUrl> findPubIndexUrl(List<Long> pubIds) {
    String hql = "select new PubIndexUrl(t.pubId, t.pubIndexUrl) from PubIndexUrl t where t.pubId in(:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }
}
