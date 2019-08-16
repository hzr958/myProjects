package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.InsideMailBoxCon;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 站内消息发件箱内容表DAO.
 * 
 * @author mjg
 * 
 */
@Repository
public class InsideMailBoxConDao extends SnsHibernateDao<InsideMailBoxCon, Long> {

  /**
   * 保存站内消息发件箱内容记录_MJG_SCM-5910.
   * 
   * @param mailboxCon
   */
  public void saveMailBoxCon(InsideMailBoxCon mailboxCon) {
    if (mailboxCon != null) {
      if (mailboxCon.getId() != null) {
        String hql =
            "update InsideMailBoxCon t set t.titleZh=?,t.titleEn=?,t.extOtherInfo=?,t.content=? where t.mailId=? ";
        super.createQuery(hql, mailboxCon.getTitleZh(), mailboxCon.getTitleEn(), mailboxCon.getExtOtherInfo(),
            mailboxCon.getContent(), mailboxCon.getMailId()).executeUpdate();
      } else {
        super.save(mailboxCon);
      }
    }
  }

  /**
   * 获取站内消息发件箱记录.
   * 
   * @param mailId
   * @return
   */
  public InsideMailBoxCon getMailBoxCon(Long mailId) {
    String hql = "from InsideMailBoxCon t where t.mailId=? and rownum=1 ";
    Object obj = super.createQuery(hql, mailId).uniqueResult();
    if (obj != null) {
      return (InsideMailBoxCon) obj;
    }
    return null;
  }
}
