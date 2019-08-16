package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailRecordLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邮件发送记录日记Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailRecordLogDao extends SnsHibernateDao<MailRecordLog, Long> {

}
