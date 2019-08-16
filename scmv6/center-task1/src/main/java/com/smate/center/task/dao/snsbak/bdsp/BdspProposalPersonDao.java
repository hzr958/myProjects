package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.BdspProposalPerson;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspProposalPersonDao extends SnsbakHibernateDao<BdspProposalPerson, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getDspPpsPsn(Long prpCode) {
    String hql = "select t.zhName from BdspProposalPerson t where t.prpCode = :prpCode";
    return super.createQuery(hql).setParameter("prpCode", prpCode).list();
  }
}
