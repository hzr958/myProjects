package com.smate.web.v8pub.dao.journal;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.v8pub.po.journal.BaseJournalTitleTo;

@Repository
public class BaseJournalTitleDAO extends PdwhHibernateDao<BaseJournalTitleTo, Long> {

  public Long searchJournalMatchBaseJnlId(String jname, String issn) {
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    String hql =
        "select jnlId from BaseJournalTitleTo where (titleXxAlias=? or titleEnAlias=? or titleAbbrAlias=?) and pissn=?";
    List list = super.createQuery(hql, nameAlias, nameAlias, nameAlias, issn).setMaxResults(1).list();
    return CollectionUtils.isEmpty(list) ? null : (Long) list.get(0);
  }

}
