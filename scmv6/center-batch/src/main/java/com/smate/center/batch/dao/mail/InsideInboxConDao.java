package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.InsideInboxCon;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 站内消息收件箱内容表DAO_SCM-5910.
 * 
 * @author mjg
 * 
 */
@Repository
public class InsideInboxConDao extends SnsHibernateDao<InsideInboxCon, Long> {

  /**
   * 保存站内消息收件箱内容记录.
   * 
   * @param insideInboxCon
   */
  public void saveInboxCon(InsideInboxCon insideInboxCon) {
    if (insideInboxCon != null) {
      if (insideInboxCon.getId() != null) {
        String hql =
            "update InsideInboxCon t set t.titleZh=?,t.titleEn=?,t.extOtherInfo=?,t.content=? where t.inboxId=? ";
        super.createQuery(hql, insideInboxCon.getTitleZh(), insideInboxCon.getTitleEn(),
            insideInboxCon.getExtOtherInfo(), insideInboxCon.getContent(), insideInboxCon.getInboxId()).executeUpdate();
      } else {
        super.save(insideInboxCon);
      }
    }
  }

  /**
   * 获取站内消息收件箱内容记录.
   * 
   * @param inboxId
   * @return
   */
  public InsideInboxCon getInsideInboxCon(Long inboxId) {
    String hql = "from InsideInboxCon t where t.inboxId=? and rownum=1 ";
    Object obj = super.createQuery(hql, inboxId).uniqueResult();
    if (obj != null) {
      return (InsideInboxCon) obj;
    }
    return null;
  }
}
