package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwRmcGid;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwRmcGidDao extends SnsHibernateDao<PsnKwRmcGid, Long> {
  public void deleteByPsnId(Long psnId) {
    String sql = "delete from PSN_KW_RMC_GID where psn_id=?";
    super.update(sql, new Object[] {psnId});
  }

  public Long getCount(Long psnId, Long gid) {
    String hql = "select count(1) from PsnKwRmcGid where psnId=:psnId and gid=:gid";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("gid", gid).uniqueResult();

  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnKwRmcGids(Long psnId) {
    String hql = "select gid from PsnKwRmcGid where psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
