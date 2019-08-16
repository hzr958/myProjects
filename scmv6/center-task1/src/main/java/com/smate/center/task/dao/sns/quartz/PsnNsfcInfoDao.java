package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PsnNsfcInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnNsfcInfoDao extends SnsHibernateDao<PsnNsfcInfo, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getPsnNsfcInfo(Long psnId) {
    String hql = "select distinct(lower(trim(disNo1))) from PsnNsfcInfo where psnId =:psnId and disNo1 is not null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
