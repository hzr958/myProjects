package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.ShareMailBoxCon;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 推荐发件箱内容表DAO.
 * 
 * @author mjg
 * 
 */
@Repository
public class ShareMailBoxConDao extends SnsHibernateDao<ShareMailBoxCon, Long> {

  /**
   * 保存推荐发件记录内容_MJG_SCM-5910.
   * 
   * @param mailboxCon
   */
  public void saveMailBoxCon(ShareMailBoxCon mailboxCon) {
    if (mailboxCon != null) {
      if (mailboxCon.getId() != null) {
        String hql =
            "update ShareMailBoxCon t set t.titleZh=?,t.titleEn=?,t.extOtherInfo=?,t.content=? where t.mailId=? ";
        super.createQuery(hql, mailboxCon.getTitleZh(), mailboxCon.getTitleEn(), mailboxCon.getExtOtherInfo(),
            mailboxCon.getContent(), mailboxCon.getMailId()).executeUpdate();
      } else {
        super.save(mailboxCon);
      }
    }
  }

  /**
   * 获取推荐发件记录内容.
   * 
   * @param mailId
   * @return
   */
  public ShareMailBoxCon getMailBoxCon(Long mailId) {
    String hql = "from ShareMailBoxCon t where t.mailId=? and rownum=1 ";
    Object obj = super.createQuery(hql, mailId).uniqueResult();
    if (obj != null) {
      return (ShareMailBoxCon) obj;
    }
    return null;
  }

}
