package com.smate.center.batch.dao.sns.pub;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.JournalGrade;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 期刊等级.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class JournalGradeDao extends SnsHibernateDao<JournalGrade, Long> {

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

  /**
   * 是否核心期刊，ISI\中文核心期刊>=3.
   * 
   * @param issn
   * @return
   */
  public boolean isHxJ(String issn) {
    if (StringUtils.isBlank(issn)) {
      return false;
    }
    int grade = this.getJournalGrade(issn);
    if (grade >= 3) {
      return true;
    }
    return false;
  }

  /**
   * 获取期刊等级For质量.
   * 
   * @param issn
   * @return
   */
  public int getJnlGradeForQuality(String issn) {

    String hql = "select grade from JournalGrade where lower(issn) = ? ";
    Integer grade = super.findUnique(hql, issn.toLowerCase());
    if (grade == null) {
      return 5;
    }
    return grade;
  }
}
