package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.KeywordsCommend;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class KeywordsCommendDao extends SnsHibernateDao<KeywordsCommend, Long> {
  public int getCommendCount(String keywordTxt) {
    String hql = "select count(1) from KeywordsCommend t where t.zhkwTxt=:keywordTxt or t.enkwTxt=:keywordTxt0";
    Long count = (Long) super.createQuery(hql).setParameter("keywordTxt", keywordTxt)
        .setParameter("keywordTxt0", keywordTxt).uniqueResult();
    return NumberUtils.toInt(count.toString());
  }

  public Long getId(String keywordTxt) {
    String hql = "select t.id from KeywordsCommend t where t.zhkwTxt=:keywordTxt or t.enkwTxt=:keywordTxt0";
    return (Long) super.createQuery(hql).setParameter("keywordTxt", keywordTxt).setParameter("keywordTxt0", keywordTxt)
        .setMaxResults(1).uniqueResult();

  }

}
