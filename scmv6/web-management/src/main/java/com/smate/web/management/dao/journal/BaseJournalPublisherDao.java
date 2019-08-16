package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseJournalPublisher;

@Repository
public class BaseJournalPublisherDao extends PdwhHibernateDao<BaseJournalPublisher, Long> {

  public BaseJournalPublisher getJouPubliser(Long dbId, Long jnlId) {
    String hql = "from BaseJournalPublisher t where t.dbId = :dbId and t.jnlId = :jnlId";
    return (BaseJournalPublisher) super.createQuery(hql).setParameter("dbId", dbId).setParameter("jnlId", jnlId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<BaseJournalPublisher> findByJnlId(Long jnlId) {
    String hql = "from BaseJournalPublisher t where  t.jnlId = :jnlId";
    return super.createQuery(hql).setParameter("jnlId", jnlId).list();
  }

  /**
   * 根据jnlId删除
   * 
   * @param jnlId
   */
  public void deleteByJnlId(Long jnlId) {
    String hql = "delete from BaseJournalPublisher t where t.jnlId=?";
    super.createQuery(hql, jnlId).executeUpdate();
  }

}
