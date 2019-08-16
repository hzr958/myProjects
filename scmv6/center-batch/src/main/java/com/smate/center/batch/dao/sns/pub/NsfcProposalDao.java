package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcProposal;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * 杰青申报书Dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class NsfcProposalDao extends SnsHibernateDao<NsfcProposal, Long> {

  @SuppressWarnings("unchecked")
  public List<NsfcProposal> getPrpListByPsnId(Long psnId) throws DaoException {

    String hql = "From NsfcProposal t where t.prpPsnId=? and  t.status<>? order by t.prpDate desc";
    return super.createQuery(hql, new Object[] {psnId, 2}).list();

  }

  @SuppressWarnings("unchecked")
  public NsfcProposal getPrpByIsisGuid(String isisGuid, Long psnId) throws DaoException {

    String hql = "From NsfcProposal t where t.isisGuid=?  and t.prpPsnId=? ";
    return (NsfcProposal) super.createQuery(hql, new Object[] {isisGuid, psnId}).uniqueResult();

  }

  public void deleteByGuids(String guids, Long psnId) throws DaoException {
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
  public List<NsfcProposal> getNsfcProposalList(Long psnId) {
    String ql = "from NsfcProposal where prpPsnId = ?";
    return super.createQuery(ql, psnId).list();
  }
}
