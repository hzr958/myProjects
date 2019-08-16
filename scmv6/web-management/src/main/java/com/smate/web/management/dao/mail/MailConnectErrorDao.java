package com.smate.web.management.dao.mail;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.mail.MailConnectError;

/**
 * 链接收件箱错误记录dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailConnectErrorDao extends SnsHibernateDao<MailConnectError, Long> {

  public boolean isExist(String account) {
    String hql = "select count(1) from MailConnectError t where t.account=:account";
    Long count = (Long) this.createQuery(hql).setParameter("account", account).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }

  public List<MailConnectError> findList() {
    String hql = "from MailConnectError t";
    return this.createQuery(hql).list();
  }

}
