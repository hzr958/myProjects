package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.InsStatisticsRol;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 机构统计信息DAO
 * 
 * @author wsn
 *
 */
@Repository
public class InsStatisticsRolDao extends RolHibernateDao<InsStatisticsRol, Long> {

  /**
   * 批量获取ROL机构统计信息
   * 
   * @param size
   * @param startInsId
   * @param endInsId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsStatisticsRol> getInsStatisticsRolBySize(Integer size, Long startInsId, Long endInsId) {
    String hql = " from InsStatisticsRol t where t.insId >:startInsId and t.insId<=:endInsId order by t.insId asc";
    return super.createQuery(hql).setParameter("startInsId", startInsId).setParameter("endInsId", endInsId)
        .setMaxResults(size).list();
  }
}
