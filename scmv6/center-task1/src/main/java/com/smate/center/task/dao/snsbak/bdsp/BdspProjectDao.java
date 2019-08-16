package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class BdspProjectDao extends SnsbakHibernateDao<BdspProject, Long> {

  public List<BdspProject> findListByIds(List<Long> prjCodeList) {
    String hql =
        "select new BdspProject(t.prjCode,t.prpCode,t.prpNo,t.orgName,t.statYear,t.reqAmt,t.grantCode,t.subjectId) from BdspProject t "
            + "where t.prjCode in(:prjCodeList)";
    return super.createQuery(hql).setParameterList("prjCodeList", prjCodeList).list();
  }

  public List<BdspProject> findlist(int batchSize) {
    String hql = "from BdspProject t1 where not exists(select 1 from BdspProjectTemp t2 where t1.prjCode=t2.prjCode)";
    return super.createQuery(hql).setMaxResults(batchSize).list();
  }

  public BdspProject getBdspPrj(Long prpCode) {
    String hql = "from BdspProject  t where t.prpCode = :prpCode";
    return (BdspProject) super.createQuery(hql).setParameter("prpCode", prpCode).uniqueResult();
  }

}
