package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.SyncInsideMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 站内短信发件箱
 * 
 * @author oyh
 * 
 */

@Repository
public class SyncInsideMailBoxDao extends SnsHibernateDao<SyncInsideMailBox, Long> {

}
