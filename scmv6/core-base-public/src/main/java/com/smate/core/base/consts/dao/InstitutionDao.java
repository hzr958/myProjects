package com.smate.core.base.consts.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 单位数据层接口.
 * 
 * @author tsz
 * 
 */
@Repository
public class InstitutionDao extends SnsHibernateDao<Institution, Long> {

  /**
   * 通过单位名获取单位Id.
   * 
   * @param zhName
   * @param enName
   * @return Long @
   */
  public Long getInsIdByName(String zhName, String enName) {
    String hql = "select id from Institution where zhName=? or enName=?";
    List<Long> list = super.find(hql, zhName, enName);
    if (list.size() > 0) {
      return list.get(0).longValue();
    }
    return null;
  }

  public Institution findByName(String name) {
    String hql = "from Institution where lower(enName) = lower(?) or lower(zhName) = lower(?) ";
    List<Institution> list = super.find(hql, name, name);
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过单位编号取得单位实体.
   * 
   * @param id
   * @return Institution
   */
  public Institution findById(Long id) {

    return super.findUniqueBy("id", id);
  }

  /**
   * 获取单位中英文名称
   * 
   * @param id
   * @return
   */
  public Institution findInsName(Long id) {
    String hql = "select new Institution( i.id,i.zhName,i.enName) from Institution i where i.id=:insId";
    return (Institution) super.createQuery(hql).setParameter("insId", id).uniqueResult();
  }

  /**
   * 批量获取单位中英文名称
   */
  @SuppressWarnings("unchecked")
  public List<Institution> findBitchInsName(List<Long> insIds) {
    if (insIds == null || insIds.size() == 0) {
      return null;
    } else {
      String hql = "select new Institution( i.id,i.zhName,i.enName) from Institution i where i.id in(:insId) ";
      return super.createQuery(hql).setParameterList("insId", insIds).list();
    }
  }

  /**
   * 通过单位名获取对应的单位记录.
   * 
   * @param zhName
   * @param enName
   * @return List<Institution>
   */
  public List<Institution> getInsListByName(String zhName, String enName, Long natureType) {
    List<Institution> result = null;
    String hql = "from Institution where zhName=? or enName=?";
    List<Institution> list = super.find(hql, zhName, enName);
    if (CollectionUtils.isNotEmpty(list)) {
      result = new ArrayList<Institution>();
      if (natureType != null) {
        for (Institution ins : list) {
          if (ins.getNature().longValue() == natureType.longValue()) {
            result.add(ins);
            continue;
          }
        }
      }
    }
    return result;

  }

  /**
   * 查找单位地区编码
   * 
   * @param insId
   * @return
   */
  public Long findInsRegionId(Long insId) {
    String hql = "select t.regionId from Institution t where t.id = :insId";
    return (Long) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }

  public List<Long> findBitchRegionId(List<Long> insId) {
    String hql = "select t.regionId from Institution t where t.id in(:insId)";
    return super.createQuery(hql).setParameterList("insId", insId).list();
  }

  /**
   * 获取智能匹配国别列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */

  @SuppressWarnings("unchecked")
  public List<Institution> getInstitution(String startWith, String excludes, int size) {

    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    String fetchHql = null;
    // 判断是否是非英文
    if (isChinese) {
      hql = "from Institution t where lower(t.zhName) like ? ";
      fetchHql = "from Institution t where lower(t.zhName) not like ?  and  lower(t.zhName) like ?  ";

    } else {
      hql = "from Institution t where lower(t.enName) like ? ";
      fetchHql = "from Institution t where lower(t.enName) not like ?  and  lower(t.enName) like ?  ";

    }

    hql = hql + "and t.enabled=1 ";
    fetchHql = fetchHql + "and t.enabled=1 ";
    if (StringUtils.isNotBlank(excludes) && excludes.matches(ServiceConstants.IDPATTERN)) {

      hql = hql + "and t.id not in( " + excludes + " )";
      fetchHql = fetchHql + "and t.id not in( " + excludes + " )";
    }

    if (isChinese) {
      hql = hql + " order by length(trim(lower(t.zhName))) asc";
      fetchHql = fetchHql + " order by length(trim(lower(t.zhName))) asc";
    } else {
      hql = hql + " order by length(trim(lower(t.enName))) asc";
      fetchHql = fetchHql + " order by length(trim(lower(t.enName))) asc";
    }

    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);

    List<Institution> list = query.list();

    if (list != null && list.size() < size) { // 拼接剩余的部分

      int fetchSize = size - list.size();
      query = super.createQuery(fetchHql,
          new Object[] {startWith.trim().toLowerCase() + "%", "_%" + startWith.trim().toLowerCase() + "%"});
      query.setMaxResults(fetchSize);

      List<Institution> fetchList = query.list();
      for (Institution ins : fetchList) {
        list.add(ins);
      }
    }

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (Institution cs : list) {
        if (isChinese) {
          cs.setName(cs.getZhName());
        } else {
          cs.setName(cs.getEnName());
        }

        if (cs.getRegionId() != null) {
          ConstRegion region = super.findUnique("from ConstRegion t where t.id = ? ", cs.getRegionId());

          if (isChinese) {
            if (region != null) {
              if (region.getSuperRegionId() == null) {
                cs.setCountry(region.getZhName());
              } else {
                // 获取国别
                ConstRegion superRegion =
                    super.findUnique("from ConstRegion t where t.id = ? ", region.getSuperRegionId());
                cs.setCountry(superRegion.getZhName());
              }
            }

          } else {
            if (region != null) {
              if (region.getSuperRegionId() == null) {
                cs.setCountry(region.getEnName());
              } else {
                // 获取国别
                ConstRegion superRegion =
                    super.findUnique("from ConstRegion t where t.id = ? ", region.getSuperRegionId());
                cs.setCountry(superRegion.getEnName());
              }
            }
          }
        }
      }
    }
    return list;
  }

  /**
   * 通过单位名获取单位信息
   * 
   * @param name
   * @return
   */
  @SuppressWarnings("unchecked")
  public Institution getInsByName(String name) {
    String hql =
        "select new Institution(i.id,i.zhName,i.enName) from Institution i where lower(i.zhName) = Lower(?) or lower(i.enName) = lower(?) and i.status = 2";
    List<Institution> insList = super.createQuery(hql, name, name).list();
    if (CollectionUtils.isNotEmpty(insList)) {
      return insList.get(0);
    }
    return null;
  }

  /**
   * 通过单位id获取单位信息
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Institution getInsById(Long insId) {
    String hql = "select new Institution(i.id,i.zhName,i.enName) from Institution i where i.id=? and i.status = 2";
    List<Institution> insList = super.createQuery(hql, insId).list();
    if (CollectionUtils.isNotEmpty(insList)) {
      return insList.get(0);
    }
    return null;
  }

  /**
   * 通过单位id获取单位信息,没有状态
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Institution getInsByIdNotStatus(Long insId) {
    String hql = "select new Institution(i.id,i.zhName,i.enName) from Institution i where i.id=?";
    List<Institution> insList = super.createQuery(hql, insId).list();
    if (CollectionUtils.isNotEmpty(insList)) {
      return insList.get(0);
    }
    return null;
  }

  /**
   * 分页查询机构
   * 
   * @param regionIds
   * @param insCharacter
   * @param page
   * @param queryStr
   * @return
   */
  public List<Institution> searchInstitution(Long regionIds, Long insCharacter, Page page, String queryStr) {
    String countHql = "select count(1) ";
    String listHql = "select new Institution(id, zhName, enName, url, nature, regionId) ";
    StringBuffer queryHql = new StringBuffer(" from Institution t where t.enabled = 1");
    if (regionIds != null && regionIds != -1) {
      queryHql.append(" and t.regionId = :regionIds ");
    } else if (regionIds != null && regionIds == -1) {
      queryHql.append(" and t.regionId is null ");
    }
    if (insCharacter != null) {
      queryHql.append(" and t.nature = :insCharacter ");
    }
    if (StringUtils.isNotBlank(queryStr)) {
      queryHql.append(" and instr(upper(nvl(t.zhName || t.enName, '')),:queryStr)>0 ");
    }
    queryHql.append("and t.status in (2,3) ");
    Query countQry = super.createQuery(countHql + queryHql.toString());
    Query listQry = super.createQuery(listHql + queryHql.toString());
    if (regionIds != null && regionIds != -1) {
      countQry.setParameter("regionIds", regionIds);
      listQry.setParameter("regionIds", regionIds);
    }
    if (insCharacter != null) {
      countQry.setParameter("insCharacter", insCharacter);
      listQry.setParameter("insCharacter", insCharacter);
    }
    if (StringUtils.isNotBlank(queryStr)) {
      countQry.setParameter("queryStr", queryStr.trim().toUpperCase());
      listQry.setParameter("queryStr", queryStr.trim().toUpperCase());
    }
    Long count = (Long) countQry.uniqueResult();
    page.setTotalCount(count);
    List<Institution> result = listQry.setMaxResults(page.getPageSize()).setFirstResult(page.getFirst() - 1).list();
    page.setResult(result);
    return result;
  }

  /**
   * 将机构按类别（Nature）分组统计
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> countInsByNature(Long regionIds, Long insCharacter, String queryStr) {
    StringBuffer hql = new StringBuffer(
        "select new Map(t.nature as character, count(1) as insCount) from Institution t where t.status in (2,3) and t.enabled = 1");
    if (regionIds != null && regionIds != -1) {
      hql.append(" and t.regionId = :regionIds ");
    } else if (regionIds != null && regionIds == -1) {
      hql.append(" and t.regionId is null ");
    }
    if (insCharacter != null) {
      hql.append(" and t.nature = :insCharacter ");
    }
    if (StringUtils.isNotBlank(queryStr)) {
      hql.append(" and instr(upper(nvl(t.zhName || t.enName, '')),:queryStr)>0 ");
    }
    hql.append(" group by t.nature order by character");
    Query qry = super.createQuery(hql.toString());
    if (regionIds != null && regionIds != -1) {
      qry.setParameter("regionIds", regionIds);
    }
    if (insCharacter != null) {
      qry.setParameter("insCharacter", insCharacter);
    }
    if (StringUtils.isNotBlank(queryStr)) {
      qry.setParameter("queryStr", queryStr.trim().toUpperCase());
    }
    return qry.list();
  }

  /**
   * 将机构按地区（region_id）分组统计
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> countInsByRegion(Long regionIds, Long insCharacter, String queryStr) {
    StringBuffer hql = new StringBuffer(
        "select new Map(nvl(t.regionId, -1) as regionId, count(1) as insCount) from Institution t where t.status in (2,3) and t.enabled = 1");
    if (regionIds != null) {
      hql.append(" and t.regionId = :regionIds ");
    }
    if (insCharacter != null) {
      hql.append(" and t.nature = :insCharacter ");
    }
    if (StringUtils.isNotBlank(queryStr)) {
      hql.append(" and instr(upper(nvl(t.zhName || t.enName, '')),:queryStr)>0 ");
    }
    hql.append(" group by t.regionId order by insCount desc");
    Query qry = super.createQuery(hql.toString());
    if (regionIds != null) {
      qry.setParameter("regionIds", regionIds);
    }
    if (insCharacter != null) {
      qry.setParameter("insCharacter", insCharacter);
    }
    if (StringUtils.isNotBlank(queryStr)) {
      qry.setParameter("queryStr", queryStr.trim().toUpperCase());
    }
    qry.setMaxResults(4);
    return qry.list();
  }

  public Long getInsNatureByInsId(Long insId) {
    String hql = "select t.nature from Institution t where id=:insId";
    return (Long) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public Institution lookUpByName(String name) {
    if (StringUtils.isBlank(name))
      return null;
    name = name.trim().toLowerCase();
    if (name.length() > 300) {
      name = name.substring(0, 300);
    }
    String hql = "from Institution t where lower(t.zhName)=:name or lower(t.enName)=:name order by t.enabled desc";
    Query query = super.createQuery(hql).setParameter("name", name);
    List<Institution> list = query.list();
    return CollectionUtils.isEmpty(list) ? null : list.get(0);
  }

  public List<Institution> getCollegeInstitution(String startWith, int size) {

    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    // 判断是否是非英文
    if (isChinese) {
      hql = "from Institution t where lower(t.zhName) like ? ";

    } else {
      hql = "from Institution t where lower(t.enName) like ? ";

    }

    hql = hql + "and t.enabled=1 and t.nature=1";

    if (isChinese) {
      hql = hql + " order by length(trim(lower(t.zhName))) asc";
    } else {
      hql = hql + " order by length(trim(lower(t.enName))) asc";
    }

    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);

    List<Institution> list = query.list();
    return list;
  }
}
