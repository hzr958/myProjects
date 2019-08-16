package com.smate.center.task.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.TemTaskSnsBrief;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class TemTaskSnsBriefDao extends SnsHibernateDao<TemTaskSnsBrief, Long> {

  @SuppressWarnings("unchecked")
  public List<TemTaskSnsBrief> getUpdatePublist(Integer size) {
    String hql = "from TemTaskSnsBrief where status=0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

}
