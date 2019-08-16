package com.smate.core.base.dyn.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.dyn.model.DynamicRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 动态刷新表dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicRefreshDao extends SnsHibernateDao<DynamicRefresh, Long> {

}
