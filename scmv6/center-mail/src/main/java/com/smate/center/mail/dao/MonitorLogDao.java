package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MonitorLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邮件管理通知邮件dao
 * 
 * @author tsz
 *
 */
@Repository
public class MonitorLogDao extends SnsHibernateDao<MonitorLog, Long> {

}
