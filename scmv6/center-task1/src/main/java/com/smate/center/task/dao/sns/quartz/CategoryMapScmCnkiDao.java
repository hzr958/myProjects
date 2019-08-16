package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryMapScmCnki;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryMapScmCnkiDao extends SnsHibernateDao<CategoryMapScmCnki, Long> {
  @SuppressWarnings("unchecked")
  public List<Long> getScmCategoryIds(String cnkiCategoryZh) {
    String hql =
        "select distinct(t.scmCategoryId) from CategoryMapScmCnki t where lower(trim(t.cnkiCategoryZh)) =:cnkiCategoryZh";
    List<Long> scmCategoryIdList = super.createQuery(hql).setParameter("cnkiCategoryZh", cnkiCategoryZh).list();
    return scmCategoryIdList;
  }
}
