package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.TaskPubReadIds;
import com.smate.core.base.utils.data.SnsHibernateDao;


@Repository
public class TaskPubReadIdsDao extends SnsHibernateDao<TaskPubReadIds, Long> {


  @SuppressWarnings("unchecked")
  public List<Long> findTaskPubReadIds(int batchSize) {
    String hql = "select t.pubId from TaskPubReadIds t where t.status =? order by t.pubId asc";
    return super.createQuery(hql, 0).list();
  }

  public void updateTaskPubReadIds(Long pubId, int status) {
    String hql = "update TaskPubReadIds set status=? where pubId=?";
    super.createQuery(hql, status, pubId).executeUpdate();
  }
}
