package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 地区数据层接口.
 * 
 * @author zhengbin
 * 
 */
@Repository
public class ConstRegionDao extends SnsHibernateDao<ConstRegion, Long> {
  /**
   * 获取中国所有省市名//省排在前面
   * 
   * @return
   * @author LIJUN
   * @date 2018年3月13日
   */
  public List<ConstRegion> getAllCNZhname() {
    String hql =
        "select new ConstRegion( id,  zhName,  enName, superRegionId) from ConstRegion cr where countryCode='CN' order by superRegionId asc";
    return super.createQuery(hql).list();

  }

  /**
   * 获取所有数据
   * 
   * @return
   * @author LIJUN
   * @date 2018年3月14日
   */
  public List<ConstRegion> getAllName() {
    String hql = "from ConstRegion";
    return super.createQuery(hql).list();

  }

  // 获取
  @SuppressWarnings("unchecked")
  public List<String> getRegionNamebyRegionIds(String locale, List<Long> regionIds) {
    String hql = "";
    if ("zh_CN".equals(locale)) {
      hql = "select zhName from ConstRegion where id in(:regionIds)";
    } else {
      hql = "select zhName from ConstRegion where id in(:regionIds)";
    }
    return super.createQuery(hql).setParameterList("regionIds", regionIds).list();
  }

  /**
   * 批量获取地区的中文名
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findZhName(Integer pageNo, Integer pageSize) {
    String hql = "select cr.zhName from ConstRegion cr order by cr.id asc";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 获取单个数据.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstRegion getConstRegionById(Long id) {

    return super.findUniqueBy("id", id);
  }

  /**
   * 清空所有数据.
   * 
   * @throws DaoException
   */
  public void removeAll() throws DaoException {

    super.createQuery("delete from ConstRegion").executeUpdate();
  }

  /**
   * 根据机构ID获取其所有父级单位ID(包含当前机构).
   * 
   * @param regionId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Long> getSuperRegionList(Long regionId, boolean needCountry) {
    List<Long> resultList = new ArrayList<Long>();
    StringBuffer hql = new StringBuffer("select REGION_ID from CONST_REGION t ");
    if (!needCountry) {
      hql.append(" where t.SUPER_REGION_ID IS NOT NULL ");
    }
    hql.append(" start with t.REGION_ID=:regionId connect by prior SUPER_REGION_ID=REGION_ID ");
    SQLQuery sqlQuery = super.getSession().createSQLQuery(hql.toString());
    sqlQuery.setParameter("regionId", regionId);
    List queryList = sqlQuery.list();
    if (CollectionUtils.isNotEmpty(queryList)) {
      for (int i = 0; i < queryList.size(); i++) {
        Object obj = queryList.get(i);
        resultList.add(((java.math.BigDecimal) obj).longValue());
      }
    }
    return resultList;
  }

  public ConstRegion findRegionNameById(Long regionId) {
    String hql = "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.id = :regionId";
    return (ConstRegion) super.createQuery(hql).setParameter("regionId", regionId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<ConstRegion> findBitchRegionName(List<Long> regionId) {
    String hql =
        "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.id in(:regionId)";
    return super.createQuery(hql).setParameterList("regionId", regionId).list();
  }

  /**
   * 获取单个数据.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public ConstRegion getConstRegionByName(String name) throws DaoException {

    name = name.trim().toLowerCase();

    Query q = super.createQuery("from ConstRegion cr where lower(cr.zhName) = lower(?) or lower(cr.enName)=lower(?) ",
        new Object[] {name, name});
    q.setCacheable(true);
    List<ConstRegion> rets = q.list();
    if (rets.size() > 0) {
      return rets.get(0);
    }

    return null;
  }

  public ConstRegion findRegionNameByRegionId(Long regionId) {
    String hql = "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.id = :regionId";
    return (ConstRegion) super.createQuery(hql).setParameter("regionId", regionId).uniqueResult();
  }

  /**
   * 获取单个数据.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Long getRegionIdByName(String name) throws DaoException {

    name = name.trim().toLowerCase();

    Query q = super.createQuery(
        "select id from ConstRegion cr where lower(cr.zhName) = lower(?) or lower(cr.enName)=lower(?) ",
        new Object[] {name, name});
    q.setCacheable(true);
    List<Long> rets = q.list();
    if (rets.size() > 0) {
      return rets.get(0);
    }

    return null;
  }

  public Long getSuperRegionId(Long regionId) {
    String hql = "select t.superRegionId from ConstRegion t where id=:regionId";
    return (Long) super.createQuery(hql).setParameter("regionId", regionId).uniqueResult();
  }

  public Long getRegionIdByCityName(String name) {
    name = name.replaceAll("\'", "");
    name = name.trim().toLowerCase();
    String sql = "select t.id from ConstRegion t where lower(t.zhName) like '%" + name + "%' or lower(t.enName) like '%"
        + name + "%'";
    Query q = super.createQuery(sql);
    q.setCacheable(true);
    List<Long> rets = q.list();
    if (rets.size() > 0) {
      return rets.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<ConstRegion> findAll() {
    String hql = "select new ConstRegion(id, zhName, enName) from ConstRegion t";
    return super.createQuery(hql).list();
  }

  /**
   * 根据机构ID获取其所有父级单位ID(包含当前机构).
   * 
   * @param regionId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Long> getSuperRegionList(Long regionId) {
    List<Long> resultList = new ArrayList<Long>();
    StringBuffer hql = new StringBuffer("select REGION_ID from CONST_REGION t ");
    // 中国香港、澳门、台湾要显示
    hql.append(" where t.SUPER_REGION_ID IS NOT NULL ");
    hql.append(" start with t.REGION_ID=:regionId connect by prior SUPER_REGION_ID=REGION_ID ");
    // 拼接查询条件参数.
    SQLQuery sqlQuery = super.getSession().createSQLQuery(hql.toString());
    sqlQuery.setParameter("regionId", regionId);
    List queryList = sqlQuery.list();
    if (CollectionUtils.isNotEmpty(queryList)) {
      for (int i = 0; i < queryList.size(); i++) {
        Object obj = queryList.get(i);
        resultList.add(((java.math.BigDecimal) obj).longValue());
      }
    }
    return resultList;
  }

}
