package com.smate.web.psn.dao.keywork;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.KeywordsZhTranEn;

/**
 * 中文翻译英文库.
 * 
 * @author liqinghua
 * 
 */
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
    String hql = "from KeywordsZhTranEn t where zhKwTxt = ? ";
    return super.findUnique(hql, zhKw.trim().toLowerCase());
  }
}
