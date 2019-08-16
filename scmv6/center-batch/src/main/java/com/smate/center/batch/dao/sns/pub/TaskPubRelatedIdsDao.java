package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.TaskPubRelatedIds;
import com.smate.core.base.utils.data.SnsHibernateDao;


@Repository
public class TaskPubRelatedIdsDao extends SnsHibernateDao<TaskPubRelatedIds, Long> {


  @SuppressWarnings("unchecked")
  public List<Long> findTaskPubRelatedIds(int batchSize) {
    String hql = "select t.pubId from TaskPubRelatedIds t where t.status =? order by t.pubId asc";
    return super.createQuery(hql, 0).list();
  }

  public void updateTaskPubRelatedIds(Long pubId, int status) {
    String hql = "update TaskPubRelatedIds set status=? where pubId=?";
    super.createQuery(hql, status, pubId).executeUpdate();
  }
}
