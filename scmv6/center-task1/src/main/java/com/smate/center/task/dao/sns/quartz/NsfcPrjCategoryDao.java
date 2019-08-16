package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.NsfcPrjCategory;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class NsfcPrjCategoryDao extends SnsHibernateDao<NsfcPrjCategory, Long> {

  public void deleteRsByPrjNo(String prjNo) {
    String hql = "delete NsfcPrjCategory where nsfcPrjNo =:nsfcPrjNo";
    super.createQuery(hql).setParameter("nsfcPrjNo", prjNo).executeUpdate();

  }

  public Long getRsCountsByPrjNo(String prjNo) {
    String hql = "select count(1) from NsfcPrjCategory where nsfcPrjNo =:nsfcPrjNo";
    return (Long) super.createQuery(hql).setParameter("nsfcPrjNo", prjNo).uniqueResult();
  }
}
