package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.journal.BaseJournalTempBatch;

@Repository
public class BaseJournalTempBatchDao extends PdwhHibernateDao<BaseJournalTempBatch, Long> {

  /**
   * 
   * @param status 期刊手工处理状态 1=待处理 2=待审核 3=已拒绝 4=已通过
   * @param handleMethod
   * @param batchId
   * @return
   * @throws DaoException
   */
  public int updateStstus(Long status, Long handleMethod, Long batchId) {
    String hql = "update BaseJournalTempBatch set status=? , handleMethod=? where tempBatchId=?";
    return super.createQuery(hql, status, handleMethod, batchId).executeUpdate();
  }

  /**
   * 根据 期刊id查找batchlist
   * 
   * @param jnlId
   * @return
   */
  public List<BaseJournalTempBatch> findByJournalId(Long jnlId) {
    return super.findBy("jnlId", jnlId);
  }

  public void updateJnlId(Long jnlId, Long upJnlId) {
    super.createQuery("update BaseJournalTempBatch set jnlId=? where jnlId=?", upJnlId, jnlId).executeUpdate();
  }

  public int updateStstusForDeleteJou(Long status, Long handleMethod, Long jnlId) throws DaoException {
    String hql = "update BaseJournalTempBatch btb "
        + "set btb.status=? , btb.handleMethod=? where exists(select 1 from BaseJournal b,BaseJournalTitle btt "
        + "where btt.jnlId=b.jnlId and b.jnlId=?)";
    return super.createQuery(hql, status, handleMethod, jnlId).executeUpdate();
  }

}
