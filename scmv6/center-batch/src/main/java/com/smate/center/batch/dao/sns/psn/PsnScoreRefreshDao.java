package com.smate.center.batch.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PsnScoreRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人信息完整度刷新Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class PsnScoreRefreshDao extends SnsHibernateDao<PsnScoreRefresh, Long> {
  /**
   * 获取要初始化个人信息度计分的记录列表.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnScoreRefresh> getRefreshRecords(int maxSize) throws DaoException {
    String hql = "from PsnScoreRefresh t";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 删除指定人信息（人员合并） zk add
   */
  public void delScoreRefresh(Long psnId) throws DaoException {
    super.createQuery("delete from PsnScoreRefresh t where t.psnId=?", psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PsnScoreRefresh> getPsnScoreRefresh(Long psnId) throws DaoException {
    return super.createQuery(" from PsnScoreRefresh t where t.psnId=?", psnId).list();
  }
}
