package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwEptGid;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关键词分组dao
 * 
 * @author Administrator
 *
 */
@Repository
public class PsnKwEptGidDao extends SnsHibernateDao<PsnKwEptGid, Long> {
  public void deleteBypsnId(Long psnId) {
    String hql = "delete from PsnKwEptGid where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public Long getCount(Long psnId, Long gid) {
    String hql = "select count(1) from PsnKwEptGid t where t.psnId=:psnId and t.gid=:gid";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("gid", gid).uniqueResult();
  }

}
