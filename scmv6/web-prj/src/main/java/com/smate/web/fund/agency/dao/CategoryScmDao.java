package com.smate.web.fund.agency.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.model.common.CategoryScm;

@Repository
public class CategoryScmDao extends SnsHibernateDao<CategoryScm, Long> {
  /**
   * 根据ID查找科技领域List
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryScm> findCategoryScm(List<Long> ids) {
    String hql = "from CategoryScm t where t.categoryId in (:ids)";
    return super.createQuery(hql).setParameterList("ids", ids).list();
  }

  /**
   * 查找一级学科领域
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryScm> findFirstCategoryScm() {
    String hql = "from CategoryScm t where t.parentCategroyId=0";
    return super.createQuery(hql).list();
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
   * 根据关键字查找科技领域
   * 
   * @param ids
   * @return
   */
  public List<CategoryScm> appFindCategoryByName(String searchKey) {
    String hql =
        "from CategoryScm t where  (instr(upper(t.categoryZh),:searchKey)>0 or instr(upper(t.categoryEn),:searchKey)>0)";
    return super.createQuery(hql).setParameter("searchKey", searchKey.toUpperCase().trim()).list();
  }

  public List<CategoryScm> appFindAllCategory() {
    String hql = "from CategoryScm t ";
    return super.createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getAreaNameByAreaIds(String locale, List<Long> areaIds) {
    String hql = "";
    if ("zh_CN".equals(locale)) {
      hql = "select categoryZh from CategoryScm where categoryId in(:areaIds)";
    } else {
      hql = "select categoryEn from CategoryScm where categoryId in(:areaIds)";
    }
    return super.createQuery(hql).setParameterList("areaIds", areaIds).list();
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
