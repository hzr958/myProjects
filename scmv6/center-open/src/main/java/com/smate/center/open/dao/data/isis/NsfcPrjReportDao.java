package com.smate.center.open.dao.data.isis;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isis.NsfcPrjReport;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * pwdh BaseJournal dao
 * 
 * @author hp
 * 
 */
@Repository
public class NsfcPrjReportDao extends RolHibernateDao<NsfcPrjReport, Long> {
  @SuppressWarnings("unchecked")
  public List<NsfcPrjReport> getNsfcPrjReportListByDeliverDate(Date DeliverDate) {
    String hql = "from NsfcPrjReport n where n.rptType=1 and n.status=1 and trunc(n.deliverDate) = trunc(?) ";
    return super.createQuery(hql).setParameter(0, DeliverDate).list();
  }
}
