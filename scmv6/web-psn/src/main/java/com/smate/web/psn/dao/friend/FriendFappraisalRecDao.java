package com.smate.web.psn.dao.friend;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.friend.FriendFappraisalRec;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

@Repository
public class FriendFappraisalRecDao extends SnsHibernateDao<FriendFappraisalRec, Long> {

  /**
   * 删除被评价人的评价信息.
   * 
   * @param psnId
   * @param workId
   */
  @Deprecated
  public void delPersonFappraisalRec(Long psnId, Long workId) {
    String hql = "delete from FriendFappraisalRec where psnId = :psnId and psnWorkId = :workId ";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("workId", workId).executeUpdate();
  }

  /**
   * 更新评价人信息lqh add.
   * 
   * @param message
   */
  public void syncPersonFappraisalRec(SnsPersonSyncMessage message) {

    String psnName = message.getNameByLang();
    String hql = "update FriendFappraisalRec set friendHead = ?,friendPsnName = ?  where friendPsnId = ? ";
    super.createQuery(hql, message.getAvatars(), psnName, message.getPsnId()).executeUpdate();
  }

}
