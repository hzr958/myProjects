package com.smate.center.batch.dao.sns.wechat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.wechat.WeChatMessagePsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 微信个人消息表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatMessagePsnDao extends SnsHibernateDao<WeChatMessagePsn, Long> {
  @SuppressWarnings("unchecked")
  public List<WeChatMessagePsn> getMsgByStatus(Integer status) {
    String hql = "from WeChatMessagePsn t where t.status = ?";
    return super.createQuery(hql, status).list();
  }
}
