package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.psn.model.PsnScoreRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人信息完整度刷新Dao.
 * 
 * @author zll
 * 
 */
@Repository
public class PsnScoreRefreshDao extends SnsHibernateDao<PsnScoreRefresh, Long> {
  /**
   * 获取要刷新个人信息度计分的记录列表.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnScoreRefresh> getRefreshRecords(int maxSize) throws DaoException {
    String hql = "from PsnScoreRefresh t";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

}
