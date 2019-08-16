package com.smate.center.open.dao.profile.psnwork;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.profile.psnwork.PsnWorkHistoryInsInfo;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 个人工作经历单位信息 Dao
 * 
 * @author zk
 * 
 */
@Repository
public class PsnWorkHistoryInsInfoDao extends HibernateDao<PsnWorkHistoryInsInfo, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 通过psnId获取PsnWorkHistoryInsInfo
   * 
   * @param psnId
   * @param insId
   * @throws DaoExcption
   */
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) {

    String hql = "from PsnWorkHistoryInsInfo p where p.psnId=? ";
    return (PsnWorkHistoryInsInfo) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 通过人员Id获取人员单位和职称
   * 
   * @param psnId
   * @return
   */
  public PsnWorkHistoryInsInfo getPsnDeptAndPositionById(Long psnId) {
    String hql =
        "select new PsnWorkHistoryInsInfo(departmentZh, departmentEn, positionZh, positionEn) from PsnWorkHistoryInsInfo p where p.psnId=:psnId ";
    return (PsnWorkHistoryInsInfo) super.createQuery(hql, psnId).setParameter("psnId", psnId).uniqueResult();
  }
}
