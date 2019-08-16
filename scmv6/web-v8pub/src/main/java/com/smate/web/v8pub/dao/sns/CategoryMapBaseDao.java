package com.smate.web.v8pub.dao.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.CategoryMapBase;

@Repository
public class CategoryMapBaseDao extends SnsHibernateDao<CategoryMapBase, Integer> {
  public CategoryMapBase getCategoryMapBase(Integer areaId) {
    String hql = "from CategoryMapBase t where t.categryId=:areaId";
    return (CategoryMapBase) super.createQuery(hql).setParameter("areaId", areaId).uniqueResult();
  }
}
