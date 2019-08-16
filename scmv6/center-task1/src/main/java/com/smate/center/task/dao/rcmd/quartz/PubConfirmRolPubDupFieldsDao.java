package com.smate.center.task.dao.rcmd.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPubDupFields;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * rol成果确认用于查重的信息.
 * 
 * @author zjh
 *
 */
@Repository
public class PubConfirmRolPubDupFieldsDao extends RcmdHibernateDao<PubConfirmRolPubDupFields, Long> {

  /**
   * 根据成果id和人员id删除成果
   * 
   * @param rolPubId
   * @param psnId
   */
  public void deleteByPubPsnId(Long rolPubId, Long psnId) {
    String hql = "delete from PubConfirmRolPubDupFields t where t.rolPubId=:rolPubId and t.ownerId=:psnId";
    super.createQuery(hql).setParameter("rolPubId", rolPubId).setParameter("psnId", psnId).executeUpdate();
  }

  public PubConfirmRolPubDupFields getByPubPsnId(Long pubId, Long psnId) {
    String hql = "select t from PubConfirmRolPubDupFields t where t.rolPubId=:pubId and t.ownerId=:psnId";
    return (PubConfirmRolPubDupFields) super.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId)
        .uniqueResult();

  }

}
