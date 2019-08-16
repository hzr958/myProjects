package com.smate.sie.web.application.dao.validate;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.web.application.model.validate.MailNotificationList;

/**
 * 邮件通知表DAO
 * 
 * @author xr 2019.4.25
 */
@Repository
public class MailNotificationListDao extends SieHibernateDao<MailNotificationList, Long> {
  /**
   * 通过mailKey找到mailValues
   *
   * @param mailKey
   * @return mailValues
   */
  public String getMailValuesByMailKey(String mailKey) {
    String hql = "select mailValues from MailNotificationList where mailKey =:mailKey";
    return (String) super.createQuery(hql).setParameter("mailKey", mailKey).uniqueResult();
  }

  /**
   * 通过mailKey获取MailNotificationList对象
   * 
   * @param audit_NOTIFICATE_SEARCH
   * @return
   */
  public MailNotificationList getByMailKey(String mailKey) {
    String hql = "from MailNotificationList t where t.mailKey= ?";
    return this.findUnique(hql, mailKey);
  }
}
