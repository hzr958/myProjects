package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.KeywordsEnTranZh;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class KeywordsEnTranZhDao extends SnsHibernateDao<KeywordsEnTranZh, Long> {
  /**
   * 查找英文翻译中文的关键词.
   * 
   * @param enKw
   * @return
   */
  public KeywordsEnTranZh findEnTranZhKw(String enKw) {
    if (StringUtils.isBlank(enKw)) {
      return null;
    }
    String hql = "from KeywordsEnTranZh t where enKwTxt=:enKw ";
    return (KeywordsEnTranZh) super.createQuery(hql).setParameter("enKw", enKw.trim().toLowerCase()).setMaxResults(1)
        .uniqueResult();
  }

}
