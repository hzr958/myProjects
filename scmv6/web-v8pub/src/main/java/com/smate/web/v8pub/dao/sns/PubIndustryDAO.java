package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubIndustryPO;

@Repository
public class PubIndustryDAO extends SnsHibernateDao<PubIndustryPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PubIndustryPO> getPubIndustryList(Long pubId) {
    String hql = "from PubIndustryPO t where t.pubId=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }


  /**
   * 通过pubId删除所有的行业
   * 
   * @param pubId
   */
  public void deleteById(Long pubId) {
    String hql = "delete from PubIndustryPO t where t.pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
