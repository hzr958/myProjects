package com.smate.web.psn.dao.psnwork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.PsnInfoDaoException;
import com.smate.web.psn.model.workhistory.PsnWorkHistoryInsInfo;

/**
 * 个人工作经历单位信息 Dao
 * 
 * 
 */
@Repository
public class PsnWorkHistoryInsInfoDao extends SnsHibernateDao<PsnWorkHistoryInsInfo, Long> {

  /**
   * 消除匹配到的记录
   * 
   * @param psnId
   * @param insId
   * @param insName
   * @throws DaoException
   */
  public void deletePsnWorkHistoryInsInfo(Long psnId, Long insId, String insName) throws PsnInfoDaoException {
    String hql =
        "delete from PsnWorkHistoryInsInfo p where p.psnId = ? and (p.insId= ? or (p.insNameZh=? or p.insNameEn= ?))";
    super.createQuery(hql, psnId, insId, insName, insName).executeUpdate();
  }

  /**
   * 通过insId,psnId获取PsnWorkHistoryInsInfo
   * 
   * @param psnId
   * @param insId
   * @throws DaoExcption
   */
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId, Long insId) throws PsnInfoDaoException {

    String hql = "from PsnWorkHistoryInsInfo p where p.psnId=? and p.insId=?";
    return (PsnWorkHistoryInsInfo) super.createQuery(hql, psnId, insId).uniqueResult();
  }

  /**
   * 通过psnId获取PsnWorkHistoryInsInfo
   * 
   * @param psnId
   * @param insId
   * @throws DaoExcption
   */
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws PsnInfoDaoException {

    String hql = "from PsnWorkHistoryInsInfo p where p.psnId=? ";
    return (PsnWorkHistoryInsInfo) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 通过人员Id列表获取List数据
   * 
   * @param psnList
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnWorkHistoryInsInfo> getPsnWorkHistoryInsInfoListByPsnIds(List<Long> psnList)
      throws PsnInfoDaoException {
    String hql = "from PsnWorkHistoryInsInfo p where p.psnId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnList).list();
  }
}
