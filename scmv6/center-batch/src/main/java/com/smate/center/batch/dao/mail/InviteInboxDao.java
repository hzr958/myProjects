package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 站内邀请收件箱
 * 
 * @author oyh
 * 
 */
@Repository
public class InviteInboxDao extends SnsHibernateDao<InviteInbox, Long> {

  public Long getTotalInviteMsg() throws DaoException {

    Long psnId = SecurityUtils.getCurrentUserId();

    return findUnique("select count(*) from InviteInbox where status in (0,1) and psnId=" + psnId);

  }

  public Long getUnreadInviteMsg() throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    return getUnreadInviteMsg(psnId);
  }

  public Long getUnreadInviteMsg(Long psnId) throws DaoException {

    return findUnique("select count(psnId) from InviteInbox where optStatus in (0) and status<>2 and psnId=" + psnId);
  }

  public Long getUnDisposeInviteMsg() throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    return findUnique("select count(*) from InviteInbox where  optStatus in (0) and status<>2 and psnId=" + psnId);

  }

  public InviteInbox getInviteInbox(Long psnId, Long mailId) throws DaoException {

    String hql = "From InviteInbox where mailId=? and psnId=?";
    return super.findUnique(hql, new Object[] {mailId, psnId});

  }

  public void updatePersonInfo(Person person) throws DaoException {
    try {
      String hql = "update InviteInbox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.psnId=?";
      super.createQuery(hql, new Object[] {person.getName(), person.getFirstName(), person.getLastName(),
          person.getAvatars(), person.getPersonId()}).executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", person.getPersonId());
      throw new DaoException(e);

    }
  }

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

  public boolean isRepeatInvite(Long recId, Long senderId, String type, Long groupId) {
    List<Object> params = new ArrayList<Object>();
    String hsql = "";
    if ("group".equals(type)) {// 群组
      hsql =
          "select a.mailId  from InviteMailBox a,InviteMailBoxCon mailCon, InviteInbox b where a.mailId = b.mailId and a.mailId=mailCon.mailId and a.senderId = ? and  mailCon.extOtherInfo like ? and b.psnId = ? and a.inviteType=1 and a.status = 0 and b.status = 0 and b.optStatus = 0 ";
      params.add(senderId);
      params.add("%\"groupId\":" + groupId + "%");
      params.add(recId);

    }
    List lst = super.createQuery(hsql, params.toArray()).list();
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

  /**
   * 忽略重复的邀请
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 2.群组邀请 3.请求加入群组
   */
  public void ignoreRepeatInvite(Long recId, Long senderId, String type, Long groupId) {
    StringBuilder sb = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    if ("group".equals(type)) {// 群组
      sb.append("update InviteInbox a set a.status=?,a.optStatus=? where");
      params.add(1);
      params.add(7);
      sb.append(" a.status = ? and a.optStatus = ? and a.psnId = ?");
      params.add(0);
      params.add(0);
      params.add(recId);
      sb.append(
          " and a.mailId in(select b.mailId from InviteMailBox b,InviteMailBoxCon mailCon where b.mailId=mailCon.mailId and b.senderId=? and b.inviteType=? and b.status = ? and  mailCon.extOtherInfo like ?)");
      params.add(senderId);
      params.add(1);
      params.add(0);
      params.add("%\"groupId\":" + groupId + "%");
      this.createQuery(sb.toString(), params.toArray()).executeUpdate();
    }

  }

  /**
   * 根据mailId查找inviteInbox
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  public List<InviteInbox> findInviteInboxListByMailId(Long mailId) throws DaoException {
    String hql = "from InviteInbox t where t.mailId = ?";
    return this.createQuery(hql, mailId).list();
  }

  /**
   * 获取邀请收件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InviteInbox> getInviteInboxList(Long psnId) {
    String ql = "from InviteInbox where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除收件箱记录.
   * 
   * @param psnId
   */
  public void delInBoxByPsnId(Long psnId) {
    String hql = "delete from InviteInbox t where psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 更新站内邀请收件记录状态_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateInboxStatus(Integer status, Long id) {
    String hql = "update InviteInbox t set t.status=? where t.id=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }
}
