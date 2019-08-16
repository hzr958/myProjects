package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.KeywordsZhTranEn;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class KeywordsZhTranEnDao extends SnsHibernateDao<KeywordsZhTranEn, Long> {
  /**
   * 查找中文翻译英文的关键词.
   * 
   * @param enKw
   * @return
   */
  public KeywordsZhTranEn findZhTranEnKw(String zhKw) {
    if (StringUtils.isBlank(zhKw)) {
      return null;
    }
    String hql = "from KeywordsZhTranEn t where zhKwTxt =:zhKw ";
    return (KeywordsZhTranEn) super.createQuery(hql).setParameter("zhKw", zhKw.trim().toLowerCase()).uniqueResult();
  }
}
