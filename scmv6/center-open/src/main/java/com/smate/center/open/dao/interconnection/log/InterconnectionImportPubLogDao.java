package com.smate.center.open.dao.interconnection.log;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.interconnection.log.InterconnectionImportPubLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 业务系统导入成果日志表
 * 
 * @author tsz
 *
 */
@Repository
public class InterconnectionImportPubLogDao extends SnsHibernateDao<InterconnectionImportPubLog, Long> {

}
