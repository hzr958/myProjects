package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.TaskPubAllBriefIds;
import com.smate.core.base.utils.data.PdwhHibernateDao;


@Repository
public class TaskPubAllBriefIdsDao extends PdwhHibernateDao<TaskPubAllBriefIds, Long> {


  @SuppressWarnings("unchecked")
  public List<TaskPubAllBriefIds> findTaskPubAllBriefIds(int batchSize) {
    String hql = "from TaskPubAllBriefIds t where t.status =? order by t.puballId asc";
    return super.createQuery(hql, 0).list();
  }

  public void updateTaskPubAllBriefIds(Long pubId, int status) {
    String hql = "update TaskPubAllBriefIds set status=? where puballId=?";
    super.createQuery(hql, status, pubId).executeUpdate();
  }
}
