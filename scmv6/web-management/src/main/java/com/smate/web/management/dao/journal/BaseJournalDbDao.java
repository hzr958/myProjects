package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalDb;

@Repository
public class BaseJournalDbDao extends PdwhHibernateDao<BaseJournalDb, Long> {

  @SuppressWarnings("unchecked")
  public List<BaseJournalDb> fundByJnlId(Long jnlId) {
    String hql = "from BaseJournalDb t where t.jnlId = :jnlId";
    return super.createQuery(hql).setParameter("jnlId", jnlId).list();
  }

  /**
   * 根据jnlId删除所有对应的表中的数据
   * 
   * @param jnlId
   */
  public void deleteByJnlId(Long jnlId) {
    String hql = "delete from BaseJournalDb t where t.jnlId=:jnlId";
    super.createQuery(hql).setParameter("jnlId", jnlId).executeUpdate();
  }

}
