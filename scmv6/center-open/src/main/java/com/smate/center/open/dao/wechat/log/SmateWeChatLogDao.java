package com.smate.center.open.dao.wechat.log;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.wechat.log.SmateWeChatLog;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * smate微信关系日志DAO.
 * 
 * @author xys
 *
 */
@Repository
public class SmateWeChatLogDao extends HibernateDao<SmateWeChatLog, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
