package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.BdspProjectOrg;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspProjectOrgDao extends SnsbakHibernateDao<BdspProjectOrg, Long> {

  public BdspProjectOrg findOrg(Long prjCode, String orgName) {
    String hql = "from BdspProjectOrg t where t.prjCode=:prjCode and t.orgZhName =:orgName";
    return (BdspProjectOrg) this.createQuery(hql).setParameter("prjCode", prjCode).setParameter("orgName", orgName)
        .uniqueResult();

  }

  @SuppressWarnings("unchecked")
  public List<BdspProjectOrg> getDspPrjOrg(Long prpCode) {
    String hql =
        "select new BdspProjectOrg(t.prjCode,t.orgZhName,t.provinceId)  from BdspProjectOrg t  where t.prjCode = :prpCode";
    return super.createQuery(hql).setParameter("prpCode", prpCode).list();
  }
}
