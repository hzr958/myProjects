package com.smate.web.management.dao.analysis.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.sns.PsnPositionGrade;
import com.smate.web.management.model.analysis.sns.PsnPositionNsfc;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPositionDao extends SnsHibernateDao<PsnPositionNsfc, Long> {

  /**
   * 保存用户职称等级.
   * 
   * @param psnId
   * @param grade
   */
  public void savePsnPosGrade(Long psnId, Integer grade, Long posCode, String posName) {
    PsnPositionGrade ppg = super.findUnique("from PsnPositionGrade t where t.psnId = ? ", psnId);
    if (ppg == null) {
      ppg = new PsnPositionGrade(psnId, grade, posCode, posName);
    } else {
      ppg.setGrade(grade);
      ppg.setPosName(posName);
      ppg.setPosCode(posCode);
    }
    super.getSession().save(ppg);
  }

  /**
   * 获取用户职称等级.
   * 
   * @param psnId
   * @return
   */
  public PsnPositionNsfc getPsnPositionNsfc(Long psnId) {
    return super.findUnique("from PsnPositionNsfc t where t.psnId = ? ", psnId);
  }

  /**
   * 获取用户职称(无名称).
   * 
   * @return
   */
  public PsnPositionGrade getPsnPositionGrade(Long psnId) {
    PsnPositionGrade ppg = super.findUnique("from PsnPositionGrade t where t.psnId = ? ", psnId);
    return ppg;
  }
}
