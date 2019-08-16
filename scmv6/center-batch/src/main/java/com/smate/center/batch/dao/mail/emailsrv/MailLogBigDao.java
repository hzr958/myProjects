package com.smate.center.batch.dao.mail.emailsrv;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.emailsrv.MailLogBig;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 邮件日志大字段DAO.
 * 
 * @author zk
 * 
 */
@Repository
public class MailLogBigDao extends EmailSrvHibernateDao<MailLogBig, Long> {

  /**
   * 查询邮件上下位参数出错
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  public String findMailContext(Long mailId) throws DaoException {
    String hql = "select t.context from MailLogBig t where t.mailId = ?";
    return (String) super.createQuery(hql, mailId).uniqueResult();
  }

  /**
   * 通过mailid获取邮件数据
   * 
   * @param mailIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<String> findMailContextByMailIds(List<Long> mailIds) throws DaoException {
    String hql = "select t.context from MailLogBig t where t.mailId in (:mailIds) ";
    return super.createQuery(hql).setParameterList("mailIds", mailIds).list();
  }

  /**
   * * 通过mailid获取邮件数据
   * 
   * @param mailIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailLogBig> getMailLogBigByMailIds(List<Long> mailIds) throws DaoException {
    String hql = "from MailLogBig m where m.mailId in (:mailIds)";
    return super.createQuery(hql).setParameterList("mailIds", mailIds).list();
  }

  /**
   * * 通过mailid获取邮件数据
   * 
   * @param mailIds
   * @return
   * @throws DaoException
   */
  public MailLogBig getMailLogBigByMailId(Long mailId) throws DaoException {
    String hql = "from MailLogBig m where m.mailId = mailId";
    return (MailLogBig) super.createQuery(hql, mailId).uniqueResult();
  }
}
