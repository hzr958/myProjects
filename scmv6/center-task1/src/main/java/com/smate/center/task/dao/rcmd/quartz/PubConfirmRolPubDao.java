package com.smate.center.task.dao.rcmd.quartz;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果提交持久层.
 * 
 * @author zjh
 *
 */
@Repository
public class PubConfirmRolPubDao extends RcmdHibernateDao<PubConfirmRolPub, Long> {
  @SuppressWarnings("unchecked")
  public List<PubConfirmRolPub> batchGetPublist(Long lastId, Integer batchSize) {
    String hql = "from PubConfirmRolPub t where  t.rolPubId>:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();

  }

  public boolean isPubUpdate(Long rolPubId, Date updateDate) throws Exception {
    if (updateDate == null) {
      return true;
    }
    String hql =
        "select count(t.rolPubId) from PubConfirmRolPub t where t.rolPubId=:rolPubId and t.updateDate =:updateDate ";
    Long ct = (Long) super.createQuery(hql).setParameter("rolPubId", rolPubId).setParameter("updateDate", updateDate)
        .uniqueResult();
    if (ct > 0) {
      return false;
    }
    return true;

  }

  public void deleteById(Long rolPubId) throws Exception {
    String sql = "delete from PubConfirmRolPub t where t.rolPubId=:rolPubId";
    this.createQuery(sql).setParameter("rolPubId", rolPubId).executeUpdate();
  }

  public void updateBrief(PubConfirmRolPub pub) throws Exception {
    String hql = "update PubConfirmRolPub t set t.briefDesc=:briefDesc where t.rolPubId=:rolPubId";
    this.createQuery(hql).setParameter("briefDesc", pub.getBriefDesc()).setParameter("rolPubId", pub.getRolPubId())
        .executeUpdate();
  }

  /**
   * 获取成果数据 ，成果认领推广,新
   */
  public PubConfirmRolPub getCfmRolPubBypubIdNew(Long rolPubId) throws DaoException {
    String hql =
        "select new PubConfirmRolPub(p.rolPubId,p.zhTitle,p.enTitle ,p.typeId,p.briefDesc,p.fulltextFileid ,p.authorNames) from PubConfirmRolPub p where rolPubId = ? ";
    return (PubConfirmRolPub) super.createQuery(hql, rolPubId).uniqueResult();
  }

}
