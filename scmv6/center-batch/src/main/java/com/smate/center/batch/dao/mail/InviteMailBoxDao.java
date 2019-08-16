package com.smate.center.batch.dao.mail;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * 站内邀请Dao
 * 
 * @author oyh
 * 
 */

@Repository
@Deprecated
public class InviteMailBoxDao extends SnsHibernateDao<InviteMailBox, Long> {

  public void updatePersonInfo(Person person) throws DaoException {
    try {
      String hql =
          "update InviteMailBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql, new Object[] {person.getName(), person.getFirstName(), person.getLastName(),
          person.getAvatars(), person.getPersonId()}).executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", person.getPersonId());
      throw new DaoException(e);

    }
  }

  /**
   * 获取邀请发件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InviteMailBox> getInviteMailBoxList(Long psnId) {
    String ql = "from InviteMailBox where senderId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  public void delMailBoxByPsnId(Long psnId) {
    String hql = "delete from InviteMailBox t where senderId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 保存站内邀请发件记录_MJG_SCM-6097.
   * 
   * @param mailBox
   */
  @Deprecated
  public void saveInviteMailBox(InviteMailBox mailBox) {
    if (mailBox != null) {
      if (mailBox.getMailId() != null) {
        super.getSession().update(mailBox);
      } else {
        super.save(mailBox);
      }
    }
  }

  /**
   * 清除站内邀请发件箱的内容数据(定时器任务msseageOldDataTaskTriggers终止后此方法将废弃)_MJG_SCM-6097.
   * 
   * @param mailbox
   */
  public void cleanInviteMailBox(InviteMailBox mailBox) {
    String sql =
        "update invite_mailbox t set t.title=null,t.title_en=null,t.ext_other_info=null,t.content=null where t.mail_id="
            + mailBox.getMailId();
    super.getSession().createSQLQuery(sql).executeUpdate();
  }
}
