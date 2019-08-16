package com.smate.center.open.dao.data.log;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.data.log.OpenDataAccessLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 访问日志记录 (只是记录参数校验过后的记录 参数没通过的记录在错误日志中 这里不记录响应的对于错)
 * 
 * @author tsz
 *
 */
@Repository
public class OpenDataAccessLogDao extends SnsHibernateDao<OpenDataAccessLog, Long> {

}
