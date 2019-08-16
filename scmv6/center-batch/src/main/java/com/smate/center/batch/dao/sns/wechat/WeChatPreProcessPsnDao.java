package com.smate.center.batch.dao.sns.wechat;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 微信个人消息查重表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatPreProcessPsnDao extends SnsHibernateDao<WeChatPreProcessPsn, Long> {
  /*
   * 通过openId，contentMd5来查询重复的消息
   * 
   * @param openId，contentMd5 return WeChatPreProcessPsn
   */
  public WeChatPreProcessPsn getWeChatPreProcessPsn(Long openId, String contentMd5) {
    String hql = "from WeChatPreProcessPsn t where t.openId = ? and t.contentMd5 = ?";
    return super.findUnique(hql, openId, contentMd5);

  }

  /*
   * 通过Id来查询未处理的消息，status=0
   * 
   * @param Id return WeChatPreProcessPsn
   */
  public WeChatPreProcessPsn getUnProcessedWeChatPreProcessPsnById(Long id) {
    String hql = "from WeChatPreProcessPsn t where t.id = ? and t.status = 0";
    return super.findUnique(hql, id);

  }

  /*
   * 通过Id来查询消息 不限status
   * 
   * @param Id return WeChatPreProcessPsn
   */
  public WeChatPreProcessPsn getWeChatPreProcessPsnById(Long id) {
    String hql = "from WeChatPreProcessPsn t where t.id = ?";
    return super.findUnique(hql, id);

  }
}
