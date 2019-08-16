package com.smate.center.batch.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PsnScoreInit;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 初始化个人信息完整度Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class PsnScoreInitDao extends SnsHibernateDao<PsnScoreInit, Long> {
  /**
   * 获取要刷新个人信息度计分的记录列表.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnScoreInit> getRefreshRecords(int maxSize) throws DaoException {
    String hql = "from PsnScoreInit t";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 删除指定人信息(人员合并用) zk add.
   */
  public void delScoreInitBypsnId(Long psnId) throws DaoException {
    super.createQuery("delete from PsnScoreInit t where t.psnId=?", psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PsnScoreInit> getpsnScoreInit(Long psnId) throws DaoException {
    return super.createQuery(" from PsnScoreInit t where t.psnId=?", psnId).list();
  }
}
