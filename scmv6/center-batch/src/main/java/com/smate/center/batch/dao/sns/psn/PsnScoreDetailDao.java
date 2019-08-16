package com.smate.center.batch.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PsnScoreDetail;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员信息完整度得分详情Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class PsnScoreDetailDao extends SnsHibernateDao<PsnScoreDetail, Long> {
  /**
   * 获取某项得分记录.
   * 
   * @param psnId
   * @param typeId
   * @return
   */
  public PsnScoreDetail getPsnScoreDetailByType(Long psnId, int typeId) throws DaoException {
    String hql = "from PsnScoreDetail t where t.psnId=? and t.typeId=?";
    return super.findUnique(hql, new Object[] {psnId, typeId});
  }

  /**
   * 获取总得分.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public int getPsnSumScore(Long psnId) throws DaoException {
    String hql = "select sum(t.scoreNum) from PsnScoreDetail t where t.psnId=?";
    Object object = super.createQuery(hql, psnId).uniqueResult();
    if (object != null) {
      return Integer.valueOf(object.toString());
    } else {
      return 0;
    }
  }

  /**
   * 获取人员的评分详情.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnScoreDetail> getPsnScoreDetailByPsn(Long psnId) throws DaoException {
    String hql = "from PsnScoreDetail t where t.psnId=?";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 删除指定人信息 人员合并 用 zk add
   */
  public void delScoreDetailById(PsnScoreDetail psnScoreDetail) throws DaoException {
    super.createQuery("delete from PsnScoreDetail t where t.id=?", psnScoreDetail.getId()).executeUpdate();
  }
}
