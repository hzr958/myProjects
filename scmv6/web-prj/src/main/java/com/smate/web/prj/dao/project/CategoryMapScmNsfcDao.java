package com.smate.web.prj.dao.project;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.CategoryMapScmNsfc;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryMapScmNsfcDao extends SnsHibernateDao<CategoryMapScmNsfc, Long> {

  public CategoryMapScmNsfc findScmNsfc(String nsfcCategoryId) {
    String hql = "select t from CategoryMapScmNsfc t where t.nsfcCategoryId =:nsfcCategoryId ";
    List<CategoryMapScmNsfc> list = super.createQuery(hql).setParameter("nsfcCategoryId", nsfcCategoryId).list();
    if(CollectionUtils.isNotEmpty(list)){
      return  list.get(0);
    }
    return null;
  }

}
