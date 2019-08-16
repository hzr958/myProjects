package com.smate.center.batch.dao.sns.wechat;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.wechat.WeChatMessageHistoryPsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 微信个人消息历史表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatMessageHistoryPsnDao extends SnsHibernateDao<WeChatMessageHistoryPsn, Long> {

  /*
   * 通过openId，contentMd5来查询重复的消息
   * 
   * @param openId，contentMd5 return WeChatPreProcessPsn
   */
  public WeChatMessageHistoryPsn getWeChatMessageHistoryPsn(Long openId, String contentMd5) {
    String hql = "from WeChatMessageHistoryPsn t where t.openId = ? and t.contentMd5 = ?";
    return super.findUnique(hql, openId, contentMd5);

  }

  /*
   * 通过Id来查询消息
   * 
   * @param Id return WeChatMessageHistoryPsn
   */
  public WeChatMessageHistoryPsn getWeChatMessageHistoryPsnById(Long id) {
    String hql = "from WeChatMessageHistoryPsn t where t.id = ?";
    return super.findUnique(hql, id);

  }
}
