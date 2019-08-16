package com.smate.web.v8pub.dao.journal;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.journal.BaseJnlCategoryRank;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseJnlCategoryRankDao extends PdwhHibernateDao<BaseJnlCategoryRank, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getRanksByCategoryIdList(List<Long> categoryIds) {

    String hql = "select distinct(rank) from BaseJnlCategoryRank where jnlCatId in (:categoryIds) order by rank asc";
    return super.createQuery(hql).setParameterList("categoryIds", categoryIds).list();
  }

}
