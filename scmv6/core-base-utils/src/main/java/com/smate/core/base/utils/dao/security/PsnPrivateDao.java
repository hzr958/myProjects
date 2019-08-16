package com.smate.core.base.utils.dao.security;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.PsnPrivate;

/**
 * 隐私人员Dao
 * 
 * @author zk
 *
 */
@Repository
public class PsnPrivateDao extends SnsHibernateDao<PsnPrivate, Long> {

  /**
   * 存在于隐私人员列表中
   * 
   * @param psnId
   * @return
   */
  public boolean existsPsnPrivate(Long psnId) {
    String hql = "select pp.psnId from PsnPrivate pp where pp.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list().size() > 0 ? true : false;
  }

  public Long isPsnPrivate(Long psnId) {
    String hql = "select count(*) from PsnPrivate t where t.psnId=?";
    return findUnique(hql, psnId);
  }

  /**
   * 得到一组隐私人员id
   * 
   * @param psnIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> isPsnPrivate(List<Long> psnIds) {
    String hql = "select t.psnId from PsnPrivate t where t.psnId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnPrivateList(Long psnId, Integer size) {
    String hql = "select t.psnId from PsnPrivate t where t.psnId >:psnId order by t.psnId asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(size).list();
  }
}
