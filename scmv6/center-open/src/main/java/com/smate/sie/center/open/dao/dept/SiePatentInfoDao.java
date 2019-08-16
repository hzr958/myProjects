package com.smate.sie.center.open.dao.dept;

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
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.SiePatent;

@Repository
public class SiePatentInfoDao extends SieHibernateDao<SiePatent, Long> {
  @SuppressWarnings("unchecked")
  public Page<SiePatent> getPubs(Map<String, Object> paramsMap, Page<SiePatent> page) {
    String orderField = ObjectUtils.toString(paramsMap.get("orderField"));
    String orderType = ObjectUtils.toString(paramsMap.get("orderType"));
    String listHql = "select t ";
    String countHql = "select count(t.id) ";
    StringBuilder orderHql = new StringBuilder();
    orderHql.append("order by ");
    StringBuilder hql = new StringBuilder();
    hql.append(" from SiePatent t where t.status=0 and  t.insId= ? ");
    List<Object> params = new ArrayList<Object>();
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    params.add(insId);

    if (StringUtils.isNotBlank(orderField)) {
      if ("title".equalsIgnoreCase(orderField)) {
        orderHql.append("nlssort(nvl(t.zhTitle,t.enTitle), 'NLS_SORT=SCHINESE_PINYIN_M') ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "");
        orderHql.append(", t.id desc");
      } else if ("updateDate".equalsIgnoreCase(orderField)) {
        orderHql.append("t.updateDt ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.id desc");
      } else {
        orderHql.append("t.applyYear nulls last,t.id desc ");
      }
    } else {
      orderHql.append("t.applyYear nulls last,t.id desc");
    }

    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      String title = paramsMap.get("title").toString().toUpperCase().trim();
      hql.append(" and ( instr(upper(t.zhTitle),?)>0 or instr(upper(t.enTitle),?)>0 )");// 专利名称
      params.add(title);
      params.add(title);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("author")))) {
      String author = paramsMap.get("author").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.authors),?)>0");// 发明人
      params.add(author);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("apply_no")))) {
      String applyNo = paramsMap.get("apply_no").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.applyNo),?)>0");// 申请号
      params.add(applyNo);
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

  /**
   * 根据更新时间查询专利数据且没有排序
   * 
   * @param paramsMap
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SiePatent> getPatsNoOrderByDate(Map<String, Object> paramsMap, Page<SiePatent> page) {
    String listHql = "select t ";
    String countHql = "select count(t.id) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from SiePatent t where t.status=0 and t.insId= ? ");
    List<Object> params = new ArrayList<Object>();
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    params.add(insId);

    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("startUpdateDate")))) {
      Date startDate = DateUtils.parseStringToDate(ObjectUtils.toString(paramsMap.get("startUpdateDate")));
      if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("endUpdateDate")))) {
        Date endDate = DateUtils.parseStringToDate(ObjectUtils.toString(paramsMap.get("endUpdateDate")));
        hql.append(" and t.updateDt >= ? and t.updateDt <= ?");
        params.add(startDate);
        params.add(endDate);
      } else {
        hql.append(" and t.updateDt >= ?");
        params.add(startDate);
      }
    }

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
  public Page<SiePatent> getPatsByMap(Map<String, Object> paramsMap, Page<SiePatent> page) {
    String listHql = "select t ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from SiePatent t where t.status=0 and t.isPublic=1 and t.insId =:insId ");
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    // 排序
    String orderField = ObjectUtils.toString(paramsMap.get("orderField"));
    String orderType = ObjectUtils.toString(paramsMap.get("orderType"));
    StringBuilder orderHql = new StringBuilder();
    orderHql.append("order by ");
    if (StringUtils.isNotBlank(orderField)) {
      if ("title".equalsIgnoreCase(orderField)) {
        orderHql.append("nlssort(nvl(t.zhTitle,t.enTitle), 'NLS_SORT=SCHINESE_PINYIN_M') ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "");
        orderHql.append(", t.id desc");
      } else if ("updateDate".equalsIgnoreCase(orderField)) {
        orderHql.append("t.updateDt ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.id desc");
      } else {
        orderHql.append("t.applyYear nulls last,t.id desc ");
      }
    } else {
      orderHql.append("t.applyYear nulls last,t.id desc");
    }
    // 除了insid的其他参数
    String title = null, author = null;
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      title = paramsMap.get("title").toString().toUpperCase().trim();
      hql.append(" and ( instr(upper(t.zhTitle),:title)>0 or instr(upper(t.enTitle),:title)>0 )");// 专利名称
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("author")))) {
      author = paramsMap.get("author").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.authors),:author)>0");// 发明人
    }
    List<Long> patArray = new ArrayList<Long>();
    if (!org.springframework.util.ObjectUtils.isEmpty(paramsMap.get("patIds"))) {
      hql.append(" and t.patId in (:patId) ");
      for (String patId : paramsMap.get("patIds").toString().split(",")) {
        patArray.add(Long.valueOf(patId));
      }
    }

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql).setParameter("insId", insId);
    if (patArray.size() != 0) {
      queryResult.setParameterList("patId", patArray);
    }
    if (title != null) {
      queryResult.setParameter("title", title);
    }
    if (author != null) {
      queryResult.setParameter("author", author);
    }
    // 记录数
    int totalCount = queryResult.list().size();
    page.setTotalCount((long) totalCount);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }
}
