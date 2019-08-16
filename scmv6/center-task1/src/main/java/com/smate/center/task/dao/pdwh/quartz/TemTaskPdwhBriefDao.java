package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.TemTaskPdwhBrief;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class TemTaskPdwhBriefDao extends PdwhHibernateDao<TemTaskPdwhBrief, Long> {

  @SuppressWarnings("unchecked")
  public List<TemTaskPdwhBrief> getUpdatePublist(Integer size) {
    String hql = "from TemTaskPdwhBrief where status=0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

}
