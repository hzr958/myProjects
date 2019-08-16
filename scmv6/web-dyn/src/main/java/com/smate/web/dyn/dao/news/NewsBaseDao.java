package com.smate.web.dyn.dao.news;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.model.news.NewsBase;


/**
 * 新闻实体dao
 * 
 * @author ajb
 *
 */

@Repository
public class NewsBaseDao extends SnsHibernateDao<NewsBase, Long> {

  public void findNewsList(NewsForm form) {
    String countSql = "select count(1) ";
    String hql = "";
    if (form.getManager()) {
      hql = " from NewsBase t where t.status != 9 ";
    } else {
      hql = " from NewsBase t where t.status = 1 ";
    }
    String orderBy = buildOrderBy(form);
    Object count = this.createQuery(countSql + hql).uniqueResult();
    form.getPage().setTotalCount(count == null ? 0L : Long.parseLong(count.toString()));
    List list =
        this.createQuery(hql + orderBy).setFirstResult((form.getPage().getPageNo() - 1) * form.getPage().getPageSize())
            .setMaxResults(form.getPage().getPageSize()).list();
    form.getPage().setResult(list);
  }

  /**
   * 发布时间，热度 ，创建时间 seq ,
   * 
   * @param form
   * @return
   */
  public String buildOrderBy(NewsForm form) {
    String orderBy = "";
    switch (form.getPage().getOrderBy()) {
      case "publish":
        orderBy = " order by t.gmtPublish desc nulls last ";
        break;
      case "heat":
        orderBy = " order by t.heat desc nulls last ";
        break;
      case "update":
        orderBy = " order by t.gmtUpdate desc nulls last ";
        break;
      case "seqNo":
        orderBy = " order by t.seqNo desc nulls last ";
        break;
      default:
        orderBy = " order by t.gmtPublish desc";
    }
    return orderBy;
  }

  @SuppressWarnings("unchecked")
  public void findNewsRcmd(NewsForm form) {
    String hql = " from NewsBase t where t.status = 1 ";
    // 排除的成果id
    if (form.getNewsIds() != null && form.getNewsIds().size() > 0) {
      hql += " and  t.id not in (:excludeNewsIds )  ";
    }
    String orderBy = buildOrderBy(form);
    Query query =
        this.createQuery(hql + orderBy).setFirstResult((form.getPageNum() - 1) * form.getPageSize()).setMaxResults(1);
    if (form.getNewsIds() != null && form.getNewsIds().size() > 0) {
      query.setParameterList("excludeNewsIds", form.getNewsIds());
    }
    List list = query.list();
    form.getPage().setResult(list);
  }

  /**
   * 上一篇
   * 
   * @param newsId
   * @return
   */
  public Long findPriorNews(Long newsId) {
    String hql =
        "select id from V_NEWS_BASE where id=(select c.p from (select id,lag(id,1,0) over (order by GMT_UPDATE desc nulls last) as p from V_NEWS_BASE where status = 1) c where c.id=:newsId)";
    BigDecimal pId = (BigDecimal) super.getSession().createSQLQuery(hql).setParameter("newsId", newsId).uniqueResult();
    if (pId != null) {
      return pId.longValue();
    } else {
      return 0L;
    }
  }

  /**
   * 下一篇
   * 
   * @param newsId
   * @return
   */
  public Long findNextNews(Long newsId) {
    String hql =
        "select id from V_NEWS_BASE where id=(select c.p from (select id,lead(id,1,0)  over (order by GMT_UPDATE desc nulls last) as p from V_NEWS_BASE where status = 1) c where c.id=:newsId)";
    BigDecimal nId = (BigDecimal) super.getSession().createSQLQuery(hql).setParameter("newsId", newsId).uniqueResult();
    if (nId != null) {
      return nId.longValue();
    } else {
      return 0L;
    }
  }
}
