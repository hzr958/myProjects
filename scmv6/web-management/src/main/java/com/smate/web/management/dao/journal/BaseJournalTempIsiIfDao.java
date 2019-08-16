package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.journal.BaseJournalTempIsiIf;

@Repository
public class BaseJournalTempIsiIfDao extends PdwhHibernateDao<BaseJournalTempIsiIf, Long> {
  public BaseJournalTempIsiIf findById(Long tempIsiId) throws DaoException {
    return super.get(tempIsiId);
  }

  public int updateStstus(Long status, Long handleMethod, Long tempIsiIfId) throws DaoException {
    String hql = "update BaseJournalTempIsiIf set status=? , handleMethod=? where tempIsiIfId=?";
    return super.createQuery(hql, status, handleMethod, tempIsiIfId).executeUpdate();
  }

  public List<BaseJournalTempIsiIf> findByJournalId(Long jnlId) throws DaoException {
    return super.findBy("jnlId", jnlId);
  }

  public void updateJnlId(Long jnlId, Long upJnlId) throws DaoException {
    super.createQuery("update BaseJournalTempIsiIf set jnlId=? where jnlId=?", upJnlId, jnlId);
  }

  public int updateStstusForDeleteJou(Long status, Long handleMethod, Long jnlId) throws DaoException {
    String hql = "update BaseJournalTempIsiIf btb "
        + "set btb.status=? , btb.handleMethod=? where exists(select 1 from BaseJournal b,BaseJournalTitle btt "
        + "where  btt.jnlId=b.jnlId and b.jnlId=?)";
    return super.createQuery(hql, status, handleMethod, jnlId).executeUpdate();
  }
}
