package com.smate.web.psn.dao.friend;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.friend.FriendFappraisalSend;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

@Repository
public class FriendFappraisalSendDao extends SnsHibernateDao<FriendFappraisalSend, Long> {

  /**
   * 删除被评价人评价信息lqh add.
   * 
   * @param message
   */
  public void delPersonFappraisalSend(Long psnId, Long workId) {

    String hql = "delete from FriendFappraisalSend  where friendPsnId = :psnId and friendWorkId = :workId  ";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("workId", workId).executeUpdate();
  }

  /**
   * 更新被评价人信息lqh add.
   * 
   * @param message
   */
  public void syncPersonFappraisalSend(SnsPersonSyncMessage message) {

    String psnName = message.getNameByLang();
    String hql = "update FriendFappraisalSend set friendPsnName = ?  where friendPsnId = ? ";
    super.createQuery(hql, psnName, message.getPsnId()).executeUpdate();
  }


}
