package com.smate.web.management.dao.journal;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalIfTo;

@Repository
public class BaseJournalIfDao extends PdwhHibernateDao<BaseJournalIfTo, Serializable> {
  public BaseJournalIfTo getJifIdByJnlId(Long jnl_id, Long dbId, String year) {
    String sql = "from BaseJournalIfTo where jnlId=? and dbId=? and ifYear=?";
    return findUnique(sql, jnl_id, dbId, year);
  }

  /**
   * 根据jnlId删除影响因子
   * 
   * @param jnlId
   */
  public void deleteByJnlId(Long jnlId) {
    String hql = "delete from BaseJournalIfTo where jnlId=?";
    super.createQuery(hql, jnlId).executeUpdate();
  }
}
