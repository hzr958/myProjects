package com.smate.center.open.dao.wechat;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.wechat.WeChatMessagePublic;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 微信群发消息表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatMessagePublicDao extends HibernateDao<WeChatMessagePublic, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

}
