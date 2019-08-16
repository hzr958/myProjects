package com.smate.sie.center.open.dao.dept;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.center.open.model.dept.SiePsnIns;
import com.smate.sie.center.open.model.dept.SiePsnInsPk;

/**
 * 单位人员DAO.
 * 
 * @author xys
 *
 */
@Repository
public class Sie6InsPsnDao extends SieHibernateDao<SiePsnIns, SiePsnInsPk> {

  /**
   * 获取单位人员信息.
   * 
   * @param paramsMap
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SiePsnIns> getInsPsns(Map<String, Object> paramsMap, Page<SiePsnIns> page) {
    String listHql = "select t ";
    String countHql = "select count(t.pk.psnId) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from SiePsnIns t where t.pk.insId=? and t.status=1");
    List<Object> params = new ArrayList<Object>();
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    params.add(insId);

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<SiePsnIns> getSiePsnInsByPsnIns(Long psnIns) {
    String hql = "select t from SiePsnIns t where t.pk.psnId=:pins ";
    return super.createQuery(hql).setParameter("pins", psnIns).list();
  }

  public List<Map<String, Object>> getInsBypsnId(List<Long> psnIds) {

    List<Map<String, Object>> resultList = null;
    if (CollectionUtils.isNotEmpty(psnIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select new Map(nvl(t.pk.insId,99) as insId,count(1) as countnum) from SiePsnIns t where 1=1 ");
      if (psnIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(psnIds, 800, "t.pk.psnId");
        sql.append(sqlConditions);
        sql.append("group by t.pk.insId order by countnum desc");
        resultList = super.createQuery(sql.toString()).list();
      } else {
        resultList = super.createQuery(
            sql.append(" and t.pk.psnId in (:psnIds) group by t.pk.insId order by countnum desc").toString())
                .setParameterList("psnIds", psnIds).list();
      }
    }
    return resultList;

  }

  public List<Long> getInsIdBypsnId(List<Long> psnIds) {

    List<Long> resultList = null;
    if (CollectionUtils.isNotEmpty(psnIds)) {
      StringBuffer sql = new StringBuffer();
      sql.append(
          "select distinct t.pk.insId from SiePsnIns t where exists (select 1 from Sie6Institution d where t.pk.insId =d.id) ");
      if (psnIds.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(psnIds, 800, "t.pk.psnId");
        sql.append(sqlConditions);
        resultList = super.createQuery(sql.toString()).list();
      } else {
        resultList = super.createQuery(sql.append(" and t.pk.psnId in (:psnIds)").toString())
            .setParameterList("psnIds", psnIds).list();
      }
    }
    return resultList;

  }

}
