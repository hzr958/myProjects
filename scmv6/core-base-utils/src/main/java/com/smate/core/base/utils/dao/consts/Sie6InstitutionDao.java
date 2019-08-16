package com.smate.core.base.utils.dao.consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * SIE 机构Dao
 * 
 * @author hd
 *
 */
@Repository
public class Sie6InstitutionDao extends SieHibernateDao<Sie6Institution, Long> {

  /**
   * 通过id获取单位列表
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Sie6Institution> getInsByIds(List<Long> ids) {
    String hql = "from Sie6Institution t where t.id in(:insIds)";
    return super.createQuery(hql).setParameterList("insIds", ids).list();
  }

  /**
   * 通过单位名称查找单位(只找被逻辑删除的单位)
   * 
   * @param insName
   * @return
   * @throws DaoException
   */
  public Sie6Institution findInstitutionByInsName(String insName) {
    String hql = "from Sie6Institution t where trim(t.zhName)=trim(?) and t.status <> 9";// or trim(t.enName)=trim(?))
    return this.findUnique(hql, insName);// insName
  }

  /**
   * 通过单位名称查找单位(不考虑逻辑删除)
   * 
   * @param insName
   * @return
   * @throws DaoException
   */
  public Sie6Institution findInsByInsName(String insName) {
    String hql = "from Sie6Institution t where trim(t.zhName)=trim(?)";
    return this.findUnique(hql, insName);
  }

  /**
   * 获取自动匹配的单位列表.
   * 
   * @param pos
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Sie6Institution> getInsLike(String insName) {
    insName = insName.toLowerCase();
    List<Object> params = new ArrayList<Object>();
    String hql = "from Sie6Institution t where  lower(t.enName) like ? or lower(t.zhName) like ? ";
    params.add("%" + insName + "%");
    params.add("%" + insName + "%");
    List<Sie6Institution> list = super.createQuery(hql, params.toArray()).list();
    return list;
  }

  /**
   * 根据名称获取匹配的单位的ID数组.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getInsIdByLikeName(String insName) {
    insName = insName.toLowerCase();
    List<Object> params = new ArrayList<Object>();
    String hql =
        "SELECT t.id from Sie6Institution t where t.status=2 and (lower(t.enName) like lower(?) or lower(t.zhName) like lower(?) ) order by t.id asc";
    params.add("%" + insName + "%");
    params.add("%" + insName + "%");
    List<Long> list = super.createQuery(hql, params.toArray()).setMaxResults(10).list();
    return list;
  }



  /**
   * 更新单位信息
   * 
   * @param objects
   * @throws DaoException
   */
  public void updateIns(Object[] objects) {
    String sql = "update Sie6Institution t set t.tel=? ,t.regionId=?,t.zhAddress=?,t.url=? where t.id=?";
    super.createQuery(sql, objects).executeUpdate();
  }

  /**
   * 通过单位中文名称查找单位
   * 
   * @param zhName
   * @return
   */
  public Sie6Institution findInstitutionByZhName(String zhName) {
    String hql = "from Sie6Institution t where trim(t.zhName)=trim(?) ";
    return this.findUnique(hql, zhName);
  }

  /**
   * 获取下一个insId
   * 
   * @return
   */
  public Long findNewInsId() {
    String sql = "select seq_institution.nextval from dual";
    return super.queryForLong(sql);
  }

