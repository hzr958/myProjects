package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.BdspProposal;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class BdspProposalDao extends SnsbakHibernateDao<BdspProposal, Long> {

  public List<BdspProposal> findlist(int batchSize) {
    String hql = "from BdspProposal t1 where not exists(select 1 from BdspProposalTemp t2 where t1.prpCode=t2.prpCode)";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }

  public BdspProposal getBdspPps(Long prpCode) {
    String hql = "from BdspProposal  t where t.prpCode = :prpCode";
    return (BdspProposal) super.createQuery(hql).setParameter("prpCode", prpCode).uniqueResult();
  }

}
