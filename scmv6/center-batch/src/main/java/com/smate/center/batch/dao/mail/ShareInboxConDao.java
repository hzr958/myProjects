package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.ShareInboxCon;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 推荐收件箱内容表DAO_SCM-5910.
 * 
 * @author mjg
 * 
 */
@Repository
public class ShareInboxConDao extends SnsHibernateDao<ShareInboxCon, Long> {

  /**
   * 保存推荐收件箱内容记录.
   * 
   * @param shareInboxCon
   */
  public void saveInboxCon(ShareInboxCon shareInboxCon) {
    if (shareInboxCon != null) {
      if (shareInboxCon.getId() != null) {
        String hql =
            "update ShareInboxCon t set t.titleZh=?,t.titleEn=?,t.extOtherInfo=?,t.content=? where t.inboxId=? ";
        super.createQuery(hql, shareInboxCon.getTitleZh(), shareInboxCon.getTitleEn(), shareInboxCon.getExtOtherInfo(),
            shareInboxCon.getContent(), shareInboxCon.getInboxId()).executeUpdate();
      } else {
        super.save(shareInboxCon);
      }
    }
  }

  /**
   * 获取推荐发件记录内容.
   * 
   * @param inboxId
   * @return
   */
  public ShareInboxCon getShareInboxCon(Long inboxId) {
    String hql = "from ShareInboxCon t where t.inboxId=? and rownum=1 ";
    Object obj = super.createQuery(hql, inboxId).uniqueResult();
    if (obj != null) {
      return (ShareInboxCon) obj;
    }
    return null;
  }
}
