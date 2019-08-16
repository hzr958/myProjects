package com.smate.center.task.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.PatentHisData;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class PatentHisDataDao extends SnsbakHibernateDao<PatentHisData, Long> {

  @SuppressWarnings("unchecked")
  public List<PatentHisData> getPatList(Integer size) {
    String hql = "from PatentHisData t  where t.status=0 order by t.id";
    return createQuery(hql).setMaxResults(size).list();

  }

  public void updateHandleStatus(Long id, int status) {
    String hql = "update PatentHisData t set  t.status = :status where t.id = :id";
    createQuery(hql).setParameter("status", status).setParameter("id", id).executeUpdate();
  }

}
