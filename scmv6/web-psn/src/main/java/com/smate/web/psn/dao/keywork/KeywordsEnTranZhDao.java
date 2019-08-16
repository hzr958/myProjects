package com.smate.web.psn.dao.keywork;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.KeywordsEnTranZh;

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
