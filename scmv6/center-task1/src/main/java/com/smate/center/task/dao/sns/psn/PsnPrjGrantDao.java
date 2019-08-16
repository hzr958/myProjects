package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.PsnPrjGrant;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 个人获得基金.
 * 
 * @author liangguokeng
 * 
 */
@Repository
public class PsnPrjGrantDao extends SnsHibernateDao<PsnPrjGrant, Long> {
  /**
   * 个人获得基金列表.
   * 
   * @param psnId
   * @param prjRole
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPrjGrant> findPsnPrjGrant(Long psnId, int prjRole) throws DaoException {
    return super.createQuery("from PsnPrjGrant t where t.psnId=? and t.prjRole=?", psnId, prjRole).list();
  }
}
