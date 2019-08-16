package com.smate.web.management.dao.journal;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalByTitle;

@Repository
public class BaseJournalByTitleDao extends PdwhHibernateDao<BaseJournalByTitle, Long> {

}
