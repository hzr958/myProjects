package com.smate.center.open.dao.interconnection.log;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.interconnection.log.InterconnectionGetPubLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 业务系统拉取成果日志记录
 * 
 * @author tsz
 *
 */
@Repository
public class InterconnectionGetPubLogDao extends SnsHibernateDao<InterconnectionGetPubLog, Long> {

}
