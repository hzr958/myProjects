package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryMapIsiNsfc;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryMapIsiNsfcDao extends SnsHibernateDao<CategoryMapIsiNsfc, Long> {
  @SuppressWarnings("unchecked")
  public List<String> getNsfcCategoryIds(String isiCategory) {
    String hql =
        "select distinct(t.nsfcCategoryId) from CategoryMapIsiNsfc t where lower(trim(t.wosCategoryEn)) =:isiCategory";
    List<String> scmCategoryIdList = super.createQuery(hql).setParameter("isiCategory", isiCategory).list();
    return scmCategoryIdList;
  }
}
