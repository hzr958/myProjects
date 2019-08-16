package com.smate.web.v8pub.dao.sns.psn;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.psn.RecommendScienceArea;

@Repository
public class RecommendScienceAreaDao extends SnsHibernateDao<RecommendScienceArea, Long> {

  /**
   * 查找人员有效的科技领域列表
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RecommendScienceArea> findRecommendScienceAreaList(Long psnId) {
    String hql = "from RecommendScienceArea t where t.psnId = :psnId order by updateDate desc,scienceAreaId desc";
    Query query = super.createQuery(hql).setParameter("psnId", psnId);
    List<RecommendScienceArea> list = query.setMaxResults(3).list();
    List<RecommendScienceArea> deletelist = query.setFirstResult(3).list();
    if (deletelist != null) {// 主要删除掉多出来的旧数据，之前是5个科技领域现在是3个了
      for (RecommendScienceArea item : deletelist) {
        super.delete(item);
      }
    }
    return list;
  }

  /**
   * 删除psn下的某个科技领
   * 
   * @param psnId
   */
  public void deletePsnOneArea(Long psnId, Integer scienceAreaId) {
    String hql = "delete RecommendScienceArea t where t.psnId = :psnId and t.scienceAreaId = :scienceAreaId";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("scienceAreaId", scienceAreaId).executeUpdate();
  }

  /**
   * 查找人员拥有的有效科技领域的数量
   * 
   * @param psnId
   * @return
   */
  public Long findPsnHasScienceArea(Long psnId) {
    String hql = "select count(1) from RecommendScienceArea t where t.psnId = :psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员所属的某个科技领域
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public RecommendScienceArea findRecommendScienceAreaByPsnIdAndId(Long psnId, Integer scienceAreaId) {
    String hql = "from RecommendScienceArea t where t.psnId = :psnId and t.scienceAreaId = :scienceAreaId";
    return (RecommendScienceArea) super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("scienceAreaId", scienceAreaId).uniqueResult();
  }

  /**
   * 删除psn下的所有的科技领
   * 
   * @param psnId
   */
  public void deletePsnAllArea(Long psnId) {
    String hql = "delete RecommendScienceArea t where t.psnId = :psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
