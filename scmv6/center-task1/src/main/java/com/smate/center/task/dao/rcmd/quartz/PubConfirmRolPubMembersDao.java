package com.smate.center.task.dao.rcmd.quartz;

import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPubMember;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 
 * @author zjh
 *
 */
@Repository
public class PubConfirmRolPubMembersDao extends RcmdHibernateDao<PubConfirmRolPubMember, Long> {

  public void deleteMembers(Long rolPubId) {
    String hql = "delete from PubConfirmRolPubMember t where t.rolPubId=:rolPubId";
    this.createQuery(hql).setParameter("rolPubId", rolPubId).executeUpdate();
  }

  public Long getRolPmIdBySeqNo(Long rolPubId, Integer seqNo) {

    if (seqNo == null) {
      return null;
    }
    String hql = "select insPmId from PubConfirmRolPubMember t where t.rolPubId=:rolPubId and t.seqNo =:seqNo ";
    return (Long) super.createQuery(hql).setParameter("rolPubId", rolPubId).setParameter("seqNo", seqNo).uniqueResult();
  }

  public Map queryCfmRolPubBypubId(Long rolPubId) throws DaoException {
    String hql =
        "select t.citedTimes as citedTimes, t.pubList as pubList from PubConfirmRolPub t where t.rolPubId=:rolPubId";
    return (Map) super.createQuery(hql).setParameter("rolPubId", rolPubId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
  }

}
