package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.SyncShareMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 文献/文件发件箱
 * 
 * @author oyh
 * 
 */

@Repository
public class SyncShareMailBoxDao extends SnsHibernateDao<SyncShareMailBox, Long> {

}
