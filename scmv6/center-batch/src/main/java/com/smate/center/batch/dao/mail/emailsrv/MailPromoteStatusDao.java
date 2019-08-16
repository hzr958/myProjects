package com.smate.center.batch.dao.mail.emailsrv;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.emailsrv.MailPromoteStatus;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 
 * 推广邮件状态表dao
 * 
 * @author zk
 * 
 */
@Repository
public class MailPromoteStatusDao extends EmailSrvHibernateDao<MailPromoteStatus, Long> {

  /**
   * 通过模板id获取记录
   * 
   * @param tempCode
   * @return
   * @throws DaoException
   */
  public MailPromoteStatus getMailStatusByTempCode(Integer tempCode) throws DaoException {
    String hql = "from MailPromoteStatus m where m.tempIdZh = ? or m.tempIdEn = ? ";
    return (MailPromoteStatus) super.createQuery(hql, tempCode, tempCode).setMaxResults(1).uniqueResult();
  }

  /**
   * 获取需要生成邮件的模板
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailPromoteStatus> getStartGenerateMailLog() throws DaoException {
    String hql =
        "select new MailPromoteStatus(m.id,m.startStatus,m.sendStatus,m.tempIdZh,m.tempIdEn,m.updateDate) from MailPromoteStatus m ,ConstEmailInterval c where c.etempCode in (m.tempIdZh,m.tempIdEn) and c.status=1 ";
    return super.createQuery(hql).list();
  }

  /**
   * 获取需要发送邮件的模板
   */
  @SuppressWarnings("unchecked")
  public List<MailPromoteStatus> getNeedSendMailLog() throws DaoException {
    String hql = "from MailPromoteStatus m where m.sendStatus = 0 ";
    return super.createQuery(hql).list();
  }

  /**
   * 
   * 更新生成邮件数据状态
   * 
   * @param tempId
   * @param status
   * @throws DaoException
   */
  public void updateMailPromoteStartStatus(Integer tempId, Integer status) throws DaoException {
    String hql = "update MailPromoteStatus m set m.startStatus = ? where m.tempIdZh = ? or m.tempIdEn = ? ";
    super.createQuery(hql, status, tempId, tempId).executeUpdate();
  }

  /**
   * 更新可发送邮件数据状态
   */
  public void updateMailPromoteSendStatus(Integer tempId, Integer status) throws DaoException {
    String hql = "update MailPromoteStatus m set m.sendStatus = ? where m.tempIdZh = ? or m.tempIdEn = ? ";
    super.createQuery(hql, status, tempId, tempId).executeUpdate();
  }
}
