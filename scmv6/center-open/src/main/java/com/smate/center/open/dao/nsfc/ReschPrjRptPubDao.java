package com.smate.center.open.dao.nsfc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcReschPrjRptPub;
import com.smate.center.open.model.nsfc.NsfcReschPrjRptPubId;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/*
 * 成果研究报告的DAO实现.
 * 
 * @Author zjh
 */
@Repository
public class ReschPrjRptPubDao extends HibernateDao<NsfcReschPrjRptPub, NsfcReschPrjRptPubId> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public List<NsfcReschPrjRptPub> getProjectReportPubsByRptId(Long rptId, int objectsPerPage, int pageNumber)
      throws Exception {
    String query =
        "select new NsfcReschPrjRptPub(pp.id,version,pubType,pubYear,defType,authors,title,source,isTag,isOpen,needSyc,listInfo,seqNo,nodeId,pubOwnerPsnId,matched,listInfoSource,citedTimes,impactFactors) from  NsfcReschPrjRptPub pp where pp.id.rptId=? order by pp.defType asc, pp.seqNo,nvl(pp.pubYear,0) desc, pp.pubYear desc,pp.pubType,id.pubId desc";
    List<NsfcReschPrjRptPub> list = super.createQuery(query, new Object[] {rptId}).list();
    return list;
  }

  public List<Long> getTop5ProjectReportPubsByRptId(Long rptId) throws Exception {

    String hql = "select t.id.pubId from NsfcReschPrjRptPub t where t.id.rptId=? and t.seqNo<=5 order by t.seqNo";
    return super.createQuery(hql, new Object[] {rptId}).list();

  }

}
