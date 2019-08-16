package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwEptRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwEptRefreshDao extends SnsHibernateDao<PsnKwEptRefresh, Long> {
  public List<Long> getRefreshPsn(Long startId) throws Exception {
    String hql = "select t.psnId from PsnKwEptRefresh t where  t.status = 0 and t.psnId>:startId order by t.psnId asc";
    List<Long> psnKwEptRefreshList = super.createQuery(hql).setParameter("startId", startId).setMaxResults(200).list();
    return psnKwEptRefreshList;

  }

  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnKwEptRefresh t where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
