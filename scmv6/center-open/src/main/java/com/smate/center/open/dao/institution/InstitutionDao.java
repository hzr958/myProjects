package com.smate.center.open.dao.institution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 单位数据层接口.
 * 
 * @author tsz
 * 
 */
@Repository
public class InstitutionDao extends HibernateDao<Institution, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

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

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> searchInstitution(String queryStr, List<Long> regionIds) {
    String listHql =
        "select new map(id as insId, zhName as zhName, enName as enName,zhAddress as zhAddress,enAddress as enAddress,url as url) ";
    StringBuffer queryHql = new StringBuffer(" from Institution t where t.enabled = 1 ");
    if (StringUtils.isNotBlank(queryStr)) {
      queryHql.append(" and instr(upper(nvl(t.zhName || t.enName, '')),:queryStr)>0 ");
    }
    if (regionIds.size() > 0) {
      queryHql.append(" and t.regionId  in (:regionIds)");
    }
    Query listQry = super.createQuery(listHql + queryHql.toString());
    if (StringUtils.isNotBlank(queryStr)) {
      listQry.setParameter("queryStr", queryStr.trim().toUpperCase());
    }
    if (regionIds.size() > 0) {
      listQry.setParameterList("regionIds", regionIds);
    }
    List<Map<String, Object>> result = listQry.setMaxResults(3).list();
    return result;
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

  public Institution findByName(String name) {
    String hql = "from Institution where lower(enName) = lower(?) or lower(zhName) = lower(?) ";
    List<Institution> list = super.find(hql, name, name);
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过单位名获取对应的单位记录.
   * 
   * @param zhName
   * @param enName
   * @return List<Institution>
   * @throws DaoException
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

}
