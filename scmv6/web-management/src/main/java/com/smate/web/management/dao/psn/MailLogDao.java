package com.smate.web.management.dao.psn;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.MailDispatchHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.psn.MailLog;


/**
 * 
 * 邮件model
 * 
 * @author zk
 * 
 */
@Repository
public class MailLogDao extends MailDispatchHibernateDao<MailLog, Long> {


  /**
   * 获取指定时间的邮件记录
   * 
   * @param assignMoths
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailLog> getMailLogBeforeAssignMonths(Date assignMoths, Integer size) throws DaoException {
    String hql = "from MailLog m where m.createDate< ? ";
    return super.createQuery(hql, assignMoths).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<MailLog> getPsnEmailListInfo(String psnEmail, Long psnId, Integer typeValue) {
    String hql =
        "select new MailLog(t.id,t.toAddr,t.template,t.lastSend,t.status) from MailLog t where t.toAddr = :psnEmail and t.psnId = :psnId and t.lastSend > trunc(sysdate- :typeValue) order by t.lastSend desc";
    return super.createQuery(hql).setParameter("psnEmail", psnEmail).setParameter("psnId", psnId)
        .setParameter("typeValue", typeValue).list();
  }

  public Long getPsnEmailList(String psnEmail, Long psnId, Integer typeValue) {
    String hql =
        "select count(t.id) from MailLog t where t.toAddr = :psnEmail and t.psnId= :psnId and t.lastSend > trunc(sysdate - :typeValue)";
    return (Long) super.createQuery(hql).setParameter("psnEmail", psnEmail).setParameter("psnId", psnId)
        .setParameter("typeValue", typeValue).uniqueResult();
  }
}
