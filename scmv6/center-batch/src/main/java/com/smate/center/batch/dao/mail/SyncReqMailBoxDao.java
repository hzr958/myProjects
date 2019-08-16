package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.SyncReqMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 站内请求Dao
 * 
 * @author oyh
 * 
 */

@Repository
public class SyncReqMailBoxDao extends SnsHibernateDao<SyncReqMailBox, Long> {

}
