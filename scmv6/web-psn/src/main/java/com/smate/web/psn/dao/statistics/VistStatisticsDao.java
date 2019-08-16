package com.smate.web.psn.dao.statistics;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.statistics.VistStatistics;

/**
 * 
 * 访问他人主页
 * 
 * @author zx
 *
 */

@Repository
public class VistStatisticsDao extends SnsHibernateDao<VistStatistics, Long> {

  public List<VistStatistics> getVistStatistics(Long psnId) throws DaoException {
    return super.createQuery("from VistStatistics where psnId =? or vistPsnId=?", psnId, psnId).list();
  }

  /**
   * 分页查询访问人员
   * 
   * @param page
   * @param vistPsnId
   * @return
   * @throws DaoException
   */
  public Page findVistPersonPage(Page page, Long vistPsnId) throws DaoException {
    Long count = 0l;
    String countSql =
        "select count(1) from (select distinct t.psn_id,t.formate_date from VIST_STATISTICS t where t.VIST_PSN_ID = ?)";
    count = (Long) super.queryForLong(countSql, new Object[] {vistPsnId});
    page.setTotalCount(count);

    String hql =
        "select new VistStatistics(t.psnId,t.formateDate) from VistStatistics t where t.vistPsnId = ?  group by t.psnId,t.formateDate order by max(t.createDate) desc";
    List result =
        super.createQuery(hql, vistPsnId).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(result);
    return page;
  }

  public void getVistPsnList(Page page, Long vistPsnId) {
    String count = "select t.psnId ";
    String select = "select t.psnId ";
    String hql =
        " from VistStatistics t where exists(select 1 from Person t2 where t2.personId = t.psnId ) and t.vistPsnId=:vistPsnId and t.psnId <> 0 group by t.psnId ";
    String order = " order by Max(t.createDate) desc";
    List list = this.createQuery(count + hql).setParameter("vistPsnId", vistPsnId).list();
    if (list == null) {
      page.setTotalCount(0L);
    } else {
      page.setTotalCount(list.size());
    }
    page.setResult(this.createQuery(select + hql + order).setParameter("vistPsnId", vistPsnId)
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list());
  }

  /**
   * 删除访问记录
   * 
   * @param psnId
   * @param vistPsnId
   * @param formateDate
   * @throws DaoException
   */
  public void delVistRecord(Long psnId, Long vistPsnId, Long formateDate) throws DaoException {
    String hql = "delete from VistStatistics t where t.psnId = ? and t.vistPsnId = ? and t.formateDate = ?";
    super.createQuery(hql, psnId, vistPsnId, formateDate).executeUpdate();
  }

  /**
   * 计算一段时间内的访问人数
   * 
   * @param vistPsnId
   * @param formateDate
   * @return
   * @throws DaoException
   */
  public Integer countInDate(Long vistPsnId, Long startDate, Long endDate) throws DaoException {
    String hql =
        "select sum(t.count) from VistStatistics t where t.vistPsnId = ? and t.formateDate >= ? and t.formateDate < ?";
    Long count = (Long) super.createQuery(hql, vistPsnId, startDate, endDate).uniqueResult();
    return count == null ? 0 : count.intValue();
  }

  /**
   * 查找某个日期前的访问过某个人的psnId
   * 
   * @param vistPsnId
   * @param formateDate
   * @return
   * @throws DaoException
   */
  public List<VistStatistics> findIpsRecordBefDay(Long vistPsnId, Long formateDate) throws DaoException {
    String hql = "from VistStatistics t where t.vistPsnId = ? and t.formateDate >= ?";
    return super.createQuery(hql, vistPsnId, formateDate).list();
  }

  /**
   * 查找访问记录
   * 
   * @param psnId
   * @param vistPsnId
   * @param actionKey
   * @param actionType
   * @param formateDate
   * @return
   * @throws DaoException
   */
  public VistStatistics findVistRecord(Long psnId, Long vistPsnId, Long actionKey, Integer actionType, Long formateDate,
      String ip, Long regionId) throws DaoException {
    String hql =
        "from VistStatistics t where t.psnId = ? and t.vistPsnId = ? and t.actionKey = ? and t.actionType = ? and t.formateDate = ? and t.ip = ? and t.provinceRegionId = ?";
    List<VistStatistics> list =
        super.createQuery(hql, psnId, vistPsnId, actionKey, actionType, formateDate, ip, regionId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 按周统计阅读数
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findPsnVisitGroupByWeek(Long psnId) {
    String sql =
        "select d.weekTime, SUM(t.count) AS visitSum FROM vist_statistics t , (select trunc(sysdate) - (level-1) * 7 AS weekTime  FROM  dual connect by level <= 8)  d  where t.vist_psn_id  = :psnId and  t.create_date < d.weekTime + 1 and t.create_date >= d.weekTime - 6  group by d.weekTime "
            + " order by d.weekTime desc";
    return super.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  /**
   * 按周推算前8周的时间点
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List findVisitTrendTime() {
    String sql = "select trunc(sysdate) - (level-1) * 7 as dt from dual connect by level <= 8 order by dt";
    return super.getSession().createSQLQuery(sql).list();
  }

  /**
   * 获取人员的省份阅读数
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Object> getVistNumByPsnId(Long psnId) throws DaoException {
    String sql = "select b.region_id,b.zh_name,b.en_name,a.vistcount from ("
        + "select province_region_id,sum(count) as vistcount from vist_statistics  where vist_psn_id=:psnId and province_region_id is not null group by province_region_id"
        + ") a,CONST_REGION b where  b.region_id=a.province_region_id ";
    return this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
  }

  /**
   * 获取单位分布阅读统计数
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getVisitIns(Long psnId) throws DaoException {
    String sql =
        "select a.ins_id as insid,sum(b.count)as count from person a,vist_statistics b where a.psn_id=b.psn_id and b.vist_psn_id=:psnId and a.ins_id is not null group by a.ins_id order by count desc,a.ins_id asc";
    return super.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  /**
   * 获取职称分布阅读统计数
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getVisitPos(Long psnId) throws DaoException {
    String sql =
        "select nvl(a.pos_grades, 0) as grade,nvl(sum(b.count), 0) as count from person a ,vist_statistics b where a.psn_id=b.psn_id and b.vist_psn_id=:psnId group by a.pos_grades order by a.pos_grades asc";
    return super.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

}
