package com.smate.sie.core.base.utils.dao.statistics;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.core.base.utils.model.statistics.SiePsnStatistics;

/**
 * 人员信息统计 Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePsnStatisticsDao extends SieHibernateDao<SiePsnStatistics, Long> {


  public Long getUnitPubSumByPsn(List<Long> psnIds) {
    if (psnIds == null || psnIds.size() <= 0) {
      return 0L;
    }
    Object ret = null;
    if (CollectionUtils.isNotEmpty(psnIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select sum(nvl(t.pubSum,0)) from SiePsnStatistics t where 1=1 ");
      if (psnIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(psnIds, 800, "t.psnId");
        sql.append(sqlConditions);
        ret = super.createQuery(sql.toString()).uniqueResult();
      } else {
        ret = super.createQuery(sql.append(" and t.psnId in (:psnIds) ").toString()).setParameterList("psnIds", psnIds)
            .uniqueResult();
      }
    }
    return ret == null ? 0L : NumberUtils.parseLong(ret.toString(), 0L);

  }

  public Long getUnitPrjSumByPsn(List<Long> psnIds) {
    if (psnIds == null || psnIds.size() <= 0) {
      return 0L;
    }
    Object ret = 0L;
    if (CollectionUtils.isNotEmpty(psnIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select sum(nvl(t.prjSum,0)) from SiePsnStatistics t where 1=1 ");
      if (psnIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(psnIds, 800, "t.psnId");
        sql.append(sqlConditions);
        ret = super.createQuery(sql.toString()).uniqueResult();
      } else {
        ret = super.createQuery(sql.append(" and t.psnId in (:psnIds) ").toString()).setParameterList("psnIds", psnIds)
            .uniqueResult();
      }
    }
    return ret == null ? 0L : NumberUtils.parseLong(ret.toString(), 0L);
  }

  public Long getUnitPafSumByPsn(List<Long> psnIds) {
    if (psnIds == null || psnIds.size() <= 0) {
      return 0L;
    }
    Object ret = 0L;
    if (CollectionUtils.isNotEmpty(psnIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select sum(nvl(t.ptSum,0)) from SiePsnStatistics t where 1=1 ");
      if (psnIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(psnIds, 800, "t.psnId");
        sql.append(sqlConditions);
        ret = super.createQuery(sql.toString()).uniqueResult();
      } else {
        ret = super.createQuery(sql.append(" and t.psnId in (:psnIds) ").toString()).setParameterList("psnIds", psnIds)
            .uniqueResult();
      }
    }
    return ret == null ? 0L : NumberUtils.parseLong(ret.toString(), 0L);
  }

  public Long getUnitPdSumByPsn(List<Long> psnIds) {
    if (psnIds == null || psnIds.size() <= 0) {
      return 0L;
    }
    Object ret = 0L;
    if (CollectionUtils.isNotEmpty(psnIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select sum(nvl(t.pdSum,0)) from SiePsnStatistics t where 1=1 ");
      if (psnIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(psnIds, 800, "t.psnId");
        sql.append(sqlConditions);
        ret = super.createQuery(sql.toString()).uniqueResult();
      } else {
        ret = super.createQuery(sql.append(" and t.psnId in (:psnIds) ").toString()).setParameterList("psnIds", psnIds)
            .uniqueResult();
      }
    }
    return ret == null ? 0L : NumberUtils.parseLong(ret.toString(), 0L);
  }

}
