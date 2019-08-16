package com.smate.sie.core.base.utils.dao.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseView;

/**
 * 
 * @author hd
 *
 */
@Repository
public class KpiImpactBaseViewDao extends SieHibernateDao<KpiImpactBaseView, Long> {


  /**
   * 统计某个insId,在指定stDate日期，keyType(数据类型：1项目2论文3专利)的指定item(统计类型)的View(阅读)记录
   * 
   * @param day
   * @param keyType
   * @param insId
   * @param item
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getCountByItems(Date day, Integer keyType, Long insId, String item) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("select new Map(nvl( t.").append(item).append(",'其他') as ").append(item);
    hql.append(",count(t.id) as count) ");
    hql.append(" from KpiImpactBaseView t where t.insId=? and t.keyType=? and trunc(t.timeRecords)=trunc(?) ");
    params.add(insId);
    params.add(keyType);
    params.add(day);
    hql.append(" group by nvl(t.").append(item).append(",'其他')");
    return this.createQuery(hql.toString(), params.toArray()).list();
  }

  /**
   * 统计指定day（社交行为记录时间）,指定keyType(数据类型：1项目2论文3专利),指定单位insId的 日期类型统计记录数
   * 
   * @param day
   * @param keyType
   * @param insId
   * @return
   */
  public Long getCountByDate(Date day, Integer keyType, Long insId) {
    StringBuilder hql = new StringBuilder();
    hql.append(
        "select count(t.id) from KpiImpactBaseView t where t.insId=? and t.keyType=? and trunc(t.timeRecords)=trunc(?) ");
    Object[] objects = new Object[] {insId, keyType, day};
    return super.findUnique(hql.toString(), objects);
  }

  /**
   * 在一个时间段内基表是否单位的数据
   * 
   * @param insId
   * @param startDate
   * @param endDate
   * @return
   */
  public Long getCountByDateTime(Long insId, Date startDate, Date endDate) {
    String hql =
        "select count(t.id) from KpiImpactBaseView t where t.insId= :insId and t.timeRecords >= :startDate and t.timeRecords < :endDate";
    Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("startDate", startDate)
        .setParameter("endDate", endDate);
    return (Long) query.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getCountByDataAndMon(Long insId, Integer keyType, Date beginTime, Date endTime) {
    StringBuilder hql = new StringBuilder();
    hql.append("select new Map(t.keyCode as item ,count(t.id) as count) ");
    hql.append(
        " from KpiImpactBaseView t where t.insId=? and t.keyType=? and  t.timeRecords>=? and t.timeRecords<? group by t.keyCode ");
    Object[] objects = new Object[] {insId, keyType, beginTime, endTime};
    return this.createQuery(hql.toString(), objects).list();
  }

  @SuppressWarnings("unchecked")
  public KpiImpactBaseView getByKeyCodeAndType(Long insId, Integer keyType, Long keyCode, Date beginTime,
      Date endTime) {
    StringBuilder hql = new StringBuilder();
    hql.append(
        " from KpiImpactBaseView t where t.insId=? and t.keyType=? and  t.timeRecords>=? and t.timeRecords<? and t.keyCode=?");
    Object[] objects = new Object[] {insId, keyType, beginTime, endTime, keyCode};
    List<KpiImpactBaseView> list = this.createQuery(hql.toString(), objects).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getIPCountByItems(Date beginTime, Date endTime, Integer keyType, Long insId,
      String item) {
    /*
     * SELECT NVL(t.country, '其他'), COUNT(DISTINCT(t.ip)) AS cnt from sie2.kpi_impact_base_view t WHERE
     * t.time >= to_date('2018/12/01', 'yyyy/MM/dd') AND t.time <to_date('2019/01/01', 'yyyy/MM/dd') AND
     * t.ins_id = 858 GROUP BY t.country ORDER BY cnt DESC;
     */
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("select new Map(nvl(t.").append(item).append(",'其他') as ").append(item);
    hql.append(",count(distinct t.ip) as count) ");
    hql.append(" from KpiImpactBaseView t where t.insId=? and t.keyType=? and trunc(t.timeRecords)>=trunc(?) ");
    hql.append(" and trunc(t.timeRecords)<trunc(?) ");
    params.add(insId);
    params.add(keyType);
    params.add(beginTime);
    params.add(endTime);
    hql.append(" group by nvl(t.").append(item).append(",'其他')");
    Query query = this.createQuery(hql.toString(), params.toArray());
    return query.list();
  }


}
