package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SieInsStatistics;

/**
 * 单位信息统计Dao
 * 
 * @author hd
 *
 */
@Repository
public class SieInsStatisticsDao extends SieHibernateDao<SieInsStatistics, Long> {

  /**
   * 获取成果最多的前十个机构ID
   * 
   * @param insIds
   * @return
   */
  public List<SieInsStatistics> getTopTenInsId(List<Long> insIds) {
    List<SieInsStatistics> resultList = null;
    if (CollectionUtils.isNotEmpty(insIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select new SieInsStatistics(t.insId,t.pubSum) from SieInsStatistics t where 1=1 ");
      if (insIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(insIds, 800, "insId");
        sql.append(sqlConditions);
        sql.append("order by t.pubSum desc, t.insId asc");
        resultList = super.createQuery(sql.toString()).setMaxResults(10).list();
      } else {
        resultList =
            super.createQuery(sql.append(" and t.insId in (:insIds) order by t.pubSum desc, t.insId asc)").toString())
                .setParameterList("insIds", insIds).setMaxResults(10).list();
      }
    }
    return resultList;

  }

  public SieInsStatistics getSieInsStatisticsById(Long insId) {
    String hql = "from SieInsStatistics where insId=:insId";
    return (SieInsStatistics) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }

  /**
   * 根据Ins_id查询单位信息统计更新时间 xr
   * 
   * @author xr
   * @param insId
   * @return Date
   */
  public Date getStUpdateDateById(Long insId) {
    String hql = "select t.updateDate from SieInsStatistics t where t.insId=:insId";
    return (Date) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }

}
