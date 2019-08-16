package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieKpiInsFundRefresh;

/**
 * 
 * @author lijianming
 * @descript 单位统计刷新表dao
 * 
 */
@Repository
public class SieKpiInsFundRefreshDao extends SieHibernateDao<SieKpiInsFundRefresh, Long> {

  /**
   * 统计状态为0的记录数
   * 
   * @return
   */
  public Long countInsIdByStatusZero() {
    String hql = "select count(t.insId) from SieKpiInsFundRefresh t where t.status = 0";
    return findUnique(hql);
  }

  /**
   * 获取需要进行数据处理的记录
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieKpiInsFundRefresh> getListByStatusZero(int maxSize) {
    String hql = "from SieKpiInsFundRefresh t where t.status = 0 order by t.priorCode desc";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

  /**
   * 删除所有刷新表的数据
   * 
   * @param insId
   */
  public void deleteAllRefreshData() {
    String hql = "delete from SieKpiInsFundRefresh";
    Query query = super.createQuery(hql);
    query.executeUpdate();
  }

  /**
   * 根据insId列表删除刷新表数据
   * 
   * @param insIdList
   */
  public void deleteRefreshDataByInsIdList(List<Long> insIdList) {
    String hql = "delete from SieKpiInsFundRefresh where insId in (:insIdList)";
    Query query = super.createQuery(hql).setParameterList("insIdList", insIdList);
    query.executeUpdate();
  }

  /**
   * 检查需要在KPI_INS_FUND_REFRESH表里删除的insId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> checkInsIdsIsDeleteInRefreshTable() {
    String hql =
        "select t.insId from SieKpiInsFundRefresh t where not exists (select 1 from SieInsFocusAgency a where a.insId = t.insId)";
    Query query = super.createQuery(hql);
    return query.list();
  }

  /**
   * 更新刷新表数据的状态为零
   */
  public void updateRefreshDataStatus() {
    String hql = "update SieKpiInsFundRefresh t set t.status = 0 where t.status != 0";
    Query query = super.createQuery(hql);
    query.executeUpdate();
  }

}
