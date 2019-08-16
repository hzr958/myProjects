package com.smate.web.psn.dao.box;



import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.mailbox.MessageNoticeOutBox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 站内通知发件箱
 * 
 * @author yangpeihai
 * 
 */

@Repository
public class MsgNoticeOutBoxDao extends SnsHibernateDao<MessageNoticeOutBox, Long> {
  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql =
          "update MessageNoticeOutBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }


}
