package com.smate.web.dyn.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.exception.DynException;
import com.smate.web.dyn.model.psn.PsnWorkHistoryInsInfo;

/**
 * 个人工作经历单位信息 Dao
 * 
 * @author zk
 * 
 */
@Repository
public class PsnWorkHistoryInsInfoDao extends SnsHibernateDao<PsnWorkHistoryInsInfo, Long> {
  /**
   * 通过psnId获取PsnWorkHistoryInsInfo
   * 
   * @param psnId
   * @param insId
   * @throws DaoExcption
   */
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws DynException {

    String hql = "from PsnWorkHistoryInsInfo p where p.psnId=? ";
    return (PsnWorkHistoryInsInfo) super.createQuery(hql, psnId).uniqueResult();
  }
}
