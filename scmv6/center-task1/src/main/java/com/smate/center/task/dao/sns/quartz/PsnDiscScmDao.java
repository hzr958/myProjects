package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PsnDiscScm;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnDiscScmDao extends SnsHibernateDao<PsnDiscScm, Long> {

  public Long getClassificationCountByPsnId(Long psnId) {

    String hql = "select count(1) from PsnDiscScm t where t.psnId =:psnId";

    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public void deleteClassificationByPsnId(Long psnId) {
    String hql = "delete PsnDiscScm t where t.psnId =:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  /**
   * 查找人员分类ID（科技领域IDs）
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPsnDiscIds(Long psnId) {
    String hql = "select t.scmDiscNo from PsnDiscScm t where t.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
