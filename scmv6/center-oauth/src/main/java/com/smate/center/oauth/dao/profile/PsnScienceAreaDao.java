package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.PsnScienceArea;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnScienceAreaDao extends SnsHibernateDao<PsnScienceArea, Long> {
  /**
   * 查找人员拥有的有效科技领域的数量
   * 
   * @param psnId
   * @param status
   * @return
   */
  public Long findPsnHasScienceArea(Long psnId, Integer status) {
    String hql = "select count(1) from PsnScienceArea t where t.psnId = :psnId and t.status = :status";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).uniqueResult();
  }
}
