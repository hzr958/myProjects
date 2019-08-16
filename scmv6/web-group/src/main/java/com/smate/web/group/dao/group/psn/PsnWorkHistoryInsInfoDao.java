package com.smate.web.group.dao.group.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.PsnWorkHistoryInsInfo;

/**
 * 个人工作经历单位信息 Dao
 * 
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
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws Exception {

    String hql = "from PsnWorkHistoryInsInfo p where p.psnId=? ";
    return (PsnWorkHistoryInsInfo) super.createQuery(hql, psnId).uniqueResult();
  }
}
