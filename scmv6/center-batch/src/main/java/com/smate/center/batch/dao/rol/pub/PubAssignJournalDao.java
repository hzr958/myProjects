package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PubAssignJournal;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果期刊DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignJournalDao extends RolHibernateDao<PubAssignJournal, Long> {

  /**
   * 获取成果期刊数据.
   * 
   * @param pubId
   * @return
   */
  public PubAssignJournal getPubAssignJournalByPubId(Long pubId) {
    String hql = "from PubAssignJournal where pubId = ? ";
    List<PubAssignJournal> list = super.find(hql, pubId);
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
  public List<PsnPmJournalRol> getPJMatchPubPJ(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp from PubAssignJournal pj ,PsnPmJournalRol pp where pj.pubId = :pubId and pp.psnId in(:psnIds) and pj.jid = pp.jid ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<PsnPmJournalRol> listResult = new ArrayList<PsnPmJournalRol>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item).list());
    }
    return listResult;
  }
}
