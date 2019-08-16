package com.smate.center.batch.dao.rcmd.journal;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rcmd.journal.RcmdPsnJournalGrade;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 人员期刊等级.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class RcmdPsnJournalGradeDao extends RcmdHibernateDao<RcmdPsnJournalGrade, Long> {

  /**
   * 删除人员期刊等级.
   * 
   * @param psnId
   */
  public void delPsnJGrade(Long psnId) {
    super.createQuery("delete from RcmdPsnJournalGrade t where t.psnId = ? ", psnId).executeUpdate();
  }

  /**
   * 保存人员期刊等级.
   * 
   * @param psnId
   * @param grade
   */
  public void savePsnJGrade(Long psnId, Integer grade) {

    if (grade == null) {
      grade = 4;
    }
    RcmdPsnJournalGrade pjg = this.get(psnId);
    if (pjg == null) {
      pjg = new RcmdPsnJournalGrade(psnId, grade);
    } else {
      pjg.setGrade(grade);
    }
    this.save(pjg);
  }
}