  public void deleteByInsId(Long mergeid) {
    String hql = "delete from Sie6Institution t where t.id= ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Sie6Institution> getListByInsId(Long insId) {
    String hql = " from Sie6Institution t where t.id= ?";
    return super.createQuery(hql, insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Sie6Institution> getAllByStatus(Page<Sie6Institution> page) {
    String hql = " from Sie6Institution t where t.status =2 order by t.id ";
    return super.createQuery(hql).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getAllInsNum() {
    String hql = "select distinct t.id from Sie6Institution t";
    return super.createQuery(hql).list();
  }

  /**
   * 通过人员的名字来获取人员的主键的ID
   */
  @SuppressWarnings("unchecked")
  public Page<Sie6Institution> getInsIdByStatus(Page<Sie6Institution> page) {
    String hql = "select new Sie6Institution(t.id) from Sie6Institution t where t.status in (2,3,8)";
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<Sie6Institution> result = q.list();
    page.setResult(result);
    return page;
  }

  /*
   * 通过insId获取单位的AutoComplete的值
   */
  public Integer getAutoCompleteByInsId(Long id) {
    String sql = "select auto_complete from institution t where t.ins_id = :insId";
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter("insId", id);
    return NumberUtils.parseInt(query.uniqueResult().toString());
  }


  /**
   * 查询单位信息，根据三个参数作为查询条件（单位id,单位名称，单位地域编号）
   * 
   * @author xr
   * @param orgName
   * @param regionId
   * @param page
   * @return
   * 
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public List<Sie6Institution> getBeanByInsIdAndLikeNameAndCompareRegionId(String orgGuid, String orgName,
      Long regionId, Page<Sie6Institution> page) {
    List<Object> params = new ArrayList<Object>();
    String countHql = "select count(0)";
    String hql = " from Sie6Institution t where t.status = 2";
    boolean orgGuidExist = StringUtils.isNotBlank(orgGuid), orgNameExist = StringUtils.isNotBlank(orgName),
        regionIdExist = NumberUtils.isNotNullOrZero(regionId);
    if (orgGuidExist) {
      hql += " and ? in (select g.guid from SieInsGuid g where g.insId = t.id)";
      params.add(orgGuid);
    }
    if (orgNameExist) {
      hql += " and (lower(t.enName) like lower(?) or lower(t.zhName) like lower(?))";
      params.add("%" + orgName + "%");
      params.add("%" + orgName + "%");
    }
    if (regionIdExist) {
      hql += " and t.id in  (select r.insId from SieInsRegion r"
          + " where (r.prvId = ? or r.cyId = ? or r.countryId = ? or" + "  r.disId = ?))";
      params.add(regionId);
      params.add(regionId);
      params.add(regionId);
      params.add(regionId);
    }
    String orderHql = " order by t.id asc";

    logger.debug(hql + orderHql);
    System.out.println(hql + orderHql);
    String maxsize = super.createQuery(countHql + hql, params.toArray()).uniqueResult().toString();
    page.setTotalCount(Integer.valueOf(maxsize));

    List<Sie6Institution> list = super.createQuery(hql + orderHql, params.toArray()).setMaxResults(page.getPageSize())
        .setFirstResult(page.getFirst() - 1).list();
    return list;
  }

  /**
   * 查询单位信息，根据参数作为查询条件（机构guid，机构名称，机构地域编号，机构类型，所属行业）
   * 
   * @author xr
   * @param page
   * @param params
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Sie6Institution> getInsListByConditions(Map<String, Object> datas, Page<Sie6Institution> page) {
    List<Object> params = new ArrayList<Object>();
    String countHql = "select count(0)";
    StringBuilder hql = new StringBuilder();
    hql.append(" from Sie6Institution t where t.status = 2");
    // 机构guid
    if ((boolean) datas.get("orgGuidExist")) {
      hql.append(" and ? in (select g.guid from SieInsGuid g where g.insId = t.id)");
      params.add(datas.get("orgGuid").toString());
    }
    // 机构名称
    if ((boolean) datas.get("orgNameExist")) {
      hql.append(" and (lower(t.enName) like lower(?) or lower(t.zhName) like lower(?))");
      params.add("%" + datas.get("orgName").toString() + "%");
      params.add("%" + datas.get("orgName").toString() + "%");
    }
    // 机构类型
    if ((boolean) datas.get("naturesExist")) {
      String natureStr = datas.get("natureStr").toString();
      String[] natures = StringUtils.split(natureStr, ",|，");
      if (natures.length == 1) {
        hql.append(" and nature =");
      } else {
        hql.append(" and nature in (");
      }
      for (int i = 0; i < natures.length; i++) {
        hql.append(" ?");
        String nature = natures[i];
        params.add(NumberUtils.parseLong(nature));
        if (natures.length != i + 1 && natures.length > 1) {
          hql.append(" ,");
        }
      }
      if (natures.length != 1) {
        hql.append(" )");
      }
    }
    // 机构所属行业
    if ((boolean) datas.get("ecoIdsExist")) {
      String ecoIdStr = datas.get("ecoIdStr").toString();
      String[] ecoIds = StringUtils.split(ecoIdStr, ",|，");
      hql.append(" and t.id in (");
      hql.append(" select p.insId from Sie6InsDisciplineEconomic p where p.ecoCode");
      if (ecoIds.length != 1) {
        hql.append(" in (");
      } else {
        hql.append(" =");
      }
      for (int i = 0; i < ecoIds.length; i++) {
        hql.append(" ?");
        params.add(ecoIds[i]);
        if (ecoIds.length != i + 1 && ecoIds.length > 1) {
          hql.append(" ,");
        }
      }
      if (ecoIds.length != 1) {
        hql.append(" )");
      }
      hql.append(" )");
    }
    // 机构地区编号
    if ((boolean) datas.get("regionIdExist")) {
      String regionIdStr = datas.get("regionIdStr").toString();
      String[] regionIds = StringUtils.split(regionIdStr, ",|，");
      hql.append(" and t.id in (select r.insId from SieInsRegion r");
      // + " = ? or r.cyId = ? or r.countryId = ? or " + " r.disId = ?) ) ";
      String[] regs = {" where r.prvId", " or r.cyId", " or r.countryId", " or r.disId"};
      for (int j = 0; j < 4; j++) {
        hql.append(regs[j]);
        for (int i = 0; i < regionIds.length; i++) {
          if (i == 0) {
            if (regionIds.length == 1) {
              hql.append(" =");
            } else {
              hql.append(" in (");
            }
          }
          hql.append(" ?");
          params.add(NumberUtils.parseLong(regionIds[i]));
          if (regionIds.length != i + 1 && regionIds.length > 1) {
            hql.append(" ,");
          }
        }
        if (regionIds.length != 1) {
          hql.append(" )");
        }
      }
      hql.append(" )");
    }

    String orderHql = " order by t.id asc";

    logger.debug(hql + orderHql);
    System.out.println(hql + orderHql);
    String maxsize = super.createQuery(countHql + hql, params.toArray()).uniqueResult().toString();
    page.setTotalCount(Integer.valueOf(maxsize));

    List<Sie6Institution> list = super.createQuery(hql + orderHql, params.toArray()).setMaxResults(page.getPageSize())
        .setFirstResult(page.getFirst() - 1).list();
    return list;
  }

  /**
   * 查询所有机构所属行业类型的行业数 eco_id eco_name eco_count
   * 
   * @param params
   * @return
   */
  @SuppressWarnings({"deprecation", "unchecked"})
  public List<Map<String, Object>> getInsCountForEncode(Map<String, Object> params) {
    StringBuilder hqlBd = new StringBuilder();
    String selectSql = "select new Map(te.ecoCode as eco_id, te.ecoZhName as eco_name, count(t.id) as eco_count)";// 返回结果
    String fromSql = " from Sie6Institution t, Sie6InsDisciplineEconomic te";
    String addFrom1 = "";
    String addFrom4 = "";
    String whereSql = " where t.id = te.insId and t.status = 2";
    String addWh1 = "";
    String addWh2 = "";
    String addWh3 = "";
    String addWh4 = "";
    String addWh5 = "";

    String guid = "";
    String insName = "";

    if ((boolean) params.get("orgGuidExist")) {
      guid = params.get("orgGuid").toString();
    }
    if ((boolean) params.get("orgNameExist")) {
      insName = params.get("orgName").toString();
    }
    List<Long> natureIdList = (List<Long>) params.get("natureIdList");
    List<String> ecoIdList = (List<String>) params.get("ecoIdList");
    List<Long> regionIdList = (List<Long>) params.get("regionIdList");

    // 分组查询
    String groupAndOrder = " group by te.ecoCode, te.ecoZhName order by te.ecoCode";
    // 匹配机构guid
    if ((boolean) params.get("orgGuidExist")) {
      addFrom1 = ", InsGuid tg";
      addWh1 = " and t.id = tg.insId and tg.guid =:guid ";
    }
    // 匹配机构名称
    if ((boolean) params.get("orgNameExist")) {
      addWh2 = " and (instr(upper(t.zhName),:insName)>0 or instr(upper(t.enName),:insName)>0) ";
    }
    // 匹配机构类型
    if ((boolean) params.get("naturesExist")) {
      addWh3 = " and t.nature in(:natureIdList)";
    }
    // 匹配选中的省份
    if ((boolean) params.get("regionIdExist")) {
      addFrom4 = ", SieInsRegion tr";
      addWh4 = " and t.id = tr.insId and (tr.prvId in (:regionIdList) or tr.countryId in (:regionIdList)"
          + " or tr.cyId in (:regionIdList) or tr.disId in (:regionIdList))";
    }
    // 匹配机构所属行业
    if ((boolean) params.get("ecoIdsExist")) {
      addWh5 = " and t.id = te.insId and te.ecoCode in (:ecoIdList) ";
    }

    hqlBd.append(selectSql).append(fromSql).append(addFrom1).append(addFrom4).append(whereSql).append(addWh1)
        .append(addWh2).append(addWh3).append(addWh4).append(addWh5).append(groupAndOrder);

    System.out.println("查询所有机构所属行业类型的行业数:" + hqlBd);

    Query query = super.createQuery(hqlBd.toString());
    if ((boolean) params.get("orgGuidExist")) {
      query.setParameter("guid", guid);
    }
    if ((boolean) params.get("orgNameExist")) {
      insName = StringEscapeUtils.unescapeHtml4(insName).toUpperCase().trim();
      query.setParameter("insName", insName);
    }
    if ((boolean) params.get("naturesExist")) {
      query.setParameterList("natureIdList", natureIdList);
    }
    if ((boolean) params.get("regionIdExist")) {
      query.setParameterList("regionIdList", regionIdList);
    }
    if ((boolean) params.get("ecoIdsExist")) {
      query.setParameterList("ecoIdList", ecoIdList);
    }
    return query.list();
  }

  /**
   * 查询所有机构类型的机构数 nature_id nature_name nature_count
   * 
   * @param params
   * @return
   */
  @SuppressWarnings({"unchecked", "deprecation"})
  public List<Map<String, Object>> getInsCountForNature(Map<String, Object> params) {
    StringBuilder hqlBd = new StringBuilder();
    String selectSql = "select new Map(tt.nature as nature_id, tt.zhName as nature_name, count(t.id) as nature_count)";// 返回结果
    String fromSql = " from Sie6Institution t, SieConstInsType tt";
    String addFrom1 = "";
    String addFrom4 = "";
    String addFrom5 = "";
    String whereSql = " where t.nature = tt.nature and t.status = 2";
    String addWh1 = "";
    String addWh2 = "";
    String addWh3 = "";
    String addWh4 = "";
    String addWh5 = "";

    String guid = "";
    String insName = "";

    if ((boolean) params.get("orgGuidExist")) {
      guid = params.get("orgGuid").toString();
    }
    if ((boolean) params.get("orgNameExist")) {
      insName = params.get("orgName").toString();
    }
    List<Long> natureIdList = (List<Long>) params.get("natureIdList");
    List<String> ecoIdList = (List<String>) params.get("ecoIdList");
    List<Long> regionIdList = (List<Long>) params.get("regionIdList");

    // 分组查询
    String groupAndOrder = " group by tt.nature, tt.zhName order by tt.nature";
    // 匹配机构guid
    if ((boolean) params.get("orgGuidExist")) {
      addFrom1 = ", InsGuid tg";
      addWh1 = " and t.id = tg.insId and tg.guid =:guid ";
    }
    // 匹配机构名称
    if ((boolean) params.get("orgNameExist")) {
      addWh2 = " and (instr(upper(t.zhName),:insName)>0 or instr(upper(t.enName),:insName)>0) ";
    }
    // 匹配机构类型
    if ((boolean) params.get("naturesExist")) {
      addWh3 = " and t.nature in(:natureIdList)";
    }
    // 匹配选中的省份
    if ((boolean) params.get("regionIdExist")) {
      addFrom4 = ", SieInsRegion tr";
      addWh4 = " and t.id = tr.insId and (tr.prvId in (:regionIdList) or tr.countryId in (:regionIdList)"
          + " or tr.cyId in (:regionIdList) or tr.disId in (:regionIdList))";
    }
    // 匹配机构所属行业
    if ((boolean) params.get("ecoIdsExist")) {
      addFrom5 = ", Sie6InsDisciplineEconomic te";
      addWh5 = " and t.id = te.insId and te.ecoCode in (:ecoIdList) ";
    }

    hqlBd.append(selectSql).append(fromSql).append(addFrom1).append(addFrom4).append(addFrom5).append(whereSql)
        .append(addWh1).append(addWh2).append(addWh3).append(addWh4).append(addWh5).append(groupAndOrder);

    Query query = super.createQuery(hqlBd.toString());
    if ((boolean) params.get("orgGuidExist")) {
      query.setParameter("guid", guid);
    }
    if ((boolean) params.get("orgNameExist")) {
      insName = StringEscapeUtils.unescapeHtml4(insName).toUpperCase().trim();
      query.setParameter("insName", insName);
    }
    if ((boolean) params.get("naturesExist")) {
      query.setParameterList("natureIdList", natureIdList);
    }
    if ((boolean) params.get("regionIdExist")) {
      query.setParameterList("regionIdList", regionIdList);
    }
    if ((boolean) params.get("ecoIdsExist")) {
      query.setParameterList("ecoIdList", ecoIdList);
    }
    return query.list();
  }

  /**
   * 查询所有机构所属省份的地区数 region_id region_name region_count
   * 
   * @param params
   * @return
   */
  @SuppressWarnings({"unchecked", "deprecation"})
  public List<Map<String, Object>> getInsCountForRegion(Map<String, Object> params) {
    StringBuilder hqlBd = new StringBuilder();
    hqlBd.append("select new Map(trr.id as region_id , trr.zhName as region_name, count(tr.insId) as region_count ) ");
    hqlBd.append(" from Sie6Institution t, SieInsRegion tr, SieConstRegion trr ");
    if ((boolean) params.get("orgGuidExist")) {
      hqlBd.append(" , InsGuid tg");
    }
    if ((boolean) params.get("ecoIdsExist")) {
      hqlBd.append(", Sie6InsDisciplineEconomic te");
    }
    hqlBd.append(" where t.id = tr.insId and tr.prvId is not null and tr.prvId = trr.id ");
    hqlBd.append(" and t.status = 2 ");

    String guid = "";
    String insName = "";

    if ((boolean) params.get("orgGuidExist")) {
      guid = params.get("orgGuid").toString();
    }
    if ((boolean) params.get("orgNameExist")) {
      insName = params.get("orgName").toString();
    }
    List<Long> natureIdList = (List<Long>) params.get("natureIdList");
    List<String> ecoIdList = (List<String>) params.get("ecoIdList");
    List<Long> regionIdList = (List<Long>) params.get("regionIdList");

    // 匹配机构guid
    if ((boolean) params.get("orgGuidExist")) {
      hqlBd.append(" and t.id = tg.insId and tg.guid =:guid ");
    }
    // 匹配机构名称
    if ((boolean) params.get("orgNameExist")) {
      hqlBd.append(" and (instr(upper(t.zhName),:insName)>0 or instr(upper(t.enName),:insName)>0) ");
    }
    // 匹配机构类型
    if ((boolean) params.get("naturesExist")) {
      hqlBd.append(" and t.nature in (:natureIdList)");
    }
    // 匹配选中的省份
    if ((boolean) params.get("regionIdExist")) {
      hqlBd.append(" and t.id = tr.insId and (tr.prvId in (:regionIdList) or tr.countryId in (:regionIdList)"
          + " or tr.cyId in (:regionIdList) or tr.disId in (:regionIdList))");
    }
    // 匹配机构所属行业
    if ((boolean) params.get("ecoIdsExist")) {
      hqlBd.append(" and t.id = te.insId and te.ecoCode in (:ecoIdList)");
    }
    // 排序
    hqlBd.append(" group by trr.id, trr.zhName order by trr.id");

    Query query = super.createQuery(hqlBd.toString());
    if ((boolean) params.get("orgGuidExist")) {
      query.setParameter("guid", guid);
    }
    if ((boolean) params.get("orgNameExist")) {
      insName = StringEscapeUtils.unescapeHtml4(insName).toUpperCase().trim();
      query.setParameter("insName", insName);
    }
    if ((boolean) params.get("naturesExist")) {
      query.setParameterList("natureIdList", natureIdList);
    }
    if ((boolean) params.get("regionIdExist")) {
      query.setParameterList("regionIdList", regionIdList);
    }
    if ((boolean) params.get("ecoIdsExist")) {
      query.setParameterList("ecoIdList", ecoIdList);
    }
    return query.list();
  }


}
