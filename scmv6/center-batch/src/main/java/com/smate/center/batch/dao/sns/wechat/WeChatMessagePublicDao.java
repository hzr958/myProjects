package com.smate.center.batch.dao.sns.wechat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.wechat.WeChatMessagePublic;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 微信群发消息表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatMessagePublicDao extends SnsHibernateDao<WeChatMessagePublic, Long> {

  @SuppressWarnings("unchecked")
  public List<WeChatMessagePublic> getMsgByStatus(Integer status) {
    String hql = "from WeChatMessagePublic t where t.status = ?";
    return super.createQuery(hql, status).list();
  }
}
