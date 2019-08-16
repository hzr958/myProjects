package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.mongodb.model.MailReturnedRecord;
import com.smate.core.base.utils.data.BaseMongoDAO;

/**
 * 邮件发送失败记录Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailReturnedRecordDao extends BaseMongoDAO<MailReturnedRecord> {

}
