package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailOriginalDataLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 原始数据日记Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailOriginalDataLogDao extends SnsHibernateDao<MailOriginalDataLog, Long> {

}
