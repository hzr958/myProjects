package com.smate.center.batch.dao.sns.pub;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.KeywordsEnTranZh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 英文翻译中文库.
 * 
 * @author liqinghua
 * 
 */
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
    String hql = "from KeywordsEnTranZh t where enKwTxt = ? ";
    return super.findUnique(hql, enKw.trim().toLowerCase());
  }
}
