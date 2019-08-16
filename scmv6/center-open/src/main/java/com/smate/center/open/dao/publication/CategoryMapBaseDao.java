package com.smate.center.open.dao.publication;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.CategoryMapBase;
import com.smate.core.base.utils.data.SnsHibernateDao;

/***
 * 科技领域分类DAO
 *
 * @author wsn
 * @createTime 2017年3月21日 下午7:47:48
 *
 */
@Repository
public class CategoryMapBaseDao extends SnsHibernateDao<CategoryMapBase, Integer> {

  /**
   * 查找科技领域一级分类
   * 
   * @return
   */
  public List<CategoryMapBase> getCategoryMapBaseFirstLevelList() {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.superCategoryId = 0 order by t.categryId";
    return super.createQuery(hql).list();
  }

  /**
   * 根据一级学科ID查找对应的二级学科
   */
  public List<CategoryMapBase> getSubCategoryMapBaseList(Integer superCategoryId) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.superCategoryId = :superCategoryId";
    return super.createQuery(hql).setParameter("superCategoryId", superCategoryId).list();
  }

  /**
   * 查找所有科技领域
   * 
   * @return
   */
  public List<CategoryMapBase> getAllCategoryMapBaseList() {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t ";
    return super.createQuery(hql).list();
  }

  /**
   * 查找所有科技领域
   * 
   * @return
   */
  public Integer getSuperCategoryBySubCategory(int subCategory) {
    String hql = "select t.superCategoryId from CategoryMapBase t  where t.categryId=:subCategory";
    Object obj = super.createQuery(hql).setParameter("subCategory", subCategory).uniqueResult();
    if (obj != null) {
      return (int) obj;
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getAreaIds(List<String> psnScienceAreas) {
    String hql =
        "select t.categryId from CategoryMapBase t where t.categoryZh in (:psnScienceAreas) or t.categoryEn in (:psnScienceAreas)";
    return super.createQuery(hql).setParameterList("psnScienceAreas", psnScienceAreas).list();
  }

  /**
   * 根据ID查找科技领域List
   * 
   * @param discodes
   * @return
   */
  public List<CategoryMapBase> findCategoryMapBases(List<Integer> discodes) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.categryId in (:ids)";
    return super.createQuery(hql).setParameterList("ids", discodes).list();
  }

}
