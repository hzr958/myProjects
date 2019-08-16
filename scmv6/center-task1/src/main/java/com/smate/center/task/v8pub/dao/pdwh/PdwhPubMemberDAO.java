package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubMemberPO;
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
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId order by p.seqNo asc nulls last ";
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

}
