package com.smate.core.base.consts.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 地区常理Dao
 * 
 * @author zk
 *
 */
@Repository
public class ConstRegionDao extends SnsHibernateDao<ConstRegion, Long> {

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

  public ConstRegion findRegionNameById(Long regionId) {
    String hql = "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.id = :regionId";
    return (ConstRegion) super.createQuery(hql).setParameter("regionId", regionId).uniqueResult();
  }

  public List<ConstRegion> findBitchRegionName(List<Long> regionId) {
    String hql =
        "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.id in(:regionId)";
    return super.createQuery(hql).setParameterList("regionId", regionId).list();
  }

  public List<ConstRegion> findNextLevelRegion(Long regionId) {
    String hql =
        "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.superRegionId =:regionId";
    return super.createQuery(hql).setParameter("regionId", regionId).list();
  }

  /**
   * 根据字符查找对应的地区对象
   * 
   * @param searchKey 查找的字符
   * @param size 每次最多查找的记录数
   * @return
   */
  public List<ConstRegion> searchForConstRegion(String searchKey, Integer size) {
    String hql = "select new ConstRegion(id, zhName, enName, superRegionId) from ConstRegion t where t.zhName like '%"
        + searchKey + "%' or t.enName like '%" + searchKey + "%'";
    return super.createQuery(hql).list();
  }

  // 通过superRegionId查询国家和区域数据
  public List<ConstRegion> findRegionData(Long superRegionId) {
    Locale locale = LocaleContextHolder.getLocale();
    if (superRegionId == null) {
      // 排除国际的中国香港中国澳门中国台湾
      return super.createQuery("from ConstRegion t where t.id not in(158,344,446) and t.superRegionId is null order by "
          + locale.getLanguage() + "Seq").list();

    } else {
      return super.createQuery(
          "from ConstRegion t where t.superRegionId=? order by nlssort(zhName,'NLS_SORT=SCHINESE_PINYIN_M')",
          superRegionId).list();
    }
  }

