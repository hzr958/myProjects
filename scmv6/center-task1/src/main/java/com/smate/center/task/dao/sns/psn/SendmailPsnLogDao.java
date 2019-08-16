package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.SendmailPsnLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class SendmailPsnLogDao extends SnsHibernateDao<SendmailPsnLog, Long> {

  @SuppressWarnings("unchecked")
  public List<SendmailPsnLog> getNeedSendMailPsnIds(Integer size) {
    String hql = "from SendmailPsnLog t where t.sendStatus=0 order by t.psnId ";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void UpdateMailSendStatus(Long id, Integer result) {
    String hql = "update SendmailPsnLog t set t.sendStatus = :result where t.id = :id ";
    super.createQuery(hql).setParameter("result", result).setParameter("id", id).executeUpdate();
  }


}
