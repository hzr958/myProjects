package com.smate.center.batch.dao.mail;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.ReqMailBox;
import com.smate.center.batch.model.sns.pub.SnsPersonSyncMessage;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;


/**
 * 站内请求Dao
 * 
 * @author oyh
 * 
 */

@Repository
public class ReqMailBoxDao extends SnsHibernateDao<ReqMailBox, Long> {
  public void updatePersonInfo(Person person) throws DaoException {
    try {
      String hql = "update ReqMailBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql, new Object[] {person.getName(), person.getFirstName(), person.getLastName(),
          person.getAvatars(), person.getPersonId()}).executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", person.getPersonId());
      throw new DaoException(e);

    }
  }

  /**
   * 获取请求更新发件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ReqMailBox> getReqMailBoxList(Long psnId) {
    String ql = "from ReqMailBox where senderId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  public void delMailBoxByPsnId(Long psnId) {
    String hql = "delete from ReqMailBox t where senderId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }
}
