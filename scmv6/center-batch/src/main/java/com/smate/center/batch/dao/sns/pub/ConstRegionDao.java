package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.ConstRegion;
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
   * 根据SuperRegionId获得国家之后的地区，目前只有美国中国等大国家使用.
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> findRegionBySuperRegionId(Long id, Locale locale) throws DaoException {

    String hql = "from ConstRegion where superRegionId = ? order by " + locale.getLanguage() + "Seq";
    Query query = createQuery(hql, id);
    query.setCacheable(true);

    List<ConstRegion> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cr : list) {

        cr.setCode(cr.getId());
        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return list;

  }

  /**
   * 获取所有国家或地区,不包括.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getAllCountryAndRegion(Locale locale) throws DaoException {

    String hql = "from ConstRegion where superRegionId is null order by  " + locale.getLanguage() + "Seq";
    Query query = createQuery(hql);
    query.setCacheable(true);

    List<ConstRegion> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cr : list) {

        cr.setCode(cr.getId());
        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return list;
  }

  /**
   * 获取所有的省份.
   * 
   * @param locale
   * @return
   * @throws DaoException
   */
  public List<ConstRegion> getAllProvince(Locale locale) throws DaoException {
    //
    String hql =
        "from ConstRegion pr where pr.superRegionId is not null and exists (from ConstRegion cnt where pr.superRegionId = cnt.id and cnt.superRegionId is null) order by  "
            + locale.getLanguage() + "Seq";
    Query query = createQuery(hql);
    query.setCacheable(true);

    List<ConstRegion> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cr : list) {

        cr.setCode(cr.getId());
        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return list;
  }

  /**
   * 获取所有的城市.
   * 
   * @param locale
   * @return
   * @throws DaoException
   */
  public List<ConstRegion> getAllCity(Locale locale) throws DaoException {
    String hql =
        "from ConstRegion ct where ct.superRegionId is not null and exists (from ConstRegion pr where ct.superRegionId = pr.id and pr.superRegionId is not null) order by  "
            + locale.getLanguage() + "Seq";
    Query query = createQuery(hql);
    query.setCacheable(true);

    List<ConstRegion> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cr : list) {

        cr.setCode(cr.getId());
        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return list;
  }

  // 通过superRegionId查询国家和区域数据
  public List<ConstRegion> findRegionData(Long superRegionId) throws DaoException {
    if (superRegionId == null) {
      return super.createQuery("from ConstRegion t where t.superRegionId is null").list();

    } else {
      return super.createQuery("from ConstRegion t where t.superRegionId=?", superRegionId).list();
    }
  }

  /**
   * 获取单个数据.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstRegion getConstRegionById(Long id) throws DaoException {

    return super.findUniqueBy("id", id);
  }

  public ConstRegion getRegionId(String regionCode) throws DaoException {

    return super.findUniqueBy("regionCode", regionCode);

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

    Query q = super.createQuery("from ConstRegion cr where lower(cr.zhName) = ? or lower(cr.enName)=? ",
        new Object[] {name, name});
    q.setCacheable(true);
    List<ConstRegion> rets = q.list();
    if (rets.size() > 0) {
      return rets.get(0);
    }

    return null;
  }

  /**
   * 获取数据库中所有数据，且是直接从数据库中查询.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getNoCacheConstRegion() throws DaoException {

    Query query = super.createQuery("from ConstRegion t ");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
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
   * 获取所有国家或地区数据.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getAllRegion() throws DaoException {

    Query query = super.createQuery("from ConstRegion t where t.superRegionId is null order by id ");
    return query.list();
  }

  /**
   * 获取指定国家或地区的省份.
   * 
   * @param superRegionId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getRegionBySuperId(Long superRegionId) throws DaoException {

    Query query = super.createQuery("from ConstRegion t where t.superRegionId = ?  ", superRegionId);
    return query.list();
  }

  /**
   * 查询国家或地区的ID是否存在.
   * 
   * @param regionId
   * @return
   * @throws DaoException
   */
  public Boolean isRegionIdExit(Long regionId) throws DaoException {

    Long count = super.findUnique("select count(t.id) from ConstRegion t where t.id = ?", regionId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取智能匹配国别列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getAcCountryRegion(String startWith, int size) throws DaoException {

    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    String hql = null;

    // 如果为空，则根据语音查找
    if (StringUtils.isBlank(startWith)) {
      Locale locale = LocaleContextHolder.getLocale();
      if (locale.equals(Locale.US)) {
        isChinese = false;
      } else {
        isChinese = true;
      }
      startWith = "";
    }
    // 判断是否是非英文
    if (isChinese) {
      hql = "from ConstRegion t where lower(t.zhName) like ? and t.superRegionId is null order by zhSeq";
    } else {
      hql = "from ConstRegion t where lower(t.enName) like ? and t.superRegionId is null order by enSeq ";
    }
    Query query = super.createQuery(hql, new Object[] {"%" + startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);
    query.setCacheable(true);
    List<ConstRegion> list = query.list();
    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cr : list) {

        cr.setCode(cr.getId());
        if (isChinese) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return list;
  }

  /**
   * V2.6的ID对应过来的V3的ID.
   * 
   * @param oldId
   * @return
   */
  public Long getOldMapingId(Integer oldId) {

    if (oldId == null) {
      return null;
    }
    String hql = "select id from ConstRegion t where t.oldId = ? ";
    return super.findUnique(hql, oldId);
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
    String hql =
        "select REGION_ID from CONST_REGION t start with t.REGION_ID=? connect by prior SUPER_REGION_ID=REGION_ID ";
    // 拼接查询条件参数.
    List<Object> paramList = new ArrayList<Object>();
    paramList.add(regionId);
    SQLQuery sqlQuery = super.getSession().createSQLQuery(hql.toString());
    sqlQuery.setParameters(paramList.toArray(), super.findTypes(paramList.toArray()));
    List queryList = sqlQuery.list();
    if (CollectionUtils.isNotEmpty(queryList)) {
      for (int i = 0; i < queryList.size(); i++) {
        Object obj = queryList.get(i);
        resultList.add(((java.math.BigDecimal) obj).longValue());
      }
    }
    return resultList;
  }

  /**
   * 根据当前语言和regionId获得地区名
   * 
   * @param regionId
   * @return
   */
  public String getRegionNameById(Long regionId) {
    boolean isChinese = true;
    String hql = null;
    Locale locale = LocaleContextHolder.getLocale();
    if (locale.equals(Locale.US)) {
      isChinese = false;
    }
    if (isChinese) {
      hql = "select t.zhName from ConstRegion t where t.id=?";
    } else {
      hql = "select t.enName from ConstRegion t where t.id=?";
    }
    String regionName = super.findUnique(hql, regionId);
    return regionName;
  }

  @SuppressWarnings("unchecked")
  public List<ConstRegion> findAll() {
    String hql = "select new ConstRegion(id, zhName, enName) from ConstRegion t";
    return super.createQuery(hql).list();
  }

}
