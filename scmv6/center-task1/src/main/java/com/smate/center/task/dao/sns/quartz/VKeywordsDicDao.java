package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.VKeywordsDic;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class VKeywordsDicDao extends SnsHibernateDao<VKeywordsDic, Long> {

  public Long getDupKwFturesHash(Long fturesHash) {
    String hql = "select applicationId from VKeywordsDic where fturesHash=:fturesHash";
    return (Long) super.createQuery(hql).setParameter("fturesHash", fturesHash).uniqueResult();

  }

}
