package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.journal.Journal;

@Repository
public class JournalDao extends PdwhHibernateDao<Journal, Long> {
  public void setMatchJnlId(Long jid, Long matchJid) {
    String hql = "update Journal set matchBaseJnlId=? where id=?";
    super.createQuery(hql, matchJid, jid).executeUpdate();
  }

  /**
   * 同步更新journal刷新表journal_syncjnl_flag
   * 
   * @param jid
   */
  public void syncJournalFlag(Long jid) {
    String sql = "insert into journal_syncjnl_flag values(?,?,?,?)";
    super.update(sql, new Object[] {jid, 0, 0, 1});
  }

  public void updateMatchJnlId(Long baseJournalId, Long newbjid) {
    String hql = "update Journal set matchBaseJnlId=null where matchBaseJnlId=?";
    super.createQuery(hql, baseJournalId).executeUpdate();
  }

  /**
   * 通过jnlId查询出与之对应的jid.
   * 
   * @param jnlId
   * @return
   * @throws DaoServiceException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findJidByJnlId(Long jnlId) throws DaoException {
    return super.createQuery("select t.id from Journal t where t.matchBaseJnlId = ? order by t.id", jnlId)
        .setMaxResults(500).list();
  }

}
