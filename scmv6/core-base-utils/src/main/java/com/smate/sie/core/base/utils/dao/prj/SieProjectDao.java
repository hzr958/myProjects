package com.smate.sie.core.base.utils.dao.prj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.prj.SieProject;

/**
 * 项目 Dao
 * 
 * @author hd
 *
 */
@Repository
public class SieProjectDao extends SieHibernateDao<SieProject, Long> {

  /**
   * 获取机构项目总数
   * 
   * @param insId
   * @return
   */
  public Long getProjectTotalNumByInsId(Long insId) {
    String hql = "select count(id) from SieProject where status = 0 and insId = ? ";
    return super.findUnique(hql, insId);
  }


  @SuppressWarnings("unchecked")
  public Page<SieProject> getProjectInfo(Map<String, Object> paramsMap, Page<SieProject> page) {
    String orderField = ObjectUtils.toString(paramsMap.get("orderField"));
    String orderType = ObjectUtils.toString(paramsMap.get("orderType"));
    String listHql = "select t ";
    String countHql = "select count(t.id) ";
    StringBuilder orderHql = new StringBuilder();
    orderHql.append("order by ");
    StringBuilder hql = new StringBuilder();
    hql.append(" from SieProject t where t.insId = ? ");
    List<Object> params = new ArrayList<Object>();
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    params.add(insId);
    if (StringUtils.isNotBlank(orderField)) {
      if ("startDate".equalsIgnoreCase(orderField)) {
        orderHql.append("t.createDate ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.id desc");
      } else if ("statYear".equalsIgnoreCase(orderField)) {
        orderHql.append("t.statYear ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.id desc");
      } else {
        orderHql.append("t.statYear nulls last,t.id desc");
      }
    } else {
      orderHql.append("t.statYear nulls last,t.id desc");
    }

    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      String title = paramsMap.get("title").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.zhTitle),?)>0");// 项目名称
      params.add(title);
    }
    // 根据名字查询psn_ins表
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<SieProject> getListByInsId(Long insId) {
    String hql = " from SieProject t where t.insId= ? order by id ";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 获取随机的maxSize条数据
   */
  @SuppressWarnings("unchecked")
  public List<SieProject> getListByInsIdOrderRandom(Long insId, int maxSize) {
    String sql =
        "select t.* from project t where t.is_public = 1 and t.status=0 and t.ins_id=:insId order by dbms_random.value";
    Query query = super.getSession().createSQLQuery(sql).addEntity(SieProject.class);
    query.setParameter("insId", insId);
    query.setFirstResult(0);
    query.setMaxResults(maxSize);
    return query.list();
  }

  /**
   * 批量获取
   * 
   * @param insId
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SieProject> findPrjByInsId(Long insId, Page<SieProject> page) {
    String hql = "from SieProject where insId =? order by id asc";
    Object[] objects = new Object[] {insId};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SieProject> result = q.list();
    page.setResult(result);
    return page;
  }

  /**
   * 联表(classSimpleName对应的表) 批量获取
   * 
   * @param insId
   * @param page
   * @param types
   * @param endTime
   * @param beginTime
   * @param classSimpleName
   * @return
   */
  public Page<SieProject> findProjectByInsIdWithBhX(Long insId, Page<SieProject> page, String types, Date beginTime,
      Date endTime, String classSimpleName) {
    StringBuilder listhql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    StringBuilder publicHql = new StringBuilder();

    publicHql.append("from SieProject t, " + classSimpleName + " r ");
    publicHql.append("where t.insId =? and t.id=r.keyId ");
    publicHql.append("and r.type in (" + types + ") ");
    publicHql.append("and r.creDate >=? and r.creDate<? ");


    listhql.append("select distinct new SieProject(t.id, t.zhTitle, t.enTitle) ");
    listhql.append(publicHql.toString());
    listhql.append("order by t.id asc ");

    countHql.append("select count(distinct t.id) ");
    countHql.append(publicHql.toString());

    Object[] objects = new Object[] {insId, beginTime, endTime};
    Query q = createQuery(listhql.toString(), objects);
    if (page.isAutoCount()) {
      long totalCount = findUnique(countHql.toString(), objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SieProject> result = q.list();
    page.setResult(result);
    return page;
  }

}


