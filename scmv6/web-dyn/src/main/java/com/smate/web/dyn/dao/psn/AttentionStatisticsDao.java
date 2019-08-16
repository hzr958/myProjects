package com.smate.web.dyn.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.psn.AttentionStatistics;

/**
 * 关注人员统计Dao
 * 
 * @author zkd
 * 
 */
@Repository(value = "attentionStatisticsDao")
public class AttentionStatisticsDao extends SnsHibernateDao<AttentionStatistics, Long> {

}
