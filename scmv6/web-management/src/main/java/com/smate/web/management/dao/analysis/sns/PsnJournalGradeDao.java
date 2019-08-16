package com.smate.web.management.dao.analysis.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.sns.PsnJournalGrade;

/**
 * 人员期刊等级.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnJournalGradeDao extends SnsHibernateDao<PsnJournalGrade, Long> {

  /**
   * 删除人员期刊等级.
   * 
   * @param psnId
   */
  public void delPsnJGrade(Long psnId) {
    super.createQuery("delete from PsnJournalGrade t where t.psnId = ? ", psnId).executeUpdate();
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
    PsnJournalGrade pjg = super.get(psnId);
    if (pjg == null) {
      pjg = new PsnJournalGrade(psnId, grade);
    } else {
      pjg.setGrade(grade);
    }
    super.save(pjg);
  }
}
