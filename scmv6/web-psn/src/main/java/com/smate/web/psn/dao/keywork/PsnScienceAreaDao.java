package com.smate.web.psn.dao.keywork;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.PsnScienceArea;

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
    // 这里做修改的时候记得solr那边也需要，SearchPersonsServiceImpl也需要修改
    String hql = "from PsnScienceArea t where t.psnId = :psnId and t.status = :status order by areaOrder asc,t.id asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  /**
   * 查找人员有效的科技领域列表
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<Integer> findPsnScienceAreaIdList(Long psnId) {
    // 这里做修改的时候记得solr那边也需要，SearchPersonsServiceImpl也需要修改
    String hql =
        "select t.scienceAreaId from PsnScienceArea t where t.psnId = :psnId and t.status = 1 order by updateDate desc,t.id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
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
  @SuppressWarnings("unchecked")
  public PsnScienceArea findPsnScienceAreaByPsnIdAndId(Long psnId, Integer scienceAreaId) {
    String hql =
        "from PsnScienceArea t where t.psnId = :psnId and t.scienceAreaId = :scienceAreaId order by t.identificationSum desc nulls last,t.id desc";
    List<PsnScienceArea> list =
        super.createQuery(hql).setParameter("psnId", psnId).setParameter("scienceAreaId", scienceAreaId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
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
   * 查找个人有效的科技领域
   * 
   * @param psnId
   * @param status
   * @return
   */
  @SuppressWarnings("unchecked")
  public String queryScienceAreaIds(Long psnId) {
    String hql = "select t.scienceAreaId from PsnScienceArea t where t.psnId = :psnId and t.status = 1";
    List<Integer> scIds = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (scIds == null || scIds.size() == 0) {
      return null;
    } else {
      StringBuffer sb = new StringBuffer();
      for (Integer id : scIds) {
        sb.append(String.valueOf(id));
        sb.append(" ");
      }
      return sb.toString().trim();
    }

  }
}
