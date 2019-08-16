package com.smate.center.open.dao.pdwh.jnl;

import com.smate.center.open.model.pdwh.jnl.BaseJournalTitleTo;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.JnlFormateUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 */
@Repository
public class BaseJournalTitleDao extends PdwhHibernateDao<BaseJournalTitleTo, Long> {

  // 导入录入新增期刊时匹配 根据fzq要求，sns期刊与基础期刊匹配时，修改为标题or，加issn号 2013/03/01
  public List<BaseJournalTitleTo> snsJnlMatchBaseJnlId(String jname, String issn) {
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    String hql = "from BaseJournalTitleTo where (lower(titleXxAlias)=? or lower(titleEnAlias)=? or lower(titleAbbrAlias)=?) and pissn=?";
    return find(hql, nameAlias, nameAlias, nameAlias, issn);
  }

  public List<BaseJournalTitleTo> findByJnlId(Long jnlId) {
    String hql = "from BaseJournalTitleTo where  jnlId=?";
    return find(hql, jnlId);
  }

}
