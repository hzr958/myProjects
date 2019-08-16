package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalTitle;

@Repository
public class BaseJnlTitleDao extends PdwhHibernateDao<BaseJournalTitle, Long> {
  @SuppressWarnings("unchecked")
  public List<BaseJournalTitle> findBaseJournalTitle(Long jnlId) {
    String hql = "from BaseJournalTitle where journal.jouId=?";
    return super.createQuery(hql, jnlId).list();
  }
}
