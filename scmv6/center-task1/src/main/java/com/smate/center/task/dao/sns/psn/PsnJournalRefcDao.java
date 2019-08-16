package com.smate.center.task.dao.sns.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnJournalRefc;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnJournalRefcDao extends SnsHibernateDao<PsnJournalRefc, Long> {


  public int getPsnJnlByRefc(Long psnId, String issn) {
    String hql = "select count(id) from PsnJournalRefc where psnId=? and issnTxt=?";
    return ((Long) super.createQuery(hql, psnId, issn.toLowerCase()).list().get(0)).intValue();
  }

}
