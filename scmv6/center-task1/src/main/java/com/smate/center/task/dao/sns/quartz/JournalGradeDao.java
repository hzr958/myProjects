package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.JournalGrade;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class JournalGradeDao extends SnsHibernateDao<JournalGrade, Long> {
  /**
   * 
   */
  public Object getHxj(Long pubId) {
    String hql =
        "select t.grade from JournalGrade t where exists(select 1 from PublicationJournal s where s.pubId=:pubId and t.issn=s.issn)";
    return super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

}
