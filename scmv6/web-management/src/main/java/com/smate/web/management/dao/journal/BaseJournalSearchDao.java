package com.smate.web.management.dao.journal;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalSearch;

@Repository
public class BaseJournalSearchDao extends PdwhHibernateDao<BaseJournalSearch, Long> {

  public void deleteBaseJournalSearch(Long id) {
    String hql = "delete from BaseJournalSearch t where t.jnlId=?";
    super.createQuery(hql, id).executeUpdate();
  }

}
