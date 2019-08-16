package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PsnDiscNsfc;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnDiscNsfcDao extends SnsHibernateDao<PsnDiscNsfc, Long> {

  public Long getMinimunPsnId() {
    String hql = "select min(t.psnId) from PsnDiscNsfc t";
    return (Long) super.createQuery(hql).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getBatchPsnId(Long lastPsnId, Integer size) {
    String hql = "select psnId from PsnDiscNsfc where psnId >:lastPsnId order by psnId asc";
    return super.createQuery(hql).setParameter("lastPsnId", lastPsnId).setMaxResults(size).list();
  }

}
