package com.smate.web.psn.dao.keywork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.CategoryMapScmNsfc;

@Repository
public class CategoryMapScmNsfcDao extends SnsHibernateDao<CategoryMapScmNsfc, Long> {
  /**
   * 根据科技领域Ids获取NSFC对应关系
   * 
   * @param ids
   * @return
   */
  public List<CategoryMapScmNsfc> findNsfcCategoryIds(List<Long> ids) {
    String hql = "select t from CategoryMapScmNsfc t where t.scmCategoryId in (:ids)";
    List<CategoryMapScmNsfc> list = super.createQuery(hql).setParameterList("ids", ids).list();
    return list;
  }
}
