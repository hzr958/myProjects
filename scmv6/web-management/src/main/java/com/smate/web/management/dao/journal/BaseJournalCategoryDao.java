package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalCategory;

@Repository
public class BaseJournalCategoryDao extends PdwhHibernateDao<BaseJournalCategory, Long> {

  public BaseJournalCategory getJouCategory(Long categoryId, Long dbId, Long jnlId) {
    String hql = "from BaseJournalCategory t where t.categoryId = :categoryId and t.dbId = :dbId and t.jnlId = :jnlId";
    return (BaseJournalCategory) super.createQuery(hql).setParameter("categoryId", categoryId)
        .setParameter("dbId", dbId).setParameter("jnlId", jnlId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<BaseJournalCategory> getJnlCatList(Long jnlId) {
    String hql = "from BaseJournalCategory t where t.jnlId = :jnlId";
    return super.createQuery(hql).setParameter("jnlId", jnlId).list();
  }

  /**
   * 根据jnlId删除
   * 
   * @param jnlId
   */
  public void deleteByJnlId(Long jnlId) {
    String hql = "delete from BaseJournalCategory t where t.jnlId=?";
    super.createQuery(hql, jnlId).executeUpdate();
  }

}
