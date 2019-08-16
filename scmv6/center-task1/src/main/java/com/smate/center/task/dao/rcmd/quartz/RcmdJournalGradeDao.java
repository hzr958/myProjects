package com.smate.center.task.dao.rcmd.quartz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.JournalGrade;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 期刊等级.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class RcmdJournalGradeDao extends SnsHibernateDao<JournalGrade, Long> {

  /**
   * 获取期刊等级.
   * 
   * @param issn
   * @return
   */
  public int getJournalGrade(String issn) {

    String hql = "select grade from JournalGrade where lower(issn) = ? ";
    Integer grade = super.findUnique(hql, issn.toLowerCase());
    if (grade == null) {
      return 4;
    }
    return grade;
  }

}
