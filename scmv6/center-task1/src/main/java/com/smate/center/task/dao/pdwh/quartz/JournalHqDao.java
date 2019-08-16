package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.JournalHq;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * CSSCI核心期刊跟ISI的期刊.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class JournalHqDao extends PdwhHibernateDao<JournalHq, Long> {

  /**
   * @param issn
   */
  public boolean isJnlHqExist(String issn) {
    String hql = "select count(id) from JournalHq where issnTxt=?";
    Long count = findUnique(hql, issn.toLowerCase());
    return count > 0;
  }
}
