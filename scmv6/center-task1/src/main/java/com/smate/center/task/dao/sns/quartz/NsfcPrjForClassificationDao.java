package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.NsfcPrjForClassification;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class NsfcPrjForClassificationDao extends SnsHibernateDao<NsfcPrjForClassification, Long> {

  @SuppressWarnings("unchecked")
  public List<NsfcPrjForClassification> getNsfcPrj(Integer status, Integer size) {
    String hql = "from NsfcPrjForClassification t where t.status =:status";
    List<NsfcPrjForClassification> rsList =
        super.createQuery(hql).setParameter("status", status).setMaxResults(size).list();
    return rsList;
  }

  @SuppressWarnings("unchecked")
  public List<String> getNsfcCategoryByPsnId(Long psnId) {
    String hql = "select t.nsfcCategoryCode from NsfcPrjForClassification t where t.psnId =:psnId";
    List<String> rsList = super.createQuery(hql).setParameter("psnId", psnId).list();
    return rsList;
  }

}
