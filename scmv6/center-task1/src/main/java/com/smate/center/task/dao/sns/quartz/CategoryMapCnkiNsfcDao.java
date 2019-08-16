package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryMapCnkiNsfc;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryMapCnkiNsfcDao extends SnsHibernateDao<CategoryMapCnkiNsfc, Long> {
  @SuppressWarnings("unchecked")
  public List<String> getNsfcCategoryIds(String cnkiCategory) {
    String hql =
        "select distinct(t.nsfcCategoryId) from CategoryMapCnkiNsfc t where lower(trim(t.cnkiCategoryZh)) =:cnkiCategory";
    List<String> scmCategoryIdList = super.createQuery(hql).setParameter("cnkiCategory", cnkiCategory).list();
    return scmCategoryIdList;
  }
}
