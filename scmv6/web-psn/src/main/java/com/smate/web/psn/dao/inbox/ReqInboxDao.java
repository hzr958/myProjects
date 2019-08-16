package com.smate.web.psn.dao.inbox;



import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.inbox.ReqInbox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 站内请求收件箱
 * 
 * @author oyh
 * 
 */
@Repository
public class ReqInboxDao extends SnsHibernateDao<ReqInbox, Long> {
  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql = "update ReqInbox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.psnId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

}
