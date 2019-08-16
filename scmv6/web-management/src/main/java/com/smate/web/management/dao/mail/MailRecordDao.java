package com.smate.web.management.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.connector.model.SearchMailInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mail.MailRecord;

/**
 * 邮件发送记录表dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailRecordDao extends SnsHibernateDao<MailRecord, Long> {

  public void searchMailRecordList(SearchMailInfo info, Page<MailRecord> page) {

  }

}
