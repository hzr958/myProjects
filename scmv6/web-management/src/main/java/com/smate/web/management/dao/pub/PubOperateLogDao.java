package com.smate.web.management.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.pub.PubOperateLog;

/**
 * 
 * @author yhx
 *
 */
@Repository
public class PubOperateLogDao extends SnsHibernateDao<PubOperateLog, Long> {
}
