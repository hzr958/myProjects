package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.ScmUserForClassification;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ScmUserForClassificationDao extends SnsHibernateDao<ScmUserForClassification, Long> {

  @SuppressWarnings("unchecked")
  public List<ScmUserForClassification> getScmUserForClassification(Integer status, Integer size) {
    String hql = "from ScmUserForClassification t where t.status =:status";
    List<ScmUserForClassification> rsList =
        super.createQuery(hql).setParameter("status", status).setMaxResults(size).list();
    return rsList;
  }

}
