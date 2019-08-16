package com.smate.center.open.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.profile.PsnScienceArea;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnScienceAreaDao extends SnsHibernateDao<PsnScienceArea, Long> {
  /**
   * 查找个人有效的科技领域
   * 
   * @param psnId
   * @param status
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnScienceArea> queryScienceArea(Long psnId, Integer status) {
    String hql =
        "select new PsnScienceArea(t.scienceAreaId, t.scienceArea, t.enScienceArea) from PsnScienceArea t where t.psnId = :psnId and t.status = :status order by updateDate desc,t.id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

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

  /**
   * 查找人员有效的科技领域ID
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPsnScienceAreaIds(Long psnId) {
    String hql =
        "select t.scienceAreaId from PsnScienceArea t where t.psnId = :psnId and t.status = 1 order by updateDate desc,t.id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }


}
