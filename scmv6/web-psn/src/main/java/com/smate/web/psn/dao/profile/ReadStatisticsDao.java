package com.smate.web.psn.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.profile.ReadStatistics;

@Repository
public class ReadStatisticsDao extends SnsHibernateDao<ReadStatistics, Long> {

  /**
   * 查询人员阅读总数
   * 
   * @param psnId
   * @return
   */
  public Long findPsnReadSumByPsnId(Long psnId) {
    String sql = "select count(total_count) from read_statistics t where t.read_psn_id = ? ";
    return super.queryForLong(sql, new Object[] {psnId});
  }
}
