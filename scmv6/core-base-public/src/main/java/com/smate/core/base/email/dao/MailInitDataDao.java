package com.smate.core.base.email.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;

/**
 * 
 * 邮件初始数据表dao
 * 
 * @author zk
 * 
 */
@Repository
public class MailInitDataDao extends EmailSrvHibernateDao<MailInitData, Long> {

  public void saveMailData(MailInitData mid) {
    super.save(mid);
  }
}
