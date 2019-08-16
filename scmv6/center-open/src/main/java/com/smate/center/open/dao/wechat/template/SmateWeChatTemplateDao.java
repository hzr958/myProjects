package com.smate.center.open.dao.wechat.template;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.wechat.template.SmateWeChatTemplate;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 微信消息 模板 dao
 * 
 * @author tsz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
@Repository
public class SmateWeChatTemplateDao extends HibernateDao<SmateWeChatTemplate, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
