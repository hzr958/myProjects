package com.smate.center.open.dao.reschproject;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;
import com.smate.center.open.model.nsfc.project.NsfcPrjRptPubId;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 结题报告成果的DAO实现.
 * 
 * @author LY
 * 
 */
@Repository("projectReportPubDao")
public class ProjectReportPubDao extends SnsHibernateDao<NsfcPrjRptPub, NsfcPrjRptPubId> {

  public List<NsfcPrjRptPub> getPrjRptPubsByRptIdForStat(Long rptId) throws Exception {
    String query =
        "from  NsfcPrjRptPub pp where pp.id.rptId=? and pp.pubType in(1,2,3,4,5) order by pp.seqNo,nvl(pp.pubYear,0) desc, pp.pubYear desc,pp.pubType,id.pubId desc";
    List<NsfcPrjRptPub> list = super.createQuery(query, new Object[] {rptId}).list();
    return list;
  }


  public List<NsfcPrjRptPub> getProjectReportPubsByRptId(Long rptId, int objectsPerPage, int pageNumber)
      throws Exception {
    String query =
        "from  NsfcPrjRptPub pp where pp.id.rptId=? order by pp.seqNo,nvl(pp.pubYear,0) desc, pp.pubYear desc,pp.pubType,id.pubId desc";
    List<NsfcPrjRptPub> list = super.createQuery(query, new Object[] {rptId}).list();
    return list;
  }

}
