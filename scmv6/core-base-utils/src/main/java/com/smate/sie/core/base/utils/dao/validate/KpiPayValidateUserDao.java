package com.smate.sie.core.base.utils.dao.validate;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidateUser;

@Repository
public class KpiPayValidateUserDao extends SieHibernateDao<KpiPayValidateUser, Long> {

  public KpiPayValidateUser judgementPaymentListByPsnId(Long psnId) {
    String hql = "from KpiPayValidateUser t where t.psnId = ? and t.endDate >= ? and t.startDate <= ?";
    Date curDate = new Date();
    KpiPayValidateUser kpiPayValidateUser = super.findUnique(hql, new Object[] {psnId, curDate, curDate});
    return kpiPayValidateUser;
  }

}
