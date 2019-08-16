package com.smate.center.task.dao.fund.rcmd;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundAgency;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundAgencyDao extends RcmdHibernateDao<ConstFundAgency, Long> {

  public ConstFundAgency getFundAgency(Long agencyId) {
    String hql = "from ConstFundAgency where id=?";
    return super.findUnique(hql, agencyId);
  }

  // 查询国家级资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getCountryFundAgencyList() {
    String hql = "from ConstFundAgency where type=120 and status=0";
    return super.createQuery(hql).list();
  }

}