  // 通过superRegionId批量查询国家和区域数据
  public List<ConstRegion> findBitchRegionData(List<Long> superRegionIds) {
    String hql = null;
    if (superRegionIds != null) {
      hql =
          "from ConstRegion t where t.superRegionId in(:superRegionIds) order by nlssort(zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    }
    return super.createQuery(hql).setParameterList("superRegionIds", superRegionIds).list();
  }

  /**
   * 获取单个数据.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public ConstRegion getConstRegionByName(String name) {
    String hql = "from ConstRegion cr where lower(cr.zhName) =:name or lower(cr.enName)=:name";
    name = name.trim().toLowerCase();
    List<ConstRegion> rets = super.createQuery(hql).setParameter("name", name).list();
    if (CollectionUtils.isNotEmpty(rets)) {
      return rets.get(0);
    }
    return null;
  }

  /**
   * 获取自动填充地区名字
   */
  public List<ConstRegion> getAcregion(String startWith, String excludes, int size) {
    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    String fetchHql = null;
    // 判断是否是非英文
    if (isChinese) {
      hql = "from ConstRegion t where lower(t.zhName) like ? ";
      fetchHql = "from ConstRegion t where lower(t.zhName) not like ?  and  lower(t.zhName) like ?  ";

    } else {
      hql = "from ConstRegion t where lower(t.enName) like ? ";
      fetchHql = "from ConstRegion t where lower(t.enName) not like ?  and  lower(t.enName) like ?  ";

    }
    if (StringUtils.isNotBlank(excludes) && excludes.matches(ServiceConstants.IDPATTERN)) {

      hql = hql + "and t.id not in( " + excludes + " )";
      fetchHql = fetchHql + "and t.id not in( " + excludes + " )";
    }

    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);

    List<ConstRegion> list = query.list();

    if (list != null && list.size() < size) { // 拼接剩余的部分

      int fetchSize = size - list.size();
      query = super.createQuery(fetchHql,
          new Object[] {startWith.trim().toLowerCase() + "%", "_%" + startWith.trim().toLowerCase() + "%"});
      query.setMaxResults(fetchSize);

      List<ConstRegion> fetchList = query.list();
      for (ConstRegion ins : fetchList) {

        list.add(ins);

      }
    }

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cs : list) {

        if (isChinese) {
          cs.setName(cs.getZhName());
        } else {
          cs.setName(cs.getEnName());
        }
      }
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getRegionNameList(String searchKey, Long superRegionId) {
    if (StringUtils.isBlank(searchKey)) {
      return null;
    }
    // 新加条件，不检索国家级地区
    String hql = "select new Map(t.zhName as name,t.enName as ename,t.id as code) from "
        + " ConstRegion t  where (instr(upper(t.zhName),:searchKey)>0 or instr(upper(t.enName),:searchKey)>0) and (t.superRegionId is not null or t.id in(158, 344, 446)) "
        + "and (t.superRegionId in (select a.id from ConstRegion a where a.superRegionCode='CN' or a.regionCode='CN') or t.id in(158, 344, 446))";
    if (superRegionId == null || superRegionId == 0L) {
      hql += " order by instr(upper(t.zhName),:searchKey) asc,instr(upper(t.enName),:searchKey) asc ,t.id";
    }
    return super.createQuery(hql).setParameter("searchKey", searchKey.toUpperCase().trim()).setMaxResults(5).list();
    // else {
    // hql +=
    // " and t.superRegionId =:superRegionId order by
    // instr(upper(t.zhName),:searchKey)
    // asc,instr(upper(t.enName),:searchKey) asc ,t.id";
    // return super.createQuery(hql).setParameter("searchKey",
    // searchKey.toUpperCase().trim())
    // .setParameter("superRegionId",
    // superRegionId).setMaxResults(5).list();
    // }
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
    // 中国香港、澳门、台湾要显示
    if (!needCountry && regionId != null && regionId != 158 && regionId != 344 && regionId != 446) {
      hql.append(" where t.SUPER_REGION_ID IS NOT NULL ");
    }
    hql.append(" start with t.REGION_ID=:regionId connect by prior SUPER_REGION_ID=REGION_ID ");
    // 拼接查询条件参数.
    List<Object> paramList = new ArrayList<Object>();
    // paramList.add(regionId);
    SQLQuery sqlQuery = super.getSession().createSQLQuery(hql.toString());
    // sqlQuery.setParameters(paramList.toArray(),
    // super.findTypes(paramList.toArray()));
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

  /**
   * 获取所有国家或地区,不包括.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getAllCountryAndRegion(Locale locale) {

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
   * 获取中国所有省级地区不包括中国,然后按拼音首字母排序
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstRegion> getChinaRegion(boolean isContainChina) {
    String hql = "select new ConstRegion(id, zhName, enName) from ConstRegion t where  superRegionId=156) ";
    if (isContainChina) {
      hql = "select new ConstRegion(id, zhName, enName) from ConstRegion t where id = 156 or superRegionId=156 ";
    }
    String locale = LocaleContextHolder.getLocale().toString();
    locale = StringUtils.isNotBlank(locale) ? locale : "zh_CN";
    if (locale.equals("zh_CN")) {
      hql += "order by nlssort(t.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    } else {
      hql += "order by t.enName";
    }
    return super.createQuery(hql).list();
  }

  /**
   * 获取自动填充省份名字
   */
  public List<ConstRegion> getAcprovinces(String startWith, int size) {
    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    String fetchHql = null;
    // 判断是否是非英文
    if (isChinese) {
      hql = "from ConstRegion t where lower(t.zhName) like ?  and superRegionId = 156";
      fetchHql =
          "from ConstRegion t where lower(t.zhName) not like ?  and  lower(t.zhName) like ?  and superRegionId = 156 ";

    } else {
      hql = "from ConstRegion t where lower(t.enName) like ?  and superRegionId = 156";
      fetchHql =
          "from ConstRegion t where lower(t.enName) not like ?  and  lower(t.enName) like ?  and superRegionId = 156";
    }


    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);
    List<ConstRegion> list = query.list();
    if (list != null && list.size() < size) { // 拼接剩余的部分
      int fetchSize = size - list.size();
      query = super.createQuery(fetchHql,
          new Object[] {startWith.trim().toLowerCase() + "%", "_%" + startWith.trim().toLowerCase() + "%"});
      query.setMaxResults(fetchSize);
      List<ConstRegion> fetchList = query.list();
      for (ConstRegion ins : fetchList) {
        list.add(ins);
      }
    }

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstRegion cs : list) {
        if (isChinese) {
          cs.setName(cs.getZhName());
        } else {
          cs.setName(cs.getEnName());
        }
      }
    }
    return list;
  }

  /**
   * 获取单个数据.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Long getRegionIdByName(String name) throws Exception {

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

  /**
   * 查询下级地区
   * 
   * @param subRegionId
   * @return
   */
  public List<Long> findSubRegionIdBySuperRegionId(List<Long> superRegionId) {
    String hql = "select t.id from ConstRegion t where t.superRegionId in (:subRegionId)";
    return super.createQuery(hql).setParameterList("subRegionId", superRegionId).list();
  }

  /**
   * 查询下级地区
   * 
   * @param subRegionId
   * @return
   */
  public List<Long> findSubRegionIds(Long superRegionId) {
    String hql = "select t.id from ConstRegion t where t.superRegionId = :subRegionId";
    return super.createQuery(hql).setParameter("subRegionId", superRegionId).list();
  }

  // 通过地区名称获取地区ID
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

  public ConstRegion findSuperRegionById(Long regionId) {
    String hql =
        "select new ConstRegion(id, zhName, enName) from ConstRegion t where t.id = (select superRegionId from ConstRegion where id=:regionId)";
    return (ConstRegion) super.createQuery(hql).setParameter("regionId", regionId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<ConstRegion> findAll() {
    String hql = "select new ConstRegion(id, zhName, enName) from ConstRegion t";
    return super.createQuery(hql).list();
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

  public List<ConstRegion> getAllConstRegion(Locale locale) {
    String hql =
        "select new ConstRegion(id,zhName,enName) from ConstRegion where superRegionId is null and id not in (344,158,446)order by "
            + locale.getLanguage() + "Seq";
    return super.find(hql);
  }
}
