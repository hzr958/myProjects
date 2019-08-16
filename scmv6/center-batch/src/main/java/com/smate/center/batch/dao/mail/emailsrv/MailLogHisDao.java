package com.smate.center.batch.dao.mail.emailsrv;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.emailsrv.MailLogHis;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 邮件数据操作dao
 * 
 * @author zk
 * 
 */
@Repository
public class MailLogHisDao extends EmailSrvHibernateDao<MailLogHis, Long> {

  /**
   * 获取指定时间的邮件记录
   * 
   * @param assignMoths
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailLogHis> getMailLogHisBeforeAssignMonths(Date assignMoths, Integer size) throws DaoException {
    String hql = "from MailLogHis m where m.createDate< ? ";
    return super.createQuery(hql, assignMoths).setMaxResults(size).list();
  }
}
