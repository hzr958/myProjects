package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.KpiInsInf;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 机构统计表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class KpiInsInfDao extends RolHibernateDao<KpiInsInf, Long> {

  /**
   * 查询前几个期刊数量靠前的单位ID.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryPreKpiInsInf(int size) {

    String hql = "select insId from KpiInsInf t order by t.journalNum desc ";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  /**
   * 查询前几个期刊数量靠前的省份单位ID.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryPreKpiInsInf(Long prvId, int size) {

    String hql = "select insId from KpiInsInf t where t.prvId = ?  order by t.journalNum desc ";
    return super.createQuery(hql, prvId).setMaxResults(size).list();
  }

  /**
   * 查询前几个期刊数量靠前的市级直辖区单位ID.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryPreKpiInsInfByDisId(Long disId, int size) {

    String hql = "select insId from KpiInsInf t where t.disId = ? order by t.journalNum desc ";
    return super.createQuery(hql, disId).setMaxResults(size).list();
  }

  /**
   * 查询前几个期刊数量靠前的市级单位ID.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryPreKpiInsInfByCyId(Long cyId, int size) {

    String hql = "select insId from KpiInsInf t where t.cyId = ? order by t.journalNum desc ";
    return super.createQuery(hql, cyId).setMaxResults(size).list();
  }
}
