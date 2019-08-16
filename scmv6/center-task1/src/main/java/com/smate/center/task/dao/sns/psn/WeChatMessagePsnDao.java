package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.WeChatMessagePsn;
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
  public List<Long> getDataByOpenId(Long openId, String token) {
    String hql =
        "select t.id from WeChatMessagePsn  t where t.token = :token and t.openId = :openId and trunc( t.createTime) >= trunc(sysdate-7)";
    return createQuery(hql).setParameter("token", token).setParameter("openId", openId).list();
  }
}
