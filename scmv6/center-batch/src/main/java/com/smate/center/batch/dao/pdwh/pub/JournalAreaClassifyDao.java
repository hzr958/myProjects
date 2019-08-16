package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.JournalAreaClassify;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 期刊领域大类.
 * 
 * 
 * @author liqinghua
 * 
 */
@Repository
public class JournalAreaClassifyDao extends PdwhHibernateDao<JournalAreaClassify, Long> {

  /**
   * 获取期刊领域大类.
   * 
   * @param issn
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getJournalAreaClassify(String issn) {

    String hql = "select classify from JournalAreaClassify where issnTxt = ? ";
    return super.createQuery(hql, issn).list();
  }
}
