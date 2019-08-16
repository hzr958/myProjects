package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailBlacklist;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 黑名单dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailBlacklistDao extends SnsHibernateDao<MailBlacklist, Long> {

  public MailBlacklist getByEmail(String email) {
    return (MailBlacklist) super.createQuery("From MailBlacklist t where t.email=:email and t.status=0")
        .setParameter("email", email).uniqueResult();
  }

  /**
   * 域名 类 黑名单
   * 
   * @param host
   * @return
   */
  public MailBlacklist getByEmailHost(String host) {
    return (MailBlacklist) super.createQuery("From MailBlacklist t where t.email=:host and t.status=0 and t.type=1")
        .setParameter("host", host).uniqueResult();
  }
}
