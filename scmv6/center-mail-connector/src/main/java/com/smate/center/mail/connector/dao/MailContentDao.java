package com.smate.center.mail.connector.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.core.base.utils.data.BaseMongoDAO;

/**
 * 邮件内容Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailContentDao extends BaseMongoDAO<MailContent> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public MailContent findByMailId(Long mailId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("mailId").is(mailId));
    return super.findOne(myQuery);
  }

}
