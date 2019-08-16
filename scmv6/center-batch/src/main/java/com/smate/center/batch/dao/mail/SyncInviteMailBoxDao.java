package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.SyncInviteMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 站内邀请发件箱
 * 
 * @author oyh
 * 
 */

@Repository
public class SyncInviteMailBoxDao extends SnsHibernateDao<SyncInviteMailBox, Long> {

}
