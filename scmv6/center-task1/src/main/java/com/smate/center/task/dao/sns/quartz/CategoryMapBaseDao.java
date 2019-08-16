package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryMapBase;
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
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.superCategoryId = 0";
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
   * 根据ID查找科技领域List
   * 
   * @param ids
   * @return
   */
  public List<CategoryMapBase> findCategoryMapBases(String ids) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.categryId in ("
            + ids + ")";
    return super.createQuery(hql).list();
  }

  /**
   * 根据ID查找科技领域List
   * 
   * @param ids
   * @return
   */
  public List<CategoryMapBase> findCategoryMapBases(List<Long> ids) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.categryId in (:ids)";
    return super.createQuery(hql).setParameterList("ids", ids).list();
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

  @SuppressWarnings("rawtypes")
  public List<Long> getSuperDiscIdList(List<Long> discIds) {
    List<Long> resultList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(discIds)) {
      String sql =
          "select distinct t.categry_id from category_map_base t start with t.categry_id in (:discIds) connect by prior t.super_category_id = t.categry_id order by t.categry_id";
      List queryList = super.getSession().createSQLQuery(sql).setParameterList("discIds", discIds).list();
      if (CollectionUtils.isNotEmpty(queryList)) {
        for (int i = 0; i < queryList.size(); i++) {
          Object obj = queryList.get(i);
          resultList.add(((java.math.BigDecimal) obj).longValue());
        }
      }
    }
    return resultList;
  }

  /**
   * 根据ID查询科技领域
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryMapBase> findCategoryByIds(List<Long> ids) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.categryId in (:ids)";
    return super.createQuery(hql).setParameterList("ids", ids).list();
  }
}
