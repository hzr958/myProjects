package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnKnowWorkEdu;
import com.smate.core.base.psn.model.PsnEdu;
import com.smate.core.base.psn.model.PsnWork;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author lcw
 * 
 */
@Repository
@SuppressWarnings("unchecked")
public class PsnKnowWorkEduDao extends SnsHibernateDao<PsnKnowWorkEdu, Long> {

  public List<PsnWork> findWorkByPsnId(Long psnId) {
    String hql = "from PsnWork t where t.psnId=?";
    return createQuery(hql, psnId).list();
  }

  public List<PsnEdu> findEduByPsnId(Long psnId) {
    String hql = "from PsnEdu t where t.psnId=?";
    return createQuery(hql, psnId).list();
  }

  public void deleteAll() {
    super.update("truncate table psn_know_work_edu");
  }

  public void delPsnKnowWorkEdu(Long psnId) {
    createQuery("delete from PsnKnowWorkEdu where psnId=?", psnId).executeUpdate();
  }

}
