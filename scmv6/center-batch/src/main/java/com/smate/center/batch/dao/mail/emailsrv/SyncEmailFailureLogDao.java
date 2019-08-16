package com.smate.center.batch.dao.mail.emailsrv;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.emailsrv.SyncEmailFailureLog;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 
 * 生成邮件出错日志表
 * 
 * @author zk
 * 
 */
@Repository
public class SyncEmailFailureLogDao extends EmailSrvHibernateDao<SyncEmailFailureLog, Long> {

}
