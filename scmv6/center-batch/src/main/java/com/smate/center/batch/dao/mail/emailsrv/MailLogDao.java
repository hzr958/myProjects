package com.smate.center.batch.dao.mail.emailsrv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.emailsrv.MailLog;
import com.smate.center.batch.model.mail.emailsrv.MailPromoteStatus;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 
 * 邮件model
 * 
 * @author zk
 * 
 */
@Repository
public class MailLogDao extends EmailSrvHibernateDao<MailLog, Long> {

  /**
   * 获取mailId
   * 
   * @return
   */
  public Long getMailId() {

    String sql = "select seq_mail_log.nextval from dual";
    BigDecimal mailId = (BigDecimal) super.getSession().createSQLQuery(sql).uniqueResult();
    return mailId.longValue();
  }

  /**
   * 保存
   * 
   * @param mailLog
   */
  public void svaeMailLog(MailLog mailLog) {
    super.save(mailLog);
  }

  /**
   * 获取未发送邮件记录
   * 
   * @param size
   * @param status
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailLog> getMailLogNotSend(Integer size, Long startMailId) throws DaoException {

    String hql = "from MailLog m where m.id > :startMailId and m.status in(0,6) order by m.priorCode desc,m.id asc";
    return super.createQuery(hql).setParameter("startMailId", startMailId).setMaxResults(size).list();
  }

  /**
   * 通过邮件模板获取未发送的推广邮件
   * 
   * @param tempCode
   * @return
   * @throws DaoException
   */
  public Integer getNotSendPromoteMailCount(Integer tempCode) throws DaoException {
    String hql = "from MailLog m where m.status in(5,6) and m.templateCode= ?";
    return (int) super.countHqlResult(hql, tempCode);
  }

  /**
   * 通过邮件模板获取未发送的推广邮件
   * 
   * @param tempCode
   * @return
   * @throws DaoException
   */
  public Integer getNotSendPromoteMailCount2(MailPromoteStatus mailStatus) throws DaoException {
    String hql = "from MailLog m where m.status in(5,6) and m.templateCode in (?,?)";
    return (int) super.countHqlResult(hql, mailStatus.getTempIdZh(), mailStatus.getTempIdEn());
  }

  /**
   * 
   * 通过邮件状态和邮件模板获取推广邮件记录
   * 
   * @param status
   * @param tempCode
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailLog> getPromoteMailByTempAndStatus(Integer status, Integer tempCode, Long startMailId, Integer size)
      throws DaoException {
    String hql = "from MailLog m where m.status = ? and m.templateCode = ? and m.id > ? order by m.id ";
    return super.createQuery(hql, status, tempCode, startMailId).setMaxResults(size).list();
  }

  /**
   * 
   * 通过邮件状态和邮件模板获取推广邮件记录
   * 
   * @param status
   * @param tempCode
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailLog> getPromoteMailByTempAndStatus2(Integer status, MailPromoteStatus mailStatus, Long startMailId,
      Integer size) throws DaoException {

    String hql = "from MailLog m where m.status = ? and m.templateCode in(?,?) and m.id > ? order by m.id ";
    return super.createQuery(hql, status, mailStatus.getTempIdZh() == null ? 0 : mailStatus.getTempIdZh(),
        mailStatus.getTempIdEn() == null ? 0 : mailStatus.getTempIdEn(), startMailId).setMaxResults(size).list();
  }

  /**
   * 获取最后三条未发送推广邮件
   * 
   * @param tempId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getlastThreeByTempId(MailPromoteStatus mailStatus) throws DaoException {
    List<Integer> tempIds = new ArrayList<Integer>();
    if (mailStatus.getTempIdEn() != null) {
      tempIds.add(mailStatus.getTempIdEn());
    }
    if (mailStatus.getTempIdZh() != null) {
      tempIds.add(mailStatus.getTempIdZh());
    }
    if (CollectionUtils.isEmpty(tempIds)) {
      return null;
    }
    String hql = "select m.id from MailLog m where  m.templateCode in (:tempIds)  order by m.id desc";
    return super.createQuery(hql).setParameterList("tempIds", tempIds).setMaxResults(3).list();
  }

  /**
   * 统计tempId在auditDate后的记录数
   * 
   * @param tempIdList
   * @param auditDate
   * @return
   * @throws DaoException
   */
  public Long countMailNumAfterDate(List<Integer> tempIdList, Date auditDate) throws DaoException {

    String hql = "select count(m.id) from MailLog m where m.templateCode in (:tempIds) and m.createDate>= :date";
    return (Long) super.createQuery(hql).setParameterList("tempIds", tempIdList).setParameter("date", auditDate)
        .uniqueResult();
  }

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
}
