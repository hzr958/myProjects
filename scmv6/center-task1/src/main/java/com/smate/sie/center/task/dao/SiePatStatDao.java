package com.smate.sie.center.task.dao;

import org.springframework.stereotype.Repository;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PatStat;

/**
 * 专利统计
 * 
 * @author hd
 *
 */
@Repository
public class SiePatStatDao extends SieHibernateDao<PatStat, Long> {

  /**
   * 获取指定统计类型的统计数.
   * 
   * @param patId
   * @param fieldName
   * @return
   */
  public Long getPatStatistic(Long patId, String fieldName) {
    StringBuilder hql = new StringBuilder();
    hql.append("select t.");
    hql.append(fieldName);
    hql.append(" from SieStPat t where t.patId=?");
    return findUnique(hql.toString(), patId);
  }

}
