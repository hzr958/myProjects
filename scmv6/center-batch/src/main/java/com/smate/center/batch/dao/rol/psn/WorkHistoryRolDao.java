package com.smate.center.batch.dao.rol.psn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.psn.WorkHistoryRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

@Repository
public class WorkHistoryRolDao extends RolHibernateDao<WorkHistoryRol, Long> {

  /**
   * 添加工作经历.
   * 
   * @param entity
   * @throws DaoException
   */
  public void save(WorkHistoryRol entity) {
    super.save(entity);
  }

  public WorkHistoryRol findById(Long id) throws DaoException {

    return super.findUniqueBy("id", id);
  }

  /**
   * 根据id删除工作经历.
   * 
   * @param id
   * @throws DaoException
   */
  public int deleteWorkHistory(Long id) throws DaoException {
    return createQuery("delete from WorkHistoryRol where id=?", id).executeUpdate();
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

    String sql = "delete from WorkHistoryRol t where t.workId= ? and t.psnId= ? ";

    return super.createQuery(sql, new Object[] {workId, psnId}).executeUpdate();
  }

  /**
   * 根据人员ID删除工作单位经历.
   * 
   * @param psnId
   */
  public void delWorkHistoryByPsnId(Long psnId) {
    String hql = "delete from WorkHistoryRol where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 根据workId,psnId,insId查找工作经历.
   * 
   * @param workId
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<WorkHistoryRol> findWorkHistory(Long workId, Long psnId) throws DaoException {

    String sql = " from WorkHistoryRol t where t.workId= ? and t.psnId= ?  ";

    return super.createQuery(sql, new Object[] {workId, psnId}).list();
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<WorkHistoryRol> getWorkHistoryList(Long psnId) {

    String ql = "from WorkHistoryRol where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 修改工作经历.
   * 
   * @param workId
   * @param psnId
   * @param insId
   * @param fromYear
   * @param fromMonth
   * @param toYear
   * @param toMonth
   * @param isActive
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void updateWorkHistory(Long workId, Long psnId, Long insId, Long fromYear, Long fromMonth, Long toYear,
      Long toMonth, Long isActive) throws DaoException {
    String sql =
        "update WorkHistoryRol t set t.fromYear =? ,t.fromMonth=? , t.toYear= ? ,t.toMonth=? ,isActive =?  where t.workId=? and t.psnId=? ";
    super.createQuery(sql, new Object[] {fromYear, fromMonth, toYear, toMonth, isActive, workId, psnId})
        .executeUpdate();
  }

  /**
   * 修改工作经历.
   * 
   * @param workHistory
   * @throws DaoException
   */
  public void updateWorkHistory(WorkHistoryRol workHistory) throws DaoException {
    super.getSession().saveOrUpdate(workHistory);
  }

  /**
   * 获取匹配单位工作年份的用户ID.
   * 
   * @param year
   * @param psnIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMatchYearPsnId(Integer year, Set<Long> psnIds, Long insId) {

    if (year == null) {
      return null;
    }
    String hql =
        "select distinct psnId from WorkHistoryRol where psnId in(:psnIds) and insId = :insId and fromYear <= :fromYear and (toYear >= :toYear or isActive = 1 )";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Long> listResult = new ArrayList<Long>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("psnIds", item).setParameter("insId", insId)
          .setParameter("fromYear", Long.valueOf(year)).setParameter("toYear", Long.valueOf(year)).list());
    }
    return listResult;
  }
}
