package com.smate.web.psn.dao.inbox;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.InviteInbox.InviteInbox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 站内邀请收件箱
 * 
 * @author zx
 * 
 */

@Repository
public class InviteInboxDao extends SnsHibernateDao<InviteInbox, Long> {

  /**
   * 判断是否已经有了相同的组或好友邀请
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；2.群组邀请 3.请求加入群组
   * @return
   */
  public boolean isRepeatInvite(Long recId, Long senderId, String type) {
    String hsql = "";
    if ("friend".equals(type)) {// 好友
      hsql =
          "select a.mailId  from InviteMailBox a, InviteInbox b where a.mailId = b.mailId and a.senderId = ? and b.psnId = ? and nvl(a.inviteType,0)=0 and a.status = 0 and b.status = 0 and b.optStatus = 0";
    }
    List lst = super.createQuery(hsql, senderId, recId).list();
    return (lst != null && lst.size() > 0 ? true : false);
  }

  /**
   * 忽略重复的邀请
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；2.群组邀请 3.请求加入群组
   */
  public void ignoreRepeatInvite(Long recId, Long senderId, String type) {
    StringBuilder sb = new StringBuilder();

    if ("friend".equals(type)) {// 好友
      sb.append("update InviteInbox a set a.status=?,a.optStatus=? where");
      sb.append(" a.status = ? and a.optStatus = ? and a.psnId = ?");
      sb.append(
          " and a.mailId in(select b.mailId from InviteMailBox b where b.senderId=? and b.inviteType=? and b.status = ?)");

    }

    this.createQuery(sb.toString(), new Object[] {1, 7, 0, 0, recId, senderId, 0, 0}).executeUpdate();
  }

  public InviteInbox getInviteInbox(Long psnId, Long mailId) throws DaoException {

    String hql = "From InviteInbox where mailId=? and psnId=?";
    return super.findUnique(hql, new Object[] {mailId, psnId});

  }

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql = "update InviteInbox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.psnId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }
}
