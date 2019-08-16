package com.smate.center.open.dao.iris;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.PublicationRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 合肥工业大学成果DAO.
 * 
 * @author xys
 *
 */
@Repository
public class HfutPublicationDao extends RolHibernateDao<PublicationRol, Long> {

  @SuppressWarnings("unchecked")
  public Page<PublicationRol> getPubs(Map<String, Object> paramsMap, Page<PublicationRol> page) {
    String orderField = ObjectUtils.toString(paramsMap.get("orderField"));
    String orderType = ObjectUtils.toString(paramsMap.get("orderType"));
    String listHql = "select t ";
    String countHql = "select count(t.id) ";
    StringBuilder orderHql = new StringBuilder();
    orderHql.append("order by ");
    StringBuilder hql = new StringBuilder();
    hql.append(" from PublicationRol t where t.insId=1392 and t.status=2");
    List<Object> params = new ArrayList<Object>();
    hql.append(" and t.typeId=?");
    Integer typeId = Integer.valueOf(paramsMap.get("typeId").toString());
    params.add(typeId);
    if (StringUtils.isNotBlank(orderField)) {
      if ("title".equalsIgnoreCase(orderField)) {
        orderHql.append("nlssort(nvl(t.zhTitle,t.enTitle), 'NLS_SORT=SCHINESE_PINYIN_M') ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "");
      } else if ("citedTimes".equalsIgnoreCase(orderField)) {
        orderHql.append("nvl(t.citedTimes,0) ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
      } else if ("updateDate".equalsIgnoreCase(orderField)) {
        orderHql.append("t.updateDate ");
        orderHql.append(StringUtils.isNotBlank(orderType) ? orderType : "desc");
      } else {
        orderHql.append("t.publishYear desc");
      }
    } else {
      orderHql.append("t.publishYear desc");
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("title")))) {
      String title = paramsMap.get("title").toString().toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(t.zhTitle),?)>0");// 中文标题
      params.add(title);
      hql.append(" or instr(upper(t.enTitle),?)>0");// 英文标题
      params.add(title);
      hql.append(" ) ");
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("authorName")))) {
      String authorName = paramsMap.get("authorName").toString().toUpperCase().trim();
      hql.append(" and instr(upper(t.authorNames),?)>0");// 作者名
      params.add(authorName);
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(paramsMap.get("publishYear")))) {
      Integer publishYear = Integer.valueOf(paramsMap.get("publishYear").toString());
      hql.append(" and t.publishYear=?");
      params.add(publishYear);
    }

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params.toArray());
    if (paramsMap.get("all") == null || !"1".equals(paramsMap.get("all").toString())) {
      queryResult.setFirstResult(page.getFirst() - 1);
      queryResult.setMaxResults(page.getPageSize());
    }
    page.setResult(queryResult.list());
    return page;
  }
}
