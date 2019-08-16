package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryScm;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryScmDao extends SnsHibernateDao<CategoryScm, Long> {

  public Long getSuperCatId(Long catId) {
    String hql = "select t.parentCategroyId from CategoryScm t where t.categoryId = :catId";
    return (Long) super.createQuery(hql).setParameter("catId", catId).uniqueResult();
  }


}
