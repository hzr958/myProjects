package com.smate.web.psn.dao.friend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.mailbox.InviteMailBox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 站内邀请Dao
 * 
 * @author lhd
 *
 */
@Repository
public class InviteMailBoxDao extends SnsHibernateDao<InviteMailBox, Long> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql =
          "update InviteMailBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }
}
