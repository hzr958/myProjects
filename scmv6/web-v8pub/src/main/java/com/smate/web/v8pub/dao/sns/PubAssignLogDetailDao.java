package com.smate.web.v8pub.dao.sns;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubAssignLogDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 指派过程匹配内容Dao
 * 
 * @author zzx
 *
 */
@Repository
public class PubAssignLogDetailDao extends SnsHibernateDao<PubAssignLogDetail, Long> {

  public List<PubAssignLogDetail> findByPubIdAndName(Long pubId, String matchedName) {
    String hql = "from PubAssignLogDetail t where t.pubId=:pubId and lower(t.matchedName)=:matchedName";
    return this.createQuery(hql).setParameter("pubId", pubId)
        .setParameter("matchedName", matchedName.trim().toLowerCase()).list();
  }

  public List<PubAssignLogDetail> findByPubIdAndEmail(Long pubId, String email) {
    String hql = "from PubAssignLogDetail t where t.pubId=:pubId and t.matchedEmail=:email";
    return this.createQuery(hql).setParameter("pubId", pubId).setParameter("email", email).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubAssignLogDetail> getAssignDetailByPdwhId(Long pdwhPubId, Long psnId) {
    String hql =
        "select new PubAssignLogDetail(psnId,pubMemberId, pubMemberName) from PubAssignLogDetail t where exists(select 1 from PubAssignLogPO f where f.pdwhPubId = :pdwhPubId and f.confirmResult =1"
            + " and t.pubId=f.pdwhPubId and t.psnId=f.psnId and f.psnId not in (:psnId)) order by t.matchedNameType";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).list();
  }

  public List<Long> findPsnIdByPubId(Long pubId) {
    String hql = "select t.psnId from PubAssignLogDetail t where t.pubId=:pubId ";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
