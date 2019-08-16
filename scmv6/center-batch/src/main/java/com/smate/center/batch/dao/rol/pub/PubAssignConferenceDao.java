package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssignConference;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果会议论文DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignConferenceDao extends RolHibernateDao<PubAssignConference, Long> {

  /**
   * 获取成果会议论文查重.
   * 
   * @param pubId
   * @return
   */
  public PubAssignConference getPubAssignConferenceByPubId(Long pubId) {
    String hql = "from PubAssignConference where pubId = ? ";
    List<PubAssignConference> list = super.find(hql, pubId);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取单位用户期刊匹配上成果期刊的期刊列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmConference> getPcMatchPubPc(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp from PubAssignConference pc ,PsnPmConference pp where pc.pubId = :pubId and pp.psnId in(:psnIds) and pc.nameHash = pp.nameHash ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<PsnPmConference> listResult = new ArrayList<PsnPmConference>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item).list());
    }
    return listResult;
  }
}
