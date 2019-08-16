package com.smate.web.psn.dao.inbox;



import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.inbox.MessageNoticeInBox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;


/**
 * 站内通知 收信箱.
 * 
 * @author yangpeihai
 * 
 */
@Repository
public class MsgNoticeInBoxDao extends SnsHibernateDao<MessageNoticeInBox, Long> {

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql =
          "update MessageNoticeInBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

}
