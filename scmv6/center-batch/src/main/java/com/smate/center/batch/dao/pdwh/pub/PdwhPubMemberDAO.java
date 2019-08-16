package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhPubMemberPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果 成员dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubMemberDAO extends PdwhHibernateDao<PdwhPubMemberPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubMemberPO> findByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.seqNo asc nulls last ";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  @SuppressWarnings("rawtypes")
  public PdwhPubMemberPO getByPubIdAndPsnId(Long pdwhPubId, Long psnId) {
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId and p.psnId =:psnId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return (PdwhPubMemberPO) list.get(0);
    }
    return null;
  }

  public void deleteAllMember(Long pdwhPubId) {
    // 先删除
    String hql = "delete from PdwhPubMemberPO p where p.pdwhPubId=:pdwhPubId ";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubMemberPO> findPosMemberByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) "
        + " order by p.seqNo asc nulls last ";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public void updateInsCount(Integer insCount, Long memberId) {
    String hql = "update PdwhPubMemberPO p  set p.insCount = :insCount where p.id = :memberId";
    this.createQuery(hql).setParameter("insCount", insCount).setParameter("memberId", memberId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getIdByPubId(Long pubId) {
    String hql = "select p.id from PdwhPubMemberPO p where p.pdwhPubId = :pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
