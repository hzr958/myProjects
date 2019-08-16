package com.smate.web.psn.dao.box;



import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.mailbox.ReqMailBox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 站内请求Dao
 * 
 * @author oyh
 * 
 */

@Repository
public class ReqMailBoxDao extends SnsHibernateDao<ReqMailBox, Long> {
  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql = "update ReqMailBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

}
