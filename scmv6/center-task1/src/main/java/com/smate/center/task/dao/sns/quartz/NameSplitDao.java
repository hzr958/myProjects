package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.NameSplit;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class NameSplitDao extends SnsHibernateDao<NameSplit, Long> {

  @SuppressWarnings("unchecked")
  public List<NameSplit> getNameSplit(Integer status, Integer size) {
    String hql = "from NameSplit t where t.status =:status";
    List<NameSplit> rsList = super.createQuery(hql).setParameter("status", status).setMaxResults(size).list();
    return rsList;
  }

}
