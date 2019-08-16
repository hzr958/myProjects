package com.smate.center.task.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.sns.pub.PubAssignLogDetail;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubAssignLogDetailDao extends SnsHibernateDao<PubAssignLogDetail, Long> {

  public PubAssignLogDetail getAssignDeatilByEmail(Long pdwhPubId, Long psnId, String matchedEmail) {
    String hql =
        "from PubAssignLogDetail  t where t.pdwhPubId = :pdwhPubId and t.psnId = :psnId and t.matchedEmail = :matchedEmail";
    return (PubAssignLogDetail) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId)
        .setParameter("matchedEmail", matchedEmail).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubAssignLogDetail> getAssignDeatil(Long pdwhPubId, Long psnId) {
    String hql = "from PubAssignLogDetail  t where t.pdwhPubId = :pdwhPubId and t.psnId = :psnId";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).list();
  }

  public void deleteAssignDetail(Long pdwhPubId, Long psnId) {
    String hql = "delete from PubAssignLogDetail  t where t.pdwhPubId = :pdwhPubId and t.psnId = :psnId";
    super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 处理联合唯一索引保存报错新开事务单独保存。
   * 
   * @author LIJUN
   * @date 2018年7月5日
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithNewTransaction(PubAssignLogDetail pubAssignLogDetail) {
    super.save(pubAssignLogDetail);
  }

  @SuppressWarnings("unchecked")
  public List<PubAssignLogDetail> getAssignDetailByPdwhId(Long pdwhPubId, Long psnId) {
    String hql =
        "select new PubAssignLogDetail(psnId,pubMemberId, pubMemberName) from PubAssignLogDetail t where exists(select 1 from PubAssignLog f where f.pdwhPubId = :pdwhPubId and f.confirmResult =1"
            + " and t.pdwhPubId=f.pdwhPubId and t.psnId=f.psnId and f.psnId not in (:psnId)) order by t.matchedNameType";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).list();
  }
}
