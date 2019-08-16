package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnCoRmcRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnCoRmcRefreshDao extends SnsHibernateDao<PsnCoRmcRefresh, Long> {

  public int queryCount(Long psnId) {
    String hql = "select count(1) from PsnCoRmcRefresh where psnId=:psnId";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return NumberUtils.toInt(count.toString());
  }

  public void updatePsnCoRmcRefresh(Long psnId) {
    String hql = "update PsnCoRmcRefresh t set t.status=0,t.mkAt=sysdate where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

}
