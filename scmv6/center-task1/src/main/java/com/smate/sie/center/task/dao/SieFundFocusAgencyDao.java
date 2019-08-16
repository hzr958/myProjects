package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieInsFocusAgency;

/**
 * 
 * @author lijianming
 * @descript 关注资助机构dao
 * 
 */
@Repository
public class SieFundFocusAgencyDao extends SieHibernateDao<SieInsFocusAgency, Long> {

  // 获取单位合集，即insId集合
  @SuppressWarnings("unchecked")
  public List<Long> getInsIdListByFocusAgency() {
    String hql = "select distinct t.insId from SieInsFocusAgency t";
    Query query = super.createQuery(hql);
    return query.list();
  }

  /**
   * 检查基金机构业务表是否有冗余数据
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> checkFocusDataIsUnnecessary(Long insId) {
    String hql =
        "select t.agencyId from SieInsFocusAgency t where not exists (select 1 from SieConstFundAgencyView f where t.agencyId = f.agencyId) and t.insId = :insId";
    Query query = super.createQuery(hql).setParameter("insId", insId);
    return query.list();
  }

  /**
   * 删除单位下冗余基金机构数据
   * 
   * @param agencyIds
   * @param insId
   */
  public void deleteFocusedAgencyByAgencyListAndInsId(List<Long> agencyIds, Long insId) {
    String hql = "delete from SieInsFocusAgency where agencyId in (:agencyIds) and insId = :insId ";
    Query query = super.createQuery(hql).setParameterList("agencyIds", agencyIds).setParameter("insId", insId);
    query.executeUpdate();
  }

  /**
   * 根据机构id和单位id删除关注表数据
   * 
   * @param agencyId
   * @param insId
   */
  public void deleteFocusedAgencyByIdAndInsId(Long agencyId, Long insId) {
    String hql = "delete from SieInsFocusAgency where agencyId = :agencyId and insId = :insId";
    Query query = super.createQuery(hql).setParameter("agencyId", agencyId).setParameter("insId", insId);
    query.executeUpdate();
  }

  /**
   * 查找需要插入到KPI_INS_FUND_REFRESH表的insId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> checkInsIdNeedInsertInReshshTable() {
    String hql =
        "select distinct t.insId from SieInsFocusAgency t where not exists (select 1 from SieKpiInsFundRefresh r where t.insId = r.insId)";
    Query query = super.createQuery(hql);
    return query.list();
  }

  /**
   * 获得单位在基金机构关注表的数据量
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieInsFocusAgency> getListByInsId(Long insId) {
    String hql = "from SieInsFocusAgency t where t.insId = :insId";
    Query query = super.createQuery(hql).setParameter("insId", insId);
    return query.list();
  }
}
