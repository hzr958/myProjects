package com.smate.center.batch.dao.sns.wechat;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.wechat.WeChatMessageHistoryPublic;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 微信群发消息历史表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class WeChatMessageHistoryPublicDao extends SnsHibernateDao<WeChatMessageHistoryPublic, Long> {

}
