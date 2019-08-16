package com.smate.web.dyn.dao.pub.rol;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.dyn.model.pub.rol.SiePubStatistics;

/**
 * SIE成果统计.
 * 
 * @author xys
 * 
 */
@Repository
public class SiePubStatisticsDao extends RolHibernateDao<SiePubStatistics, Long> {

  /**
   * 获取指定统计类型的统计数.
   * 
   * @param pubId
   * @param fieldName
   * @return
   */
  public Long getSiePubStatistic(Long pubId, String fieldName) {
    StringBuilder hql = new StringBuilder();
    hql.append("select t.");
    hql.append(fieldName);
    hql.append(" from SiePubStatistics t where t.pubId=?");
    return findUnique(hql.toString(), pubId);
  }
}
