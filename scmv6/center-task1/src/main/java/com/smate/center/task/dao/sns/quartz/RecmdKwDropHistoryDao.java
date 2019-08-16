package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.RecmdKwDropHistory;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class RecmdKwDropHistoryDao extends SnsHibernateDao<RecmdKwDropHistory, Long> {

  public int getDropCount(Long psnId) {
    String hql = "select count(1) from RecmdKwDropHistory t where t.psnId=:psnId";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return NumberUtils.toInt(count.toString());

  }


}
