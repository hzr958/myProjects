package com.smate.center.open.dao.proposal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.proposal.NsfcProposal;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 
 * 杰青申报书Dao.
 * 
 * @author tsz
 * 
 */
@Repository
public class NsfcProposalDao extends HibernateDao<NsfcProposal, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  @SuppressWarnings("unchecked")
  public List<NsfcProposal> getPrpListByPsnId(Long psnId) {

    String hql = "From NsfcProposal t where t.prpPsnId=? and  t.status<>? order by t.prpDate desc";
    return super.createQuery(hql, new Object[] {psnId, 2}).list();

  }

  public NsfcProposal getPrpByIsisGuid(String isisGuid, Long psnId) {

    String hql = "From NsfcProposal t where t.isisGuid=?  and t.prpPsnId=? ";
    return (NsfcProposal) super.createQuery(hql, new Object[] {isisGuid, psnId}).uniqueResult();

  }

  public void deleteByGuids(String guids, Long psnId) {
    if (StringUtils.isBlank(guids)) {
      return;
    }
    List<String> guidList = new ArrayList<String>();
    for (String guid : guids.split(",")) {
      guidList.add(guid);
    }
    StringBuffer sb = new StringBuffer();
    sb.append("delete NsfcProposal t where t.isisGuid in(:guidList) and t.prpPsnId =:psnId");
    super.createQuery(sb.toString()).setParameter("psnId", psnId).setParameterList("guidList", guidList)
        .executeUpdate();
  }

  /**
   * 获取杰青申报书记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<NsfcProposal> getNsfcProposalList(Long psnId) {
    String ql = "from NsfcProposal where prpPsnId = ?";
    return super.createQuery(ql, psnId).list();
  }
}
