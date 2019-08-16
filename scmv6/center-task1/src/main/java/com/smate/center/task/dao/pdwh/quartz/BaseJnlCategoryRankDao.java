package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.BaseJnlCategoryRank;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class BaseJnlCategoryRankDao extends PdwhHibernateDao<BaseJnlCategoryRank, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getRanksByCategoryIdList(List<Long> categoryIds) {

    String hql = "select distinct(rank) from BaseJnlCategoryRank where jnlCatId in (:categoryIds) order by rank asc";
    return super.createQuery(hql).setParameterList("categoryIds", categoryIds).list();
  }

}
