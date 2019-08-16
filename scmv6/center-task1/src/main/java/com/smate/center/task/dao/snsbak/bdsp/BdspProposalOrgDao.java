package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.BdspProjectOrg;
import com.smate.center.task.model.snsbak.bdsp.BdspProposalOrg;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspProposalOrgDao extends SnsbakHibernateDao<BdspProposalOrg, Long> {

  @SuppressWarnings("unchecked")
  public List<BdspProposalOrg> getDspPpsOrg(Long prpCode) {
    String hql =
        "select new BdspProposalOrg(t.prpCode,t.orgZhName,t.provinceId)  from BdspProposalOrg t where t.prpCode = :prpCode";
    return super.createQuery(hql).setParameter("prpCode", prpCode).list();
  }

  public BdspProposalOrg findOrg(Long prpCode, String orgName) {
    String hql = "from BdspProposalOrg t where t.prpCode=:prpCode and t.orgZhName =:orgName";
    return (BdspProposalOrg) this.createQuery(hql).setParameter("prpCode", prpCode).setParameter("orgName", orgName)
        .uniqueResult();

  }

}
