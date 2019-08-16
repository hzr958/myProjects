package com.smate.core.base.utils.dao.wechat;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.wechat.WeChatRelationHis;


/**
 * 
 * 微信openid与科研之友openid关联Dao 历史表操作
 * 
 * @author zk
 *
 */
@Repository
public class WeChatRelationHisDao extends SnsHibernateDao<WeChatRelationHis, Long> {

  public void insertRelData(WeChatRelationHis weChatRelationHis) {
    Session session = getSession();
    session.save(weChatRelationHis);
  }
}
