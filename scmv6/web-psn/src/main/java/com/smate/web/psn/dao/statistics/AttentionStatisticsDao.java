package com.smate.web.psn.dao.statistics;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.statistics.AttentionStatistics;

/**
 * 关注人员统计Dao
 * 
 * @author zkd
 * 
 */
@Repository(value = "attentionStatisticsDao")
public class AttentionStatisticsDao extends SnsHibernateDao<AttentionStatistics, Long> {

}
