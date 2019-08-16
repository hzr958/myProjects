package com.smate.web.group.dao.grp.member;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.member.CategoryMapBase;

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
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.superCategoryId = :superCategoryId order by t.categryId asc";
    return super.createQuery(hql).setParameter("superCategoryId", superCategoryId).list();
  }

  /**
   * 根据ID查找科技领域List
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryMapBase> findCategoryMapBases(String ids) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.categryId in ("
            + ids + ") order by instr(:idsStr,t.categryId)";
    return super.createQuery(hql).setParameter("idsStr", ids).list();
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
}
