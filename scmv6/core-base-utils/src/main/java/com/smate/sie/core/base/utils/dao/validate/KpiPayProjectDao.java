package com.smate.sie.core.base.utils.dao.validate;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.KpiPayProject;

@Repository
public class KpiPayProjectDao extends SieHibernateDao<KpiPayProject, Long> {


  public List<KpiPayProject> judgementPaymentByInsIds(List<Long> insIds) {
    String hql =
        "from KpiPayProject t where t.insId in(:insId) and t.status = 1 and t.endDate >= :endDate order by t.endDate desc";
    List<KpiPayProject> list =
        super.createQuery(hql).setParameterList("insId", insIds).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list;
    }
  }
}
