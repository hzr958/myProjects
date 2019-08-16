package com.smate.center.task.dao.sns.quartz;

import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnSid;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * PsnSid DAO.
 * 
 * @author hd
 * 
 */
@Repository
public class PsnSidDao extends SnsHibernateDao<PsnSid, Long> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public Long findSidByPsnId(Long psnId) {
    String hql = "select t.sid from PsnSid t where t.psnId = ?";
    return (Long) super.createQuery(hql, psnId).uniqueResult();
  }

  public Long getSidSequence() {
    String sql = "select seq_psn_sid.nextval sid from dual";
    return (Long) super.getSession().createSQLQuery(sql).addScalar("sid", new LongType()).uniqueResult();
  }
}
