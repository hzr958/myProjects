package com.smate.web.management.dao.journal;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJnlCategoryRank;

@Repository
public class BaseJnlCategoryRankDao extends PdwhHibernateDao<BaseJnlCategoryRank, Long> {
  /**
   * 根据jcId,year获取 期刊分类排名
   * 
   * @param jcId
   * @param year
   * @return
   */
  public BaseJnlCategoryRank getJnlCatRankByJcIdAndYear(Long jcId, Integer year) {
    String hql = "from BaseJnlCategoryRank where jnlCatId=? and year=?";
    return findUnique(hql, jcId, year);
  }

  public void deleteBaseJnlCategoryRankByjnlId(Long jnlId) {
    String hql =
        "delete from BaseJnlCategoryRank where jnlCatId in(select t2.id from BaseJournalCategory t2 where t2.jnlId=?)";
    super.createQuery(hql, jnlId).executeUpdate();
  }
}
