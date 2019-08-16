package com.smate.center.open.dao.wechat;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.wechat.WeChatMessagePsn;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 微信个人消息表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatMessagePsnDao extends HibernateDao<WeChatMessagePsn, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
