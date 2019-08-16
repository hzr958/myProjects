package com.smate.web.group.dao.grp.grpbase;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.grpbase.GrpStatistics;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpStatisticsDao extends SnsHibernateDao<GrpStatistics, Long> {
  /**
   * 获取群组成员和成果统计数
   * 
   * @return
   */
  public GrpStatistics getSumPubsAndSumMember(Long grpId) {
    String hql = "select new GrpStatistics(t.sumMember,t.sumPubs) from GrpStatistics t where t.grpId=:grpId";
    List<Object> list = this.createQuery(hql).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return (GrpStatistics) list.get(0);
    } else {
      return new GrpStatistics(0, 0);
    }
  }
}
