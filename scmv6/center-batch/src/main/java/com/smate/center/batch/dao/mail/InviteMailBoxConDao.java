package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.InviteMailBoxCon;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 邀请发件箱内容表DAO.
 * 
 * @author mjg
 * 
 */
@Repository
@Deprecated
public class InviteMailBoxConDao extends SnsHibernateDao<InviteMailBoxCon, Long> {

  /**
   * 保存邀请发件箱内容记录.
   * 
   * @param inviteMailBoxCon
   */
  public void saveMailBoxCon(InviteMailBoxCon inviteMailBoxCon) {
    if (inviteMailBoxCon != null) {
      if (inviteMailBoxCon.getId() != null) {
        String hql =
            "update InviteMailBoxCon t set t.titleZh=?,t.titleEn=?,t.extOtherInfo=?,t.content=? where t.mailId=? ";
        super.createQuery(hql, inviteMailBoxCon.getTitleZh(), inviteMailBoxCon.getTitleEn(),
            inviteMailBoxCon.getExtOtherInfo(), inviteMailBoxCon.getContent(), inviteMailBoxCon.getMailId())
                .executeUpdate();
      } else {
        super.save(inviteMailBoxCon);
      }
    }
  }

  /**
   * 获取邀请发件箱内容记录.
   * 
   * @param mailId
   * @return
   */
  public InviteMailBoxCon getMailBoxCon(Long mailId) {
    String hql = "from InviteMailBoxCon t where t.mailId=? and rownum=1 ";
    Object obj = super.createQuery(hql, mailId).uniqueResult();
    if (obj != null) {
      return (InviteMailBoxCon) obj;
    }
    return null;
  }

}
