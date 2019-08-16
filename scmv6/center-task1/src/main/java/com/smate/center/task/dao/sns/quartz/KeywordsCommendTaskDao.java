package com.smate.center.task.dao.sns.quartz;

import java.math.BigDecimal;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.KeywordsCommendTask;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class KeywordsCommendTaskDao extends SnsHibernateDao<KeywordsCommendTask, Long> {
  public int getKeyWordsCommendFlag() {
    String hql = "from KeywordsCommendTask t where t.id =1 and t.flag = 1";
    KeywordsCommendTask keywordsCommendTask = (KeywordsCommendTask) super.createQuery(hql).uniqueResult();
    if (keywordsCommendTask == null)
      return 0;
    else
      return 1;
  }

  /**
   * PsnKwRmcTask任务的开关
   * 
   * @return
   */
  public int getPsnKwRmcTask() {
    String sql = "select count(1) from psn_kw_rmc_task t where t.id =1 and t.flag = 1";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    return ((BigDecimal) query.uniqueResult()).intValue();
  }

  public void updatePsnKwRmcFlag() {
    String sql = "update psn_kw_rmc_task t set t.flag=1 where t.id =1 and t.flag=0";
    super.update(sql);
  }

  public void updateFlag() {
    String hql = "update KeywordsCommendTask t set t.flag=0 where t.id =1";
    super.createQuery(hql).executeUpdate();
  }

}
