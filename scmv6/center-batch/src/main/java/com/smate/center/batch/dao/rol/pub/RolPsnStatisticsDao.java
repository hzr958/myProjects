package com.smate.center.batch.dao.rol.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.prj.RolPsnStatistics;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * rol同步sns的PsnStatistics
 * 
 * @author zzx
 *
 */
@Repository
public class RolPsnStatisticsDao extends RolHibernateDao<RolPsnStatistics, Long> {
  /**
   * 查询人员信息统计表
   * 
   * @param psnId
   * @return
   */
  public RolPsnStatistics findByPsnId(Long psnId) {
    String hql = "from RolPsnStatistics t where t.psnId = ?";
    return (RolPsnStatistics) super.createQuery(hql, new Object[] {psnId}).uniqueResult();
  }
}
