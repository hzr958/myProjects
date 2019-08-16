package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryMapScmIsi;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryMapScmIsiDao extends SnsHibernateDao<CategoryMapScmIsi, Long> {
  @SuppressWarnings("unchecked")
  public List<Long> getScmCategoryIds(String isiCategory) {
    String hql =
        "select distinct(t.scmCategoryId) from CategoryMapScmIsi t where lower(trim(t.wosCategoryEn)) =:isiCategory";
    List<Long> scmCategoryIdList = super.createQuery(hql).setParameter("isiCategory", isiCategory).list();
    return scmCategoryIdList;
  }
}
