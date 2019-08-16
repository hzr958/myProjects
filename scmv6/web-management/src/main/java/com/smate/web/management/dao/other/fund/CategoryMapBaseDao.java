package com.smate.web.management.dao.other.fund;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.web.management.model.other.fund.CategoryMapBase;

@Repository
public class CategoryMapBaseDao extends HibernateDao<CategoryMapBase, Long> {

  @SuppressWarnings("unchecked")
  public List<CategoryMapBase> getCategoryMapBaseList(String discCode) {
    if (StringUtils.isBlank(discCode)) {

      return super.createQuery(
          "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.superCategoryId = 0 order by t.categryId")
              .list();

    } else {

      return super.createQuery(
          "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t where t.superCategoryId = ? order by t.categryId ",
          Integer.parseInt(discCode)).list();
    }
  }

  public Long getDisSuperId(String str) {
    Integer superCategoryId =
        (Integer) super.createQuery("select t.superCategoryId from CategoryMapBase t where t.categryId =? ",
            Integer.parseInt(str)).uniqueResult();
    return Long.valueOf(superCategoryId);
  }

  public CategoryMapBase getCategoryDisByDisId(Long disId) {
    String hql =
        "select new CategoryMapBase(categryId, categoryZh, categoryEn, superCategoryId) from CategoryMapBase t  where t.categryId = :disId";
    return (CategoryMapBase) super.createQuery(hql).setParameter("disId", Integer.parseInt(disId.toString()))
        .uniqueResult();
  }

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.RCMD;
  }

}
