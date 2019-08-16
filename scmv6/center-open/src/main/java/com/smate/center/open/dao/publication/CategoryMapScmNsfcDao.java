package com.smate.center.open.dao.publication;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.CategoryMapScmNsfc;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryMapScmNsfcDao extends SnsHibernateDao<CategoryMapScmNsfc, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getScmCategroyNoList() {
    String hql = "select distinct(scmCategoryId) from  CategoryMapScmNsfc order by scmCategoryId asc";

    return super.createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getNsfcCategorybyCategoryId(Long id, Integer size) {
    String hql =
        "select lower(trim(nsfcCategoryId)) from CategoryMapScmNsfc where categoryId =:id and length(nsfcCategoryId)=:size";
    return super.createQuery(hql).setParameter("id", id).setParameter("size", size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getScmCategoryByNsfcCategory(String nsfcCategory) {
    String hql = "select distinct(scmCategoryId) from CategoryMapScmNsfc where nsfcCategoryId =:nsfcCategory";
    return super.createQuery(hql).setParameter("nsfcCategory", nsfcCategory).list();

  }

  public Long getScmIdByDiscCode(String discCode) {
    String hql = "select scmCategoryId from CategoryMapScmNsfc  where nsfcCategoryId :=discCode ";
    return (Long) super.createQuery(hql).setParameter("discCode", discCode).uniqueResult();
  }

}
