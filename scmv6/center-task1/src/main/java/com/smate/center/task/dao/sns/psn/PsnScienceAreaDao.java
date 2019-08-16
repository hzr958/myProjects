package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.psn.model.PsnScienceArea;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人研究领域
 * 
 * @author zjh
 *
 */
@Repository
public class PsnScienceAreaDao extends SnsHibernateDao<PsnScienceArea, Long> {
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdByCatId(Integer scienceAreaId) {
    String hql = "select psnId from PsnScienceArea where scienceAreaId=:scienceAreaId  and status=1";
    return super.createQuery(hql).setParameter("scienceAreaId", scienceAreaId).list();

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
