package com.smate.sie.center.open.dao.dept;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.center.open.model.dept.IsisProject;

@Repository
public class SieProjectInfoDao extends SieHibernateDao<IsisProject, Long> {

  @SuppressWarnings("unchecked")
  public Page<IsisProject> getProjectInfo(Map<String, Object> paramsMap, Page<IsisProject> page) {
    String orderField = ObjectUtils.toString(paramsMap.get("orderField"));
    String orderType = ObjectUtils.toString(paramsMap.get("orderType"));
    String listHql = "select t ";
    String countHql = "select count(t.id) ";
    StringBuilder orderHql = new StringBuilder();
    orderHql.append("order by ");
    StringBuilder hql = new StringBuilder();
    hql.append(" from IsisProject t where t.insId = ? ");
    List<Object> params = new ArrayList<Object>();
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    params.add(insId);

    if (StringUtils.isNotBlank(orderField)) {
      if ("startDate".equalsIgnoreCase(orderField)) {
        orderHql.append("t.startDate ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.prjCode desc");
      } else if ("statYear".equalsIgnoreCase(orderField)) {
        orderHql.append("t.statYear ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.prjCode desc");
      } else {
        orderHql.append("t.statYear nulls last,t.prjCode desc");
      }
    } else {
      orderHql.append("t.statYear nulls last,t.prjCode desc");
    }

    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      String title = paramsMap.get("title").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.zhTitle),?)>0");// 项目名称
      params.add(title);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("author")))) {
      String author = paramsMap.get("author").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.psnName),?)>0");// 负责人
      params.add(author);
    }
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

}
