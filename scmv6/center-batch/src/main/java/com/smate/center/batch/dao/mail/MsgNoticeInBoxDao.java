package com.smate.center.batch.dao.mail;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.MessageNoticeInBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 站内通知 收信箱.
 * 
 * @author yangpeihai
 * 
 */
@Repository
public class MsgNoticeInBoxDao extends SnsHibernateDao<MessageNoticeInBox, Long> {
  /**
   * 获取总共的站内通知 数量.
   * 
   * @return
   */
  public Long getTotleMsgNoticeCount() {
    Long currUserId = SecurityUtils.getCurrentUserId();
    Long totleMsgNoticeCount = findUnique("select count(*) from MessageNoticeOutBox where senderId=?", currUserId);
    return totleMsgNoticeCount;
  }

  /**
   * 获取未读的的站内通知 数量.
   * 
   * @return
   */
  public Long getUnReadMsgNoticeCount() {
    Long currUserId = SecurityUtils.getCurrentUserId();
    return getUnReadMsgNoticeCount(currUserId);
  }

  /**
   * 获取未读的的站内通知 数量.
   * 
   * @return
   */
  public Long getUnReadMsgNoticeCount(Long psnId) {

    Long unReadCount =
        findUnique("select count(senderId) from PsnMessageNoticeInBox where status=0 and senderId=?", psnId);
    return unReadCount;
  }

  /**
   * 获取未读的的站内通知的数量，然后将查询的记录的状态修改为已读
   * 
   * @param psnId
   * @return
   */
  public Long getUnReadNoticeNumThenUpdateToReaded(String ids, Long psnId) {
    Long unReadNum = getUnReadMsgNoticeCount(psnId);
    if (unReadNum > 0 && StringUtils.isNotEmpty(ids)) {
      this.createQuery("update PsnMessageNoticeInBox set status=1 where senderId=? and status=0 and id in(" + ids + ")",
          psnId).executeUpdate();
    }
    return unReadNum;
  }

  public void updatePersonInfo(Person person) throws DaoException {
    try {
      String hql =
          "update MessageNoticeInBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql, new Object[] {person.getName(), person.getFirstName(), person.getLastName(),
          person.getAvatars(), person.getPersonId()}).executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", person.getPersonId());
      throw new DaoException(e);

    }
  }

  /**
   * 获取站内通知收件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MessageNoticeInBox> getMsgNoticeInBoxList(Long psnId) {
    String ql = "from MessageNoticeInBox where senderId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除收件箱记录.
   * 
   * @param psnId
   */
  public void delInBoxByPsnId(Long psnId) {
    String hql = "delete from MessageNoticeInBox t where senderId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 根据mailId查找MessageNoticeInBox
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  public List<MessageNoticeInBox> findMessageNoticeInBoxListByMailId(Long mailId) throws DaoException {
    String hql = "from MessageNoticeInBox t where t.noticeId = ?";
    return this.createQuery(hql, mailId).list();
  }
}
