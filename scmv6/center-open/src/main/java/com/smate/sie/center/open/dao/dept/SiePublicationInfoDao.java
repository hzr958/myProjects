package com.smate.sie.center.open.dao.dept;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.SiePublication;

@Repository
public class SiePublicationInfoDao extends SieHibernateDao<SiePublication, Long> {
  @SuppressWarnings("unchecked")
  public Page<SiePublication> getPubs(Map<String, Object> paramsMap, Page<SiePublication> page) {
    String orderField = ObjectUtils.toString(paramsMap.get("orderField"));
    String orderType = ObjectUtils.toString(paramsMap.get("orderType"));
    String listHql = "select t ";
    String countHql = "select count(t.pubId) ";
    StringBuilder orderHql = new StringBuilder();
    orderHql.append("order by ");
    StringBuilder hql = new StringBuilder();
    hql.append(" from SiePublication t where 1=1 and  t.status=0  ");
    Integer pubType = Integer.valueOf(paramsMap.get("pubType").toString());
    switch (pubType) {
      case 0:
        hql.append("and t.pubType in (3,4) ");
        break;
      case 3:
        hql.append("and t.pubType = 3 ");
        break;
      case 4:
        hql.append("and t.pubType = 4 ");
        break;
      default:
        hql.append("and 1=2"); // 输入其他的值则不返回数据
    }
    if (StringUtils.isNotBlank(orderField)) {
      if ("title".equalsIgnoreCase(orderField)) {
        orderHql.append("nlssort(nvl(t.zhTitle,t.enTitle), 'NLS_SORT=SCHINESE_PINYIN_M') ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "");
        orderHql.append(", t.id desc");
      } else if ("updateDate".equalsIgnoreCase(orderField)) {
        orderHql.append("t.updateDate ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
        orderHql.append(" nulls last,t.id desc");
      } else {
        orderHql.append("t.publishYear nulls last,t.id desc");
      }
    } else {
      orderHql.append("t.publishYear nulls last,t.id desc");
    }

    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    hql.append(" and t.insId= :insid ");
    // 标题
    String title = null;
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      title = paramsMap.get("title").toString().toUpperCase().trim();
      hql.append(" and (instr(upper(t.zhTitle),:title)>0 or instr(upper(t.enTitle),:title)>0 )");
    }
    // 作者
    String author = null;
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("author")))) {
      author = "%" + paramsMap.get("author").toString().toLowerCase().trim().replaceAll(",|\\.|;|，|；|\\s", "") + "%";
      hql.append(" and lower(regexp_replace(t.authorNames, ',|\\.|;|，|；|\\s|<strong>|</strong>|-', '')) like :author ");
    }
    int publishYear = 0;
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("publishYear")))) {
      String tempYear = paramsMap.get("publishYear").toString().trim().replace(" ", "");
      paramsMap.replace("publishYear", tempYear);
      publishYear = Integer.valueOf(paramsMap.get("publishYear").toString().trim());
      hql.append(" and t.publishYear = :publishyear ");// 年份
    }

    ArrayList<Long> pubids = (ArrayList<Long>) paramsMap.get("pubids");
    if (pubids.size() > 0) {
      hql.append(" and t.pubId in (:pubids) ");
    }
    // 记录数
    Query queryCountResult = super.createQuery(countHql + hql).setParameter("insid", insId);
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      queryCountResult.setParameter("title", title);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("author")))) {
      queryCountResult.setParameter("author", author);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("publishYear")))) {
      queryCountResult.setParameter("publishyear", publishYear);
    }
    if (pubids.size() > 0) {
      queryCountResult.setParameterList("pubids", pubids);
    }
    page.setTotalCount((Long) queryCountResult.uniqueResult());
    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql).setParameter("insid", insId);
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      queryResult.setParameter("title", title);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("author")))) {
      queryResult.setParameter("author", author);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("publishYear")))) {
      queryResult.setParameter("publishyear", publishYear);
    }
    if (pubids.size() > 0) {
      queryResult.setParameterList("pubids", pubids);
    }
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<SiePublication> getPubsNoOrderByDate(Map<String, Object> paramsMap, Page<SiePublication> page) {
    String listHql = "select t ";
    String countHql = "select count(t.pubId) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from SiePublication t where 1=1 and t.status=0 ");
    Long insId = Long.valueOf(paramsMap.get("insId").toString());
    hql.append(" and t.insId= :insid ");
    Date startDate = null;
    Date endDate = null;
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("startUpdateDate")))) {
      startDate = DateUtils.parseStringToDate(ObjectUtils.toString(paramsMap.get("startUpdateDate")));
      if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("endUpdateDate")))) {
        endDate = DateUtils.parseStringToDate(ObjectUtils.toString(paramsMap.get("endUpdateDate")));
        hql.append(" and t.updateDate >= (:startDate) and t.updateDate <= (:endDate)");
      } else {
        hql.append(" and t.updateDate >= (:startDate)");
      }
    }

    // 记录数
    Query queryCountResult = super.createQuery(countHql + hql).setParameter("insid", insId);
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("startUpdateDate")))) {
      queryCountResult.setParameter("startDate", startDate);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("endUpdateDate")))) {
      queryCountResult.setParameter("endDate", endDate);
    }
    page.setTotalCount((Long) queryCountResult.uniqueResult());
    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql).setParameter("insid", insId);
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("startUpdateDate")))) {
      queryResult.setParameter("startDate", startDate);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("endUpdateDate")))) {
      queryResult.setParameter("endDate", endDate);
    }
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

}
