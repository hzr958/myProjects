package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnUpdateDiscLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnUpdateDiscLogDao extends SnsHibernateDao<PsnUpdateDiscLog, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getAllUndealPsnId(int size) {
    String hql = "select distinct(t.psnId) from PsnUpdateDiscLog  t where t.status=0 order by t.psnId";
    return createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getPsnDiscLogs(Long psnId) {
    String hql = "select t.keyWords from PsnUpdateDiscLog  t where t.status=0 and t.psnId= :psnId";
    return createQuery(hql).setParameter("psnId", psnId).list();
  }

  public void updatePsnUpdateDiscLog(Long psnId, int status) {
    String hql = "update PsnUpdateDiscLog set status = :status where psnId = :psnId and status = 0";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).executeUpdate();
  }

}
