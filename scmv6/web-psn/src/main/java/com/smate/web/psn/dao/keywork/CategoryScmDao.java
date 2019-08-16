package com.smate.web.psn.dao.keywork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.CategoryScm;

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
   * 根据关键字查找科技领域
   * 
   * @param ids
   * @return
   */
  public List<CategoryScm> findCategoryByName(String searchKey) {
    String hql =
        "from CategoryScm t where  (instr(upper(t.categoryZh),:searchKey)>0 or instr(upper(t.categoryEn),:searchKey)>0)";
    return super.createQuery(hql).setParameter("searchKey", searchKey.toUpperCase().trim()).setMaxResults(5).list();
  }

  /**
   * 根据关键字查找科技领域不需要一级科技领域
   * 
   * @param ids
   * @return
   */
  public List<CategoryScm> findCategoryByNameNotFirst(String searchKey) {
    String hql =
        "from CategoryScm t where  (instr(upper(t.categoryZh),:searchKey)>0 or instr(upper(t.categoryEn),:searchKey)>0) and t.parentCategroyId != 0";
    return super.createQuery(hql).setParameter("searchKey", searchKey.toUpperCase().trim()).setMaxResults(5).list();
  }

  public CategoryScm findCategoryById(Long categoryId) {
    String hql = "from CategoryScm t where categoryId = ?";
    return super.findUnique(hql, categoryId);
  }
}
