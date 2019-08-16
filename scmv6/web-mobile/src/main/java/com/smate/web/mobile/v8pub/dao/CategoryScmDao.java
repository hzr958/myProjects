package com.smate.web.mobile.v8pub.dao;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.mobile.v8pub.sns.po.CategoryScm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryScmDao extends SnsHibernateDao<CategoryScm, Long> {
  /**
   * 根据ID查找科技领域List
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryScm> findCategoryScm(String ids) {
    String hql = "from CategoryScm t where t.categoryId in (" + ids + ") order by instr(:idsStr,t.categoryId)";
    return super.createQuery(hql).setParameter("idsStr", ids).list();
  }

  /**
   * 根据一级学科ID查找对应的二级学科
   */
  public List<Long> getSubCategoryIdList(Long parentCategroyId) {
    String hql = "select categoryId from CategoryScm t where t.parentCategroyId = :parentCategroyId";
    return super.createQuery(hql).setParameter("parentCategroyId", parentCategroyId).list();
  }

  /**
   * 获取全部的一级学科领域
   * 
   * @return
   */
  public List<CategoryScm> findFirstScienceArea() {
    String hql = "from CategoryScm t where t.parentCategroyId=0";
    return super.createQuery(hql).list();
  }

  /**
   * 获取一级学科领域下的二级学科领域
   * 
   * @return
   */
  public List<CategoryScm> findSecondScienceArea(Long parentId) {
    String hql = "from CategoryScm t where t.parentCategroyId=:parentId";
    return super.createQuery(hql).setParameter("parentId", parentId).list();
  }
}
