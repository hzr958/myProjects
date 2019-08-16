package com.smate.center.batch.dao.mail;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.ReqInbox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 站内请求收件箱
 * 
 * @author oyh
 * 
 */
@Repository
public class ReqInboxDao extends SnsHibernateDao<ReqInbox, Long> {

  public Long getTotalReqMsg() throws DaoException {

    Long psnId = SecurityUtils.getCurrentUserId();

    return findUnique("select count(*) from ReqInbox where status in (0,1) and psnId=" + psnId);

  }

  public Long getUnreadReqMsg() throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    return getUnreadReqMsg(psnId);

  }

  public Long getUnreadReqMsg(Long psnId) throws DaoException {

    return findUnique("select count(psnId) from ReqInbox where status in (0) and psnId= ? ", psnId);

  }

  public void updatePersonInfo(Person person) throws DaoException {
    try {
      String hql = "update ReqInbox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.psnId=?";
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
  public List<ReqInbox> getReqInboxList(Long psnId) {
    String ql = "from ReqInbox where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除收件箱记录.
   * 
   * @param psnId
   */
  public void delInBoxByPsnId(Long psnId) {
    String hql = "delete from ReqInbox t where psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 根据mailId查找ReqInbox
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  public List<ReqInbox> findReqInboxListByMailId(Long mailId) throws DaoException {
    String hql = "from ReqInbox t where t.mailId = ?";
    return this.createQuery(hql, mailId).list();
  }
}
