package com.smate.center.open.dao.data.isis;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPub;
import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPubPK;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * @author hp
 * @date 2015-10-27
 */
@Repository
public class NsfcPrjRptPubDao extends RolHibernateDao<NsfcPrjRptPub, NsfcPrjRptPubPK> {
  @SuppressWarnings("unchecked")
  public List<NsfcPrjRptPub> getNsfcPrjRptPubListByRptId(Set<Long> rptIdSet) {
    String hql = "from NsfcPrjRptPub n where n.nsfcPrjRptPubPK.rptId in (:rptIdSet) ";
    return super.createQuery(hql).setParameterList("rptIdSet", rptIdSet).list();
  }
}
