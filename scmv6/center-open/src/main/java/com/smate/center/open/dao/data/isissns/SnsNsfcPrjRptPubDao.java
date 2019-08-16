package com.smate.center.open.dao.data.isissns;

import java.util.List;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPub;
import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPubPK;

import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjRptPub;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author hp
 * @date 2016-02-16
 */
@Repository
public class SnsNsfcPrjRptPubDao extends SnsHibernateDao<NsfcPrjRptPub, NsfcPrjRptPubPK> {


  @SuppressWarnings("unchecked")
  public List<SnsNsfcPrjRptPub> getPubsByRptIds(Set<Long> rptIdSet) {
    // 根据title剔重
    String query =
        "from  SnsNsfcPrjRptPub pp where pp.nsfcPrjRptPubPK.rptId in (:rptIdSet) and pp.pubType in (1,2,3,4,5)"
            + " and pp.rowid in ( select min(pp2.rowid) from  SnsNsfcPrjRptPub pp2 where pp2.nsfcPrjRptPubPK.rptId in (:rptIdSet) and pp2.pubType in (1,2,3,4,5)  group by  pp2.title )";
    return super.createQuery(query).setParameterList("rptIdSet", rptIdSet).list();
  }
}
