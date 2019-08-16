package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwEpt;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员关键词表dao
 * 
 * @author zjh
 *
 */
@Repository
public class PsnKwEptDao extends SnsHibernateDao<PsnKwEpt, Long> {

  public void deleteBypsnId(Long psnId) {
    String hql = "delete from PsnKwEpt t where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
