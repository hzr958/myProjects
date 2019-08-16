package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwRmcSync;
import com.smate.core.base.utils.data.SnsHibernateDao;


@Repository
public class PsnKwRmcSyncDao extends SnsHibernateDao<PsnKwRmcSync, Long> {
  public int queryCount(Long psnId) {
    String hql = "select count(1) from PsnKwRmcSync where psnId=:psnId";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return NumberUtils.toInt(count.toString());
  }

  public void addNew(Long psnId) {
    String hql = "insert into psn_kw_rmc_sync(psn_Id) values (?)";
    super.update(hql, new Object[] {psnId});
  }

}
