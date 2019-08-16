package com.smate.web.fund.recommend.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.recommend.model.PsnScienceArea;

@Repository
public class PsnScienceAreaDao extends SnsHibernateDao<PsnScienceArea, Long> {

  /**
   * 查找人员有效的科技领域列表
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<PsnScienceArea> findPsnScienceAreaList(Long psnId, Integer status) {
    String hql = "from PsnScienceArea t where t.psnId = :psnId and t.status = :status";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  /**
   * 将人员所有科研领域置为无效状态
   * 
   * @param psnId
   * @param status
   */
  public void updateSAStatusByPsnId(Long psnId, Integer status) {
    String hql = "update PsnScienceArea t set t.status= :status where t.psnId=:psnId ";
    super.createQuery(hql).setParameter("status", status).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 查找人员所属的某个科技领域
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public PsnScienceArea findPsnScienceAreaByPsnIdAndId(Long psnId, Long scienceAreaId) {
    String hql = "from PsnScienceArea t where t.psnId = :psnId and t.scienceAreaId = :scienceAreaId";
    return (PsnScienceArea) super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("scienceAreaId", scienceAreaId).uniqueResult();
  }

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
        "select new PsnScienceArea(t.scienceAreaId, t.scienceArea, t.enScienceArea) from PsnScienceArea t where t.psnId = :psnId and t.status = :status order by nvl(t.identificationSum,0) desc,t.scienceAreaId desc";
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
  public List<Long> findPsnScienceAreaIds(Long psnId) {
    String hql =
        "select t.scienceAreaId from PsnScienceArea t where t.psnId = :psnId and t.status = 1 order by t.areaOrder asc,t.id asc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
