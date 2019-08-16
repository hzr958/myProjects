package com.smate.web.psn.dao.rol.psnwork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.rol.workhistory.RolWorkHistory;

/**
 * ROL库工作经历DAO
 * 
 * @author Administrator
 *
 */
@Repository
public class RolWorkHistoryDao extends RolHibernateDao<RolWorkHistory, Long> {

  /**
   * 根据workId, psnId查找ROL库中的工作经历
   */
  public List<RolWorkHistory> findRolWorkHistory(Long workId, Long psnId) throws DaoException {

    String sql = " from RolWorkHistory t where t.workId= ? and t.psnId= ?  ";

    return super.createQuery(sql, new Object[] {workId, psnId}).list();
  }

  /**
   * 修改工作经历.
   * 
   * @param workHistory
   * @throws DaoException
   */
  public void updateRolWorkHistory(RolWorkHistory rolWorkHistory) throws DaoException {
    super.getSession().saveOrUpdate(rolWorkHistory);
  }

  /**
   * 根据workId，psnId,insId删除工作经历.
   * 
   * @param workId
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public int deleteWorkHistory(Long workId, Long psnId) throws DaoException {

    String sql = "delete from RolWorkHistory t where t.workId= :workId and t.psnId= :psnId ";
    return super.createQuery(sql).setParameter("workId", workId).setParameter("psnId", psnId).executeUpdate();
  }
}
