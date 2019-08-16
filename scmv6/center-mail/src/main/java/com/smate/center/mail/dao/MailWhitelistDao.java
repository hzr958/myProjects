package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailWhitelist;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 白名单dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailWhitelistDao extends SnsHibernateDao<MailWhitelist, Long> {

  public MailWhitelist getByEmail(String email) {
    return (MailWhitelist) super.createQuery("From MailWhitelist t where t.email=:email and t.status=0")
        .setParameter("email", email).uniqueResult();
  }

}
