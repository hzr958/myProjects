package com.smate.center.batch.dao.rcmd.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmRolPubMember;
import com.smate.core.base.utils.data.RcmdHibernateDao;


/**
 * 成果提交持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PubConfirmRolPubMembersDao extends RcmdHibernateDao<PubConfirmRolPubMember, Long> {

  public void deleteMembers(Long rolPubId) {
    String hql = "delete from PubConfirmRolPubMember t where t.rolPubId=?";
    this.createQuery(hql, new Object[] {rolPubId}).executeUpdate();
  }

  public List<PubConfirmRolPubMember> getByDtId(Long rolPubId) {
    String hql = "select t from PubConfirmRolPubMember t where t.rolPubId=?";
    return this.createQuery(hql, new Object[] {rolPubId}).list();
  }

  /**
   * 根据指派的SEQNO获取pub_member id.
   * 
   * @param rolPubId
   * @param seqNo
   * @return
   */
  public Long getRolPmIdBySeqNo(Long rolPubId, Integer seqNo) {

    if (seqNo == null) {
      return null;
    }
    String hql = "select insPmId from PubConfirmRolPubMember t where t.rolPubId=? and t.seqNo = ? ";
    return super.findUnique(hql, rolPubId, seqNo);
  }

  @SuppressWarnings("unchecked")
  public List<PubConfirmRolPubMember> queryByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from PubConfirmRolPubMember t where t.psnId=?", psnId).list();
  }

}
