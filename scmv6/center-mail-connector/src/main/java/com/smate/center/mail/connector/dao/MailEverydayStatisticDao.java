package com.smate.center.mail.connector.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.connector.model.MailEverydayStatistic;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 每天邮件统计Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailEverydayStatisticDao extends SnsHibernateDao<MailEverydayStatistic, Long> {
  /**
   * 查找今天的mail统计记录表
   * 
   * @return
   */
  public MailEverydayStatistic findCurrentInfo() {
    String hql =
        "from  MailEverydayStatistic t where t.createDate > trunc(sysdate) and t.createDate < trunc(sysdate+1) ";
    return (MailEverydayStatistic) super.createQuery(hql).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<MailEverydayStatistic> findList(MailEverydayStatistic form, Page<MailEverydayStatistic> page) {
    String count = "select count(1) ";
    String hql = "from MailEverydayStatistic t where 1=1 ";
    String order = "order by t.createDate desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (StringUtils.isNotBlank(form.getStatisticsDateStartStr())
        && StringUtils.isNotBlank(form.getStatisticsDateEndStr())) {
      sb.append(" and t.createDate>=to_date(?,'yyyy-mm-dd') and t.createDate<=to_date(?,'yyyy-mm-dd') ");
      params.add(form.getStatisticsDateStartStr());
      params.add(form.getStatisticsDateEndStr());
    }
    if (StringUtils.isNotBlank(form.getStatisticsDateStartStr())
        && StringUtils.isBlank(form.getStatisticsDateEndStr())) {
      sb.append(" and t.createDate>= to_date(?,'yyyy-mm-dd') ");
      params.add(form.getStatisticsDateStartStr());
    }
    if (StringUtils.isBlank(form.getStatisticsDateStartStr())
        && StringUtils.isNotBlank(form.getStatisticsDateEndStr())) {
      sb.append(" and t.createDate<=to_date(?,'yyyy-mm-dd') ");
      params.add(form.getStatisticsDateEndStr());
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<MailEverydayStatistic> statisticList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(statisticList);
    return statisticList;
  }
}
