package com.smate.center.merge.dao.thirdparth;

import com.smate.center.merge.model.sns.thirdparty.WeChatRelationHis;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

/**
 * 微信openid与科研之友openid关联Dao 历史表操作.
 * 
 * @author zk
 *
 */
@Repository
public class WeChatRelationHisDao extends SnsHibernateDao<WeChatRelationHis, Long> {
}
