package com.smate.center.batch.dao.mail.emailsrv;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.emailsrv.MailInitData;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 
 * 各节点初始数据表dao
 * 
 * @author zk
 * 
 */
@Repository
public class MailInitDataDao extends EmailSrvHibernateDao<MailInitData, Long> {

  /**
   * 获取指定月份前size条记录
   * 
   * @param assignMoths
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailInitData> getMailInitDataBeforeAssignMonths(Date assignMoths, Integer size) throws DaoException {

    String hql = "from MailInitData m where m.createDate<?";
    return super.createQuery(hql, assignMoths).setMaxResults(size).list();
  }

  /**
   * 获取需要重新发送的邮件
   * 
   * @param id
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailInitData> getResendMailInitData(Long id, Integer size) throws DaoException {
    String hql = "from MailInitData m where m.id> ? and m.status=1 order by m.id ";
    return super.createQuery(hql, id).setMaxResults(size).list();
  }
}
