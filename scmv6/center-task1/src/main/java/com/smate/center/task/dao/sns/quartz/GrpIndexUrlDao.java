package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.GrpIndexUrl;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组站外地址Dao
 * 
 * @author zzx
 *
 */
@Repository
public class GrpIndexUrlDao extends SnsHibernateDao<GrpIndexUrl, Long> {
  /**
   * 通过GRP_ID查找.
   * 
   * @param grpId
   * @return
   */
  public GrpIndexUrl find(Long grpId) {
    String sql = "from GrpIndexUrl where grpId = ?";
    return super.findUnique(sql, grpId);
  }

  /**
   * 查询总记录数
   * 
   * @return
   */
  public Long getCount() {
    String hql = "select count(t.grpId) from GrpIndexUrl t";
    return (Long) super.createQuery(hql).uniqueResult();

  }

  /**
   * 分页获取群组Id
   * 
   * @param index
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getNeedInitGrpId(Integer index, Integer batchSize) {
    String hql = "select t.grpId from GrpIndexUrl t where t.grpIndexUrl is null";
    return this.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();

  }


}
