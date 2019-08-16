package com.smate.center.oauth.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnDisciplineKeyDao extends SnsHibernateDao<PsnDisciplineKey, Long> {

  /**
   * 查询某人的关键词数据.
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<PsnDisciplineKey> findByPsnId(Long psnId) {
    return super.createQuery("from PsnDisciplineKey t where t.psnId = ? and t.status=1", psnId).list();
  }

  /**
   * 统计人员有效关键词数量
   * 
   * @param psnId
   * @param status
   * @return
   */
  public Long countPsnDisciplineKey(Long psnId) {
    String hql = "select count(1) from PsnDisciplineKey t where t.psnId=:psnId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }
}
